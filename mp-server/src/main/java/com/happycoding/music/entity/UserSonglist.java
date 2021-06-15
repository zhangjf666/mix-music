package com.happycoding.music.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.happycoding.music.model.UserSongListType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户歌单
 * </p>
 *
 * @author zjf
 * @since 2021-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_songlist")
@ApiModel(value="UserSonglist对象", description="用户歌单")
public class UserSonglist implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @ApiModelProperty(value = "歌单类型(1:我喜欢,2:自定义歌单)")
    private UserSongListType type;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @Version
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "删除标记")
    @TableLogic
    private String delFlag;


}
