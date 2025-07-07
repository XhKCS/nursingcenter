package com.neusoft.nursingcenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("customer_nursing_service")
public class CustomerNursingService {
    @TableId(value="id",type= IdType.AUTO)
    private Integer id;

    private Integer customerId; //客户id

    private Integer levelId; //护理级别id

    private Integer programId; //护理项目id

    private String programCode; //护理项目编号

    private String programName; //护理项目名称

    private Double programPrice; //护理项目价格

    private String executionPeriod; //执行周期，如：每天 / 每周

    private Integer executionTimes; // 每个周期的执行次数

    private String purchaseDate; //购买日期

    private Integer totalCount; //购买总数量

    private Integer usedCount; //当前已使用的数量  剩余数量=totalCount - usedCount

    private String expirationDate; //服务到期日期

    public CustomerNursingService() {}

    public CustomerNursingService(Integer id, Integer customerId, Integer levelId, Integer programId, String programCode, String programName, Double programPrice, String executionPeriod, Integer executionTimes, String purchaseDate, Integer totalCount, Integer usedCount, String expirationDate) {
        this.id = id;
        this.customerId = customerId;
        this.levelId = levelId;
        this.programId = programId;
        this.programCode = programCode;
        this.programName = programName;
        this.programPrice = programPrice;
        this.executionPeriod = executionPeriod;
        this.executionTimes = executionTimes;
        this.purchaseDate = purchaseDate;
        this.totalCount = totalCount;
        this.usedCount = usedCount;
        this.expirationDate = expirationDate;
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

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    public Integer getProgramId() {
        return programId;
    }

    public void setProgramId(Integer programId) {
        this.programId = programId;
    }

    public String getProgramCode() {
        return programCode;
    }

    public void setProgramCode(String programCode) {
        this.programCode = programCode;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public Double getProgramPrice() {
        return programPrice;
    }

    public void setProgramPrice(Double programPrice) {
        this.programPrice = programPrice;
    }

    public String getExecutionPeriod() {
        return executionPeriod;
    }

    public void setExecutionPeriod(String executionPeriod) {
        this.executionPeriod = executionPeriod;
    }

    public Integer getExecutionTimes() {
        return executionTimes;
    }

    public void setExecutionTimes(Integer executionTimes) {
        this.executionTimes = executionTimes;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(Integer usedCount) {
        this.usedCount = usedCount;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
}
