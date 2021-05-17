package com.happycoding.music.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description: 歌单信息
 * @Date: 2021/5/11 9:38
 */
@Data
public class PlayListDetailDto {

    private PlayListDto playList;

    private List<SongInfoDto> songInfo;
}
