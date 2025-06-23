package com.neusoft.nursingcenter.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.nursingcenter.entity.Customer;
import com.neusoft.nursingcenter.entity.PageResponseBean;
import com.neusoft.nursingcenter.entity.ResponseBean;
import com.neusoft.nursingcenter.entity.User;
import com.neusoft.nursingcenter.mapper.CustomerMapper;
import com.neusoft.nursingcenter.mapper.UserMapper;
import com.neusoft.nursingcenter.service.UserService;
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
    private UserService userService;

    @Autowired
    private CustomerMapper customerMapper;

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

    @RequestMapping("/page")
    public PageResponseBean<List<User>> page(@RequestBody Map<String, Object> request) {
        Long current = (Long) request.get("current"); //当前页面
        Long size = (Long) request.get("size"); //一页的行数

        IPage<User> page = new Page<>(current, size);
        IPage<User> result = userMapper.selectPage(page, null);
        List<User> userList = result.getRecords();
        long total = result.getTotal();
        PageResponseBean<List<User>> rb = null;

        if (total > 0) {
            rb = new PageResponseBean<>(userList);
            rb.setTotal(total);
        } else {
            rb = new PageResponseBean<>(500, "No data");
        }
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

    @RequestMapping("/getById")
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

    @RequestMapping("/getByAccount")
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

    // 只能添加护工
    @RequestMapping("/add")
    public ResponseBean<String> add(@RequestBody User user) {
        user.setUserType(1); //通过后端请求只能添加普通用户（护工），不能添加管理员
        User check = userMapper.getByAccount(user.getAccount());
        if (check != null) {
            return new ResponseBean<>(500, "已存在账号相同的用户");
        }
        int result = userMapper.insert(user);
        ResponseBean<String> rb = null;
        if (result > 0) {
            rb = new ResponseBean<>("添加成功");
        } else {
            rb = new ResponseBean<>(500, "添加失败");
        }
        return rb;
    }

    // 也只能修改护工的信息
    @RequestMapping("/update")
    public ResponseBean<String> update(@RequestBody User user) {
        user.setUserType(1); //通过后端请求只能添加普通用户（护工），不能添加管理员
        User check = userMapper.getByAccount(user.getAccount());
        if (check != null && check.getUserId() != user.getUserId()) {
            return new ResponseBean<>(500, "已存在账号相同的用户");
        }
        int result = userMapper.insert(user);
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
    @RequestMapping("/delete")
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
