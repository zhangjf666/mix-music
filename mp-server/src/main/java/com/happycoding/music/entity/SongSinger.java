package com.happycoding.music.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 歌曲-歌手表
 * </p>
 *
 * @author zjf
 * @since 2021-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("song_singer")
@ApiModel(value="SongSinger对象", description="歌曲-歌手表")
public class SongSinger implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "歌曲id")
    private String songId;

    @ApiModelProperty(value = "歌手id")
    private String singerId;

    public SongSinger(String songId, String singerId){
        this.songId = songId;
        this.singerId = singerId;
    }
}
