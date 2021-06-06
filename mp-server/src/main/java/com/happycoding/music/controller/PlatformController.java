package com.happycoding.music.controller;

import com.alibaba.fastjson.JSONObject;
import com.happycoding.music.common.annotation.Anonymous;
import com.happycoding.music.common.model.Response;
import com.happycoding.music.dto.*;
import com.happycoding.music.model.MusicPlatform;
import com.happycoding.music.service.SongService;
import com.happycoding.music.service.impl.MiguService;
import com.happycoding.music.service.impl.NeteaseService;
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
    @Autowired
    private SongService songService;

    @Anonymous
    @ApiOperation("推荐歌单")
    @GetMapping("/recommend")
    public Response<List<PlayListDto>> getRecommendPlayList(@RequestParam(name = "limit", defaultValue = "30") Integer limit) {
        List<PlayListDto> playList = neteaseService.personalized(limit);
        return Response.ok(playList);
    }

    @Anonymous
    @ApiOperation("歌单信息")
    @GetMapping("/playlistDetail")
    public Response<PlayListDetailDto> getPlayListDetail(String playListId, @RequestParam(name = "platform", defaultValue = "1") MusicPlatform musicPlatform) {
        PlayListDetailDto playListDetail = null;
        switch (musicPlatform) {
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

    @Anonymous
    @ApiOperation("歌曲信息")
    @GetMapping("/song")
    public Response<List<SongInfoDto>> getSongInfo(String songIds, @RequestParam(name = "platform", defaultValue = "1") MusicPlatform musicPlatform) {
        List<SongInfoDto> songInfoDtoList = new ArrayList<>();
        switch (musicPlatform) {
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

    @Anonymous
    @ApiOperation("获取歌词")
    @GetMapping("/lyric")
    public Response<String> getLyric(String songId) {
        String lyric = songService.queryLyric(songId);
        return Response.ok(lyric);
    }

    @Anonymous
    @ApiOperation("获取歌曲url")
    @GetMapping("/url")
    public Response<SongUrlDto> getUrl(String songId) {
        SongUrlDto songUrlDto = songService.queryUrl(songId);
        return Response.ok(songUrlDto);
    }

    @Anonymous
    @ApiOperation("获取歌曲详情(包括url和歌词)")
    @GetMapping("/songDetail")
    public Response<SongInfoDto> getSongDetail(SongInfoDto songInfo) {
        return Response.ok(songService.queryDetail(songInfo));
    }

    @Anonymous
    @ApiOperation("搜索")
    @GetMapping("/search")
    public Response<SongSearchPage> search(String keyword,
                                              @RequestParam(name = "type", defaultValue = "1") String type,
                                              @RequestParam(name = "limit", defaultValue = "20") long limit,
                                              @RequestParam(name = "offset", defaultValue = "0") long offset) {
        SongSearchPage songListPage = neteaseService.cloudSearch(keyword, type, limit, offset, true);
        return Response.ok(songListPage);
    }

    @Anonymous
    @ApiOperation("首页轮播图")
    @GetMapping("/banner")
    public Response search(@RequestParam(name = "type", defaultValue = "pc") String type) {
        JSONObject result = neteaseService.banner(type);
        return Response.ok(result);
    }

    @Anonymous
    @ApiOperation("推荐新歌")
    @GetMapping("/recommend/newsong")
    public Response personalizedNewSongs(
            @RequestParam(name = "limit", defaultValue = "10") Integer limit,
            @RequestParam(name = "areaId", defaultValue = "0") Integer areaId) {
        List<SongInfoDto> songInfoDtoList = neteaseService.personalizedNewSong(limit, areaId);
        return Response.ok(songInfoDtoList);
    }

    @Anonymous
    @ApiOperation("默认搜索关键词")
    @GetMapping("/searchDefaultKeyword")
    public Response searchDefaultKeyword() {
        JSONObject result = neteaseService.searchDefaultKeyword();
        return Response.ok(result);
    }

    @Anonymous
    @ApiOperation("热搜列表")
    @GetMapping("/searchHotDetail")
    public Response searchHotDetail() {
        JSONObject result = neteaseService.searchHotDetail();
        return Response.ok(result);
    }

    @Anonymous
    @ApiOperation("搜索建议")
    @GetMapping("/searchSuggest")
    public Response searchSuggest(@RequestParam(name = "keywords", defaultValue = "") String keywords,
                                  @RequestParam(name = "type", defaultValue = "mobile") String type) {
        JSONObject result = neteaseService.searchSuggest(keywords, type);
        return Response.ok(result);
    }

    @Anonymous
    @ApiOperation("全部歌单分类")
    @GetMapping("/allTags")
    public Response allTags() {
        JSONObject result = neteaseService.allTags();
        return Response.ok(result.get("sub"));
    }

    @Anonymous
    @ApiOperation("精品歌单分类")
    @GetMapping("/highQualityTags")
    public Response highQualityTags() {
        JSONObject result = neteaseService.highQualityTags();
        return Response.ok(result.get("tags"));
    }

    @Anonymous
    @ApiOperation("热门歌单分类")
    @GetMapping("/hotTags")
    public Response hotTags() {
        JSONObject result = neteaseService.hotTags();
        return Response.ok(result.get("tags"));
    }

    @Anonymous
    @ApiOperation("精品歌单")
    @GetMapping("/highQualityList")
    public Response<PlayListPage> highQualityList(@RequestParam(name = "cat", defaultValue = "全部") String cat,
                                    @RequestParam(name = "limit", defaultValue = "50") long limit,
                                    @RequestParam(name = "lasttime", defaultValue = "0") long lasttime) {
        PlayListPage result = neteaseService.highQualityList(cat, limit, lasttime);
        return Response.ok(result);
    }

    @Anonymous
    @ApiOperation("分类歌单")
    @GetMapping("/categoryList")
    public Response<PlayListPage> categoryList(@RequestParam(name = "cat", defaultValue = "全部") String cat,
                                               @RequestParam(name = "order", defaultValue = "hot") String order,
                                               @RequestParam(name = "limit", defaultValue = "50") long limit,
                                               @RequestParam(name = "offset", defaultValue = "0") long offset) {
        PlayListPage result = neteaseService.categoryList(cat, order,limit, offset);
        return Response.ok(result);
    }

    @Anonymous
    @ApiOperation("新歌速递")
    @GetMapping("/topSong")
    public Response<List<SongInfoDto>> topSong(@RequestParam(name = "areaId", defaultValue = "0") String areaId,
                                               @RequestParam(name = "limit", defaultValue = "100") long limit,
                                               @RequestParam(name = "offset", defaultValue = "0") long offset) {
        List<SongInfoDto> result = neteaseService.topSong(areaId, limit, offset, true);
        return Response.ok(result);
    }

    @Anonymous
    @ApiOperation("榜单内容摘要")
    @GetMapping("/topListDetail")
    public Response topListDetail(@RequestParam(name = "areaId", defaultValue = "0") String areaId,
                                               @RequestParam(name = "limit", defaultValue = "100") long limit,
                                               @RequestParam(name = "offset", defaultValue = "0") long offset) {
        JSONObject result = neteaseService.topListDetail();
        return Response.ok(result);
    }
}
