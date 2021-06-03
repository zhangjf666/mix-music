package com.happycoding.music.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "源平台歌曲id")
    private String sourceSongId;

    @ApiModelProperty(value = "源平台代码")
    private String sourcePlatform;

    @ApiModelProperty(value = "目标平台歌曲id")
    private String destSongId;

    @ApiModelProperty(value = "目标平台代码")
    private String destPlatform;


}
