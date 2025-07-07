package com.neusoft.nursingcenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.nursingcenter.entity.*;
import com.neusoft.nursingcenter.mapper.CustomerMapper;
import com.neusoft.nursingcenter.mapper.UserMapper;
import com.neusoft.nursingcenter.redisdao.RedisDao;
import com.neusoft.nursingcenter.service.UserService;
import com.neusoft.nursingcenter.util.JWTTool;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@CrossOrigin("*")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private RedisDao redisDao;

    @PostMapping("/login")
    public ResponseBean<String> login(@RequestBody Map<String, Object> request, HttpServletRequest httpServletRequest) {
        String account = (String) request.get("account");
        String password = (String) request.get("password");
        User dbUser = userMapper.getByAccount(account);
        ResponseBean<String> rb = null;

        if (dbUser == null) {
            rb = new ResponseBean<>(500, "该账号不存在");
            return rb;
        }
        if (dbUser.getPassword().equals(password)) {
            try {
                dbUser.setPassword(null);
                // 把登录用户对象转换为json
                ObjectMapper om=new ObjectMapper();
                String userJson= om.writeValueAsString(dbUser);
                System.out.println("userJson：" + userJson);
                String token = JWTTool.createToken(userJson);
                System.out.println("生成相应token：" + token);
                //	把令牌存入redis中一份，键为user-{userId}
                redisDao.set("user-"+dbUser.getUserId().toString(), token, JWTTool.calendarInterval, TimeUnit.SECONDS);
                //	同时传递给前端一份
                rb = new ResponseBean<>(token);
//                httpServletRequest.getSession().setAttribute("user", dbUser);
            } catch (Exception e) {
                System.out.println("Exception happened: " + e.getMessage());
                rb = new ResponseBean<>(500, e.getMessage());
            }
        } else {
            rb = new ResponseBean<>(500, "登录密码错误");
        }
        return rb;
    }

    // 根据token加载当前已登录的user对象
    @PostMapping("/load")
    public ResponseBean<User> load(HttpServletRequest httpServletRequest) {
        ResponseBean<User> rb = null;
        try {
            if (httpServletRequest.getHeader("token") != null) {
                String token = httpServletRequest.getHeader("token");
                // 获取用户json
                String userJson = JWTTool.parseToken(token);
                // json转化为对象
                ObjectMapper om = new ObjectMapper();
                User user = om.readValue(userJson, User.class);
                System.out.println("从token中转换得到的user："+user);
                rb = new ResponseBean<>(user);
            }
            else {
                rb = new ResponseBean<>(500, "登录已过期");
            }
        } catch (Exception e) {
            System.out.println("Exception happened: " + e.getMessage());
            rb = new ResponseBean<>(500, e.getMessage());
        }
        return rb;
    }

    @PostMapping("/logout")
    public ResponseBean<String> logout(HttpServletRequest httpServletRequest) {
        ResponseBean<String> rb = null;
        //从redis中删除存储的token
        try {
            if (httpServletRequest.getHeader("token") != null) {
                String token = httpServletRequest.getHeader("token");
                // 获取对象json
                String json = JWTTool.parseToken(token);
                // json转化为对象（User或Customer）
                ObjectMapper om = new ObjectMapper();
                Map<String, Object> map = om.readValue(json, Map.class);
                if (map.containsKey("userId")) {
                    User user = om.readValue(json, User.class);
                    System.out.println("退出登录的user："+user.toString());
                    redisDao.delete("user-"+user.getUserId().toString());
                }
                else if (map.containsKey("customerId")) {
                    Customer customer = om.readValue(json, Customer.class);
                    System.out.println("退出登录的customer："+customer.toString());
                    redisDao.delete("customer-"+customer.getCustomerId().toString());
                }
                rb = new ResponseBean<>("已退出登录");
            }
            else {
                rb = new ResponseBean<>(500, "无相应令牌");
            }
        } catch (Exception e) {
            System.out.println("Exception happened: "+e.getMessage());
            rb = new ResponseBean<>(500, e.getMessage());
        }
        return rb;
    }

    // 无条件分页查询
    @PostMapping("/pageAll")
    public PageResponseBean<List<UserView>> pageAll(@RequestBody Map<String, Object> request) {
        int current = (int) request.get("current"); //当前页面
        int size = (int) request.get("size"); //一页的行数

        IPage<User> page = new Page<>(current, size);
        IPage<User> result = userMapper.selectPage(page, null);
        List<User> userList = result.getRecords();
        long total = result.getTotal();
        PageResponseBean<List<UserView>> rb = null;

        if (total > 0) {
            List<UserView> userViewList = new ArrayList<>();
            for (User user : userList) {
                userViewList.add(new UserView(user));
            }
            rb = new PageResponseBean<>(userViewList);
            rb.setTotal(total);
        } else {
            rb = new PageResponseBean<>(500, "No data");
        }
        return rb;
    }

    // 有条件分页查询，组合条件为：name-用户姓名; userType-用户角色类型
    @PostMapping("/page")
    public PageResponseBean<List<UserView>> page(@RequestBody Map<String, Object> request) {
        String name = (String) request.get("name");
        int userType = (int) request.get("userType");
        int current = (int) request.get("current"); //当前页面
        int size = (int) request.get("size"); //一页的行数

        IPage<User> page = new Page<>(current, size);
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.like("name", name);
        qw.eq("user_type", userType);
        IPage<User> result = userMapper.selectPage(page, qw);
        List<User> userList = result.getRecords();
        long total = result.getTotal();
        PageResponseBean<List<UserView>> rb = null;

        if (total > 0) {
            List<UserView> userViewList = new ArrayList<>();
            for (User user : userList) {
                userViewList.add(new UserView(user));
            }
            rb = new PageResponseBean<>(userViewList);
            rb.setTotal(total);
        } else {
            rb = new PageResponseBean<>(500, "No data");
        }
        return rb;
    }

    @PostMapping("/listAll")
    public ResponseBean<List<UserView>> listAll() {
        List<User> userList = userMapper.selectList(null);
        ResponseBean<List<UserView>> rb = null;

        if (userList.size() > 0) {
            List<UserView> userViewList = new ArrayList<>();
            for (User user : userList) {
                userViewList.add(new UserView(user));
            }
            rb = new PageResponseBean<>(userViewList);
        } else {
            rb = new ResponseBean<>(500, "No data");
        }
        return rb;
    }

    @PostMapping("/getById")
    public ResponseBean<UserView> getById(@RequestBody Map<String, Object> request) {
        int userId = (int) request.get("userId");
        User user = userMapper.selectById(userId);
        ResponseBean<UserView> rb = null;

        if (user != null) {
            rb = new ResponseBean<>(new UserView(user));
        } else {
            rb = new ResponseBean<>(500, "数据库中没有该id的用户");
        }
        return rb;
    }

    @PostMapping("/getByAccount")
    public ResponseBean<UserView> getByAccount(@RequestBody Map<String, Object> request) {
        String account = (String) request.get("account");
        User user = userMapper.getByAccount(account);
        ResponseBean<UserView> rb = null;

        if (user != null) {
            rb = new ResponseBean<>(new UserView(user));
        } else {
            rb = new ResponseBean<>(500, "数据库中没有该账号的用户");
        }
        return rb;
    }

    // 只能添加护工
    @PostMapping("/add")
    public ResponseBean<String> add(@RequestBody UserView userView) {
        User user = new User(userView);
        user.setUserType(1); //通过后端请求只能添加普通用户（护工），不能添加管理员
        User check = userMapper.getByAccount(user.getAccount());
        if (check != null) {
            return new ResponseBean<>(500, "已存在账号相同的用户");
        }
        // 默认密码是手机号后六位
        String defaultPassword = user.getPhoneNumber().substring(5, 11);
        user.setPassword(defaultPassword);
        int result = userMapper.insert(user);
        ResponseBean<String> rb = null;
        if (result > 0) {
            rb = new ResponseBean<>("添加成功");
        } else {
            rb = new ResponseBean<>(500, "添加失败");
        }
        return rb;
    }

    // 也只能修改护工的信息；正常的修改不能修改密码
    @PostMapping("/update")
    public ResponseBean<String> update(@RequestBody UserView userView) {
        User user = new User(userView);
        user.setUserType(1); //通过后端请求只能添加普通用户（护工），不能添加管理员
        User check = userMapper.getByAccount(user.getAccount());
        if (check != null && check.getUserId() != user.getUserId()) {
            return new ResponseBean<>(500, "已存在账号相同的用户");
        }
        int result = userMapper.updateUser(user);
        ResponseBean<String> rb = null;
        if (result > 0) {
            rb = new ResponseBean<>("修改成功");
        } else {
            rb = new ResponseBean<>(500, "修改失败");
        }
        return rb;
    }

    // 也只能删除护工
    // 注意，如果目前存在由该护工负责护理的客户，则会提示不能删除该护工
    @PostMapping("/delete")
    public ResponseBean<String> delete(@RequestBody Map<String, Object> request) {
        int userId = (int) request.get("userId");
        List<Customer> customerList = customerMapper.listByNurseId(userId);
        if (customerList.size() > 0) {
            return new ResponseBean<>(500, "目前存在由该用户负责护理的客户，不能删除该用户！");
        }
        int result = userMapper.deleteById(userId);
        ResponseBean<String> rb = null;
        if (result > 0) {
            rb = new ResponseBean<>("添加成功");
        } else {
            rb = new ResponseBean<>(500, "添加失败");
        }
        return rb;
    }
}
