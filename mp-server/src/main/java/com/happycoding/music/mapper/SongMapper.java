package com.happycoding.music.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.happycoding.music.dto.SongInfoDto;
import com.happycoding.music.entity.Song;
import com.happycoding.music.model.MusicPlatform;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 歌曲信息表 Mapper 接口
 * </p>
 *
 * @author zjf
 * @since 2021-06-03
 */
public interface SongMapper extends BaseMapper<Song> {

    /**
     * 查询歌曲相关连歌曲
     * @param dto
     * @return
     */
    @Select("select * from song where id in (select dest_id from song_relative where source_id = #{dto.id})")
    @Results({
            @Result(property = "id",column = "id",id = true),
            @Result(property = "singers", javaType = Set.class,column = "id",many = @Many(select = "com.happycoding.music.mapper.SingerMapper.getSongSingers")),
            @Result(property = "albums", javaType = Set.class,column = "id",many = @Many(select = "com.happycoding.music.mapper.AlbumMapper.getSongAlbums"))
    })
    List<Song> queryRelativeSong(@Param("dto") SongInfoDto dto);

    /**
     * 根据歌曲id跟平台查找歌曲
     * @param songId 歌曲id
     * @param platform 平台
     * @return
     */
    @Select("select * from song s where s.song_id = #{songId} and s.platform = #{platform}")
    @Results({
            @Result(property = "id",column = "id",id = true),
            @Result(property = "singers", javaType = Set.class,column = "id",many = @Many(select = "com.happycoding.music.mapper.SingerMapper.getSongSingers")),
            @Result(property = "albums", javaType = Set.class,column = "id",many = @Many(select = "com.happycoding.music.mapper.AlbumMapper.getSongAlbums"))
    })
    Song querySongBySongId(String songId, MusicPlatform platform);

    /**
     * 根据id跟平台查找歌曲
     * @param id 歌曲id
     * @return
     */
    @Select("select * from song s where s.id = #{id}")
    @Results({
            @Result(property = "id",column = "id",id = true),
            @Result(property = "singers", javaType = Set.class,column = "id",many = @Many(select = "com.happycoding.music.mapper.SingerMapper.getSongSingers")),
            @Result(property = "albums", javaType = Set.class,column = "id",many = @Many(select = "com.happycoding.music.mapper.AlbumMapper.getSongAlbums"))
    })
    Song querySongById(String id);
}
