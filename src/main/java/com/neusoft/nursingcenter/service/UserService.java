package com.neusoft.nursingcenter.service;

import com.neusoft.nursingcenter.entity.ResponseBean;
import com.neusoft.nursingcenter.entity.User;

public interface UserService {
    int deleteUser(int userId);

    int updateUser(User user);

}
