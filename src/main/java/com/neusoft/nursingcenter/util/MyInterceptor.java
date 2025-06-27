package com.neusoft.nursingcenter.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neusoft.nursingcenter.entity.User;
import com.neusoft.nursingcenter.redisdao.RedisDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;

//拦截器
@Component
public class MyInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisDao rd;

    //	请求到达控制器之间，可以做什么
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // TODO Auto-generated method stub
        System.out.println("访问过程经过了拦截器");
        String sentToken=request.getHeader("token");
        System.out.println(sentToken);
//		如果前端不能提供令牌，则阻止前端继续访问
        if(sentToken==null) {
//			PrintWriter属于字符型输出流子类
            PrintWriter pw=response.getWriter();
            pw.print("invalid token");
//			字符型输出流使用后需要关闭
            pw.close();
//			通过返回false阻止前端继续访问控制器
            return false;
        }else{
            try {
                String userJson=JWTTool.parseToken(sentToken);
                ObjectMapper om=new ObjectMapper();
                User user=om.readValue(userJson, User.class);
//			从令牌中截取到userId
//			    String userIdStr=sentToken.substring(0, sentToken.indexOf(":"));
//    			再到redis中进行按这个userId取出相应令牌
                String storedToken=rd.get(user.getUserId().toString());
                if(!sentToken.equals(storedToken)) {
//				PrintWriter属于字符型输出流子类
                    PrintWriter pw=response.getWriter();
                    pw.print("invalid token");
//				字符型输出流使用后需要关闭
                    pw.close();
                    return false;
                }
            } catch (Exception e) {
                System.out.println("Exception happened: ");
                System.out.println(e.getMessage());
                PrintWriter pw=response.getWriter();
                pw.print("invalid token");
//				字符型输出流使用后需要关闭
                pw.close();
                return false;
            }
        }
        return true;
    }

}
