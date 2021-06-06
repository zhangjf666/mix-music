package com.happycoding.music.dto;

import com.happycoding.music.common.annotation.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2020/7/1 8:56
 */
@Data
public class MenuQueryDto {
    @ApiModelProperty(value = "id")
    @Query
    private String id;

    @ApiModelProperty(value = "上级菜单id")
    @Query
    private String pid;

    @ApiModelProperty(value = "名称")
    @Query(match = Query.Matching.INNER_LIKE)
    private String name;
}
