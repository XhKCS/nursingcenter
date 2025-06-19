package com.neusoft.nursingcenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("bed_usage_record")
public class BedUsageRecord {
    @TableId(value="id",type=IdType.AUTO)
    private Integer id;

    private String bedNumber; //床位号

    private Integer customerId;

    private String customerName;

    private Integer customerGender; //0-女性  1-男性

    private String startDate;

    private String endDate;

    private Integer status; //状态：1-使用中 / 0-已失效

    private Boolean isDeleted; //是否已删除

    public BedUsageRecord() {}

    public BedUsageRecord(Integer id, String bedNumber, Integer customerId, String customerName, Integer customerGender, String startDate, String endDate, Integer status, Boolean isDeleted) {
        this.id = id;
        this.bedNumber = bedNumber;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerGender = customerGender;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.isDeleted = isDeleted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBedNumber() {
        return bedNumber;
    }

    public void setBedNumber(String bedNumber) {
        this.bedNumber = bedNumber;
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

    public Integer getCustomerGender() {
        return customerGender;
    }

    public void setCustomerGender(Integer customerGender) {
        this.customerGender = customerGender;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
