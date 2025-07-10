package com.neusoft.nursingcenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.nursingcenter.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("select * from user where account=#{account}")
    User getByAccount(String account);

    @Select("select * from user where name=#{name}")
    User getByName(String name);

    // 用户并没有不能重名的限制，所以不应该有通过姓名返回单个用户的方法
//    @Select("select * from user where name=#{name}")
//    User getByName(String name);

    // 普通的修改不能修改密码
    @Update("update user set account=#{e.account}, name=#{e.name}, phone_number=#{e.phoneNumber}, gender=#{e.gender}, email=#{e.email} where user_id=#{e.userId}")
    int updateUser(@Param("e") User updatedUser);
}
