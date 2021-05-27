package com.happycoding.music.dto;

import lombok.Data;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description: 专辑信息
 * @Date: 2021/5/27 9:52
 */
@Data
public class AlbumInfoDto {

    /**
     * 专辑id
     */
    private String id;

    /**
     * 专辑名称
     */
    private String name;
}
