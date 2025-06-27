package com.neusoft.nursingcenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.nursingcenter.entity.CustomerNursingService;
import com.neusoft.nursingcenter.entity.NursingProgram;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CustomerNursingServiceMapper extends BaseMapper<CustomerNursingService> {
    @Select("select * from customer_nursing_service where customer_id=#{customerId}")
    List<CustomerNursingService> listByCustomerId(int customerId);

    @Select("select * from customer_nursing_service where customer_id=#{customerId} and program_code=#{programCode}")
    CustomerNursingService getByCustomerIdAndProgramCode(@Param("customerId") int customerId, @Param("programCode") String programCode);

    @Update("update customer_nursing_service set program_code=#{e.programCode}, program_name=#{e.name}, program_price=#{e.price} where program_id=#{e.id}")
    int updateByNursingProgram(@Param("e") NursingProgram nursingProgram);

    @Delete("delete from customer_nursing_service where customer_id=#{customerId} and level_id=#{levelId}")
    int deleteByCustomerIdAndLevelId(@Param("customerId") int customerId, @Param("levelId") int levelId);

    @Select("select * from customer_nursing_service where program_id=#{programId}")
    List<CustomerNursingService> listByProgramId(int programId);
}
