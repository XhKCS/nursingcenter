package com.neusoft.nursingcenter.service;

import com.neusoft.nursingcenter.entity.Customer;
import com.neusoft.nursingcenter.entity.CustomerNursingService;
import com.neusoft.nursingcenter.entity.NursingRecord;
import com.neusoft.nursingcenter.entity.ResponseBean;
import com.neusoft.nursingcenter.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
@Service
public class NursingRecordServiceImpl implements NursingRecordService{

    @Autowired
    private NursingRecordMapper nursingRecordMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private NursingProgramMapper nursingProgramMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CustomerNursingServiceMapper customerNursingServiceMapper;

    @Transactional
    @Override
    public Integer add(Map<String, Object> request) {
//        String customerName = (String) request.get("customerName");
        String programName = (String) request.get("programName");
        String programCode = (String) request.get("programCode");
        String nursingTime = (String) request.get("nursingTime");
        int executionCount = (int) request.get("executionCount");
        String description = (String) request.get("description");
        String nurseName = (String) request.get("nurseName");

        // 为什么不直接从前端拿customerId？明明NursingRecord的字段里都有；而且客户有可能重名，所以要用customerId
//        int customerId = customerMapper.getByName(customerName).getCustomerId();
        int customerId = (int) request.get("customerId");
        Customer customer = customerMapper.selectById(customerId);
        int programId = nursingProgramMapper.getByName(programName).getId();
        String nursePhone = userMapper.getByName(nurseName).getPhoneNumber();

        NursingRecord nursingRecord = new NursingRecord(0,customerId, customer.getName(),programId,programCode,programName,description,nurseName,nursePhone,nursingTime,executionCount,false);
        int result = nursingRecordMapper.insert(nursingRecord);

        CustomerNursingService customerNursingService = customerNursingServiceMapper.getByCustomerIdAndProgramCode(customerId,programCode);
        customerNursingService.setUsedCount(customerNursingService.getUsedCount()+executionCount);

        int result2 = customerNursingServiceMapper.updateById(customerNursingService);
        if(result<=0){
            throw new RuntimeException("尝试添加护理记录时出错");
        }

        if(result2<=0){
            throw new RuntimeException("尝试更新用户护理项目已使用数量时出错");
        }

        return result;
    }
}
