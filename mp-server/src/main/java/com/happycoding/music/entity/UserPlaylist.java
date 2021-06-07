package com.happycoding.music.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 用户播放列表记录
 * </p>
 *
 * @author zjf
 * @since 2021-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_playlist")
@ApiModel(value="UserPlaylist对象", description="用户播放列表记录")
public class UserPlaylist implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "歌曲id")
    private String songId;

    public UserPlaylist(String userId, String songId) {
        this.userId = userId;
        this.songId = songId;
    }
}
