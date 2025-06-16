package com.neusoft.nursingcenter.controller;

import com.neusoft.nursingcenter.entity.PageResponseBean;
import com.neusoft.nursingcenter.entity.ResponseBean;
import com.neusoft.nursingcenter.entity.User;
import com.neusoft.nursingcenter.mapper.UserMapper;
import com.neusoft.nursingcenter.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserServiceImpl userService;

    @RequestMapping("/getAll")
    public ResponseBean<List<User>> getAll() {
        List<User> userList = userMapper.selectList(null);
        ResponseBean<List<User>> rb = null;

        if (userList.size() > 0) {
            rb = new ResponseBean<>(userList);
        } else {
            rb = new ResponseBean<>(500, "No data");
        }
        return rb;
    }

    @RequestMapping("getById")
    public ResponseBean<User> getById(int userId) {
        User user = userMapper.selectById(userId);
        ResponseBean<User> rb = null;

        if (user != null) {
            rb = new ResponseBean<>(user);
        } else {
            rb = new ResponseBean<>(500, "数据库中没有该id的用户");
        }
        return rb;
    }

    @RequestMapping("getByAccount")
    public ResponseBean<User> getByAccount(String account) {
        User user = userMapper.getByAccount(account);
        ResponseBean<User> rb = null;

        if (user != null) {
            rb = new ResponseBean<>(user);
        } else {
            rb = new ResponseBean<>(500, "数据库中没有该id的用户");
        }
        return rb;
    }

    @RequestMapping("getByName")
    public ResponseBean<User> getByName(String name) {
        User user = userMapper.getByName(name);
        ResponseBean<User> rb = null;

        if (user != null) {
            rb = new ResponseBean<>(user);
        } else {
            rb = new ResponseBean<>(500, "数据库中没有该id的用户");
        }
        return rb;
    }

}
