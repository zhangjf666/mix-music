package com.happycoding.music.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 歌曲信息表
 * </p>
 *
 * @author zjf
 * @since 2021-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("song")
@ApiModel(value="Song对象", description="歌曲信息表")
public class Song implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "内部id")
    private Long internalId;

    @ApiModelProperty(value = "歌曲id")
    private String id;

    @ApiModelProperty(value = "歌曲名称")
    private String name;

    @ApiModelProperty(value = "歌曲图片")
    private String picUrl;

    @ApiModelProperty(value = "时长")
    private Long duration;

    @ApiModelProperty(value = "歌曲url")
    private String url;

    @ApiModelProperty(value = "音质")
    private String br;

    @ApiModelProperty(value = "歌词")
    private String lyric;

    @ApiModelProperty(value = "所属歌曲平台")
    private String platform;


}