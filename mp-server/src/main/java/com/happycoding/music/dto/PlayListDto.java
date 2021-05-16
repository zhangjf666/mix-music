package com.happycoding.music.dto;

import com.happycoding.music.model.MusicPlatform;
import lombok.Data;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description: 歌单信息
 * @Date: 2021/5/11 9:38
 */
@Data
public class PlayListDto {

    /**
     * id
     */
    String id;

    /**
     * 图片地址
     */
    String picUrl;

    /**
     * 歌单名称
     */
    String name;

    /**
     * 摘要
     */
    String summary;

    /**
     * 来源平台
     */
    MusicPlatform musicPlatform;

    /**
     * 歌曲数量
     */
    Long trackCount;

    /**
     * 播放次数
     */
    Long playCount;
}
