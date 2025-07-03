package com.neusoft.nursingcenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.nursingcenter.entity.*;
import com.neusoft.nursingcenter.mapper.FoodMapper;
import com.neusoft.nursingcenter.mapper.MealItemMapper;
import com.neusoft.nursingcenter.service.FoodService;
import com.neusoft.nursingcenter.util.FoodStructOutputUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@CrossOrigin("*")
@RestController
@RequestMapping("/food")
public class FoodController {
    @Autowired
    private FoodMapper foodMapper;

    @Autowired
    private MealItemMapper mealItemMapper;

    @Autowired
    private FoodService foodService;

    @Resource
    private FoodStructOutputUtil foodStructOutputUtil;

    @PostMapping("/getById")
    public ResponseBean<Food> getById(@RequestBody Map<String, Object> request){
        int id = (int)request.get("id");
        Food food = foodMapper.selectById(id);
        ResponseBean<Food> rb =null;
        if(food!=null){
            rb = new ResponseBean<>(food);
        }else {
            rb = new ResponseBean<>(500,"No data");
        }
        return rb;
    }

    @PostMapping("/getByName")
    public ResponseBean<Food> getByName(@RequestBody Map<String, Object> request){
        String name = (String)request.get("name");
        Food food = foodMapper.getByName(name);
        ResponseBean<Food> rb =null;
        if(food!=null){
            rb = new ResponseBean<>(food);
        }else {
            rb = new ResponseBean<>(500,"No data");
        }
        return rb;
    }

    @PostMapping("/list")
    public ResponseBean<List<Food>> list(@RequestBody Map<String, Object> request){
        List<Food> foods = foodMapper.selectList(null);
        ResponseBean<List<Food>> rb =null;
        if(foods!=null){
            rb = new ResponseBean<>(foods);
        }else {
            rb = new ResponseBean<>(500,"No data");
        }
        return rb;
    }

    @PostMapping("/page")
    public PageResponseBean<List<Food>> page (@RequestBody Map<String, Object> request){
        int current = (int)request.get("current");
        int size = (int)request.get("size");
        String name = (String)request.get("name");
        List<String> type = (List<String>) request.get("type");

        QueryWrapper<Food> qw = new QueryWrapper<>();
        qw.like("name", name);

        Consumer<QueryWrapper<Food>> consumer = null;
        if (!type.isEmpty()) {
            consumer = wrapper -> {
                for (String f : type) {
                    wrapper.or().eq(null != f, "type", f);
                }
            };
            qw.and(consumer);
        }

        IPage<Food> page = new Page<>(current,size);
        IPage<Food> result = foodMapper.selectPage(page,qw);
        List<Food> list = result.getRecords();
        long total = result.getTotal();

        PageResponseBean<List<Food>> prb = null;
        if(total > 0){
            prb = new PageResponseBean<>(list);
            prb.setTotal(total);
        }else {
            prb = new PageResponseBean<>(500, "No data");
        }
        return prb;
    }

    @PostMapping("/listByType")
    public ResponseBean<List<Food>> listByType(@RequestBody Map<String, Object> request) {
        List<String> types = new ArrayList<>();

        if(request.get("foodType") instanceof String){
            types.add((String) request.get("foodType"));
        }else {
            types = (List<String>) request.get("foodType");
        }

        ResponseBean<List<Food>> rb = null;
        if(types.isEmpty()){
            rb = new ResponseBean<>(500, "No data");
            return rb;
        }
        LambdaQueryWrapper<Food> lqw = new LambdaQueryWrapper<>();
        for (String type :types){
            lqw.or().eq(Food ::getType,type);
        }
        List<Food> foodList = foodMapper.selectList(lqw);

        if (foodList.size() > 0) {
            rb = new ResponseBean<>(foodList);
        } else {
            rb = new ResponseBean<>(500, "No data");
        }
        return rb;
    }

//    @PostMapping("/listByTypeAndWeekDay")
//    public ResponseBean<List<Food>> listByTypeAndWeekDay(@RequestBody Map<String, Object> request) {
//        String weekDay = (String) request.get("weekDay");
//        String foodType = (String) request.get("foodType");
//
//        LambdaQueryWrapper<MealItem> lqw = new LambdaQueryWrapper<>();
//        lqw.eq(MealItem ::getFoodType,foodType).eq(MealItem :: getWeekDay, weekDay);
//        List<MealItem> mList = mealItemMapper.selectList(lqw);
//
//        ResponseBean<List<Food>> rb = null;
//        if(mList.isEmpty()){
//            rb = new ResponseBean<>(500,"No data");
//            return  rb;
//        }
//
//        LambdaQueryWrapper<Food> flqw =new LambdaQueryWrapper<>();
//        for(MealItem mealItem : mList){
//            flqw.or().eq(Food ::getId,mealItem.getFoodId());
//        }
//        List<Food> foodList = foodMapper.selectList(flqw);
//
//        if (foodList.size() > 0) {
//            rb = new ResponseBean<>(foodList);
//        } else {
//            rb = new ResponseBean<>(500, "No data");
//        }
//        return rb;
//    }

    // 要先检查重名
    @PostMapping("/add")
    public ResponseBean<Integer> add(@RequestBody Map<String, Object> request) {
        ResponseBean<Integer> rb = null;

        String name = (String) request.get("name");
        String description = (String) request.get("description");
        double price;
        if(request.get("price") instanceof Double){
            price = (double) request.get("price");
        }else {
            int pi = (int) request.get("price");
            price = (double) pi;
        }

        String imageUrl = (String) request.get("imageUrl");

        List<String> type = new ArrayList<>();
        if(request.get("type") instanceof String){
            type.add((String) request.get("type"));
        }else {
            type = (List<String>) request.get("type");
        }

        List<Food> foodList = new ArrayList<>();

        for(String t : type){
            QueryWrapper<Food> qw = new QueryWrapper<>();
            qw.eq("name",name).eq("type",t);
            Food check = foodMapper.selectOne(qw);
            if (check != null) {
                rb = new ResponseBean<>(500, "不能在同一类型添加重名的食品");
                return rb;
            }
            Food food = new Food(0,name,t,description,price,imageUrl);
            foodList.add(food);
        }

        int result = foodMapper.insertBatchSomeColumn(foodList);
        if(result > 0) {
            rb = new ResponseBean<>(result);
        }else {
            rb = new ResponseBean<>(500,"Fail to add");
        }
        return rb;
    }

    // 需要级联更新
    @PostMapping("/update")
    public ResponseBean<Integer> update(@RequestBody Food data) {
        ResponseBean<Integer> rb = null;
        try {
            int result = foodService.updateFood(data);
            if(result > 0) {
                rb = new ResponseBean<>(result);
            }else {
                rb = new ResponseBean<>(500,"Fail to update");
            }
        } catch (Exception e) {
            rb = new ResponseBean<>(500, e.getMessage());
        }
        return rb;
    }

    // 需要级联删除
    @PostMapping("/delete")
    public ResponseBean<Integer> delete(@RequestBody Map<String, Object> request) {
        int id = (int) request.get("id");
        ResponseBean<Integer> rb = null;
        try {
            int result = foodService.deleteFoodById(id);
            if(result > 0) {
                rb = new ResponseBean<>(result);
            }else {
                rb = new ResponseBean<>(500,"Fail to update");
            }
        } catch (Exception e) {
            rb = new ResponseBean<>(500, e.getMessage());
        }
        return rb;
    }

    @PostMapping("/deleteBatch")
    public ResponseBean<Integer> deleteBatch(@RequestBody Map<String, Object> request) {
        List<Integer> ids =(List<Integer>) request.get("ids");
        ResponseBean<Integer> rb = null;
        try {
            int result = foodService.deleteFoodByIds(ids);
            if(result > 0) {
                rb = new ResponseBean<>(result);
            }else {
                rb = new ResponseBean<>(500,"Fail to update");
            }
        } catch (Exception e) {
            rb = new ResponseBean<>(500, e.getMessage());
        }
        return rb;
    }


    @PostMapping("/getPurchaseByIdAndTime")
    public ResponseBean<Integer> getPurchaseByIdAndTime(@RequestBody Map<String, Object> request) {
        int foodId = (int) request.get("foodId");
        String startTime = (String) request.get("startTime");
        String endTime = (String) request.get("endTime");

        int purchaseCount = foodService.getPurchaseByIdAndTime(foodId,startTime,endTime);

        ResponseBean<Integer> rb = null;

        if(purchaseCount >= 0) {
            rb = new ResponseBean<>(purchaseCount);
        }else {
            rb = new ResponseBean<>(500,"Fail to get");
        }
        return rb;
    }

    @PostMapping("/aiObj")
    public ResponseBean<Food> aiCreateObj(@RequestBody Map<String, Object> request){
        String query = (String)request.get("query");
        ResponseBean<Food> rb =null;
        try {
            Food food = foodStructOutputUtil.chatObj(query);
            if(food!=null){
                System.out.println(food.toString());
                rb = new ResponseBean<>(food);
            }else {
                rb = new ResponseBean<>(500,"No data");
            }
        } catch (Exception e) {
            System.out.println("Exception happened: ");
            e.printStackTrace();
            rb = new ResponseBean<>(500, e.getMessage());
        }
        return rb;
    }
}
