package one.hust.edu.cn.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import one.hust.edu.cn.entities.CommonResult;
import one.hust.edu.cn.entities.MyFile;
import one.hust.edu.cn.service.DataService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

@Slf4j
@RestController
public class DataController {
    @Resource
    private DataService fileService;
    @PostMapping(value = "/uploadFile")
    public CommonResult uploadFile(@RequestParam("file") MultipartFile file){
        //获取文件名
        String fileName = file.getOriginalFilename();
        String filePath = "D:/研究生资料/南六218实验室/代炜琦项目组文件/github同步代码/uploadFilePackage/";
        try {
            file.transferTo(new File(filePath+fileName));
            MyFile myFile = new MyFile();
            myFile.setDataName(filePath+fileName);
            fileService.uploadFile(myFile);
            return new CommonResult<>(200,"上传成功，文件位于："+filePath+fileName,myFile);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult<>(400,"上传失败",null);
        }
    }
    @GetMapping(value = "getDataList")
    public CommonResult getDataList(){
        List<MyFile> list = fileService.getDataList();
        return new CommonResult<>(200,"获取所有文件列表成功",list);
    }
    @GetMapping(value = "deleteDataById/{id}")
    public CommonResult deleteDataById(@PathVariable("id") Integer id){
        Integer result = fileService.deleteDataById(id);
        if(result<1){
            return new CommonResult<>(200,"不存在id为："+id+"的文件",null);
        }
        return new CommonResult<>(200,"成功删除id为："+id+"的文件",null);
    }
}
