package com.happycoding.music.model;

import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONObject;
import lombok.Data;

import java.util.List;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2021/5/11 13:22
 */
@Data
public class NeteaseResponse {
    private int code;

    private String message;

    private List<String> cookie;

    private Object body;
}
