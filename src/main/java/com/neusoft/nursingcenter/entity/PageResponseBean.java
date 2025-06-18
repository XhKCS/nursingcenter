package com.neusoft.nursingcenter.entity;

public class PageResponseBean<T> extends ResponseBean<T> {

    private long total;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }


    // 数据操作成功
    public PageResponseBean(T data) {
        super(data);
        // TODO Auto-generated constructor stub
    }

    // 数据操作失败
    public PageResponseBean(Integer status, String msg) {
        super(status, msg);
        // TODO Auto-generated constructor stub
        this.total = 0;
    }

    public PageResponseBean() {
        super();
        // TODO Auto-generated constructor stub
    }


}
