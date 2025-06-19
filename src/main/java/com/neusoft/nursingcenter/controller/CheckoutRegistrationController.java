package com.neusoft.nursingcenter.controller;

import com.neusoft.nursingcenter.entity.CheckoutRegistration;
import com.neusoft.nursingcenter.entity.OutingRegistration;
import com.neusoft.nursingcenter.entity.PageResponseBean;
import com.neusoft.nursingcenter.entity.ResponseBean;
import com.neusoft.nursingcenter.mapper.CheckoutRegistrationMapper;
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

    @PostMapping("/page")
    public PageResponseBean<List<CheckoutRegistration>> page (@RequestBody Map<String, Object> request){
        return checkoutRegistrationService.page(request);
    }

    @PostMapping("/add")
    public ResponseBean<Integer> add(@RequestBody CheckoutRegistration checkoutRegistration) {
        Integer result = checkoutRegistrationMapper.insert(checkoutRegistration);
        ResponseBean<Integer> rb = null;
        if(result > 0) {
            rb = new ResponseBean<>(result);
        }else {
            rb = new ResponseBean<>(500,"Fail to add");
        }
        return rb;
    }

    @PostMapping("/update")
    public ResponseBean<Integer> update(@RequestBody CheckoutRegistration data) {
       return checkoutRegistrationService.update(data);
    }

    @PostMapping("/delete")
    public ResponseBean<Integer> delete(@RequestBody Map<String, Object> request) {
        int id = (int) request.get("id");
        Integer result = checkoutRegistrationMapper.deleteById(id);
        ResponseBean<Integer> rb =null;
        if(result > 0) {
            rb = new ResponseBean<>(result);
        }else {
            rb = new ResponseBean<>(500,"Fail to delete");
        }

        return rb;
    }
}
