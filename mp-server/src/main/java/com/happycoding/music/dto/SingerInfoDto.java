package com.happycoding.music.dto;

import lombok.Data;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description: 歌手信息
 * @Date: 2021/5/27 9:50
 */
@Data
public class SingerInfoDto {
    /**
     * 歌手id
     */
    private String id;

    /**
     * 歌手姓名
     */
    private String name;
}
