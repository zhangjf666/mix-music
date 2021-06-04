package com.happycoding.music.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.happycoding.music.dto.SongInfoDto;
import com.happycoding.music.entity.Song;

import java.util.List;

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
    List<SongInfoDto> queryRelativeSong(SongInfoDto dto);
}
