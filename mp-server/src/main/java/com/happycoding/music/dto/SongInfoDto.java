package com.happycoding.music.dto;

import com.happycoding.music.model.MusicPlatform;
import lombok.Data;

import java.util.List;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description: 歌曲信息
 * @Date: 2021/5/11 9:38
 */
@Data
public class SongInfoDto {
    /**
     * 内部id
     */
    String id;
    /**
     * 音乐id
     */
    String songId;

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
     * 歌手信息
     */
    List<SingerInfoDto> singers;

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
    MusicPlatform platform;

    /**
     * 音质
     */
    String br;

    /**
     * 所属专辑
     */
    List<AlbumInfoDto> albums;
}
