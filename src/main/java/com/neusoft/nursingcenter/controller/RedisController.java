package com.neusoft.nursingcenter.controller;

import com.neusoft.nursingcenter.redisdao.RedisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/redisController")
public class RedisController {
    @Autowired
    private RedisDao redisDao;

    @GetMapping("/save")
    public String save(String key, String val) {
        redisDao.set(key, val, 300, TimeUnit.SECONDS);
        return "ok";
    }

    @GetMapping("/saveWithTimeout")
    public String save(String key, String val, int timeout) {
        redisDao.set(key, val, timeout, TimeUnit.SECONDS);
        return "ok";
    }

    @GetMapping("/get")
    public String get(String key) {
        String val = redisDao.get(key);
        return val;
    }
}
