package com.neusoft.nursingcenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@TableName("nursing_program")
@JsonPropertyOrder({"id", "programCode", "name", "price", "status", "executionPeriod", "executionTimes", "description", "isDeleted"}) // 指定属性的顺序
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

    @TableLogic
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

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "NursingProgram{" +
                "id=" + id +
                ", programCode='" + programCode + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", status=" + status +
                ", executionPeriod='" + executionPeriod + '\'' +
                ", executionTimes=" + executionTimes +
                ", description='" + description + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
