package com.neusoft.nursingcenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.nursingcenter.entity.*;
import com.neusoft.nursingcenter.mapper.MealItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/mealItem")
public class MealItemController {
    @Autowired
    private MealItemMapper mealItemMapper;

    @GetMapping("/page")
    public PageResponseBean<List<MealItem>> page (@RequestBody Map<String, Object> request){
        Long current = (Long)request.get("current");
        Long size = (Long)request.get("size");
        String foodName = (String)request.get("foodName");
        String foodType = (String)request.get("foodType");

        QueryWrapper<MealItem> qw = new QueryWrapper<>();
        qw.like("foodName",foodName);
        qw.eq("foodType",foodType);

        IPage<MealItem> page = new Page<>(current,size);
        IPage<MealItem> result = mealItemMapper.selectPage(page,qw);
        List<MealItem> list = result.getRecords();
        Long total = result.getTotal();

        PageResponseBean<List<MealItem>> prb = null;
        if(total > 0){
            prb = new PageResponseBean<>(list);
            prb.setTotal(total);
        }else {
            prb = new PageResponseBean<>(500, "No data");
        }
        return prb;
    }

    @PostMapping("/add")
    public ResponseBean<Integer> add(@RequestBody MealItem mealItem) {
        Integer result = mealItemMapper.insert(mealItem);
        ResponseBean<Integer> rb = null;
        if(result > 0) {
            rb = new ResponseBean<>(result);
        }else {
            rb = new ResponseBean<>(500,"Fail to add");
        }
        return rb;
    }

    @PostMapping("/update")
    public ResponseBean<Integer> update(@RequestBody MealItem data) {
        Integer result = mealItemMapper.updateById(data);
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
        Integer result = mealItemMapper.deleteById(id);
        ResponseBean<Integer> rb =null;
        if(result > 0) {
            rb = new ResponseBean<>(result);
        }else {
            rb = new ResponseBean<>(500,"Fail to delete");
        }

        return rb;
    }
}
