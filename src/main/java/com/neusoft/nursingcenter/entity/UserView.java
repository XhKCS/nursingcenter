package com.neusoft.nursingcenter.entity;

// 专门转给前端的User对象ViewModel，隐藏了密码
public class UserView {
    private Integer userId;

    private String account; //账号，也可以认为是用户名

    private String name; //真实姓名

    private String phoneNumber;

    private Integer gender; // 0-女性  1-男性

    private String email;

    private Integer userType; // 0-管理员  1-护工

    public UserView() {}

    public UserView(User user) {
        this.userId = user.getUserId();
        this.account = user.getAccount();
        this.name = user.getName();
        this.phoneNumber = user.getPhoneNumber();
        this.gender = user.getGender();
        this.email = user.getEmail();
        this.userType = user.getUserType();
    }

    public UserView(Integer userId, String account, String name, String phoneNumber, Integer gender, String email, Integer userType) {
        this.userId = userId;
        this.account = account;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.email = email;
        this.userType = userType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "UserView{" +
                "userId=" + userId +
                ", account='" + account + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", gender=" + gender +
                ", email='" + email + '\'' +
                ", userType=" + userType +
                '}';
    }
}
