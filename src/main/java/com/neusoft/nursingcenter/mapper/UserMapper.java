package com.neusoft.nursingcenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.nursingcenter.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("select * from user where account=#{account}")
    User getByAccount(String account);

    // 用户并没有不能重名的限制，所以不应该有通过姓名返回单个用户的方法
//    @Select("select * from user where name=#{name}")
//    User getByName(String name);
}
