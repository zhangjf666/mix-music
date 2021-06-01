package com.happycoding.music.dto;

import lombok.Data;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description: 歌单分页
 * @Date: 2021/5/28 10:24
 */
@Data
public class PlayListPage extends FlowPage<PlayListDto> {
    /**
     * 歌单分类
     */
    private String cat;
}
