package com.happycoding.music.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2021/5/11 9:38
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NeteaseOption {

    private String crypto = "";

    private Map<String,String> cookie = new HashMap<>();

    private String proxy;

    private String realIp;

    private String token="";

    private String url;
}
