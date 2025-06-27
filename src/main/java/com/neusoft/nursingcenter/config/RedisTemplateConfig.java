package com.neusoft.nursingcenter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

//整合redis的配置类
@Configuration
public class RedisTemplateConfig {
    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory rcf)
    {
        RedisTemplate<String,Object> redisTemplate=new RedisTemplate<>();
        redisTemplate.setConnectionFactory(rcf);
        // 配置键序列化器
        StringRedisSerializer keySerializer=new StringRedisSerializer();
        // 配置值序列化器
        Jackson2JsonRedisSerializer valueSerializer = new Jackson2JsonRedisSerializer(Object.class);
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(valueSerializer);
        return redisTemplate;
    }

}
