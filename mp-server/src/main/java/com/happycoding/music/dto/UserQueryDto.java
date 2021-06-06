package com.happycoding.music.dto;

import com.happycoding.music.common.annotation.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2020/7/1 9:14
 */
@Data
public class UserQueryDto {
    @ApiModelProperty(value = "id")
    @Query
    private String id;

    @ApiModelProperty(value = "用户名")
    @Query(match = Query.Matching.INNER_LIKE)
    private String username;

    @ApiModelProperty(value = "昵称")
    @Query(column = "nick_name",match = Query.Matching.INNER_LIKE)
    private String nickName;

    @ApiModelProperty(value = "邮箱")
    @Query(match = Query.Matching.INNER_LIKE)
    private String email;

    @ApiModelProperty(value = "手机")
    @Query(column = "mobile_phone")
    private String mobilePhone;

    @ApiModelProperty(value = "状态:1:启用 0:禁用")
    @Query
    private Boolean enabled;
}
