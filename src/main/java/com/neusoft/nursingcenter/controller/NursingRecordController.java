package com.neusoft.nursingcenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.nursingcenter.entity.*;
import com.neusoft.nursingcenter.mapper.CustomerNursingServiceMapper;
import com.neusoft.nursingcenter.mapper.NursingRecordMapper;
import com.neusoft.nursingcenter.service.NursingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/nursingRecord")
public class NursingRecordController {

    @Autowired
    private NursingRecordMapper nursingRecordMapper;

    @Autowired
    private CustomerNursingServiceMapper customerNursingServiceMapper;

    @Autowired
    private NursingRecordService nursingRecordService;


    @PostMapping("/page")
    public PageResponseBean<List<NursingRecord>> page (@RequestBody Map<String, Object> request){
        int current = (int)request.get("current");
        int size = (int)request.get("size");
        Integer customerId = (Integer)request.get("customerId");

        QueryWrapper<NursingRecord> qw = new QueryWrapper<>();
        qw.eq(null != customerId,"customer_id",customerId);
        qw.eq("is_deleted", 0);

        IPage<NursingRecord> page = new Page<>(current,size);
        IPage<NursingRecord> result = nursingRecordMapper.selectPage(page,qw);

        List<NursingRecord> list = result.getRecords();
        long total = result.getTotal();

        PageResponseBean<List<NursingRecord>> prb = null;
        if(total > 0){
            prb = new PageResponseBean<>(list);
            prb.setTotal(total);
        }else {
            prb = new PageResponseBean<>(500, "No data");
        }
        return prb;
    }

    // 添加护理记录单同时，要改变对应客户护理服务的剩余数量，也就是增加usedCount
//    @PostMapping("/add")
//    public ResponseBean<Integer> add(@RequestBody NursingRecord nursingRecord) {
//        CustomerNursingService customerNursingService = customerNursingServiceMapper.getByCustomerIdAndProgramCode(nursingRecord.getCustomerId(), nursingRecord.getProgramCode());
//        if (customerNursingService == null) {
//            return new ResponseBean<>(500, "该客户护理服务不存在");
//        }
//        int oldCount = customerNursingService.getUsedCount();
//        customerNursingService.setUsedCount(oldCount + nursingRecord.getExecutionCount());
//        int res1 = customerNursingServiceMapper.updateById(customerNursingService);
//        if (res1 <= 0) {
//            return new ResponseBean<>(500, "更新客户护理服务信息时失败");
//        }
//
//        nursingRecord.setDeleted(false);
//        int result = nursingRecordMapper.insert(nursingRecord);
//        ResponseBean<Integer> rb = null;
//        if(result > 0) {
//            rb = new ResponseBean<>(result);
//        }else {
//            rb = new ResponseBean<>(500,"Fail to add");
//        }
//        return rb;
//    }

    @PostMapping("/add")
    public ResponseBean<Integer> add(@RequestBody Map<String, Object> request) {
        ResponseBean<Integer> rb = null;
        try {
            int result = nursingRecordService.add(request);
            if (result > 0) {
                rb = new ResponseBean<>(result);
            } else {
                rb = new ResponseBean<>(500, "Fail to add");
            }
        } catch (Exception e) {
            rb = new ResponseBean<>(500, e.getMessage());
        }
        return rb;
    }

    @PostMapping("/delete")
    public ResponseBean<Integer> delete(@RequestBody Map<String, Object> request) {
        int id = (int) request.get("id");
        NursingRecord nursingRecord = nursingRecordMapper.selectById(id);
        nursingRecord.setIsDeleted(true);
        int result = nursingRecordMapper.updateById(nursingRecord);
        ResponseBean<Integer> rb =null;
        if(result > 0) {
            rb = new ResponseBean<>(result);
        }else {
            rb = new ResponseBean<>(500,"Fail to delete");
        }

        return rb;
    }

    @PostMapping("/deleteBatch")
    public ResponseBean<String> deleteBatch(@RequestBody List<NursingRecord> recordList) {
        ResponseBean<String> rb = null;
        try {
            for (NursingRecord record : recordList) {
                record.setIsDeleted(true);
                int result = nursingRecordMapper.updateById(record);
                if (result <= 0) {
                    return new ResponseBean<>(500, "删除过程中失败");
                }
            }
            rb = new ResponseBean<>("删除成功，共删除"+recordList.size()+"条数据");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            rb = new ResponseBean<>(500, e.getMessage());
        }
        return rb;
    }

    @PostMapping("/deleteBatchByIds")
    public ResponseBean<String> deleteBatchByIds(@RequestBody Map<String, Object> request) {
        ResponseBean<String> rb = null;

        List<Integer> ids = (List<Integer>) request.get("ids");
        try {
            for (Integer id : ids) {
                NursingRecord record = nursingRecordMapper.selectById(id);
                record.setIsDeleted(true);
                int result = nursingRecordMapper.updateById(record);
                if (result <= 0) {
                    return new ResponseBean<>(500, "删除过程中失败");
                }
            }
            rb = new ResponseBean<>("删除成功，共删除"+ids.size()+"条数据");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            rb = new ResponseBean<>(500, e.getMessage());
        }
        return rb;
    }

    // 由于是逻辑删除，所以不能用以下方法进行批量删除
//    @PostMapping("/deleteBatch")
//    public ResponseBean<Integer> deleteBatch(@RequestBody Map<String, Object> request) {
//        List<Integer> ids =(List<Integer>) request.get("ids");
//        int result = nursingRecordMapper.deleteBatchIds(ids);
//        ResponseBean<Integer> rb =null;
//        if(result > 0) {
//            rb = new ResponseBean<>(result);
//        }else {
//            rb = new ResponseBean<>(500,"Fail to delete");
//        }
//
//        return rb;
//    }
}
