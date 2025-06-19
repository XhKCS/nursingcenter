package com.neusoft.nursingcenter.service;

import com.neusoft.nursingcenter.entity.Food;
import com.neusoft.nursingcenter.entity.MealItem;
import com.neusoft.nursingcenter.mapper.FoodMapper;
import com.neusoft.nursingcenter.mapper.MealItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FoodServiceImpl implements FoodService {
    @Autowired
    private FoodMapper foodMapper;

    @Autowired
    private MealItemMapper mealItemMapper;

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
}
