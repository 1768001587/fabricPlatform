package one.hust.edu.cn.service;

import one.hust.edu.cn.entities.User;
import java.util.List;

public interface UserService {
    //返回所有用户
    List<User> getAllUser();
    //根据id查用户
    User findUserById(Integer id);
    //用户登录
    boolean login(User user);
    //用户注册
    boolean register(User user);
}
