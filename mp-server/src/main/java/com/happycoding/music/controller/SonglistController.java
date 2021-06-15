package com.happycoding.music.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.happycoding.music.common.model.Response;
import com.happycoding.music.common.utils.SpringSecurityUtil;
import com.happycoding.music.dto.SongInfoDto;
import com.happycoding.music.dto.UserSonglistDto;
import com.happycoding.music.entity.UserSonglist;
import com.happycoding.music.model.UserSongListType;
import com.happycoding.music.service.SonglistSongService;
import com.happycoding.music.service.UserSonglistService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description: 用户歌单接口
 * @Date: 2021/6/7 13:22
 */
@Slf4j
@RestController
@RequestMapping("/songlist")
@RequiredArgsConstructor
@Api(tags = "用户歌单接口")
public class SonglistController {
    @Autowired
    private UserSonglistService userSonglistService;
    @Autowired
    private SonglistSongService songlistSongService;

    @ApiOperation("获取用户歌单列表")
    @GetMapping
    public Response<List<UserSonglistDto>> getList(@RequestParam(name = "type", defaultValue = "0") UserSongListType type) {
        List<UserSonglistDto> list = new ArrayList<>();
        if(type == UserSongListType.ALL){
            list = userSonglistService.queryList(Wrappers.<UserSonglist>lambdaQuery().eq(UserSonglist::getUserId,
                            SpringSecurityUtil.getCurrentUserId()));
        } else {
            list = userSonglistService.queryList(Wrappers.<UserSonglist>lambdaQuery().eq(UserSonglist::getUserId,
                    SpringSecurityUtil.getCurrentUserId()).eq(UserSonglist::getType, type));
        }
        return Response.ok(list);
    }

    @ApiOperation("创建歌单")
    @PostMapping
    public Response<UserSonglistDto> createSonglist(UserSonglistDto dto) {
        dto = userSonglistService.create(dto);
        return Response.ok(dto);
    }

    @ApiOperation("更新歌单")
    @PutMapping
    public Response updateSonglist(UserSonglistDto dto) {
        return Response.ok(userSonglistService.update(dto));
    }

    @ApiOperation("删除歌单")
    @DeleteMapping
    public Response deleteSonglist(String id) {
        return Response.ok(userSonglistService.deleteById(id));
    }

    @ApiOperation("获取歌单歌曲详情")
    @GetMapping("/detail")
    public Response<List<SongInfoDto>> getSonglistDetail(String id) {
        List<SongInfoDto> list = userSonglistService.getSonglistDetailById(id);
        return Response.ok(list);
    }

    @ApiOperation("歌单添加歌曲")
    @PostMapping("/addSong")
    public Response addSong(String songlistId, String songId) {
        return Response.ok(userSonglistService.addSong(songlistId, songId));
    }

    @ApiOperation("歌单删除歌曲")
    @PostMapping("/delSong")
    public Response delSong(String songlistId, String songId) {
        return Response.ok(userSonglistService.deleteSong(songlistId, songId));
    }

    @ApiOperation("歌单中是否存在歌曲")
    @GetMapping("/existSong")
    public Response existSong(String songlistId, String songId) {
        return Response.ok(songlistSongService.existSong(songlistId, songId));
    }

    @ApiOperation("更新歌单歌曲")
    @PostMapping("/updateSongs")
    public Response updateSongs(String songlistId, List<String> songIds) {
        return Response.ok(userSonglistService.updateSongs(songlistId, songIds));
    }
}
