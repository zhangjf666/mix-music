package com.happycoding.music.model;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description: 音乐来源平台
 * @Date: 2021/5/11 9:38
 */
public enum MusicPlatform {
    All("0"),
    Netease("1"),
    Migu("2");

    private String platform;

    MusicPlatform(String platform){
        this.platform = platform;
    }

    @JsonValue
    @Override
    public String toString(){
        return platform;
    }
}
