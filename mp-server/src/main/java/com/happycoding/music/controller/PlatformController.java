package com.happycoding.music.controller;

import com.happycoding.music.common.model.Response;
import com.happycoding.music.dto.PlayListDto;
import com.happycoding.music.dto.SongInfoDto;
import com.happycoding.music.model.MusicPlatform;
import com.happycoding.music.service.MiguService;
import com.happycoding.music.service.NeteaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2021/5/10 11:04
 */
@Slf4j
@RestController
@RequestMapping("/platform")
@RequiredArgsConstructor
@Api(tags = "歌曲接口")
public class PlatformController {
    @Autowired
    private NeteaseService neteaseService;
    @Autowired
    private MiguService miguService;

    @ApiOperation("热门歌单")
    @GetMapping("/recommend")
    public Response<List<PlayListDto>> getRecommendPlayList(MusicPlatform musicPlatform){
        List<PlayListDto> playList = new ArrayList<>();
        switch (musicPlatform){
            case Netease:
                playList.addAll(neteaseService.personalized(30));
                break;
            case Migu:
                playList.addAll(miguService.personalized(1, 30, "1"));
                break;
            case All:
            default:
                playList.addAll(neteaseService.personalized(30));
                playList.addAll(miguService.personalized(1, 30, "1"));
                break;

        }
        return Response.ok(playList);
    }

    @ApiOperation("歌曲信息")
    @GetMapping("/song")
    public Response<List<SongInfoDto>> getRecommendPlayList(String songIds, MusicPlatform musicPlatform){
        List<SongInfoDto> info = new ArrayList<>();
        switch (musicPlatform){
            case Netease:
                info = neteaseService.songDetail(songIds);
                break;
            case Migu:
                info = miguService.song(songIds);
                break;
            case All:
            default:
                break;

        }
        return Response.ok(info);
    }
}
