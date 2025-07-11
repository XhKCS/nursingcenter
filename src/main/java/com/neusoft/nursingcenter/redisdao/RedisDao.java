package com.neusoft.nursingcenter.redisdao;

import java.util.concurrent.TimeUnit;

public interface RedisDao {
    //	向redis中存储数据的方法
    void set(String key,String value);

    //	带过期时间的存储方法
    void set(String key, String value, long time, TimeUnit unit);

    //	从redis中读取数据的方法
    String get(String key);
    //  删除键值对的方法
    boolean delete(String key);
}
