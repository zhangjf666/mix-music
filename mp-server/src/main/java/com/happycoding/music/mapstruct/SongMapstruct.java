package com.happycoding.music.mapstruct;

import com.happycoding.music.common.base.BaseMapstruct;
import com.happycoding.music.dto.SongInfoDto;
import com.happycoding.music.entity.Song;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2021/6/4 10:05
 */
@Mapper(componentModel = "spring", uses = {SingerMapstruct.class, AlbumMapstruct.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SongMapstruct extends BaseMapstruct<SongInfoDto, Song> {
}
