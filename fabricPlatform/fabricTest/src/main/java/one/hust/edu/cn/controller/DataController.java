package one.hust.edu.cn.controller;

import com.auth0.jwt.JWT;
import lombok.extern.slf4j.Slf4j;
import one.hust.edu.cn.entities.CommonResult;
import one.hust.edu.cn.entities.DataAuthority;
import one.hust.edu.cn.entities.MyFile;
import one.hust.edu.cn.myAnnotation.CheckToken;
import one.hust.edu.cn.service.*;
import one.hust.edu.cn.utils.TxtUtil;
import one.hust.edu.cn.vo.DataUserAuthorityVO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

@Slf4j
@RestController
public class DataController {
    @Resource
    private DataService fileService;
    @Resource
    ChannelAuthorityService channelAuthorityService;
    @Resource
    DataAuthorityService dataAuthorityService;
    @Resource
    FabricService fabricService;
    @Resource
    UserService userService;
    @Resource
    ChannelService channelService;

    //上传文件
    @CheckToken
    @PostMapping(value = "/data/uploadFile/{channelId}")
    @ResponseBody
    @Transactional
    public CommonResult uploadFile(@RequestParam("file") MultipartFile file, @PathVariable("channelId") Integer channelId, HttpServletRequest httpServletRequest){
        //获取文件名
        String fileName = file.getOriginalFilename();
        String filePath = "D:/研究生资料/南六218实验室/代炜琦项目组文件/github同步代码/uploadFilePackage/";
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        Integer originUserId = JWT.decode(token).getClaim("id").asInt();

        try {
            //进行文件传输
            file.transferTo(new File(filePath + fileName));
            MyFile myFile = new MyFile();
            myFile.setChannelId(channelId);//这里后面要做出选择channel
            myFile.setDataName(filePath + fileName);
            //文件大小以KB作为单位
            // 首先先将.getSize()获取的Long转为String 单位为B
            Double size = Double.parseDouble(String.valueOf(file.getSize()));
            BigDecimal b = new BigDecimal(size);
            // 2表示2位 ROUND_HALF_UP表明四舍五入，
            size = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            // 此时size就是保留两位小数的浮点数
            myFile.setDataSize(size);
            myFile.setData(TxtUtil.getTxtContent(myFile));
            myFile.setOriginUserId(originUserId);
            myFile.setDataType(fileName.substring(fileName.lastIndexOf(".")) + "文件");
            //初次创建时将初始时间和修改时间写成一样
            myFile.setCreatedTime(new Timestamp(new Date().getTime()));
            myFile.setModifiedTime(new Timestamp(new Date().getTime()));
            fileService.uploadFile(myFile);
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////执行fabric操作
            String result = "";
            // 1. 权限申请 一次上链
            String username = "userA";
            String dstChannelName = "channel1";
            String txId = fabricService.applyForCreateFile(username, dstChannelName, myFile.getId()+"");
            if (txId == null || txId.isEmpty()) {
                System.out.println("申请文件创建权限失败");
                return new CommonResult<>(300,"文件创建权限失败",null);
            }
            System.out.println("1.创建文件成功 txId: " + txId);
            result+="1.创建文件成功 txId: " + txId+"\r\n";
            //hash
            // 3. 更新链上hash 二次上链
            String rawRes = fabricService.updateForCreateFile(TxtUtil.getTxtContent(myFile), username, dstChannelName, myFile.getId()+"", txId);
            System.out.println("2. 更新链上hash ： " + rawRes);
            result+="2. 更新链上hash ： " + rawRes+"\r\n";
            // 4. 授予用户文件的查改权限
            rawRes = fabricService.grantUserPermissionOnFile("channel1", myFile.getId()+"", "read", "role1", Collections.singletonList(username));
            System.out.println("3.授予用户文件读取权限：" + rawRes);
            result+="3.授予用户文件读取权限：" + rawRes+"\r\n";
            rawRes = fabricService.grantUserPermissionOnFile("channel1", myFile.getId()+"", "modify", "role1", Collections.singletonList(username));
            System.out.println("4.授予用户文件修改权限：" + rawRes);
            result+="4.授予用户文件修改权限：" + rawRes+"\r\n";
            //写入上传者权限
            dataAuthorityService.addMasterDataAuthority(originUserId,myFile.getId());

            return new CommonResult<>(200,"上传成功，文件位于："+filePath+fileName+"\r\n"+result,myFile);
        } catch (Exception e) {
            return new CommonResult<>(400, e.getMessage(), null);
        }
    }

    //获取文件列表
    @CheckToken
    @GetMapping(value = "/data/getDataList")
    public CommonResult getDataList(HttpServletRequest httpServletRequest){
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        Integer userId = JWT.decode(token).getClaim("id").asInt();
        List<DataAuthority> list = dataAuthorityService.findDataAuthorityByUserId(userId);//获取该用户的所有权限
        List<DataUserAuthorityVO> result = new ArrayList<>();
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < list.size(); i++) {
            DataUserAuthorityVO temp = new DataUserAuthorityVO();
            Integer dataSampleId = list.get(i).getDataSampleId();
            if(!set.contains(dataSampleId)){
                set.add(dataSampleId);
                temp.setMyFile(fileService.findDataById(dataSampleId));
                Set<Integer> s = new HashSet<>();
                for (int j = 0; j < list.size(); j++) {
                    if(list.get(j).getDataSampleId().equals(dataSampleId)){
                        s.add(list.get(j).getAuthorityKey());
                    }
                }
                temp.setAuthoritySet(s);
                result.add(temp);
            }
        }
        return new CommonResult<>(200,"获取该用户所有文件权限列表成功",result);
    }

    //根据文件id删除文件
    //上传文件
    @CheckToken
    @PostMapping(value = "/data/deleteDataById")
    @ResponseBody
    public CommonResult deleteDataById(@RequestBody Map<String, String> params, HttpServletRequest httpServletRequest) {
        Integer dataId = Integer.valueOf(params.get("dataId"));
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        Integer result = fileService.deleteDataById(dataId);
        if (result < 1) {
            return new CommonResult<>(400, "不存在id为：" + dataId + "的文件", null);
        }
        return new CommonResult<>(200, "成功删除id为：" + dataId + "的文件,token为：" + token, null);
    }

    //根据文件id获取文件内容
    @CheckToken
    @PostMapping(value = "/data/getData")
    @ResponseBody
    public CommonResult getData(@RequestBody Map<String, String> params, HttpServletRequest httpServletRequest) {
        Integer dataId = Integer.valueOf(params.get("dataId"));
        MyFile result = fileService.findDataById(dataId);
        if(result==null){
            return new CommonResult<>(400,"不存在id为："+dataId+"的文件",null);
        }
        // 1. 申请读取权限
        String username = "userA";
        String dstChannelName = "channel1";
        String txId = fabricService.applyForReadFile(username, dstChannelName, String.valueOf(dataId));
        if (txId == null || txId.isEmpty()) {
            System.out.println("申请文件读取权限失败");
            return new CommonResult<>(300,"申请文件读取权限失败",null);
        }
        // 2. 读取文件
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        String txtContent = TxtUtil.getTxtContent(result);
        return new CommonResult<>(200, "文件token为：" + token + "\r\ntxId：" + txId, txtContent);

        // TODO: 二次上链？
    }

    //根据文件id对文件内容进行更新
    @CheckToken
    @PostMapping(value = "/data/updateData")
    @ResponseBody
    public CommonResult updateData(@RequestBody Map<String, String> params){
        Integer dataId = Integer.valueOf(params.get("dataId"));
        String dataContent = params.get("dataContent");
        MyFile myFile = fileService.findDataById(dataId);
        if (myFile == null) {
            return new CommonResult<>(400, "不存在id为：" + dataId + "的文件", null);
        }
        // 1. 申请文件修改权限
        String username = "userA";
        String dstChannelName = "channel1";
        String txId = fabricService.applyForModifyFile(username, dstChannelName, String.valueOf(dataId));
        if (txId == null || txId.isEmpty()) {
            System.out.println("申请文件修改权限失败");
            return new CommonResult<>(300,"申请文件修改权限失败",null);
        }
        // 2. 修改文件
        File old_file = new File(myFile.getDataName());
        old_file.delete();
        File new_file = new File(myFile.getDataName());
        //创建新文件
        try {
            /* 写入Txt文件 */
            new_file.createNewFile(); // 创建新文件
            BufferedWriter out = new BufferedWriter(new FileWriter(new_file));
            out.write(dataContent); // \r\n即为换行
            out.flush(); // 把缓存区内容压入文件
            out.close(); // 最后记得关闭文件
        } catch (Exception e) {
            e.printStackTrace();
        }
        //更新数据库
        myFile.setData(dataContent);
        Double size = Double.parseDouble(String.valueOf(new_file.length())) / 1024;
        BigDecimal b = new BigDecimal(size);
        size = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        myFile.setDataSize(size);
        myFile.setModifiedTime(new Timestamp(new Date().getTime()));
        fileService.updateFile(myFile);
        // 3. 更新hash值到fabric 二次上链
        String res = fabricService.updateForModifyFile(TxtUtil.getTxtContent(myFile), username, dstChannelName, String.valueOf(dataId), txId);
        System.out.println("更新hash值结果：" + res);
        return new CommonResult<>(200, "id为：" + dataId + "的文件更新成功\r\ntxId："+txId, null);
    }
}
