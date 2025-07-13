package com.neusoft.nursingcenter.mapper;

import com.neusoft.nursingcenter.entity.NursingProgram;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.nursingcenter.entity.Customer;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CustomerMapper extends BaseMapper<Customer>{
    @Select("select * from customer where nurse_id=#{nurseId} and is_deleted=0")
    List<Customer> listByNurseId(int nurseId);

//    @Select("select * from customer where name=#{name} and is_deleted=0")
//    Customer getByName(String name);

    @Select("select * from customer where customer_id=#{customerId} and is_deleted=0")
    Customer getById(int customerId);

    // 客户的身份证号和手机号不能重复
    @Select("select * from customer where id_card=#{idCard} and is_deleted=0")
    Customer getByIdCard(String idCard);

    @Select("select * from customer where phone_number=#{phoneNumber} and is_deleted=0")
    Customer getByPhoneNumber(String phoneNumber);


}
