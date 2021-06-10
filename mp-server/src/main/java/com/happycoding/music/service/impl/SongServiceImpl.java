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
    public SongInfoDto create(SongInfoDto dto){
        Song song = baseMapstruct.toEntity(dto);
        Song existSong = baseMapper.selectOne(Wrappers.<Song>lambdaQuery().eq(Song::getSongId, song.getSongId())
                .eq(Song::getPlatform, song.getPlatform()));
        if(existSong == null){
            baseMapper.insert(song);
        } else {
            song.setId(existSong.getId());
        }
        for (Singer singer : song.getSingers()) {
            Singer existSinger = singerMapper.selectOne(Wrappers.<Singer>lambdaQuery().eq(Singer::getSingerName, singer.getSingerName())
                    .eq(Singer::getPlatform, singer.getPlatform()));
            if(existSinger == null){
                singerMapper.insert(singer);
                SongSinger songSinger = new SongSinger(song.getId(), singer.getId());
                songSingerMapper.insert(songSinger);
            } else {
                SongSinger songSinger = new SongSinger(song.getId(), existSinger.getId());
                songSingerMapper.insert(songSinger);
            }
        }
        for (Album album : song.getAlbums()) {
            Album existAlbum = albumMapper.selectOne(Wrappers.<Album>lambdaQuery().eq(Album::getAlbumName, album.getAlbumName())
                    .eq(Album::getPlatform, album.getPlatform()));
            if(existAlbum == null){
                albumMapper.insert(album);
                SongAlbum songAlbum = new SongAlbum(song.getId(), album.getId());
                songAlbumMapper.insert(songAlbum);
            } else {
                SongAlbum songAlbum = new SongAlbum(song.getId(), existAlbum.getId());
                songAlbumMapper.insert(songAlbum);
            }
        }
        return baseMapstruct.toDto(song);
    }

    @Override
    public SongInfoDto querySong(String songId, MusicPlatform platform) {
        Song song = baseMapper.selectOne(Wrappers.<Song>lambdaQuery().eq(Song::getSongId, songId).eq(Song::getPlatform,
                platform));
        if(song != null){
            return baseMapstruct.toDto(song);
        }
        return null;
    }

    @Override
    public String queryLyric(String id) {
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
    public SongUrlDto queryUrl(String id) {
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
            //不保存歌曲url,网易歌曲url随机生成,过一段时间后就无效了
            //updateById(song);
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
        return baseMapstruct.toDto(baseMapper.queryRelativeSong(dto));
    }

    @Override
    public SongInfoDto querySongBySongId(String songId, MusicPlatform platform) {
        return baseMapstruct.toDto(baseMapper.querySongBySongId(songId, platform));
    }
}
