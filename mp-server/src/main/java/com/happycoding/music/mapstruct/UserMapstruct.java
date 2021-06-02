package com.happycoding.music.mapstruct;

import com.happycoding.music.common.base.BaseMapstruct;
import com.happycoding.music.common.support.mapstruct.MapStructTransform;
import com.happycoding.music.dto.UserDto;
import com.happycoding.music.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2020/6/9 10:08
 */
@Mapper(componentModel = "spring",uses = {RoleMapstruct.class, MapStructTransform.class},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapstruct extends BaseMapstruct<UserDto, User> {
}
