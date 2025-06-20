package com.neusoft.nursingcenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.nursingcenter.entity.*;
import com.neusoft.nursingcenter.mapper.FoodMapper;
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

    @Autowired
    private FoodMapper foodMapper;

    @RequestMapping("/page")
    public PageResponseBean<List<MealItem>> page (@RequestBody Map<String, Object> request){
        int current = (int)request.get("current");
        int size = (int)request.get("size");
        String foodName = (String)request.get("foodName");
        String foodType = (String)request.get("foodType");
        String weekDay = (String) request.get("weekDay");

        QueryWrapper<MealItem> qw = new QueryWrapper<>();
        qw.like("foodName",foodName);
        qw.eq("foodType",foodType);
        qw.eq("week_day", weekDay);

        IPage<MealItem> page = new Page<>(current,size);
        IPage<MealItem> result = mealItemMapper.selectPage(page,qw);
        List<MealItem> list = result.getRecords();
        long total = result.getTotal();

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
    public ResponseBean<Integer> add(@RequestBody Map<String, Object> request) {
        ResponseBean<Integer> rb = null;
        String foodName = (String) request.get("foodName");
        String weekDay = (String) request.get("weekDay");
        int status = (int) request.get("status");

        Food food = foodMapper.getByName(foodName);
        if (food == null) {
            return new ResponseBean<>(500, "不存在该名称的食品");
        }
        MealItem check = mealItemMapper.getByFoodIdAndWeekDay(food.getId(), weekDay);
        if (check != null) {
            rb = new ResponseBean<>(500, "相同周期内不能存在重名的膳食安排");
            return rb;
        }
        MealItem mealItem = new MealItem(0, food.getId(), food.getName(), food.getType(), food.getDescription(), food.getPrice(), food.getImageUrl(), weekDay, status);
        int result = mealItemMapper.insert(mealItem);
        if(result > 0) {
            rb = new ResponseBean<>(result);
        }else {
            rb = new ResponseBean<>(500,"Fail to add");
        }
        return rb;
    }

    @PostMapping("/update")
    public ResponseBean<Integer> update(@RequestBody MealItem data) {
        ResponseBean<Integer> rb = null;
        MealItem check = mealItemMapper.getByFoodIdAndWeekDay(data.getFoodId(), data.getWeekDay());
        if (check != null && check.getId() != data.getId()) {
            rb = new ResponseBean<>(500, "相同周期内不能存在重名的膳食安排");
            return rb;
        }
        int result = mealItemMapper.updateById(data);
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
        int result = mealItemMapper.deleteById(id);
        ResponseBean<Integer> rb =null;
        if(result > 0) {
            rb = new ResponseBean<>(result);
        }else {
            rb = new ResponseBean<>(500,"Fail to delete");
        }

        return rb;
    }
}
