package com.happycoding.music.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
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
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @Version
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "删除标记")
    @TableLogic
    private String delFlag;


}
