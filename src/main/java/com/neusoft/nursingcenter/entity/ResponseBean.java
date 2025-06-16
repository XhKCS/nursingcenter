package com.neusoft.nursingcenter.entity;

//全局响应数据结构类
//表示出数据状态
public class ResponseBean<T> {
    //	数据处理后的状态码(200成功,5XX代表失败),可以给前端程序员参考
    private Integer status;

    //  数据处理后消息(字符串表示),给普通用户看
    private String msg;

    //  业务数据
    private T data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    //	数据操作成功
    public ResponseBean(T data) {
        super();
        this.msg="success";
        this.status=200;
        this.data = data;
    }
    //	数据操作失败
    public ResponseBean(Integer status, String msg) {
        super();
        this.status = status;
        this.msg = msg;
    }
    public ResponseBean() {
        super();
        // TODO Auto-generated constructor stub
    }







}

