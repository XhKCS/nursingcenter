package com.neusoft.nursingcenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.nursingcenter.entity.Customer;
import com.neusoft.nursingcenter.entity.DietConfigItem;
import com.neusoft.nursingcenter.entity.PageResponseBean;
import com.neusoft.nursingcenter.entity.ResponseBean;
import com.neusoft.nursingcenter.mapper.DietConfigItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/dietConfigItem")
public class DietConfigItemController {

    @Autowired
    private DietConfigItemMapper dietConfigItemMapper;

    @PostMapping("/page")
    public PageResponseBean<List<DietConfigItem>> getPagedCustomersByName(@RequestBody Map<String, Object> request){
        int current = (int) request.get("current");
        int size = (int) request.get("size");
        Integer customerId = (Integer) request.get("customerId");
        String  name =(String) request.get("name");

        LambdaQueryWrapper<DietConfigItem> qw = new LambdaQueryWrapper<DietConfigItem>();
        qw.eq(DietConfigItem ::getCustomerId, customerId);
        qw.like(DietConfigItem ::getName,name);

        IPage<DietConfigItem> page = new Page<>(current,size);
        IPage<DietConfigItem> result = dietConfigItemMapper.selectPage(page,qw);
        List<DietConfigItem> list =result.getRecords();
        long total =result.getTotal();

        PageResponseBean<List<DietConfigItem>> prb =null;
        if(total > 0) {
            prb = new PageResponseBean<>(list);
            prb.setTotal(total);
        }else {
            prb = new PageResponseBean<>(500,"No data");
        }
        return prb;
    }

    @PostMapping("/add")
    public ResponseBean<Integer> add(@RequestBody DietConfigItem dietConfigItem) {
        int result = dietConfigItemMapper.insert(dietConfigItem);
        ResponseBean<Integer> rb = null;
        if(result > 0) {
            rb = new ResponseBean<>(result);
        }else {
            rb = new ResponseBean<>(500,"Fail to add");
        }
        return rb;
    }

    @PostMapping("/update")
    public ResponseBean<Integer> update(@RequestBody DietConfigItem data) {
        int result = dietConfigItemMapper.updateById(data);
        ResponseBean<Integer> rb = null;
        if(result > 0) {
            rb = new ResponseBean<>(result);
        }else {
            rb = new ResponseBean<>(500,"Fail to update");
        }
        return rb;
    }

    @PostMapping("/delete")
    public ResponseBean<Integer> delete(@RequestBody Map<String, Object> request) {
        int id = (int) request.get("id");
        int result = dietConfigItemMapper.deleteById(id);
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
        int result = dietConfigItemMapper.deleteBatchIds(ids);
        ResponseBean<Integer> rb =null;
        if(result > 0) {
            rb = new ResponseBean<>(result);
        }else {
            rb = new ResponseBean<>(500,"Fail to delete");
        }

        return rb;
    }

}
