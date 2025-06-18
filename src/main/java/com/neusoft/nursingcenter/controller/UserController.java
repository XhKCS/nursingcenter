package com.neusoft.nursingcenter.controller;

import com.neusoft.nursingcenter.entity.PageResponseBean;
import com.neusoft.nursingcenter.entity.ResponseBean;
import com.neusoft.nursingcenter.entity.User;
import com.neusoft.nursingcenter.mapper.UserMapper;
import com.neusoft.nursingcenter.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserServiceImpl userService;

    @RequestMapping("/login")
    public ResponseBean<User> login(@RequestBody Map<String, Object> request, HttpServletRequest httpServletRequest) {
        String account = (String) request.get("account");
        String password = (String) request.get("password");
        User dbUser = userMapper.getByAccount(account);
        ResponseBean<User> rb = null;

        if (dbUser == null) {
            rb = new ResponseBean<>(500, "该账号不存在");
            return rb;
        }
        if (dbUser.getPassword().equals(password)) {
            httpServletRequest.getSession().setAttribute("user", dbUser); //存进session中
            rb = new ResponseBean<>(dbUser);
        } else {
            rb = new ResponseBean<>(500, "登录密码错误");
        }
        return rb;
    }

    // 加载当前session中已登录的user对象
    @RequestMapping("/load")
    public ResponseBean<User> load(HttpServletRequest httpServletRequest) {
        ResponseBean<User> rb = null;
        if (httpServletRequest.getSession().getAttribute("user") != null) {
            User user = (User) httpServletRequest.getSession().getAttribute("user");
            rb = new ResponseBean<>(user);
        }
        else {
            rb = new ResponseBean<>(500, "登录已过期");
        }
        return rb;
    }

    @RequestMapping("/logout")
    public ResponseBean<String> logout(HttpServletRequest httpServletRequest) {
        ResponseBean<String> rb = null;
        //清空session中存储的user对象
        httpServletRequest.getSession().setAttribute("user", null);
        rb = new ResponseBean<>("已退出登录");
        return rb;
    }

    @RequestMapping("/listAll")
    public ResponseBean<List<User>> listAll() {
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
    public ResponseBean<User> getById(@RequestBody Map<String, Object> request) {
        int userId = (int) request.get("userId");
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
    public ResponseBean<User> getByAccount(@RequestBody Map<String, Object> request) {
        String account = (String) request.get("account");
        User user = userMapper.getByAccount(account);
        ResponseBean<User> rb = null;

        if (user != null) {
            rb = new ResponseBean<>(user);
        } else {
            rb = new ResponseBean<>(500, "数据库中没有该账号的用户");
        }
        return rb;
    }


}
