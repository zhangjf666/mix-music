package com.happycoding.music.mapper;

import com.happycoding.music.entity.Album;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.happycoding.music.entity.Singer;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

/**
 * <p>
 * 专辑信息表 Mapper 接口
 * </p>
 *
 * @author zjf
 * @since 2021-06-03
 */
public interface AlbumMapper extends BaseMapper<Album> {

    /**
     * 歌曲id获取专辑
     * @param id 内部id
     * @return
     */
    @Select("select * from album where id in (select album_id from song_album where song_id = #{id})")
    Set<Album> getSongAlbums(Long id);
}
