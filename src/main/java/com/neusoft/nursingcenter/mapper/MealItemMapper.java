package com.neusoft.nursingcenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.nursingcenter.config.MyBaseMapper;
import com.neusoft.nursingcenter.entity.Food;
import com.neusoft.nursingcenter.entity.MealItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MealItemMapper extends MyBaseMapper<MealItem> {
    @Select("select * from meal_item where food_id=#{foodId}")
    List<MealItem> listByFoodId(int foodId);

    @Select("select * from meal_item where week_day=#{weekDay}")
    List<MealItem> listByWeekDay(int weekDay);

    @Select("select * from meal_item where food_id=#{foodId} and week_day=#{weekDay}")
    MealItem getByFoodIdAndWeekDay(@Param("foodId") int foodId, @Param("weekDay") String weekDay);

    @Update("update meal_item set food_name=#{food.name}, food_type=#{food.type}, food_description=#{food.description}, food_image_url=#{food.imageUrl} where food_id=#{food.id}")
    int updateAllByFood(@Param("food") Food food);

    @Delete("delete from meal_item where food_id=#{foodId}")
    int deleteAllByFoodId(int foodId);
}
