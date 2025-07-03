package com.neusoft.nursingcenter.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.neusoft.nursingcenter.entity.Food;
import com.neusoft.nursingcenter.entity.MealItem;
import com.neusoft.nursingcenter.entity.MealReservation;
import com.neusoft.nursingcenter.mapper.FoodMapper;
import com.neusoft.nursingcenter.mapper.MealItemMapper;
import com.neusoft.nursingcenter.mapper.MealReservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class FoodServiceImpl implements FoodService {
    @Autowired
    private FoodMapper foodMapper;

    @Autowired
    private MealItemMapper mealItemMapper;

    @Autowired
    private MealReservationMapper mealReservationMapper;

    @Transactional
    @Override
    public int updateFood(Food food) {
        List<MealItem> mealItemList = mealItemMapper.listByFoodId(food.getId());
        if (mealItemList.size() > 0) {
            // 需要先更新所有对应的mealItem
            int res1 = mealItemMapper.updateAllByFood(food);
            if (res1 <= 0) {
                throw new RuntimeException("更新过程中失败");
            }
        }
        return foodMapper.updateById(food);
    }

    @Transactional
    @Override
    public int deleteFoodById(int foodId) {
        List<MealItem> mealItemList = mealItemMapper.listByFoodId(foodId);
        if (mealItemList.size() > 0) {
            // 需要先删除所有对应的mealItem
            int res1 = mealItemMapper.deleteAllByFoodId(foodId);
            if (res1 <= 0) {
                throw new RuntimeException("删除过程中失败");
            }
        }
        return foodMapper.deleteById(foodId);
    }

    @Transactional
    @Override
    public int deleteFoodByIds(List<Integer> ids) {
        List<MealItem> mealItemList = new ArrayList<>();
        for(int id : ids){
            List<MealItem> list = mealItemMapper.listByFoodId(id);
            mealItemList.addAll(list);
        }

        if (mealItemList.size() > 0) {
            // 需要先删除所有对应的mealItem
            int res1 = mealItemMapper.deleteBatchIds(mealItemList);
            if (res1 <= 0) {
                throw new RuntimeException("删除过程中失败");
            }
        }
        return foodMapper.deleteBatchIds(ids);
    }

    @Override
    public int getPurchaseByIdAndTime(int foodId, String startTime, String endTime) {
        List<MealItem> mealItemList = mealItemMapper.listByFoodId(foodId);
        List<MealReservation> mealReservationList = new ArrayList<>();
        for(MealItem mealItem : mealItemList){
            QueryWrapper<MealReservation> qw = new QueryWrapper<>();
            qw.eq("meal_item_id",mealItem.getId());
            qw.ge(!startTime.isEmpty(),"purchase_time",startTime);
            qw.le(!endTime.isEmpty(),"purchase_time",endTime);
            List<MealReservation> mealReservations = mealReservationMapper.selectList(qw);
            mealReservationList.addAll(mealReservations);
        }
        return mealReservationList.stream().mapToInt(MealReservation ::getPurchaseCount).sum();
    }
}
