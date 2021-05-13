package com.happycoding.music.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2021/5/13 14:03
 */
@Data
public class MiguResponse {

    private int code;

    private String message;

    private JSONObject body;
}
