package edu.cn.service;


import edu.cn.dao.UserDao;
import edu.cn.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public User getByID(int id){
        return userDao.getByID(id);
    }

}
