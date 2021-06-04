package com.happycoding.music.dto;

import com.happycoding.music.model.MusicPlatform;
import lombok.Data;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description: 专辑信息
 * @Date: 2021/5/27 9:52
 */
@Data
public class AlbumInfoDto {

    /**
     * id
     */
    private String id;

    /**
     * 专辑id
     */
    private String albumId;

    /**
     * 专辑名称
     */
    private String albumName;

    /**
     * 歌手平台
     */
    private MusicPlatform platform;
}
