package com.happycoding.music.service;

import com.happycoding.music.common.base.BaseService;
import com.happycoding.music.dto.UserSonglistDetailDto;
import com.happycoding.music.dto.UserSonglistDto;
import com.happycoding.music.entity.UserSonglist;

import java.util.List;

/**
 * <p>
 * 用户歌单 服务类
 * </p>
 *
 * @author zjf
 * @since 2021-06-07
 */
public interface UserSonglistService extends BaseService<UserSonglistDto, UserSonglist> {

    /**
     * 删除歌单
     * @param id 歌单id
     * @return
     */
    boolean deleteById(String id);

    /**
     * 获取歌单歌曲详情
     * @param id 歌单id
     * @return
     */
    UserSonglistDetailDto getSonglistDetailById(String id);

    /**
     * 歌单添加歌曲
     * @param id 歌单id
     * @param songId 歌曲id
     * @return
     */
    boolean addSong(String id, String songId);

    /**
     * 歌单删除歌曲
     * @param id 歌单id
     * @param songId 歌曲id
     * @return
     */
    boolean deleteSong(String id, String songId);

    /**
     * 歌单更新歌曲
     * @param id 歌单id
     * @param songIds 歌曲id列表
     * @return
     */
    boolean updateSongs(String id, List<String> songIds);

    /**
     * 歌单更新歌曲
     * @param collectSonglistId 收藏歌单id
     * @return
     */
    UserSonglistDto existCollectSonglist(String collectSonglistId);
}
