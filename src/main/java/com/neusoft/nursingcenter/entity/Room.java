package com.neusoft.nursingcenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("room")
public class Room {
    @TableId(value="id", type= IdType.AUTO)
    private Integer id;

    private String roomNumber;

    private Integer floor; //楼层

    private Integer bedCount; //房间内床位数量

    public Room() {}

    public Room(Integer id, String roomNumber, Integer floor, Integer bedCount) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.floor = floor;
        this.bedCount = bedCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getBedCount() {
        return bedCount;
    }

    public void setBedCount(Integer bedCount) {
        this.bedCount = bedCount;
    }
}
