package one.hust.edu.cn.controller;

import com.auth0.jwt.JWT;
import lombok.extern.slf4j.Slf4j;
import one.hust.edu.cn.entities.CommonResult;
import one.hust.edu.cn.entities.MyFile;
import one.hust.edu.cn.myAnnotation.CheckToken;
import one.hust.edu.cn.service.DataService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
public class DataController{
    @Resource
    private DataService fileService;

    //上传文件
    @CheckToken
    @PostMapping(value = "/uploadFile")
    public CommonResult uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest httpServletRequest){
        //获取文件名
        String fileName = file.getOriginalFilename();
        String filePath = "D:/研究生资料/南六218实验室/代炜琦项目组文件/github同步代码/uploadFilePackage/";
        try {
            file.transferTo(new File(filePath+fileName));
            MyFile myFile = new MyFile();
            myFile.setDataName(filePath+fileName);
            //文件大小以KB作为单位
            // 首先先将.getSize()获取的Long转为String 然后将String转为Float并除以1024 （因为1KB=1024B）
            Double size = Double.parseDouble(String.valueOf(file.getSize())) / 1024;
            BigDecimal b = new BigDecimal(size);
            // 2表示2位 ROUND_HALF_UP表明四舍五入，
            size = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            // 此时size就是保留两位小数的浮点数
            myFile.setDataSize(size);
            // 从 http 请求头中取出 token
            String token = httpServletRequest.getHeader("token");
            Integer originUserId = JWT.decode(token).getClaim("id").asInt();
            myFile.setOriginUserId(originUserId);
            myFile.setDataType(fileName.substring(fileName.lastIndexOf("."))+"文件");
            //初次创建时将初始时间和修改时间写成一样
            myFile.setCreatedTime(new Timestamp(new Date().getTime()));
            myFile.setModifiedTime(new Timestamp(new Date().getTime()));
            fileService.uploadFile(myFile);
            return new CommonResult<>(200,"上传成功，文件位于："+filePath+fileName,myFile);
        } catch (Exception e) {
            return new CommonResult<>(400,e.getMessage(),null);
        }
    }
    //获取文件列表
    @GetMapping(value = "getDataList")
    public CommonResult getDataList(){
        List<MyFile> list = fileService.getDataList();
        return new CommonResult<>(200,"获取所有文件列表成功",list);
    }
    //根据文件id删除文件
    @GetMapping(value = "deleteDataById/{id}")
    public CommonResult deleteDataById(@PathVariable("id") Integer id){
        Integer result = fileService.deleteDataById(id);
        if(result<1){
            return new CommonResult<>(200,"不存在id为："+id+"的文件",null);
        }
        return new CommonResult<>(200,"成功删除id为："+id+"的文件",null);
    }
}
