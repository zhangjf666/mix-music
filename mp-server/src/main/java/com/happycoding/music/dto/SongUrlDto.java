package com.happycoding.music.dto;

import lombok.Data;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2021/5/17 11:27
 */
@Data
public class SongUrlDto {
    /**
     * 歌曲url
     */
    private String url = "";

    /**
     * 品质
     */
    private String br = "HQ";
}
