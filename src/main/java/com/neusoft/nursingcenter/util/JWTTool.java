package com.neusoft.nursingcenter.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTTool {
    //	令牌私有秘钥
    private static final String SECRET="All-The-Webs";
    //	定义令牌过期时间的单位
    private static final int calendarField= Calendar.SECOND;
    //	定义令牌的过期时间
    public static final int calendarInterval = 600; //600秒过期

    //	创建令牌的方法
    public static String createToken(String mobileJson){
//		确认令牌签发日期、过期时间
//		签发日期
        Date iaDate=new Date();
//		过期日期
        Calendar nowTime=Calendar.getInstance();
        nowTime.add(calendarField, calendarInterval);
        Date expiresDate=nowTime.getTime();
//		确认令牌的类型和加密算法
        Map<String,Object> map=new HashMap<>();
        map.put("typ", "JWT");
        map.put("alg", "HS256");
//		结合前两步结果，创建令牌字符串
        String token= JWT.create()
                .withHeader(map)
                .withClaim("mobileJson",mobileJson)
                .withIssuedAt(iaDate)
                .withExpiresAt(expiresDate)
                .sign(Algorithm.HMAC256(SECRET));
        return token;
    }
    //	验证令牌的方法
    private static Map<String, Claim> verifyToken(String token){
        DecodedJWT jwt=null;
        JWTVerifier verifier=JWT.require(Algorithm.HMAC256(SECRET)).build();
        jwt=verifier.verify(token);
        return jwt.getClaims();
    }

    //	把令牌中的用户信息取出，并以json形式返回
    public static String parseToken(String token){
        Map<String,Claim> claims=verifyToken(token);
        Claim mobileClaim=claims.get("mobileJson");

        return mobileClaim.asString();
    }

}
