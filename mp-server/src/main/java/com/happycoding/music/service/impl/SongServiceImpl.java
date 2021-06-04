package com.happycoding.music.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.happycoding.music.common.base.BaseServiceImpl;
import com.happycoding.music.common.exception.BusinessException;
import com.happycoding.music.dto.SongInfoDto;
import com.happycoding.music.dto.SongUrlDto;
import com.happycoding.music.entity.*;
import com.happycoding.music.mapper.*;
import com.happycoding.music.mapstruct.SongMapstruct;
import com.happycoding.music.model.MusicPlatform;
import com.happycoding.music.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 歌曲信息表 服务实现类
 * </p>
 *
 * @author zjf
 * @since 2021-06-03
 */
@Service
public class SongServiceImpl extends BaseServiceImpl<SongMapstruct, SongInfoDto, SongMapper, Song> implements SongService {
    @Autowired
    private SingerMapper singerMapper;
    @Autowired
    private AlbumMapper albumMapper;
    @Autowired
    private SongAlbumMapper songAlbumMapper;
    @Autowired
    private SongSingerMapper songSingerMapper;
    @Autowired
    private NeteaseService neteaseService;
    @Autowired
    private MiguService miguService;

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public boolean create(SongInfoDto dto){
        Song song = baseMapstruct.toEntity(dto);
        if(baseMapper.selectCount(Wrappers.<Song>lambdaQuery().eq(Song::getSongId, song.getSongId())
                .eq(Song::getPlatform, song.getPlatform())) <= 0){
            baseMapper.insert(song);
        }
        for (Singer singer : song.getSingers()) {
            if(singerMapper.selectCount(Wrappers.<Singer>lambdaQuery().eq(Singer::getSingerId, singer.getId())
                    .eq(Singer::getPlatform, singer.getPlatform())) <= 0) {
                singerMapper.insert(singer);
                SongSinger songSinger = new SongSinger(song.getId(), singer.getId());
                songSingerMapper.insert(songSinger);
            }
        }
        for (Album album : song.getAlbums()) {
            if(albumMapper.selectCount(Wrappers.<Album>lambdaQuery().eq(Album::getAlbumId, album.getAlbumId())
                    .eq(Album::getPlatform, album.getPlatform())) <= 0){
                albumMapper.insert(album);
                SongAlbum songAlbum = new SongAlbum(song.getId(), album.getId());
                songAlbumMapper.insert(songAlbum);
            }
        }
        dto = baseMapstruct.toDto(song);
        return true;
    }

    @Override
    public SongInfoDto querySong(String songId, MusicPlatform platform) {
        Song song = baseMapper.selectOne(Wrappers.<Song>lambdaQuery().eq(Song::getSongId, songId).eq(Song::getPlatform,
                platform));
        if(song != null){
            baseMapstruct.toDto(song);
        }
        return null;
    }

    @Override
    public String queryLyric(Long id) {
        String lyric = "";
        Song song = getById(id);
        if(song == null){
            throw new BusinessException("歌曲不存在");
        }
        lyric = song.getLyric();
        if(StringUtils.isBlank(lyric)){
            switch (song.getPlatform()){
                case Netease:
                    lyric = neteaseService.lyric(song.getSongId());
                    break;
                case Migu:
                    lyric = miguService.lyric(song.getSongId());
                    break;
                default:
                    break;
            }
            song.setLyric(lyric);
            updateById(song);
        }
        return lyric;
    }

    @Override
    public SongUrlDto queryUrl(Long id) {
        SongUrlDto songUrlDto = new SongUrlDto();
        Song song = getById(id);
        if(song == null){
            throw new BusinessException("歌曲不存在");
        }
        String url = song.getUrl();
        if(StringUtils.isBlank(url)){
            switch (song.getPlatform()){
                case Netease:
                    songUrlDto = neteaseService.songUrl(song.getSongId());
                    break;
                case Migu:
                    songUrlDto = miguService.songUrl(song.getSongId(),"HQ");
                    break;
                default:
                    break;
            }
            song.setUrl(songUrlDto.getUrl());
            song.setBr(songUrlDto.getBr());
            updateById(song);
        } else {
            songUrlDto.setUrl(song.getUrl());
            songUrlDto.setBr(song.getBr());
        }
        return songUrlDto;
    }

    @Override
    public SongInfoDto queryDetail(SongInfoDto dto) {
        switch (dto.getPlatform()){
            case Netease:
                dto = neteaseService.songDetail(dto);
                break;
            case Migu:
                dto = miguService.songDetail(dto);
                break;
            default:
                break;
        }
        return dto;
    }

    @Override
    public List<SongInfoDto> queryRelative(SongInfoDto dto) {
        return baseMapper.queryRelativeSong(dto);
    }
}
