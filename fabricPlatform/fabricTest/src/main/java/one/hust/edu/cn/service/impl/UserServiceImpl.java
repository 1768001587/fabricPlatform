package one.hust.edu.cn.service.impl;

import one.hust.edu.cn.dao.UserDao;
import one.hust.edu.cn.entities.User;
import one.hust.edu.cn.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;

    @Override
    public List<User> getAllUser() {
        return userDao.getAllUser();
    }

    @Override
    public User findUserById(Integer id) {
        return userDao.findUserById(id);
    }

    @Override
    public boolean login(User user) {
        if(userDao.login(user)==1) return true;
        else return false;
    }

    @Override
    public boolean register(User user) {
        try{
            userDao.register(user);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}