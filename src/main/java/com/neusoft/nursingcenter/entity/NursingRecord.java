package com.neusoft.nursingcenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("nursing_record")
public class NursingRecord {
    @TableId(value="id", type= IdType.AUTO)
    private Integer id;

    private Integer customerId;

    private String customerName;

    private Integer programId;

    private String programCode;

    private String programName;

    private String description;

    private String nurseName;

    private String nursePhone;

    private String nursingTime; //护理时间

    private Integer executionCount; //本次护理（消耗）的数量

    private Boolean isDeleted;

    public NursingRecord() {}

    public NursingRecord(Integer id, Integer customerId, String customerName, Integer programId, String programCode, String programName, String description, String nurseName, String nursePhone, String nursingTime, Integer executionCount, Boolean isDeleted) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.programId = programId;
        this.programCode = programCode;
        this.programName = programName;
        this.description = description;
        this.nurseName = nurseName;
        this.nursePhone = nursePhone;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNurseName() {
        return nurseName;
    }

    public void setNurseName(String nurseName) {
        this.nurseName = nurseName;
    }

    public String getNursePhone() {
        return nursePhone;
    }

    public void setNursePhone(String nursePhone) {
        this.nursePhone = nursePhone;
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
