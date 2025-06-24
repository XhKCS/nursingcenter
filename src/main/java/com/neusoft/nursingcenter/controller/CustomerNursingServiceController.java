package com.neusoft.nursingcenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.nursingcenter.entity.*;
import com.neusoft.nursingcenter.mapper.CustomerNursingServiceMapper;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/customerNursingService")
public class CustomerNursingServiceController {
    @Autowired
    private CustomerNursingServiceMapper customerNursingServiceMapper;

    @PostMapping("/page")
    PageResponseBean<List<CustomerNursingService>> pageWithConditions(@RequestBody Map<String, Object> request) {
        int current = (int)request.get("current");
        int size = (int)request.get("size");
        int customerId = (int) request.get("customerId");
        String programName = (String)request.get("programName");

        QueryWrapper<CustomerNursingService> qw = new QueryWrapper<>();
        qw.eq("customerId", customerId);
        qw.like("program_name", programName);

        IPage<CustomerNursingService> page = new Page<>(current,size);
        IPage<CustomerNursingService> result = customerNursingServiceMapper.selectPage(page,qw);
        List<CustomerNursingService> list = result.getRecords();
        long total = result.getTotal();
        System.out.println(total);

        PageResponseBean<List<CustomerNursingService>> prb = null;
        if(total > 0){
            prb = new PageResponseBean<>(list);
            prb.setTotal(total);
        }else {
            prb = new PageResponseBean<>(500, "No data");
        }
        return prb;
    }

    // 由前端控制只能修改的字段：应该只能通过续费修改总数量
    @PostMapping("/update")
    public ResponseBean<Integer> update(@RequestBody CustomerNursingService customerNursingService) {
        ResponseBean<Integer> rb = null;
        int result = customerNursingServiceMapper.updateById(customerNursingService);
        if (result > 0) {
            rb = new ResponseBean<>(result);
        } else {
            rb = new ResponseBean<>(500, "修改失败");
        }
        return rb;
    }

    // 由前端控制只能修改的字段：应该只能通过续费修改总数量
    @PostMapping("/add")
    public ResponseBean<Integer> add(@RequestBody CustomerNursingService customerNursingService) {
        ResponseBean<Integer> rb = null;
        CustomerNursingService check = customerNursingServiceMapper.getByCustomerIdAndProgramCode(customerNursingService.getCustomerId(), customerNursingService.getProgramCode());
        if (check != null) {
            rb = new ResponseBean<>(500, "不能为一个客户重复添加项目");
            return rb;
        }
//        customerNursingService.setDeleted(false);
        int result = customerNursingServiceMapper.insert(customerNursingService);
        if (result > 0) {
            rb = new ResponseBean<>(result);
        } else {
            rb = new ResponseBean<>(500, "添加失败");
        }
        return rb;
    }

    @PostMapping("/delete")
    public ResponseBean<Integer> delete(@RequestBody Map<String, Object> request) {
        int id = (int) request.get("id");
        ResponseBean<Integer> rb = null;
        CustomerNursingService customerNursingService = customerNursingServiceMapper.selectById(id);
        if (customerNursingService != null) {
            rb = new ResponseBean<>(500, "不存在该id的数据");
            return rb;
        }
//        customerNursingService.setDeleted(true);
//        int result = customerNursingServiceMapper.updateById(customerNursingService);
        int result = customerNursingServiceMapper.deleteById(id);
        if (result > 0) {
            rb = new ResponseBean<>(result);
        } else {
            rb = new ResponseBean<>(500, "删除失败");
        }
        return rb;
    }

    @PostMapping("/deleteByCustomerAndLevel")
    public ResponseBean<Integer> deleteByCustomerAndLevel(@RequestBody Map<String, Object> request) {
        int customerId = (int) request.get("customerId");
        int levelId = (int) request.get("levelId");
        ResponseBean<Integer> rb = null;
        int result = customerNursingServiceMapper.deleteByCustomerIdAndLevelId(customerId, levelId);
        if (result > 0) {
            rb = new ResponseBean<>(result);
        } else {
            rb = new ResponseBean<>(500, "删除失败");
        }
        return rb;
    }

}
