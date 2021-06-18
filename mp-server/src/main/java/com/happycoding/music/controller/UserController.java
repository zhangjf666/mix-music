package com.happycoding.music.controller;

import com.happycoding.music.common.model.Page;
import com.happycoding.music.common.model.Response;
import com.happycoding.music.common.support.valid.Insert;
import com.happycoding.music.common.support.valid.Update;
import com.happycoding.music.common.utils.SpringSecurityUtil;
import com.happycoding.music.dto.SongInfoDto;
import com.happycoding.music.dto.UserConfigDto;
import com.happycoding.music.dto.UserDto;
import com.happycoding.music.dto.UserQueryDto;
import com.happycoding.music.entity.UserConfig;
import com.happycoding.music.mapstruct.UserConfigMapstruct;
import com.happycoding.music.service.UserConfigService;
import com.happycoding.music.service.UserPlaylistService;
import com.happycoding.music.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

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

    private final UserConfigService userConfigService;

    private final UserConfigMapstruct userConfigMapstruct;

    private final UserPlaylistService userPlaylistService;

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
    public Response getUser(@PathVariable String id){
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
    public Response delete(String ids){
        String[] userIds = ids.split(",");
        for (String id: userIds) {
            userService.deleteById(id);
        }
        return Response.ok();
    }

    @ApiOperation("获取用户配置")
    @GetMapping("/config")
    public Response<UserConfig> getConfig(){
        UserConfig config = userConfigService.getById(SpringSecurityUtil.getCurrentUserId());
        return Response.ok(config);
    }

    @ApiOperation("更改用户配置")
    @PutMapping("/config")
    public Response putConfig(@RequestBody UserConfigDto dto){
        dto.setId(SpringSecurityUtil.getCurrentUserId());
        userConfigService.updateById(userConfigMapstruct.toEntity(dto));
        return Response.ok();
    }

    @ApiOperation("获取用户播放列表")
    @GetMapping("/playlist")
    public Response<List<SongInfoDto>> getPlaylist(){
        return Response.ok(userPlaylistService.queryUserPlaylistByUserId(SpringSecurityUtil.getCurrentUserId()));
    }

    @ApiOperation("更新用户播放列表")
    @PutMapping("/playlist")
    public Response<List<SongInfoDto>> putPlaylist(String songIds){
        String[] ids = songIds.split(",");
        userPlaylistService.updateUserPlayList(SpringSecurityUtil.getCurrentUserId(), Arrays.asList(ids));
        return Response.ok();
    }
}
