package com.happycoding.music.dto;

import com.happycoding.music.model.UserSongListType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2021/6/7 13:32
 */
@Data
public class UserSonglistDto {
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "歌单名称")
    private String listName;

    @ApiModelProperty(value = "歌单描述")
    private String listDescription;

    @ApiModelProperty(value = "歌单封面url")
    private String picUrl;

    @ApiModelProperty(value = "歌曲数量")
    private Integer songCount;

    @ApiModelProperty(value = "歌单类型(1:我喜欢,2:自定义歌单,3:收藏的歌单)")
    private UserSongListType type;

    @ApiModelProperty(value = "收藏的歌单id")
    private String collectListId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "删除标记")
    private String delFlag;
}
