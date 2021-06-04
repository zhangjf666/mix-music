package com.happycoding.music.dto;

import com.happycoding.music.model.MusicPlatform;
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
     * id
     */
    private String id;

    /**
     * 歌手id
     */
    private String singerId;

    /**
     * 歌手姓名
     */
    private String singerName;

    /**
     * 歌手平台
     */
    private MusicPlatform platform;
}
