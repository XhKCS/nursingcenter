package com.neusoft.nursingcenter.service;

import com.neusoft.nursingcenter.entity.ResponseBean;
import com.neusoft.nursingcenter.entity.User;
import com.neusoft.nursingcenter.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


//    @Override
//    public ResponseBean<User> getUser(User user) {
//        User result = userMapper.selectById(user.getUserId());
//
//
//    }
}
