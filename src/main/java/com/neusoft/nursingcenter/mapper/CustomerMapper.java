package com.neusoft.nursingcenter.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.nursingcenter.entity.Customer;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CustomerMapper extends BaseMapper<Customer>{
    @Select("select * from customer where nurse_id=#{nurseId} and is_deleted=0")
    List<Customer> listByNurseId(int nurseId);
}
