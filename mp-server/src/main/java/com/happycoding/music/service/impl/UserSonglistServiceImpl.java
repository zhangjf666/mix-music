package com.happycoding.music.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.happycoding.music.common.base.BaseServiceImpl;
import com.happycoding.music.dto.SongInfoDto;
import com.happycoding.music.dto.UserSonglistDto;
import com.happycoding.music.entity.Song;
import com.happycoding.music.entity.SonglistSong;
import com.happycoding.music.entity.UserSonglist;
import com.happycoding.music.mapper.UserSonglistMapper;
import com.happycoding.music.mapstruct.UserSonglistMapstruct;
import com.happycoding.music.service.SongService;
import com.happycoding.music.service.SonglistSongService;
import com.happycoding.music.service.UserSonglistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户歌单 服务实现类
 * </p>
 *
 * @author zjf
 * @since 2021-06-07
 */
@Service
public class UserSonglistServiceImpl extends BaseServiceImpl<UserSonglistMapstruct, UserSonglistDto,
        UserSonglistMapper, UserSonglist> implements UserSonglistService {

    @Autowired
    private SonglistSongService songlistSongService;
    @Autowired
    private SongService songService;

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public boolean deleteById(String id) {
        songlistSongService.remove(Wrappers.<SonglistSong>lambdaQuery().eq(SonglistSong::getSonglistId, id));
        removeById(id);
        return true;
    }

    @Override
    public List<SongInfoDto> getSonglistDetailById(String id) {
        List<SonglistSong> songlistSongs =
                songlistSongService.list(Wrappers.<SonglistSong>lambdaQuery().eq(SonglistSong::getSonglistId, id));
        List<String> songIds = songlistSongs.stream().map(SonglistSong::getSongId).collect(Collectors.toList());
        List<Song> songList = songService.list(Wrappers.<Song>lambdaQuery().in(Song::getId, songIds));
        return songService.getBaseMapstruct().toDto(songList);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public boolean addSong(String id, String songId) {
        SonglistSong sls = new SonglistSong(id, songId);
        if(songlistSongService.count(Wrappers.<SonglistSong>lambdaQuery()
                .eq(SonglistSong::getSonglistId, id).eq(SonglistSong::getSongId, songId)) > 0){
            return true;
        }
        songlistSongService.save(sls);
        UserSonglist userSonglist = getById(id);
        userSonglist.setSongCount(userSonglist.getSongCount() + 1);
        return updateById(userSonglist);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public boolean deleteSong(String id, String songId) {
        songlistSongService.remove(Wrappers.<SonglistSong>lambdaQuery()
                .eq(SonglistSong::getSonglistId, id).eq(SonglistSong::getSongId, songId));
        int count = songlistSongService.count(Wrappers.<SonglistSong>lambdaQuery().eq(SonglistSong::getSonglistId,id));
        UserSonglist userSonglist = getById(id);
        userSonglist.setSongCount(count);
        return updateById(userSonglist);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public boolean updateSongs(String id, List<String> songIds) {
        songlistSongService.remove(Wrappers.<SonglistSong>lambdaQuery().eq(SonglistSong::getSonglistId, id));
        List<SonglistSong> slsList = new ArrayList<>();
        for (String songId : songIds) {
            SonglistSong songlistSong = new SonglistSong(id, songId);
            slsList.add(songlistSong);
        }
        songlistSongService.saveBatch(slsList);
        return true;
    }
}
