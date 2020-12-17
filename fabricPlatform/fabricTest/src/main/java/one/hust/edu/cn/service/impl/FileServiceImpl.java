package one.hust.edu.cn.service.impl;

import one.hust.edu.cn.dao.FileDao;
import one.hust.edu.cn.entities.MyFile;
import one.hust.edu.cn.service.FileService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FileServiceImpl implements FileService
{
    @Resource
    private FileDao fileDao;

    @Override
    public void uploadFile(MyFile myFile) {
        fileDao.uploadFile(myFile);
    }
}
