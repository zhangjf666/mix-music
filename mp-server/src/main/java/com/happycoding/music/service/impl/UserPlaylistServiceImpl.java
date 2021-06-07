package com.happycoding.music.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.happycoding.music.dto.SongInfoDto;
import com.happycoding.music.entity.Song;
import com.happycoding.music.entity.UserPlaylist;
import com.happycoding.music.mapper.UserPlaylistMapper;
import com.happycoding.music.service.SongService;
import com.happycoding.music.service.UserPlaylistService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户播放列表记录 服务实现类
 * </p>
 *
 * @author zjf
 * @since 2021-06-07
 */
@Service
public class UserPlaylistServiceImpl extends ServiceImpl<UserPlaylistMapper, UserPlaylist> implements UserPlaylistService {

    @Autowired
    private SongService songService;

    @Override
    public List<SongInfoDto> queryUserPlaylistByUserId(String userId) {
        List<UserPlaylist> songIds = list(Wrappers.<UserPlaylist>lambdaQuery().eq(UserPlaylist::getUserId, userId));
        List<Song> songs = songService.listByIds(songIds.stream().map(UserPlaylist::getSongId).collect(Collectors.toList()));
        return songService.getBaseMapstruct().toDto(songs);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateUserPlayList(String userId, List<String> songIds) {
        remove(Wrappers.<UserPlaylist>lambdaQuery().eq(UserPlaylist::getUserId, userId));
        List<UserPlaylist> newPlaylist = new ArrayList<>();
        for (String songId : songIds) {
            UserPlaylist playlist = new UserPlaylist(userId, songId);
            newPlaylist.add(playlist);
        }
        saveBatch(newPlaylist);
    }
}
