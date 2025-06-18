package com.neusoft.nursingcenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.nursingcenter.entity.BedUsageRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BedUsageRecordMapper extends BaseMapper<BedUsageRecord> {
    @Select("select * from bed_usage_record where customer_id=#{customerId} and status=1 and is_deleted=0")
    BedUsageRecord getCurrentUsingRecord(int customerId);

    @Select("select * from bed_usage_record where bed_id=#{bedId} and is_deleted=0")
    List<BedUsageRecord> listByBedId(int bedId);

    @Select("select * from bed_usage_record where customer_id=#{customerId} and is_deleted=0")
    List<BedUsageRecord> listByCustomerId(int customerId);

    @Select("select * from bed_usage_record where status=#{status} and is_deleted=0")
    List<BedUsageRecord> listByStatus(int status);

    @Select("select * from bed_usage_record where customer_id=#{customerId} and status=#{status} and is_deleted=0")
    List<BedUsageRecord> listByCustomerIdAndStatus(@Param("customerId") int customerId, @Param("status") int status);
}
