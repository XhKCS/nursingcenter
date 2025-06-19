package com.neusoft.nursingcenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.nursingcenter.entity.CustomerNursingService;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CustomerNursingServiceMapper extends BaseMapper<CustomerNursingService> {
    @Select("select * from customer_nursing_service where customer_id=#{customerId} and is_deleted=0")
    List<CustomerNursingService> listByCustomerId(int customerId);

    @Select("select * from customer_nursing_service where customer_id=#{customerId} and program_code=#{programCode} and is_deleted=0")
    CustomerNursingService getByCustomerIdAndProgramCode(@Param("customerId") int customerId, @Param("programCode") String programCode);
}
