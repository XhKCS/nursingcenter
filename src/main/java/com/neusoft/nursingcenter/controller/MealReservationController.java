package com.neusoft.nursingcenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.nursingcenter.entity.DietConfigItem;
import com.neusoft.nursingcenter.entity.MealReservation;
import com.neusoft.nursingcenter.entity.PageResponseBean;
import com.neusoft.nursingcenter.entity.ResponseBean;
import com.neusoft.nursingcenter.mapper.MealReservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/mealReservation")
public class MealReservationController {

    @Autowired
    private MealReservationMapper mealReservationMapper;

    @GetMapping("/page")
    public PageResponseBean<List<MealReservation>> getPagedCustomersByName(@RequestBody Map<String, Object> request){
        Long current = (Long) request.get("current");
        Long size = (Long) request.get("size");
        Integer customerId = (Integer) request.get("customerId");

        QueryWrapper<MealReservation> qw = new QueryWrapper<>();
        qw.eq("customerId", customerId);

        IPage<MealReservation> page = new Page<>(current,size);
        IPage<MealReservation> result = mealReservationMapper.selectPage(page,qw);
        List<MealReservation> list =result.getRecords();
        Long total =result.getTotal();

        PageResponseBean<List<MealReservation>> prb =null;
        if(total > 0) {
            prb = new PageResponseBean<>(list);
            prb.setTotal(total);
        }else {
            prb = new PageResponseBean<>(500,"No data");
        }
        return prb;
    }

    @PostMapping("/add")
    public ResponseBean<Integer> add(@RequestBody MealReservation mealReservation) {
        Integer result = mealReservationMapper.insert(mealReservation);
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
        Integer result = mealReservationMapper.updateById(data);
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
        Integer result = mealReservationMapper.deleteById(id);
        ResponseBean<Integer> rb =null;
        if(result > 0) {
            rb = new ResponseBean<>(result);
        }else {
            rb = new ResponseBean<>(500,"Fail to delete");
        }

        return rb;
    }
}
