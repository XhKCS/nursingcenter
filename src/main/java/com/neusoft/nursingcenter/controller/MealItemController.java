package com.neusoft.nursingcenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.nursingcenter.entity.*;
import com.neusoft.nursingcenter.mapper.FoodMapper;
import com.neusoft.nursingcenter.mapper.MealItemMapper;
import com.neusoft.nursingcenter.service.FoodService;
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

    @Autowired
    private FoodService foodService;

    @PostMapping("/page")
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
//        System.out.println(list.toString());
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

    @PostMapping("/listByWeekday")
    public ResponseBean<List<MealItem>> listByWeekday(@RequestBody Map<String, Object> request) {
        String weekDay = (String) request.get("weekday");

        List<MealItem> mealItemList = mealItemMapper.listByWeekDay(weekDay);

        ResponseBean<List<MealItem>> rb = null;
        if(!mealItemList.isEmpty()) {
            rb = new ResponseBean<>(mealItemList);
        }else {
            rb = new ResponseBean<>(500,"No data");
        }
        return rb;
    }

    @PostMapping("/getById")
    public ResponseBean<MealItem> getById(@RequestBody Map<String, Object> request) {
        int id = (int) request.get("id");

        MealItem mealItem = mealItemMapper.selectById(id);

        ResponseBean<MealItem> rb = null;
        if(mealItem!=null) {
            rb = new ResponseBean<>(mealItem);
        }else {
            rb = new ResponseBean<>(500,"No data");
        }
        return rb;
    }

    @PostMapping("/add")
    public ResponseBean<Integer> add(@RequestBody Map<String, Object> request) {
        ResponseBean<Integer> rb = null;
        List<Integer> ids = new ArrayList<>();
        List<String> weekDays = new ArrayList<>();
        if(request.get("foodId") instanceof Integer){
            ids.add((Integer) request.get("foodId"));
        }else {
            ids = (List<Integer>) request.get("foodId");
        }

        if(request.get("weekDay") instanceof String){
            weekDays.add((String) request.get("weekDay"));
        }else {
            weekDays = (List<String>) request.get("weekDay");
        }

        int status = (int) request.get("status");

        List<MealItem> mealItemList = new ArrayList<>();

        for (String weekDay :weekDays){
            for(Integer id : ids){
                Food food = foodMapper.selectById(id);
                MealItem check = mealItemMapper.getByFoodIdAndWeekDay(id, weekDay);
                if (check != null) {
                    System.out.println("相同周期内不能存在重名的膳食安排");
                    rb = new ResponseBean<>(500, "相同周期内不能存在重名的膳食安排"+"("+food.getName()+"-"+weekDay+")");
                    return rb;
                }
                MealItem mealItem = new MealItem(0, food.getId(), food.getName(), food.getType(), food.getDescription(), food.getPrice(), food.getImageUrl(), weekDay, status);

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
        String weekDay = (String) request.get("weekDay");
        int status = (int) request.get("status");
        int foodId = (int) request.get("foodId");
        int id = (int) request.get("id");
        Food food = foodMapper.selectById(foodId);
        MealItem check = mealItemMapper.getByFoodIdAndWeekDay(foodId, weekDay);
        if (check != null && id!=check.getId()) {
            rb = new ResponseBean<>(500, "相同周期内不能存在同一食物膳食安排");
            return rb;
        }
        MealItem mealItem = new MealItem(id, foodId, food.getName(), food.getType(), food.getDescription(), food.getPrice(), food.getImageUrl(), weekDay, status);
        int result = mealItemMapper.updateById(mealItem);
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

    @PostMapping("/getPurchaseByIdAndTime")
    public ResponseBean<Integer> getPurchaseByIdAndTime(@RequestBody Map<String, Object> request) {
        int mealItemId = (int) request.get("mealItemId");
        String startTime = (String) request.get("startTime");
        String endTime = (String) request.get("endTime");

        MealItem mealItem = mealItemMapper.selectById(mealItemId);

        int purchaseCount = foodService.getPurchaseByIdAndTime(mealItem.getFoodId(),startTime,endTime);

        ResponseBean<Integer> rb = null;

        if(purchaseCount >= 0) {
            rb = new ResponseBean<>(purchaseCount);
        }else {
            rb = new ResponseBean<>(500,"Fail to get");
        }

        return rb;
    }
}
