package com.happycoding.music.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2021/6/7 9:19
 */
@Data
@ApiModel(value="UserConfigDto")
public class UserConfigDto implements Serializable {

    @ApiModelProperty(value = "用户id")
    private String id;

    @ApiModelProperty(value = "播放方式")
    private String playMode;
}
