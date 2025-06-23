package com.neusoft.nursingcenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.nursingcenter.entity.*;
import com.neusoft.nursingcenter.mapper.FoodMapper;
import com.neusoft.nursingcenter.mapper.MealItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

@CrossOrigin("*")
@RestController
@RequestMapping("/mealItem")
public class MealItemController {
    @Autowired
    private MealItemMapper mealItemMapper;

    @Autowired
    private FoodMapper foodMapper;

    @RequestMapping("/page")
    public PageResponseBean<List<MealItem>> page (@RequestBody Map<String, Object> request) {
        int current = (int) request.get("current");
        int size = (int) request.get("size");
        String foodName = (String) request.get("foodName");

        List<String> foodType = (List<String>) request.get("foodType");
        List<String> weekDay = (List<String>) request.get("weekDay");

        LambdaQueryWrapper<MealItem> qw = new LambdaQueryWrapper<>();

        qw.like(MealItem::getFoodName, foodName);
        Consumer<LambdaQueryWrapper<MealItem>> consumerf = null;
        if (!foodType.isEmpty()) {
            consumerf = wrapper -> {
                for (String f : foodType) {
                    wrapper.or().eq(null != f, MealItem::getFoodType, f);
                }
            };
            qw.and(consumerf);
        }
        Consumer<LambdaQueryWrapper<MealItem>> consumerw = null;
        if (!weekDay.isEmpty()) {
            consumerw = wrapper -> {
                for (String w : weekDay) {
                    wrapper.or().eq(null != w, MealItem::getWeekDay, w);
                }
            };
            qw.and(consumerw);
        }

        IPage<MealItem> page = new Page<>(current, size);
        IPage<MealItem> result = mealItemMapper.selectPage(page, qw);
        List<MealItem> list = result.getRecords();
        long total = result.getTotal();

        PageResponseBean<List<MealItem>> prb = null;
        if (total > 0) {
            prb = new PageResponseBean<>(list);
            prb.setTotal(total);
        } else {
            prb = new PageResponseBean<>(500, "No data");
        }
        return prb;
    }

    @PostMapping("/add")
    public ResponseBean<Integer> add(@RequestBody Map<String, Object> request) {
        ResponseBean<Integer> rb = null;
        List<String> foodNames = new ArrayList<>();
        List<String> weekDays = new ArrayList<>();
        if(request.get("foodName") instanceof String){
            foodNames.add((String) request.get("foodName"));
        }else {
            foodNames = (List<String>) request.get("foodName");
        }

        if(request.get("weekDay") instanceof String){
            weekDays.add((String) request.get("weekDay"));
        }else {
            weekDays = (List<String>) request.get("weekDay");
        }

        int status = (int) request.get("status");

        List<MealItem> mealItemList = new ArrayList<>();

        for (String weekDay :weekDays){
            for(String foodName : foodNames){
                Food food = foodMapper.getByName(foodName);
                MealItem check = mealItemMapper.getByFoodIdAndWeekDay(food.getId(), weekDay);
                if (check != null) {
                    System.out.println("相同周期内不能存在重名的膳食安排");
                    rb = new ResponseBean<>(500, "相同周期内不能存在重名的膳食安排"+"("+foodName+"-"+weekDay+")");
                    return rb;
                }
                MealItem mealItem = new MealItem(0, food.getId(), foodName, food.getType(), food.getDescription(), food.getPrice(), food.getImageUrl(), weekDay, status);

                mealItemList.add(mealItem);
            }
        }

        int result = mealItemMapper.insertBatchSomeColumn(mealItemList);
        if(result > 0) {
            rb = new ResponseBean<>(result);
        }else {
            rb = new ResponseBean<>(500,"Fail to add");
        }
        return rb;
    }

    @PostMapping("/update")
    public ResponseBean<Integer> update(@RequestBody Map<String, Object> request) {
        ResponseBean<Integer> rb = null;
        String foodName = (String) request.get("foodName");
        String weekDay = (String) request.get("weekDay");
        int status = (int) request.get("status");
        int id = (int) request.get("id");
        Food food = foodMapper.getByName(foodName);
        MealItem check = mealItemMapper.getByFoodIdAndWeekDay(food.getId(), weekDay);
        if (check != null && !Objects.equals(check.getId(), food.getId())) {
            rb = new ResponseBean<>(500, "相同周期内不能存在重名的膳食安排");
            return rb;
        }
        MealItem mealItem = new MealItem(id, food.getId(), foodName, food.getType(), food.getDescription(), food.getPrice(), food.getImageUrl(), weekDay, status);
        int result = mealItemMapper.updateById(mealItem);
        System.out.println(result);
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

    @PostMapping("/deleteBatch")
    public ResponseBean<Integer> deleteBatch(@RequestBody Map<String, Object> request) {
        List<Integer> ids =(List<Integer>) request.get("ids");
        int result = mealItemMapper.deleteBatchIds(ids);
        ResponseBean<Integer> rb =null;
        if(result > 0) {
            rb = new ResponseBean<>(result);
        }else {
            rb = new ResponseBean<>(500,"Fail to delete");
        }

        return rb;
    }
}
