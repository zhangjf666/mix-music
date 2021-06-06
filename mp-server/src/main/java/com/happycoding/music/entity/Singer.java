package com.happycoding.music.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.happycoding.music.model.MusicPlatform;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 歌手信息表
 * </p>
 *
 * @author zjf
 * @since 2021-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("singer")
@ApiModel(value="Singer对象", description="歌手信息表")
public class Singer implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "歌手id")
    private String singerId;

    @ApiModelProperty(value = "歌手名称")
    private String singerName;

    @ApiModelProperty(value = "所属歌曲平台")
    private MusicPlatform platform;


}
