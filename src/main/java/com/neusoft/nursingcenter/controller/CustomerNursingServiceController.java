package com.neusoft.nursingcenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.nursingcenter.entity.*;
import com.neusoft.nursingcenter.mapper.*;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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

    @Autowired
    private LevelWithProgramMapper levelWithProgramMapper;

    @Autowired
    private NursingProgramMapper nursingProgramMapper;

    @PostMapping("/page")
    public PageResponseBean<List<CustomerNursingService>> pageWithConditions(@RequestBody Map<String, Object> request) {
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
//        System.out.println(total);

        PageResponseBean<List<CustomerNursingService>> prb = null;
        if(total > 0){
            prb = new PageResponseBean<>(list);
            prb.setTotal(total);
        }else {
            prb = new PageResponseBean<>(500, "No data");
        }
        return prb;
    }

    @PostMapping("/pageAvailableProgramsByCustomer")
    public PageResponseBean<List<NursingProgram>> pageAvailableProgramsByCustomer(@RequestBody Map<String, Object> request) {
        PageResponseBean<List<NursingProgram>> prb = null;
        int current = (int) request.get("current");
        int size = (int) request.get("size");
        int customerId = (int) request.get("customerId");
        String programName = (String) request.get("programName");

        try {
            QueryWrapper<NursingProgram> qw = new QueryWrapper<>();
            qw.like("name", programName);
            // 不和已购买的项目重复
            List<CustomerNursingService> customerNursingServiceList = customerNursingServiceMapper.listByCustomerId(customerId);
            if (customerNursingServiceList.size() > 0) {
                Consumer<QueryWrapper<NursingProgram>> consumer1 = qw2 -> {
                    for (CustomerNursingService service : customerNursingServiceList) {
                        qw2.ne("id", service.getProgramId());
                    }
                };
                qw.and(consumer1);
            }
            // 在客户的护理级别下选择
            Customer customer = customerMapper.selectById(customerId);
            NursingLevel nursingLevel = nursingLevelMapper.getByName(customer.getNursingLevelName());
            List<LevelWithProgram> levelWithProgramList = levelWithProgramMapper.listByLevelId(nursingLevel.getId());
            if (levelWithProgramList.isEmpty()) {
                return new PageResponseBean<>(500, "No data");
            }
            Consumer<QueryWrapper<NursingProgram>> consumer2 = qw3 -> {
                for (LevelWithProgram lwp : levelWithProgramList) {
                    qw3.or().eq("id", lwp.getProgramId());
                }
            };
            qw.and(consumer2);
            IPage<NursingProgram> page = new Page<>(current,size);
            IPage<NursingProgram> result = nursingProgramMapper.selectPage(page, qw);
            List<NursingProgram> list = result.getRecords();
            long total = result.getTotal();
            if(total > 0) {
                prb = new PageResponseBean<>(list);
                prb.setTotal(total);
            } else {
                prb = new PageResponseBean<>(500, "No data");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            prb = new PageResponseBean<>(500, e.getMessage());
        }

        return prb;
    }

    // 由前端控制只能修改的字段：应该只能通过续费修改总数量和到期时间
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
        if (customerNursingService == null) {
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

    @PostMapping("/deleteBatch")
    public ResponseBean<String> deleteBatch(@RequestBody List<CustomerNursingService> customerNursingServiceList) {
        ResponseBean<String> rb = null;
        try {
            int count = 0;
            for (CustomerNursingService service : customerNursingServiceList) {
                int res = customerNursingServiceMapper.deleteById(service.getId());
                if (res > 0) count++;
            }
            if (count > 0) {
                rb = new ResponseBean<>("成功为客户移除了"+count+"个护理项目");
            } else {
                rb = new ResponseBean<>(500, "批量移除失败");
            }
        } catch (Exception e) {
            rb = new ResponseBean<>(500, e.getMessage());
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
