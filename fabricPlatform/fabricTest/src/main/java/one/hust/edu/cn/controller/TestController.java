package one.hust.edu.cn.controller;

import lombok.extern.slf4j.Slf4j;
import one.hust.edu.cn.entities.CommonResult;
import one.hust.edu.cn.entities.MyFile;
import one.hust.edu.cn.entities.User;
import one.hust.edu.cn.service.FileService;
import one.hust.edu.cn.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
public class TestController {
    @Resource
    private UserService service;
    @Resource
    private FileService fileService;
    @GetMapping(value = "/test/{id}")
    public String test(@PathVariable("id") int id){
        return "this is fabricTest id="+id;
    }

    @GetMapping(value = "/testAPI/{id}")
    public CommonResult testAPI(@PathVariable("id") int id){
        return new CommonResult<>(200,"通用API测试成功,id为："+id,null);
    }

    @GetMapping(value = "/findUserById/{id}")
    public CommonResult findUserById(@PathVariable("id") Integer id){
        User result = service.findUserById(id);
        if(result==null) return new CommonResult<>(400,"无此用户",null);
        return new CommonResult<>(200,"用户id："+id,result);
    }

    @GetMapping(value = "/getAllUser")
    public CommonResult getAllUser(){
        List<User> result = service.getAllUser();
        if(result==null) return new CommonResult<>(400,"无用户",null);
        return new CommonResult<>(200,"查找成功",result);
    }
    @RequestMapping(value = "/toUpload")
    public ModelAndView toUpload(ModelAndView mv){
        mv.setViewName("uploadFile.html");
        return mv;
    }

    @PostMapping(value = "/uploadFile")
    public CommonResult uploadFile(@RequestParam("file") MultipartFile file){
        //获取文件名
        String fileName = file.getOriginalFilename();
        //获取文件后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //重新生成文件名
        fileName = UUID.randomUUID()+suffixName;
        //指定本地文件夹存储图片
        String filePath = "D:/研究生资料/南六218实验室/代炜琦项目组文件/github同步代码/uploadFilePackage/";
        try {
            //将图片保存到static文件夹里
            file.transferTo(new File(filePath+fileName));
            MyFile myFile = new MyFile();
            myFile.setFilename(fileName);
            fileService.uploadFile(myFile);
            return new CommonResult<>(200,"上传成功，文件位于："+filePath+fileName,myFile);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult<>(400,"上传失败",null);
        }
    }
}
