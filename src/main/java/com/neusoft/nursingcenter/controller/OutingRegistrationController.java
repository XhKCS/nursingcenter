package com.neusoft.nursingcenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.nursingcenter.entity.MealItem;
import com.neusoft.nursingcenter.entity.OutingRegistration;
import com.neusoft.nursingcenter.entity.PageResponseBean;
import com.neusoft.nursingcenter.entity.ResponseBean;
import com.neusoft.nursingcenter.mapper.OutingRegistrationMapper;
import com.neusoft.nursingcenter.service.OutingRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/outingRegistration")
public class OutingRegistrationController {

    @Autowired
    private OutingRegistrationMapper outingRegistrationMapper;

    @Autowired
    private OutingRegistrationService outingRegistrationService;

    // request需包含参数：int current, int size, String name
    @PostMapping("/page")
    public PageResponseBean<List<OutingRegistration>> page (@RequestBody Map<String, Object> request){
        return outingRegistrationService.page(request);
    }

    @PostMapping("/add")
    public ResponseBean<Integer> add(@RequestBody OutingRegistration outingRegistration) {
        if (outingRegistration.getActualReturnDate() == null) {
            outingRegistration.setActualReturnDate("");
        }
        outingRegistration.setReviewStatus(0); //默认已提交状态
        outingRegistration.setReviewerId(0);
        outingRegistration.setReviewTime("");
        outingRegistration.setRejectReason("");
        int result = outingRegistrationMapper.insert(outingRegistration);
        ResponseBean<Integer> rb = null;
        if(result > 0) {
            rb = new ResponseBean<>(result);
        }else {
            rb = new ResponseBean<>(500,"Fail to add");
        }
        return rb;
    }

    // 管理员审批与护工登记回院时间
    @PostMapping("/update")
    public ResponseBean<Integer> update(@RequestBody OutingRegistration data) {
        int result = outingRegistrationMapper.updateById(data);
        ResponseBean<Integer> rb = null;
        if(result > 0) {
            rb = new ResponseBean<>(result);
        }else {
            rb = new ResponseBean<>(500,"Fail to update");
        }
        return rb;
    }

    // 撤销申请
    @PostMapping("/delete")
    public ResponseBean<Integer> delete(@RequestBody Map<String, Object> request) {
        int id = (int) request.get("id");
        int result = outingRegistrationMapper.deleteById(id);
        ResponseBean<Integer> rb =null;
        if(result > 0) {
            rb = new ResponseBean<>(result);
        }else {
            rb = new ResponseBean<>(500,"Fail to delete");
        }

        return rb;
    }
}
