package com.neusoft.nursingcenter.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.nursingcenter.entity.*;
import com.neusoft.nursingcenter.mapper.BedMapper;
import com.neusoft.nursingcenter.mapper.CustomerMapper;
import com.neusoft.nursingcenter.mapper.OutingRegistrationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OutingRegistrationServiceImpl implements OutingRegistrationService{

    @Autowired
    OutingRegistrationMapper outingRegistrationMapper;

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    BedMapper bedMapper;

    @Override
    public PageResponseBean<List<OutingRegistration>> page(Map<String, Object> request) {
        int current = (int)request.get("current");
        int size = (int)request.get("size");
        String name = (String)request.get("name");
        Integer nurseId = (Integer) request.get("nurseId");

//        LambdaQueryWrapper<Customer> cqw = new LambdaQueryWrapper<>();
//        cqw.like(null != name,Customer :: getName,name);
//
//        List<Customer> clist = customerMapper.selectList(cqw);
//
//        LambdaQueryWrapper<OutingRegistration> qw = new LambdaQueryWrapper<>();
//        for (Customer customer : clist){
//            qw.or().eq(OutingRegistration :: getCustomerId,customer.getCustomerId());
//        }
        QueryWrapper<OutingRegistration> qw = new QueryWrapper<>();
        qw.like("customer_name", name);
        qw.eq(null != nurseId, "nurse_id", nurseId);

        IPage<OutingRegistration> page = new Page<>(current,size);
        IPage<OutingRegistration> result = outingRegistrationMapper.selectPage(page,qw);
        List<OutingRegistration> list = result.getRecords();
        long total = result.getTotal();

        PageResponseBean<List<OutingRegistration>> prb = null;
        if(total > 0){
            prb = new PageResponseBean<>(list);
            prb.setTotal(total);
        }else {
            prb = new PageResponseBean<>(500, "No data");
        }
        return prb;
    }

    @Override
    public int update(OutingRegistration outingRegistration) {
        if (outingRegistration.getReviewStatus() == 2) { //外出申请通过
            Customer customer = customerMapper.selectById(outingRegistration.getCustomerId());
            Bed usingBed = bedMapper.getBedByNumber(customer.getBedNumber());
            if (outingRegistration.getActualReturnDate() != null && !outingRegistration.getActualReturnDate().isEmpty()) {
                // 已登记回院，则对应床位状态仍是使用中
                usingBed.setStatus(2); //有人
            } else { //还未登记回院，则对应状态为外出
                usingBed.setStatus(1); //外出
            }
            int res1 = bedMapper.updateById(usingBed);
            if (res1 <= 0) {
                throw new RuntimeException("更新对应床位状态时失败");
            }
        }
        return outingRegistrationMapper.updateById(outingRegistration);
    }
}
