package com.happycoding.music.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.happycoding.music.common.base.IEnum;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description: 音乐来源平台
 * @Date: 2021/5/11 9:38
 */
public enum MusicPlatform implements IEnum<String> {
    /**
     * 所有平台
     */
    All("0", "所有平台"),
    /**
     * 网易云音乐
     */
    Netease("1", "网易云"),
    /**
     * 咪咕
     */
    Migu("2", "咪咕");

    private String platform;

    private String name;

    MusicPlatform(String platform, String name){
        this.platform = platform;
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    @Override
    public String toString(){
        return platform;
    }

    @JsonValue
    @Override
    public String getId() {
        return platform;
    }

    @JsonCreator
    public static MusicPlatform findLevelEmum(String id){
        for (MusicPlatform item : MusicPlatform.values()) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }
}
