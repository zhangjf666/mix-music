package com.happycoding.music.controller;

import cn.hutool.core.lang.Dict;
import com.happycoding.music.common.model.Response;
import com.happycoding.music.service.NeteaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2021/5/10 11:04
 */
@Slf4j
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
@Api(tags = "测试接口")
public class TestController {
    @Autowired
    private NeteaseService neteaseService;

    @ApiOperation("测试调用")
    @GetMapping
    public Response test(String id){
        return Response.ok();
    }

    @ApiOperation("测试新歌速递")
    @GetMapping("/topSong")
    public Response testTopSong(int areaId, int limit, int offset, boolean total){
        Dict dict = neteaseService.getTopSong(areaId, limit, offset, total);
        return Response.ok(dict);
    }
}
