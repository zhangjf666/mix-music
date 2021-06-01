package com.happycoding.music.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description: 流式分页
 * @Date: 2021/6/1 8:49
 */
@Data
public class FlowPage<T> {
    /**
     * 总数
     */
    private long total;

    /**
     * 偏移量,页码乘以每页条数
     */
    private long offset;

    /**
     * 是否后续还有
     */
    private boolean more;

    /**
     * 数据
     */
    private List<T> data;
}
