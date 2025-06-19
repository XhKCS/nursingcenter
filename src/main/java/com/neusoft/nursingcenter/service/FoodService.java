package com.neusoft.nursingcenter.service;

import com.neusoft.nursingcenter.entity.Food;

public interface FoodService {
    int updateFood(Food food);

    int deleteFoodById(int foodId);
}
