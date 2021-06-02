package com.happycoding.music.config;

import com.happycoding.music.common.support.mapstruct.MapStructTransform;
import com.happycoding.music.config.properties.AsyncProperties;
import com.happycoding.music.config.properties.SystemProperties;
import com.happycoding.music.config.security.JwtProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2020/6/12 17:05
 */
@Configuration
public class CustomConfig {

    @Bean
    public MapStructTransform mapStructTransform(){
        return new MapStructTransform();
    }

    @Bean
    @ConfigurationProperties(prefix = "jwt")
    public JwtProperties jwtProperties() {return new JwtProperties();}

    @Bean
    @ConfigurationProperties(prefix = "system")
    public SystemProperties systemProperties() {return new SystemProperties();}

    @Bean
    @ConfigurationProperties(prefix = "async.pool")
    public AsyncProperties asyncProperties() {return new AsyncProperties();}
}
