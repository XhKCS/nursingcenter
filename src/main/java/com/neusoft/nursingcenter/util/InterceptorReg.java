package com.neusoft.nursingcenter.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

// 这个类会注入拦截器对象
@Component
public class InterceptorReg implements WebMvcConfigurer {
    //	注入拦截器对象
    @Autowired
//    private HandlerInterceptor mi;
    private MyInterceptor mi;

    //	注册拦截器的方法
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // TODO Auto-generated method stub
        List<String> path=new ArrayList<>();
        path.add("/user/login");
        path.add("/customer/login");
        path.add("/redisController/**");
        path.add("/v3/**");
        path.add("/swagger-ui/**");
        path.add("/swagger-ui.html");
        path.add("/webjars/**");
        path.add("/error");
        path.add("/doc**");

//		设定后端拦截器不拦截哪些映射路径
        registry.addInterceptor(mi).excludePathPatterns(path);
        WebMvcConfigurer.super.addInterceptors(registry);
    }

}