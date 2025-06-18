package com.neusoft.nursingcenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("nursing_program")
public class NursingProgram {
    @TableId(value="id", type= IdType.AUTO)
    private Integer id;

    private String programCode; //护理项目编号

    private String name;

    private Double price;

    private Integer status; // 0-停用  1-启用

    private String executionPeriod; //执行周期，如：每天 / 每周

    private Integer executionTimes; // 每个周期的执行次数

    private String description;

    private Boolean isDeleted; //是否已删除

    public NursingProgram() {}

    public NursingProgram(Integer id, String programCode, String name, Double price, Integer status, String executionPeriod, Integer executionTimes, String description, Boolean isDeleted) {
        this.id = id;
        this.programCode = programCode;
        this.name = name;
        this.price = price;
        this.status = status;
        this.executionPeriod = executionPeriod;
        this.executionTimes = executionTimes;
        this.description = description;
        this.isDeleted = isDeleted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProgramCode() {
        return programCode;
    }

    public void setProgramCode(String programCode) {
        this.programCode = programCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
