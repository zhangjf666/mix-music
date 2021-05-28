package com.happycoding.music.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description: 歌单分页
 * @Date: 2021/5/28 10:24
 */
@Data
public class PlayListPage {

    /**
     * 歌单总数
     */
    private long total;

    /**
     * 更新时间
     */
    private long lastTime;

    /**
     * 是否后续还有
     */
    private boolean more;

    /**
     * 歌单分类
     */
    private String cat;

    /**
     * 歌单列表
     */
    private List<PlayListDto> playLists;
}
