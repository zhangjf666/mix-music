package com.happycoding.music.mapstruct;

import com.happycoding.music.common.base.BaseMapstruct;
import com.happycoding.music.dto.MenuDto;
import com.happycoding.music.entity.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2020/6/9 10:10
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuMapstruct extends BaseMapstruct<MenuDto, Menu> {
}
