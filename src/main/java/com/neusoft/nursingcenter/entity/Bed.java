package com.neusoft.nursingcenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("bed")
public class Bed {
    @TableId(value="id", type=IdType.AUTO)
    private Integer id;

    private Integer roomId; //所属房间Id，外键

    private String bedNumber; //床位号

    private Integer status; //状态：0-空闲 / 1-外出 / 2-有人

    public Bed() {}

    public Bed(Integer id, Integer roomId, String bedNumber, Integer status) {
        this.id = id;
        this.roomId = roomId;
        this.bedNumber = bedNumber;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getBedNumber() {
        return bedNumber;
    }

    public void setBedNumber(String bedNumber) {
        this.bedNumber = bedNumber;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
