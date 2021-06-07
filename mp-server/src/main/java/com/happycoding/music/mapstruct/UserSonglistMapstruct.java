package com.happycoding.music.mapstruct;

import com.happycoding.music.common.base.BaseMapstruct;
import com.happycoding.music.dto.UserSonglistDto;
import com.happycoding.music.entity.UserSonglist;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2021/6/7 13:33
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserSonglistMapstruct extends BaseMapstruct<UserSonglistDto, UserSonglist> {
}
