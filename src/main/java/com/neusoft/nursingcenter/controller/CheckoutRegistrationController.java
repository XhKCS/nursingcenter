package com.neusoft.nursingcenter.controller;

import com.neusoft.nursingcenter.entity.*;
import com.neusoft.nursingcenter.mapper.CheckoutRegistrationMapper;
import com.neusoft.nursingcenter.mapper.CustomerMapper;
import com.neusoft.nursingcenter.service.CheckoutRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/checkoutRegistration")
public class CheckoutRegistrationController {

    @Autowired
    private CheckoutRegistrationMapper checkoutRegistrationMapper;

    @Autowired
    private CheckoutRegistrationService  checkoutRegistrationService;

    @Autowired
    private CustomerMapper customerMapper;

    // request需包含参数：int current, int size, String name
    @PostMapping("/page")
    public PageResponseBean<List<CheckoutRegistration>> page (@RequestBody Map<String, Object> request){
        return checkoutRegistrationService.page(request);
    }

    @PostMapping("/add")
    public ResponseBean<Integer> add(@RequestBody CheckoutRegistration checkoutRegistration) {
        int result = checkoutRegistrationMapper.insert(checkoutRegistration);
        Customer customer = customerMapper.selectById(checkoutRegistration.getCustomerId());
        checkoutRegistration.setCustomerName(checkoutRegistration.getCustomerName());
        checkoutRegistration.setCheckinDate(customer.getCheckinDate());
        checkoutRegistration.setBedNumber(customer.getBedNumber());

        checkoutRegistration.setReviewStatus(0);
        checkoutRegistration.setReviewerId(0);
        checkoutRegistration.setReviewTime("");
        checkoutRegistration.setRejectReason("");
        ResponseBean<Integer> rb = null;
        if(result > 0) {
            rb = new ResponseBean<>(result);
        }else {
            rb = new ResponseBean<>(500,"Fail to add");
        }
        return rb;
    }

    // 管理员审批
    @PostMapping("/update")
    public ResponseBean<Integer> update(@RequestBody CheckoutRegistration data) {
        ResponseBean<Integer> rb = null;
        try {
            rb = checkoutRegistrationService.update(data);
        } catch (Exception e) {
            rb = new ResponseBean<>(500, e.getMessage());
        }
        return rb;
    }

    // 撤销申请
    @PostMapping("/delete")
    public ResponseBean<Integer> delete(@RequestBody Map<String, Object> request) {
        int id = (int) request.get("id");
        int result = checkoutRegistrationMapper.deleteById(id);
        ResponseBean<Integer> rb =null;
        if(result > 0) {
            rb = new ResponseBean<>(result);
        }else {
            rb = new ResponseBean<>(500,"Fail to delete");
        }

        return rb;
    }
}
