package com.neusoft.nursingcenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("customer")
public class Customer {
    @TableId(value="customer_id",type=IdType.AUTO)
    private Integer customerId;

    private Integer customerType; // 0-自理老人  1-护理老人

    private Integer nurseId; //所属护工的id，外键

    private String nursingLevelName; //所属护理级别的名称，外键

    private String name;

    private String idCard; // 身份证号

    private Integer age;

    private Integer gender; //性别  0-女性  1-男性

    private String bloodType;

    private String relative; // 家属

    private String phoneNumber;

    private String building; //楼栋

    private String roomNumber; //房间号

    private String bedNumber; //床位号

    private String checkinDate; //入住日期

    private String expirationDate; //合同到期时间

    private Boolean isDeleted; //是否已删除

    public Customer() {}

    public Customer(Integer customerId, Integer customerType, Integer nurseId, String nursingLevelName, String name, String idCard, Integer age, Integer gender, String bloodType, String relative, String phoneNumber, String building, String roomNumber, String bedNumber, String checkinDate, String expirationDate, Boolean isDeleted) {
        this.customerId = customerId;
        this.customerType = customerType;
        this.nurseId = nurseId;
        this.nursingLevelName = nursingLevelName;
        this.name = name;
        this.idCard = idCard;
        this.age = age;
        this.gender = gender;
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

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Integer customerType) {
        this.customerType = customerType;
    }

    public Integer getNurseId() {
        return nurseId;
    }

    public void setNurseId(Integer nurseId) {
        this.nurseId = nurseId;
    }

    public String getNursingLevelName() {
        return nursingLevelName;
    }

    public void setNursingLevelName(String nursingLevelName) {
        this.nursingLevelName = nursingLevelName;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
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

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", customerType=" + customerType +
                ", nurseId=" + nurseId +
                ", nursingLevelName='" + nursingLevelName + '\'' +
                ", name='" + name + '\'' +
                ", idCard='" + idCard + '\'' +
                ", age=" + age +
                ", gender=" + gender +
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
