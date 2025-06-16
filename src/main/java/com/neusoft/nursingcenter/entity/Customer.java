package com.neusoft.nursingcenter.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("user")
public class Customer {
    private int customerId;

    private int customerType; // 0-自理老人  1-护理老人

    private int nursingLevelId;

    private String name;

    private String idCard; // 身份证号

    private int age;

    private String bloodType;

    private String relative; // 家属

    private String phoneNumber;

    private String building;

    private String roomNumber; //房间号

    private String bedNumber; //床位号

    private String checkinDate; //入住日期

    private String expirationDate; //合同到期时间

    private boolean isDeleted; //是否已删除

    public Customer() {}

    public Customer(int customerId, int customerType, int nursingLevelId, String name, String idCard, int age, String bloodType, String relative, String phoneNumber, String building, String roomNumber, String bedNumber, String checkinDate, String expirationDate, boolean isDeleted) {
        this.customerId = customerId;
        this.customerType = customerType;
        this.nursingLevelId = nursingLevelId;
        this.name = name;
        this.idCard = idCard;
        this.age = age;
        this.bloodType = bloodType;
        this.relative = relative;
        this.phoneNumber = phoneNumber;
        this.building = building;
        this.roomNumber = roomNumber;
        this.bedNumber = bedNumber;
        this.checkinDate = checkinDate;
        this.expirationDate = expirationDate;
        this.isDeleted = isDeleted;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getCustomerType() {
        return customerType;
    }

    public void setCustomerType(int customerType) {
        this.customerType = customerType;
    }

    public int getNursingLevelId() {
        return nursingLevelId;
    }

    public void setNursingLevelId(int nursingLevelId) {
        this.nursingLevelId = nursingLevelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getRelative() {
        return relative;
    }

    public void setRelative(String relative) {
        this.relative = relative;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getBedNumber() {
        return bedNumber;
    }

    public void setBedNumber(String bedNumber) {
        this.bedNumber = bedNumber;
    }

    public String getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(String checkinDate) {
        this.checkinDate = checkinDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", customerType=" + customerType +
                ", nursingLevelId=" + nursingLevelId +
                ", name='" + name + '\'' +
                ", idCard='" + idCard + '\'' +
                ", age=" + age +
                ", bloodType='" + bloodType + '\'' +
                ", relative='" + relative + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", building='" + building + '\'' +
                ", roomNumber='" + roomNumber + '\'' +
                ", bedNumber='" + bedNumber + '\'' +
                ", checkinDate='" + checkinDate + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
