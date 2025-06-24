package com.neusoft.nursingcenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.nursingcenter.entity.*;
import com.neusoft.nursingcenter.mapper.*;
import com.neusoft.nursingcenter.service.NursingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
    private NursingRecordService nursingRecordService;


    @PostMapping("/page")
    public PageResponseBean<List<NursingRecord>> page (@RequestBody Map<String, Object> request){
        int current = (int)request.get("current");
        int size = (int)request.get("size");
        Integer customerId = (Integer)request.get("customerId");

        QueryWrapper<NursingRecord> qw = new QueryWrapper<>();
        qw.eq(null != customerId,"customer_id",customerId);
        qw.eq("is_deleted",false);

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

    @PostMapping("/add")
    public ResponseBean<Integer> add(@RequestBody Map<String, Object> request) {
        Integer result = nursingRecordService.add(request);
        ResponseBean<Integer> rb = null;
        if(result > 0) {
            rb = new ResponseBean<>(result);
        }else {
            rb = new ResponseBean<>(500,"Fail to add");
        }
        return rb;
    }

    @PostMapping("/delete")
    public ResponseBean<Integer> delete(@RequestBody Map<String, Object> request) {
        int id = (int) request.get("id");
        NursingRecord nursingRecord = nursingRecordMapper.selectById(id);
        nursingRecord.setDeleted(true);
        Integer result = nursingRecordMapper.updateById(nursingRecord);
        ResponseBean<Integer> rb =null;
        if(result > 0) {
            rb = new ResponseBean<>(result);
        }else {
            rb = new ResponseBean<>(500,"Fail to delete");
        }

        return rb;
    }

    @PostMapping("/deleteBatch")
    public ResponseBean<Integer> deleteBatch(@RequestBody Map<String, Object> request) {
        List<Integer> ids =(List<Integer>) request.get("ids");
        int result = nursingRecordMapper.deleteBatchIds(ids);
        ResponseBean<Integer> rb =null;
        if(result > 0) {
            rb = new ResponseBean<>(result);
        }else {
            rb = new ResponseBean<>(500,"Fail to delete");
        }

        return rb;
    }
}
