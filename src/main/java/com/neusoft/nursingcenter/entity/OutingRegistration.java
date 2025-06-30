package com.neusoft.nursingcenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("outing_registration")
public class OutingRegistration {
    @TableId(value="id", type= IdType.AUTO)
    private Integer id;

    private Integer nurseId;

    private Integer customerId;

    private String customerName;

    private String outingReason; //外出事由

    private String outingDate; //外出日期

    private String expectedReturnDate; //预计回院时间

    private String actualReturnDate; //实际回院时间

    private String escortName; //陪同人姓名

    private String escortPhone; //陪同人电话

    private String escortRelation; //陪同人与老人的关系

    private Integer reviewStatus; //审批状态：0-已提交 / 1-不通过 / 2-通过

    private String reviewTime; //审批时间

    private Integer reviewerId; //审批人的id

    private String rejectReason; //申请不通过的原因

    public OutingRegistration() {}

    public OutingRegistration(Integer id, Integer nurseId, Integer customerId, String customerName, String outingReason, String outingDate, String expectedReturnDate, String actualReturnDate, String escortName, String escortPhone, String escortRelation, Integer reviewStatus, String reviewTime, Integer reviewerId, String rejectReason) {
        this.id = id;
        this.nurseId = nurseId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.outingReason = outingReason;
        this.outingDate = outingDate;
        this.expectedReturnDate = expectedReturnDate;
        this.actualReturnDate = actualReturnDate;
        this.escortName = escortName;
        this.escortPhone = escortPhone;
        this.escortRelation = escortRelation;
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

    public Integer getNurseId() {
        return nurseId;
    }

    public void setNurseId(Integer nurseId) {
        this.nurseId = nurseId;
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

    public String getOutingReason() {
        return outingReason;
    }

    public void setOutingReason(String outingReason) {
        this.outingReason = outingReason;
    }

    public String getOutingDate() {
        return outingDate;
    }

    public void setOutingDate(String outingDate) {
        this.outingDate = outingDate;
    }

    public String getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(String expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    public String getActualReturnDate() {
        return actualReturnDate;
    }

    public void setActualReturnDate(String actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }

    public String getEscortName() {
        return escortName;
    }

    public void setEscortName(String escortName) {
        this.escortName = escortName;
    }

    public String getEscortPhone() {
        return escortPhone;
    }

    public void setEscortPhone(String escortPhone) {
        this.escortPhone = escortPhone;
    }

    public String getEscortRelation() {
        return escortRelation;
    }

    public void setEscortRelation(String escortRelation) {
        this.escortRelation = escortRelation;
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
