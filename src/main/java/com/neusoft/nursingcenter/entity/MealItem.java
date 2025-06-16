package com.neusoft.nursingcenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("meal_item")
public class MealItem {
    @TableId(value="id", type= IdType.AUTO)
    private Integer id;

    private Integer foodId;

    private String weekDay; //周一 / 周二 /.../ 周日

    private Integer status; // 1-启用 / 0-停用

    public MealItem() {}

    public MealItem(Integer id, Integer foodId, String weekDay, Integer status) {
        this.id = id;
        this.foodId = foodId;
        this.weekDay = weekDay;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
