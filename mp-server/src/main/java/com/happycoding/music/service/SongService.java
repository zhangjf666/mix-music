package com.happycoding.music.service;

import com.happycoding.music.common.base.BaseService;
import com.happycoding.music.dto.SongInfoDto;
import com.happycoding.music.dto.SongUrlDto;
import com.happycoding.music.entity.Song;
import com.happycoding.music.model.MusicPlatform;

import java.util.List;

/**
 * <p>
 * 歌曲信息表 服务类
 * </p>
 *
 * @author zjf
 * @since 2021-06-03
 */
public interface SongService extends BaseService<SongInfoDto, Song> {

    /**
     * 添加歌曲信息
     * @param dto Dto
     * @return
     */
    @Override
    SongInfoDto create(SongInfoDto dto);

    /**
     * 查询歌曲
     * @param songId 歌曲id
     * @param platform 音乐平台
     * @return
     */
    SongInfoDto querySong(String songId, MusicPlatform platform);

    /**
     * 查询歌词
     * @param id 歌曲id
     * @return
     */
   String queryLyric(String id);

    /**
     * 获取歌曲url
     * @param id 歌曲id
     * @return
     */
    SongUrlDto queryUrl(String id);

    /**
     * 获取歌曲详情(包括url和歌词)
     * @param dto 歌曲信息
     * @return
     */
    SongInfoDto queryDetail(SongInfoDto dto);

    /**
     * 查询关联歌曲
     * @param dto 原歌曲
     * @return
     */
    List<SongInfoDto> queryRelative(SongInfoDto dto);

    /**
     * 根据歌曲id跟平台查找歌曲
     * @param songId 歌曲id
     * @param platform 平台
     * @return
     */
    SongInfoDto querySongBySongId(String songId, MusicPlatform platform);
}
