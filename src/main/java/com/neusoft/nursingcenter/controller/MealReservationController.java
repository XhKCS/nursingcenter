package com.neusoft.nursingcenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.nursingcenter.entity.*;
import com.neusoft.nursingcenter.mapper.CustomerMapper;
import com.neusoft.nursingcenter.mapper.MealItemMapper;
import com.neusoft.nursingcenter.mapper.MealReservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;

@CrossOrigin("*")
@RestController
@RequestMapping("/mealReservation")
public class MealReservationController {

    @Autowired
    private MealReservationMapper mealReservationMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private MealItemMapper mealItemMapper;

    @PostMapping("/page")
    public PageResponseBean<List<MealReservation>> page (@RequestBody Map<String, Object> request) throws ParseException {
        int current = (int) request.get("current");
        int size = (int) request.get("size");
        int isDeleted = (int) request.get("isDeleted");
        String startTime = (String) request.get("startTime");
        String endTime = (String) request.get("endTime");

        QueryWrapper<MealReservation> qw = new QueryWrapper<>();
        qw.eq("is_deleted",isDeleted);
        qw.ge(!startTime.isEmpty(),"purchase_time",startTime);
        qw.le(!endTime.isEmpty(),"purchase_time",endTime);
        qw.orderByDesc("purchase_time");

        IPage<MealReservation> page = new Page<>(current,size);
        IPage<MealReservation> result = mealReservationMapper.selectPage(page,qw);
        List<MealReservation> list =result.getRecords();
        long total = result.getTotal();

        PageResponseBean<List<MealReservation>> prb =null;
        if(total > 0) {
            prb = new PageResponseBean<>(list);
            prb.setTotal(total);
        }else {
            prb = new PageResponseBean<>(500,"No data");
        }
        return prb;
    }

    @PostMapping("/getByCustomerId")
    public PageResponseBean<List<MealReservation>> getByCustomerId(@RequestBody Map<String, Object> request){;
        Integer customerId = (Integer) request.get("customerId");

        QueryWrapper<MealReservation> qw = new QueryWrapper<>();
        qw.eq("customer_id", customerId);
        qw.orderByDesc("purchase_time");

        List<MealReservation> result = mealReservationMapper.selectList(qw);

        PageResponseBean<List<MealReservation>> prb =null;
        if(!result.isEmpty()) {
            prb = new PageResponseBean<>(result);
        }else {
            prb = new PageResponseBean<>(500,"No data");
        }
        return prb;
    }

    @PostMapping("/add")
    public ResponseBean<Integer> add(@RequestBody Map<String, Object> request) {
        List<Integer> ids = (List<Integer>) request.get("mealItemIds");
        List<Integer> counts = (List<Integer>) request.get("purchaseCounts");
        Integer customerId = (Integer) request.get("customerId");
        String purchaseTime =(String) request.get("purchaseTime");

        List<MealReservation> mealReservationList = new ArrayList<>();
        Customer customer = customerMapper.selectById(customerId);

        for(int id :ids){
            int count = counts.get(ids.indexOf(id));
            MealItem mealItem = mealItemMapper.selectById(id);
            MealReservation mealReservation = new MealReservation(0,customerId,id,customer.getName(),mealItem.getFoodName(),count,purchaseTime,false);
            mealReservationList.add(mealReservation);
        }


        int result = mealReservationMapper.insertBatchSomeColumn(mealReservationList);
        ResponseBean<Integer> rb = null;
        if(result > 0) {
            rb = new ResponseBean<>(result);
        }else {
            rb = new ResponseBean<>(500,"Fail to add");
        }
        return rb;
    }

    @PostMapping("/update")
    public ResponseBean<Integer> update(@RequestBody MealReservation data) {
        int result = mealReservationMapper.updateById(data);
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
        MealReservation mealReservation = mealReservationMapper.selectById(id);
        mealReservation.setDeleted(true);
        int result = mealReservationMapper.updateById(mealReservation);
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
        List<Integer> ids = (List<Integer>) request.get("ids");
        int result =0;
        for (int id : ids){
            MealReservation mealReservation = mealReservationMapper.selectById(id);
            mealReservation.setDeleted(true);
            result += mealReservationMapper.updateById(mealReservation);
        }

        ResponseBean<Integer> rb =null;
        if(result > 0) {
            rb = new ResponseBean<>(result);
        }else {
            rb = new ResponseBean<>(500,"Fail to delete");
        }

        return rb;
    }
}
