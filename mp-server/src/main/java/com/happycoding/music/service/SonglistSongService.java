package com.happycoding.music.service;

import com.happycoding.music.entity.SonglistSong;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 歌单-歌曲表 服务类
 * </p>
 *
 * @author zjf
 * @since 2021-06-07
 */
public interface SonglistSongService extends IService<SonglistSong> {

    boolean existSong(String songlistId, String songId);
}
