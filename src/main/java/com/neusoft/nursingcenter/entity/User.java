package com.neusoft.nursingcenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("user")
public class User {
    @TableId(value="user_id",type= IdType.AUTO)
    private int userId;

    private String account; //账号

    private String password;

    private String name;

    private String phoneNumber;

    private int sex; // 0-女性  1-男性

    private String email;

    private int userType; // 0-管理员  1-护工

    public User() {}

    public User(int userId, String account, String password, String name, String phoneNumber, int sex, String email, int userType) {
        this.userId = userId;
        this.account = account;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.sex = sex;
        this.email = email;
        this.userType = userType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", sex=" + sex +
                ", email='" + email + '\'' +
                ", userType=" + userType +
                '}';
    }
}
