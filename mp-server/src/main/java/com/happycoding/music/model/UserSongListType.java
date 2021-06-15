package com.happycoding.music.model;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.happycoding.music.common.base.IEnum;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description: 用户歌单类型
 * @Date: 2021/6/15 11:33
 */
public enum UserSongListType implements IEnum<String> {
    /**
     * 全部
     */
    ALL("0", "全部"),
    /**
     * 我喜欢
     */
    FAVORITE("1", "我喜欢"),
    /**
     * 自定义歌单
     */
    CREATE("2", "自定义歌单"),
    /**
     * 收藏的歌单
     */
    COLLECT("3", "收藏的歌单");

    @EnumValue
    private String type;

    private String name;

    UserSongListType(String type, String name){
        this.type = type;
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public String getType() { return this.type; }

    @Override
    public String toString(){
        return type;
    }

    @JsonCreator
    public static UserSongListType findLevelEmum(String type){
        for (UserSongListType item : UserSongListType.values()) {
            if (item.getId().equals(type)) {
                return item;
            }
        }
        return null;
    }

    @Override
    @JsonValue
    public String getId() {
        return this.type;
    }
}
