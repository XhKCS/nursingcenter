package com.neusoft.nursingcenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("meal_reservation")
public class MealReservation {
    @TableId(value="id", type= IdType.AUTO)
    private Integer id;

    private Integer customerId;

    private Integer mealItemId;

    private Integer purchaseCount; //购买数量

    private String purchaseTime; //购买时间

    private Boolean isDeleted;

    public MealReservation() {}

    public MealReservation(Integer id, Integer customerId, Integer mealItemId, Integer purchaseCount, String purchaseTime, Boolean isDeleted) {
        this.id = id;
        this.customerId = customerId;
        this.mealItemId = mealItemId;
        this.purchaseCount = purchaseCount;
        this.purchaseTime = purchaseTime;
        this.isDeleted = isDeleted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getMealItemId() {
        return mealItemId;
    }

    public void setMealItemId(Integer mealItemId) {
        this.mealItemId = mealItemId;
    }

    public Integer getPurchaseCount() {
        return purchaseCount;
    }

    public void setPurchaseCount(Integer purchaseCount) {
        this.purchaseCount = purchaseCount;
    }

    public String getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(String purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
