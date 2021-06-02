package com.happycoding.music.mapstruct;

import com.happycoding.music.common.base.BaseMapstruct;
import com.happycoding.music.dto.RoleDto;
import com.happycoding.music.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2020/6/9 10:09
 */
@Mapper(componentModel = "spring", uses = {MenuMapstruct.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapstruct extends BaseMapstruct<RoleDto, Role> {
}
