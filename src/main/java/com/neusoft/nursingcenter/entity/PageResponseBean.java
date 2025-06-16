package com.neusoft.nursingcenter.entity;

public class PageResponseBean<T> extends ResponseBean<T> {

    private long total;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public PageResponseBean() {
        super();
        // TODO Auto-generated constructor stub
    }

    public PageResponseBean(Integer status, String msg) {
        super(status, msg);
        // TODO Auto-generated constructor stub
    }

    public PageResponseBean(T data) {
        super(data);
        // TODO Auto-generated constructor stub
    }





}
