package com.neusoft.nursingcenter.service;

import com.neusoft.nursingcenter.entity.Food;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FoodService {
    int updateFood(Food food);

    int deleteFoodById(int foodId);

    @Transactional
    int deleteFoodByIds(List<Integer> ids);
}
