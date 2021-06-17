package com.happycoding.music.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.happycoding.music.common.base.BaseServiceImpl;
import com.happycoding.music.common.utils.SpringSecurityUtil;
import com.happycoding.music.dto.PlayListDetailDto;
import com.happycoding.music.dto.SongInfoDto;
import com.happycoding.music.dto.UserSonglistDetailDto;
import com.happycoding.music.dto.UserSonglistDto;
import com.happycoding.music.entity.Song;
import com.happycoding.music.entity.SonglistSong;
import com.happycoding.music.entity.UserSonglist;
import com.happycoding.music.mapper.UserSonglistMapper;
import com.happycoding.music.mapstruct.UserSonglistMapstruct;
import com.happycoding.music.model.UserSongListType;
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
    @Autowired
    private NeteaseService neteaseService;

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public boolean deleteById(String id) {
        songlistSongService.remove(Wrappers.<SonglistSong>lambdaQuery().eq(SonglistSong::getSonglistId, id));
        removeById(id);
        return true;
    }

    @Override
    public UserSonglistDetailDto getSonglistDetailById(String id) {
        UserSonglistDetailDto userSonglistDetailDto = new UserSonglistDetailDto();
        UserSonglist userSonglist = getById(id);
        if(userSonglist != null){
            userSonglistDetailDto.setSongList(baseMapstruct.toDto(userSonglist));
            UserSongListType type = userSonglist.getType();
            if(type == UserSongListType.COLLECT){
                //收藏的歌单,直接获取网易的
                PlayListDetailDto playListDetailDto = neteaseService.playListDetail(userSonglist.getCollectListId());
                userSonglistDetailDto.setSongs(playListDetailDto.getSongs());
            } else {
                //其他歌单
                List<SonglistSong> songlistSongs =
                        songlistSongService.list(Wrappers.<SonglistSong>lambdaQuery().eq(SonglistSong::getSonglistId, id));
                List<String> songIds = songlistSongs.stream().map(SonglistSong::getSongId).collect(Collectors.toList());
                List<SongInfoDto> list = new ArrayList<>();
                for (String songId : songIds) {
                    list.add(songService.querySongById(songId));
                }
                userSonglistDetailDto.setSongs(list);
            }
        }
        return userSonglistDetailDto;
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
        Song song = songService.getById(songId);
        UserSonglist userSonglist = getById(id);
        userSonglist.setSongCount(userSonglist.getSongCount() + 1);
        userSonglist.setPicUrl(song.getPicUrl());
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

    @Override
    public UserSonglistDto existCollectSonglist(String collectSonglistId) {
        UserSonglistDto dto = null;
        UserSonglist userSonglist =
                baseMapper.selectOne(Wrappers.<UserSonglist>lambdaQuery().eq(UserSonglist::getUserId,
                SpringSecurityUtil.getCurrentUserId()).eq(UserSonglist::getCollectListId, collectSonglistId));
        if(userSonglist != null){
            dto = baseMapstruct.toDto(userSonglist);
        }
        return dto;
    }
}
