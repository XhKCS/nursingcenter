package com.neusoft.nursingcenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("checkout_registration")
public class CheckoutRegistration {
    @TableId(value="id",type= IdType.AUTO)
    private Integer id;

    private Integer customerId;

    private String customerName;

    private Integer checkoutType; //退住类型：0-正常退住 / 1-死亡退住 / 2-保留床位

    private String checkoutReason; //退住原因

    private String checkoutDate; //退住日期

    private Integer reviewStatus; //审批状态：0-不通过 / 1-通过

    private String reviewTime; //审批时间

    private Integer reviewerId; //审批人的id

    private String rejectReason; //申请不通过的原因

    public CheckoutRegistration() {}

    public CheckoutRegistration(Integer id, Integer customerId, String customerName, Integer checkoutType, String checkoutReason, String checkoutDate, Integer reviewStatus, String reviewTime, Integer reviewerId, String rejectReason) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.checkoutType = checkoutType;
        this.checkoutReason = checkoutReason;
        this.checkoutDate = checkoutDate;
        this.reviewStatus = reviewStatus;
        this.reviewTime = reviewTime;
        this.reviewerId = reviewerId;
        this.rejectReason = rejectReason;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getCheckoutType() {
        return checkoutType;
    }

    public void setCheckoutType(Integer checkoutType) {
        this.checkoutType = checkoutType;
    }

    public String getCheckoutReason() {
        return checkoutReason;
    }

    public void setCheckoutReason(String checkoutReason) {
        this.checkoutReason = checkoutReason;
    }

    public String getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(String checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public Integer getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(Integer reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public String getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(String reviewTime) {
        this.reviewTime = reviewTime;
    }

    public Integer getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Integer reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }
}
