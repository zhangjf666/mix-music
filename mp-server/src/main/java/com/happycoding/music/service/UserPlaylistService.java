package com.happycoding.music.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.happycoding.music.dto.SongInfoDto;
import com.happycoding.music.entity.UserPlaylist;

import java.util.List;

/**
 * <p>
 * 用户播放列表记录 服务类
 * </p>
 *
 * @author zjf
 * @since 2021-06-07
 */
public interface UserPlaylistService extends IService<UserPlaylist> {

    /**
     * 获取用户播放列表
     * @param userId 用户id
     * @return
     */
    List<SongInfoDto> queryUserPlaylistByUserId(String userId);

    /**
     * 更新播放列表
     * @param userId 用户id
     * @param songIds 歌曲id
     */
    void updateUserPlayList(String userId, List<String> songIds);
}
