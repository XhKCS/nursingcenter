package com.neusoft.nursingcenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("nursing_record")
public class NursingRecord {
    @TableId(value="id", type= IdType.AUTO)
    private Integer id;

    private Integer customerId;

    private Integer programId;

    private String nursingTime; //护理时间

    private Integer executionCount; //本次护理（消耗）的数量

    private Boolean isDeleted;

    public NursingRecord() {}

    public NursingRecord(Integer id, Integer customerId, Integer programId, String nursingTime, Integer executionCount, Boolean isDeleted) {
        this.id = id;
        this.customerId = customerId;
        this.programId = programId;
        this.nursingTime = nursingTime;
        this.executionCount = executionCount;
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

    public Integer getProgramId() {
        return programId;
    }

    public void setProgramId(Integer programId) {
        this.programId = programId;
    }

    public String getNursingTime() {
        return nursingTime;
    }

    public void setNursingTime(String nursingTime) {
        this.nursingTime = nursingTime;
    }

    public Integer getExecutionCount() {
        return executionCount;
    }

    public void setExecutionCount(Integer executionCount) {
        this.executionCount = executionCount;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
