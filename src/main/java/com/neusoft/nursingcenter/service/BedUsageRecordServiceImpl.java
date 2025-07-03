package com.neusoft.nursingcenter.service;

import com.neusoft.nursingcenter.entity.Bed;
import com.neusoft.nursingcenter.entity.BedUsageRecord;
import com.neusoft.nursingcenter.entity.Customer;
import com.neusoft.nursingcenter.mapper.BedMapper;
import com.neusoft.nursingcenter.mapper.BedUsageRecordMapper;
import com.neusoft.nursingcenter.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class BedUsageRecordServiceImpl implements BedUsageRecordService {
    @Autowired
    private BedUsageRecordMapper bedUsageRecordMapper;

    @Autowired
    private BedMapper bedMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Transactional
    @Override
    public boolean exchange(Map<String, Object> request) {
        int customerId = (int) request.get("customerId"); //客户ID
        String roomNumber = (String) request.get("roomNumber"); //新房间号
        String bedNumber = (String) request.get("bedNumber"); // 新床位号
        String startDate = (String) request.get("startDate");
        String endDate = (String) request.get("endDate");

        BedUsageRecord oldRecord = bedUsageRecordMapper.getCurrentUsingRecord(customerId);
        oldRecord.setEndDate(startDate); //旧床位使用的结束日期为新床位使用的开始日期
        oldRecord.setStatus(0); //旧床位使用记录设置为失效
        int res1 = bedUsageRecordMapper.updateById(oldRecord);
        if (res1 <= 0) {
            throw new RuntimeException("清除旧床位记录过程中出错");
        }
        Bed oldBed = bedMapper.getBedByNumber(oldRecord.getBedNumber());
        oldBed.setStatus(0); //旧床位状态设置为空闲中
        int res2 = bedMapper.updateById(oldBed);
        if (res2 <= 0) {
            throw new RuntimeException("改变旧床位状态过程中出错");
        }

        Bed newBed = bedMapper.getBedByNumber(bedNumber);
        if (newBed.getStatus() != 0) {
            throw new RuntimeException("只能调换到当前空闲的床位！");
        }
        newBed.setStatus(2); //新床位状态设置为有人
        int res3 = bedMapper.updateById(newBed);
        if (res3 <= 0) {
            throw new RuntimeException("改变新床位状态过程中出错");
        }
        Customer customer = customerMapper.selectById(customerId);
        // 创建新的床位使用记录
        BedUsageRecord bedUsageRecord = new BedUsageRecord(0, bedNumber, customerId, customer.getName(), customer.getGender(), startDate, endDate, 1, false);
        int res4 = bedUsageRecordMapper.insert(bedUsageRecord);
        if (res4 <= 0) {
            throw new RuntimeException("添加新床位记录过程中出错");
        }
        // 修改客户信息的房间号和床位号
        customer.setRoomNumber(roomNumber);
        customer.setBedNumber(bedNumber);
        int res5 = customerMapper.updateById(customer);
        if (res5 <= 0) {
            throw new RuntimeException("修改客户信息过程中出错");
        }
        return true;
    }
}
