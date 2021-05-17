package com.happycoding.music.dto;

import com.happycoding.music.model.MusicPlatform;
import lombok.Data;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description: 歌曲信息
 * @Date: 2021/5/11 9:38
 */
@Data
public class SongInfoDto {

    /**
     * 音乐id
     */
    String id;

    /**
     * 名称
     */
    String name;

    /**
     * 图片url
     */
    String picUrl;

    /**
     * 时长
     */
    long duration;

    /**
     * 歌手id
     */
    String singerId;

    /**
     * 歌手名称
     */
    String singerName;

    /**
     * 歌曲播放url
     */
    String url;

    /**
     * 歌词
     */
    String lyric;

    /**
     * 属于哪个平台
     */
    MusicPlatform musicPlatform;

    /**
     * 音质
     */
    String br;

    /**
     * 专辑id
     */
    String albumId;

    /**
     * 专辑名称
     */
    String albumName;
}
