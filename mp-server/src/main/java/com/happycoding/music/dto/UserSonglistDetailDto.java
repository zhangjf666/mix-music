package com.happycoding.music.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2021/6/17 14:29
 */
@Data
public class UserSonglistDetailDto {
    private UserSonglistDto songList;

    private List<SongInfoDto> songs;
}
