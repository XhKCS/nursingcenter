package com.neusoft.nursingcenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("meal_item")
public class MealItem {
    @TableId(value="id", type= IdType.AUTO)
    private Integer id;

    private Integer foodId;

    private String foodName;

    private String foodType;

    private String foodDescription;

    private double foodPrice;

    private String foodImageUrl;

    private String weekDay; //周一 / 周二 /.../ 周日

    private Integer status; // 1-启用 / 0-停用

    public MealItem() {}

    public MealItem(Integer id, Integer foodId, String foodName, String foodType, String foodDescription, double foodPrice, String foodImageUrl, String weekDay, Integer status) {
        this.id = id;
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodType = foodType;
        this.foodDescription = foodDescription;
        this.foodPrice = foodPrice;
        this.foodImageUrl = foodImageUrl;
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

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }

    public double getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(double foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getFoodImageUrl() {
        return foodImageUrl;
    }

    public void setFoodImageUrl(String foodImageUrl) {
        this.foodImageUrl = foodImageUrl;
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
