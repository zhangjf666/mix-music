package com.happycoding.music.controller;

import com.happycoding.music.common.model.Page;
import com.happycoding.music.common.model.Response;
import com.happycoding.music.common.support.valid.Insert;
import com.happycoding.music.common.support.valid.Update;
import com.happycoding.music.dto.UserDto;
import com.happycoding.music.dto.UserQueryDto;
import com.happycoding.music.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2020/7/1 9:09
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Api(tags = "用户接口")
public class UserController {
    
    private final UserService userService;

    @ApiOperation("查询用户")
    @GetMapping
    @PreAuthorize("@ph.check('system:user:list')")
    public Response getUser(@Validated UserQueryDto queryDto, Page pageable){
        Page page = userService.queryPage(queryDto, pageable);
        return Response.ok(page);
    }

    @ApiOperation("查询单个用户")
    @GetMapping("/{id}")
    @PreAuthorize("@ph.check('system:user:list')")
    public Response getUser(@PathVariable Long id){
        return Response.ok(userService.queryById(id));
    }

    @ApiOperation("创建用户")
    @PostMapping
    @PreAuthorize("@ph.check('system:user:add')")
    public Response create(@Validated(Insert.class) @RequestBody UserDto dto){
        userService.create(dto);
        return Response.ok();
    }

    @ApiOperation("编辑用户")
    @PutMapping
    @PreAuthorize("@ph.check('system:user:edit')")
    public Response update(@Validated(Update.class) @RequestBody UserDto dto){
        userService.update(dto);
        return Response.ok();
    }

    @ApiOperation("删除用户")
    @DeleteMapping
    @PreAuthorize("@ph.check('system:user:del')")
    public Response delete(@RequestBody Set<Long> ids){
        for (Long id: ids) {
            userService.deleteById(id);
        }
        return Response.ok();
    }
}
