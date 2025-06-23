package com.neusoft.nursingcenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.nursingcenter.entity.*;
import com.neusoft.nursingcenter.mapper.NursingRecordMapper;
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

    @PostMapping("/page")
    public PageResponseBean<List<NursingRecord>> page (@RequestBody Map<String, Object> request){
        int current = (int)request.get("current");
        int size = (int)request.get("size");
        Integer customerId = (Integer)request.get("customerId");

        LambdaQueryWrapper<NursingRecord> qw = new LambdaQueryWrapper<>();
        qw.eq(null != customerId,NursingRecord :: getCustomerId,customerId);
        qw.eq(NursingRecord :: getDeleted,false);

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
    public ResponseBean<Integer> add(@RequestBody NursingRecord nursingRecord) {
        nursingRecord.setDeleted(false);
        Integer result = nursingRecordMapper.insert(nursingRecord);
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
}
