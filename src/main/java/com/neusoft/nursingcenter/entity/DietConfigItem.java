package com.neusoft.nursingcenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("diet_config_item")
public class DietConfigItem {
    @TableId(value="id", type=IdType.AUTO)
    private Integer id;

    private Integer customerId;

    private String name;

    private String description;

    public DietConfigItem() {}

    public DietConfigItem(Integer id, Integer customerId, String name, String description) {
        this.id = id;
        this.customerId = customerId;
        this.name = name;
        this.description = description;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
