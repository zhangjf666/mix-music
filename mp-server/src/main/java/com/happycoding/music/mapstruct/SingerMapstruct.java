package com.happycoding.music.mapstruct;

import com.happycoding.music.common.base.BaseMapstruct;
import com.happycoding.music.dto.SingerInfoDto;
import com.happycoding.music.entity.Singer;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2021/6/4 10:38
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SingerMapstruct extends BaseMapstruct<SingerInfoDto, Singer> {
}
