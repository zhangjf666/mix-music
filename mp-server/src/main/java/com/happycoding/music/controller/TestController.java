package com.happycoding.music.controller;

import com.happycoding.music.common.model.Response;
import com.happycoding.music.util.NeteaseRequestUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @ApiOperation("测试调用")
    @GetMapping
    public Response test(String id){
        return Response.ok(NeteaseRequestUtil.testRequest(id));
    }

}
