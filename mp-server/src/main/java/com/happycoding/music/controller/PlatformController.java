package com.happycoding.music.controller;

import com.alibaba.fastjson.JSONObject;
import com.happycoding.music.common.model.Response;
import com.happycoding.music.dto.PlayListDetailDto;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description: 歌曲平台接口
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

    @ApiOperation("推荐歌单")
    @GetMapping("/recommend")
    public Response<List<PlayListDto>> getRecommendPlayList(@RequestParam(name = "limit", defaultValue = "30") Integer limit){
        List<PlayListDto> playList = neteaseService.personalized(limit);
        return Response.ok(playList);
    }

    @ApiOperation("歌单信息")
    @GetMapping("/playlist")
    public Response<PlayListDetailDto> getPlayListDetail(String playListId, MusicPlatform musicPlatform){
        PlayListDetailDto playListDetail = null;
        switch (musicPlatform){
            case Netease:
                playListDetail = neteaseService.playListDetail(playListId);
                break;
            case Migu:
                playListDetail = miguService.playListDetail(playListId);
                break;
            case All:
            default:
                break;

        }
        return Response.ok(playListDetail);
    }

    @ApiOperation("歌曲信息")
    @GetMapping("/song")
    public Response<List<SongInfoDto>> getSongInfo(String songIds, MusicPlatform musicPlatform){
        List<SongInfoDto> songInfoDtoList = new ArrayList<>();
        switch (musicPlatform){
            case Netease:
                songInfoDtoList = neteaseService.songInfo(songIds);
                break;
            case Migu:
                songInfoDtoList = miguService.songInfo(songIds);
                break;
            case All:
            default:
                break;
        }
        return Response.ok(songInfoDtoList);
    }

    @ApiOperation("获取歌词")
    @GetMapping("/lyric")
    public Response<String> getLyric(String songId, MusicPlatform musicPlatform){
        String lyric = "";
        switch (musicPlatform){
            case Netease:
                lyric = neteaseService.lyric(songId);
                break;
            case Migu:
                lyric = miguService.lyric(songId);
                break;
            case All:
            default:
                break;
        }
        return Response.ok(lyric);
    }

    @ApiOperation("获取歌曲详情(包括url和歌词)")
    @GetMapping("/songDetail")
    public Response<SongInfoDto> getSongDetail(SongInfoDto songInfo){
        switch (songInfo.getMusicPlatform()){
            case Netease:
                songInfo = neteaseService.songDetail(songInfo);
                break;
            case Migu:
                songInfo = miguService.songDetail(songInfo);
                break;
            case All:
            default:
                break;
        }
        return Response.ok(songInfo);
    }

    @ApiOperation("搜索")
    @GetMapping("/search")
    public Response<List<SongInfoDto>> search(String keyword,@RequestParam(name = "musicPlatform",
            defaultValue = "0") MusicPlatform musicPlatform){
        List<SongInfoDto> songInfoDtoList = new ArrayList<>();
        switch (musicPlatform){
            case Netease:
                songInfoDtoList.addAll(neteaseService.cloudSearch(keyword, "1", 30, 0, true));
                break;
            case Migu:
                songInfoDtoList.addAll(miguService.search(keyword,2, 30, 3));
                break;
            case All:
            default:
                songInfoDtoList.addAll(neteaseService.cloudSearch(keyword, "1", 30, 0, true));
                songInfoDtoList.addAll(miguService.search(keyword,2, 30, 3));
                break;
        }
        return Response.ok(songInfoDtoList);
    }

    @ApiOperation("首页轮播图")
    @GetMapping("/banner")
    public Response search(@RequestParam(name = "type", defaultValue = "pc") String type){
        JSONObject result = neteaseService.banner(type);
        return Response.ok(result);
    }

    @ApiOperation("推荐新歌")
    @GetMapping("/personalizedSongs")
    public Response personalizedSongs(
            @RequestParam(name = "limit", defaultValue = "10") Integer limit,
            @RequestParam(name = "areaId", defaultValue = "0") Integer areaId){
        List<SongInfoDto> songInfoDtoList = neteaseService.personalizedSongs(limit, areaId);
        return Response.ok(songInfoDtoList);
    }

    @ApiOperation("默认搜索关键词")
    @GetMapping("/searchDefaultKeyword")
    public Response searchDefaultKeyword(){
        JSONObject result = neteaseService.searchDefaultKeyword();
        return Response.ok(result);
    }

    @ApiOperation("热搜列表")
    @GetMapping("/searchHotDetail")
    public Response searchHotDetail(){
        JSONObject result = neteaseService.searchHotDetail();
        return Response.ok(result);
    }

    @ApiOperation("搜索建议")
    @GetMapping("/searchSuggest")
    public Response searchSuggest(@RequestParam(name = "keywords", defaultValue = "") String keywords,
                                  @RequestParam(name = "type", defaultValue = "mobile") String type){
        JSONObject result = neteaseService.searchSuggest(keywords, type);
        return Response.ok(result);
    }
}
