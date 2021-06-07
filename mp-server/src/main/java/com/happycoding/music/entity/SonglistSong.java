package com.happycoding.music.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 歌单-歌曲表
 * </p>
 *
 * @author zjf
 * @since 2021-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("songlist_song")
@ApiModel(value="SonglistSong对象", description="歌单-歌曲表")
public class SonglistSong implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "歌单id")
    private String songlistId;

    @ApiModelProperty(value = "歌曲id")
    private String songId;

    public SonglistSong(String songlistId, String songId) {
        this.songlistId = songlistId;
        this.songId = songId;
    }
}
