package com.happycoding.music.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.happycoding.music.entity.SonglistSong;
import com.happycoding.music.mapper.SonglistSongMapper;
import com.happycoding.music.service.SonglistSongService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;

/**
 * <p>
 * 歌单-歌曲表 服务实现类
 * </p>
 *
 * @author zjf
 * @since 2021-06-07
 */
@Service
public class SonglistSongServiceImpl extends ServiceImpl<SonglistSongMapper, SonglistSong> implements SonglistSongService {

    @Override
    public boolean existSong(String songlistId, String songId) {
        return baseMapper.selectCount(Wrappers.<SonglistSong>lambdaQuery()
                .eq(SonglistSong::getSonglistId, songlistId).eq(SonglistSong::getSongId, songId)) > 0;
    }
}
