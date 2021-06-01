package com.happycoding.music.dto;

import lombok.Data;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description: 搜索页面
 * @Date: 2021/6/1 8:48
 */
@Data
public class SongSearchPage extends FlowPage<SongInfoDto> {
    /**
     * 搜索内容
     */
    private String keywords;
}
