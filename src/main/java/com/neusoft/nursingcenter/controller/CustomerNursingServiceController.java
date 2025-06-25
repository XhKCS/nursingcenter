package com.neusoft.nursingcenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.nursingcenter.entity.*;
import com.neusoft.nursingcenter.mapper.CustomerMapper;
import com.neusoft.nursingcenter.mapper.CustomerNursingServiceMapper;
import com.neusoft.nursingcenter.mapper.NursingLevelMapper;
import io.swagger.v3.oas.models.security.SecurityScheme;
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

    @Autowired
    private NursingLevelMapper nursingLevelMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @PostMapping("/page")
    PageResponseBean<List<CustomerNursingService>> pageWithConditions(@RequestBody Map<String, Object> request) {
        int current = (int)request.get("current");
        int size = (int)request.get("size");
        int customerId = (int) request.get("customerId");
        String programName = (String)request.get("programName");

        QueryWrapper<CustomerNursingService> qw = new QueryWrapper<>();
        qw.eq("customer_id", customerId);
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

    @PostMapping("/addBatch")
    public ResponseBean<String> addBatch(@RequestBody List<CustomerNursingService> customerNursingServiceList) {
        ResponseBean<String> rb = null;
        int count = 0;
        for (CustomerNursingService service : customerNursingServiceList) {
            CustomerNursingService check = customerNursingServiceMapper.getByCustomerIdAndProgramCode(service.getCustomerId(), service.getProgramCode());
            System.out.println(service.getProgramName());
            if (check == null) {
                int res = customerNursingServiceMapper.insert(service);
                if (res > 0) {
                    count++;
                    System.out.println("添加成功，count = "+count);
                }
            }
        }
        if (count > 0) {
            rb = new ResponseBean<>("成功为客户添加了"+count+"个护理项目");
        } else {
            rb = new ResponseBean<>(500, "批量添加服务失败");
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

    // 移除客户的护理级别
    @PostMapping("/deleteByCustomer")
    public ResponseBean<Integer> deleteByCustomerAndLevel(@RequestBody Customer customer) {
        NursingLevel nursingLevel = nursingLevelMapper.getByName(customer.getNursingLevelName());
        ResponseBean<Integer> rb = null;
        try {
            int res1 = customerNursingServiceMapper.deleteByCustomerIdAndLevelId(customer.getCustomerId(), nursingLevel.getId());
            customer.setNursingLevelName(""); //所属护理级别设置为空
            int result = customerMapper.updateById(customer);
            if (result > 0) {
                rb = new ResponseBean<>(result);
            } else {
                rb = new ResponseBean<>(500, "移除失败");
            }
        } catch (Exception e) {
            rb = new ResponseBean<>(500, e.getMessage());
        }
        return rb;
    }

}
