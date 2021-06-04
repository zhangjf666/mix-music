package com.happycoding.music.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 歌曲-专辑表
 * </p>
 *
 * @author zjf
 * @since 2021-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("song_album")
@ApiModel(value="SongAlbum对象", description="歌曲-专辑表")
public class SongAlbum implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "歌曲id")
    private Long songId;

    @ApiModelProperty(value = "专辑id")
    private Long albumId;

    public SongAlbum(Long songId, Long albumId) {
        this.songId = songId;
        this.albumId = albumId;
    }
}
