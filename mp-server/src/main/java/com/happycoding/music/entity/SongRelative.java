package com.happycoding.music.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 各平台歌曲对应表
 * </p>
 *
 * @author zjf
 * @since 2021-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("song_relative")
@ApiModel(value="SongRelative对象", description="各平台歌曲对应表")
public class SongRelative implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "源平台歌曲id")
    private String sourceId;

    @ApiModelProperty(value = "目标平台歌曲id")
    private String destId;

    public SongRelative(String sourceId, String destId) {
        this.sourceId = sourceId;
        this.destId = destId;
    }
}
