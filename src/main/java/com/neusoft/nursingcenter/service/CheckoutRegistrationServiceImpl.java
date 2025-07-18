package com.neusoft.nursingcenter.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.nursingcenter.entity.*;
import com.neusoft.nursingcenter.mapper.CheckoutRegistrationMapper;
import com.neusoft.nursingcenter.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@Service
public class CheckoutRegistrationServiceImpl implements CheckoutRegistrationService{

    @Autowired
    private CheckoutRegistrationMapper checkoutRegistrationMapper;

    @Autowired
    private CustomerService customerService;

    @Override
    public PageResponseBean<List<CheckoutRegistration>> page(Map<String, Object> request) {
        int current = (int)request.get("current");
        int size = (int)request.get("size");
        String name = (String)request.get("name");
        Integer nurseId = (Integer) request.get("nurseId");

//        LambdaQueryWrapper<Customer> cqw = new LambdaQueryWrapper<>();
//        cqw.like(null != name,Customer :: getName,name);
//
//        List<Customer> clist = customerMapper.selectList(cqw);
//
//        LambdaQueryWrapper<CheckoutRegistration> qw = new LambdaQueryWrapper<>();
//        for (Customer customer : clist){
//            qw.or().eq(CheckoutRegistration :: getCustomerId,customer.getCustomerId());
//        }
        QueryWrapper<CheckoutRegistration> qw = new QueryWrapper<>();
        qw.like("customer_name", name);
        qw.eq(null != nurseId, "nurse_id", nurseId);

        IPage<CheckoutRegistration> page = new Page<>(current,size);
        IPage<CheckoutRegistration> result = checkoutRegistrationMapper.selectPage(page,qw);
        List<CheckoutRegistration> list = result.getRecords();
        long total = result.getTotal();

        PageResponseBean<List<CheckoutRegistration>> prb = null;
        if(total > 0){
            prb = new PageResponseBean<>(list);
            prb.setTotal(total);
        }else {
            prb = new PageResponseBean<>(500, "No data");
        }
        return prb;
    }

    @Transactional
    @Override
    // 如果退住申请通过了，就将对应客户逻辑删除
    public ResponseBean<Integer> update(@RequestBody CheckoutRegistration data) {
        int result = checkoutRegistrationMapper.updateById(data);
        ResponseBean<Integer> rb = null;
        if(result > 0) {
            if(data.getReviewStatus() == 2) { //退住申请通过
               if(customerService.deleteCustomerById(data.getCustomerId()) > 0){
                   rb = new ResponseBean<>(result);
               }else {
                   throw new RuntimeException("修改过程中失败");
               }
            } else {
                rb = new ResponseBean<>(result);
            }

        }else {
            rb = new ResponseBean<>(500,"Fail to update");
        }
        return rb;
    }


}
