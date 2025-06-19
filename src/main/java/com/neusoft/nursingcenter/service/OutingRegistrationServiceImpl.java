package com.neusoft.nursingcenter.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.nursingcenter.entity.Customer;
import com.neusoft.nursingcenter.entity.OutingRegistration;
import com.neusoft.nursingcenter.entity.PageResponseBean;
import com.neusoft.nursingcenter.entity.ResponseBean;
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

    @Override
    public PageResponseBean<List<OutingRegistration>> page(Map<String, Object> request) {
        int current = (int)request.get("current");
        int size = (int)request.get("size");
        String name = (String)request.get("name");

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
}
