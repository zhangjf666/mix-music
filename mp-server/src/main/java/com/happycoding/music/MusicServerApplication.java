package com.happycoding.music;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2021/5/10 10:13
 */
@EnableAsync
@SpringBootApplication
@EnableTransactionManagement
@MapperScan({"com.happycoding.music.mapper"})
public class MusicServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MusicServerApplication.class, args);
    }
}

