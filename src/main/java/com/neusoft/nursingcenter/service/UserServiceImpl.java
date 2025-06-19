package com.neusoft.nursingcenter.service;

import com.neusoft.nursingcenter.entity.BedUsageRecord;
import com.neusoft.nursingcenter.entity.ResponseBean;
import com.neusoft.nursingcenter.entity.User;
import com.neusoft.nursingcenter.mapper.BedMapper;
import com.neusoft.nursingcenter.mapper.BedUsageRecordMapper;
import com.neusoft.nursingcenter.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Transactional
    @Override
    public int deleteUser(int userId) {
        return 0;
    }

    @Transactional
    @Override
    public int updateUser(User user) {
        return 0;
    }


}
