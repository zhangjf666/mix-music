package com.happycoding.music.mapper;

import com.happycoding.music.entity.Singer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

/**
 * <p>
 * 歌手信息表 Mapper 接口
 * </p>
 *
 * @author zjf
 * @since 2021-06-03
 */
public interface SingerMapper extends BaseMapper<Singer> {

    /**
     * 歌曲id获取歌手
     * @param id 内部id
     * @return
     */
    @Select("select * from singer where id in (select singer_id from song_singer where song_id = #{id})")
    Set<Singer> getSongSingers(Long id);
}
