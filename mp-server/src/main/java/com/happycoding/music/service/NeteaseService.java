package com.happycoding.music.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.happycoding.music.common.exception.BusinessException;
import com.happycoding.music.dto.*;
import com.happycoding.music.model.MusicPlatform;
import com.happycoding.music.model.NeteaseOption;
import com.happycoding.music.model.NeteaseResponse;
import com.happycoding.music.util.NeteaseRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.happycoding.music.util.NeteaseCryptoUtil.*;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2021/5/11 14:09
 */
@Slf4j
@Service
public class NeteaseService {
    @Autowired
    private MiguService miguService;

    /**
     * 推荐歌单
     * @param limit
     * @return
     */
    public List<PlayListDto> personalized(Integer limit){
        Map<String, Object> data = new HashMap<>();
        data.put("limit", limit);
        data.put("total", true);
        data.put("n", 1000);

        NeteaseOption option = new NeteaseOption();
        option.setCrypto("weapi");

        NeteaseResponse response = NeteaseRequestUtil.getResponse("POST",
                "https://music.163.com/weapi/personalized/playlist", data, option);
        checkError(response);

        List<PlayListDto> playList = new ArrayList<>();
        JSONArray list = response.getBody().getJSONArray("result");
        if(list == null || list.isEmpty()){
            return playList;
        }
        for (Object o : list) {
            JSONObject jo = (JSONObject)o;
            PlayListDto dto = new PlayListDto();
            dto.setMusicPlatform(MusicPlatform.Netease);
            dto.setId(jo.getString("id"));
            dto.setName(jo.getString("name"));
            dto.setPicUrl(jo.getString("picUrl"));
            dto.setSummary(jo.getString("copywriter"));
            dto.setPlayCount(jo.getLong("playCount"));
            dto.setTrackCount(jo.getLong("trackCount"));
            playList.add(dto);
        }
        return playList;
    }

    /**
     * 歌词
     * @param id
     * @return
     */
    public String lyric(String id){
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("lv", -1);
        data.put("kv", -1);
        data.put("tv", -1);

        NeteaseOption option = new NeteaseOption();
        option.setCrypto("linuxapi");

        NeteaseResponse response = NeteaseRequestUtil.getResponse("POST",
                "https://music.163.com/api/song/lyric", data, option);
        checkError(response);

        String lyric = response.getBody().getJSONObject("lrc").getString("lyric");
        return lyric;
    }

    /**
     * 多类型搜索
     * @param keywords
     * @param type
     * @return
     */
    public JSONObject searchMultiMatch(String keywords,String type){
        Map<String, Object> data = new HashMap<>();
        data.put("s", keywords);
        data.put("type", type);

        NeteaseOption option = new NeteaseOption();
        option.setCrypto("weapi");

        NeteaseResponse response = NeteaseRequestUtil.getResponse("POST",
                "https://music.163.com/weapi/search/suggest/multimatch", data, option);
        checkError(response);
        return response.getBody();
    }

    /**
     * 搜索建议
     * @param keywords
     * @param type
     * @return
     */
    public JSONObject searchSuggest(String keywords,String type){
        Map<String, Object> data = new HashMap<>();
        data.put("s", keywords);

        type = "mobile".equals(type) ? "keyword":"web";
        NeteaseOption option = new NeteaseOption();
        option.setCrypto("weapi");

        NeteaseResponse response = NeteaseRequestUtil.getResponse("POST",
                "https://music.163.com/weapi/search/suggest/"+type, data, option);
        checkError(response);
        return response.getBody();
    }

    /**
     * 热搜列表(详细)
     * @return
     */
    public JSONObject searchHotDetail(){
        Map<String, Object> data = new HashMap<>();

        NeteaseOption option = new NeteaseOption();
        option.setCrypto("weapi");

        NeteaseResponse response = NeteaseRequestUtil.getResponse("POST",
                "https://music.163.com/weapi/hotsearchlist/get", data, option);
        checkError(response);
        return response.getBody();
    }

    /**
     * 搜索
     * @param keywords
     * @param type
     * @param limit
     * @param offset
     * @param total
     * @return
     */
    public List<SongInfoDto> cloudSearch(String keywords, String type, long limit, long offset, boolean total){
        Map<String, Object> data = new HashMap<>();
        data.put("s", keywords);
        data.put("type", StringUtils.isNotBlank(type) ? type : "1");
        data.put("limit", limit);
        data.put("offset", offset);
        data.put("total", true);

        NeteaseOption option = new NeteaseOption();
        option.setCrypto("weapi");

        NeteaseResponse response = NeteaseRequestUtil.getResponse("POST",
                "https://music.163.com/api/cloudsearch/pc", data, option);
        checkError(response);

        JSONArray songs = response.getBody().getJSONObject("result").getJSONArray("songs");
        List<SongInfoDto> songInfoDtoList = new ArrayList<>();
        if(songs != null && !songs.isEmpty()){
            for (int i = 0; i < songs.size(); i++) {
                JSONObject song = songs.getJSONObject(i);
                SongInfoDto info = getSongInfo(song);
                songInfoDtoList.add(info);
            }
        }
        return songInfoDtoList;
    }

    /**
     * 歌曲url
     * @param ids
     * @return
     */
    public SongUrlDto songUrl(String ids){
        Map<String, Object> data = new HashMap<>();
        data.put("ids", ids.split(","));
        data.put("br", 999000);

        NeteaseOption option = new NeteaseOption();
        option.setCrypto("eapi");
        option.setUrl("/api/song/enhance/player/url");

        NeteaseResponse response = NeteaseRequestUtil.getResponse("POST",
                "https://interface3.music.163.com/eapi/song/enhance/player/url", data, option);
        checkError(response);

        SongUrlDto songUrl = new SongUrlDto();
        JSONArray songs = response.getBody().getJSONArray("data");
        if(songs != null && !songs.isEmpty()){
            JSONObject song = songs.getJSONObject(0);
            if(StringUtils.isNotBlank(song.getString("url"))){
                songUrl.setUrl(song.getString("url"));
            }
            int br = song.getInteger("br");
            if(br <= 128000){
                songUrl.setBr("LQ");
            } else if(br >= 320000 && br < 640000){
                songUrl.setBr("HQ");
            } else {
                songUrl.setBr("SQ");
            }
        }
        return songUrl;
    }

    /**
     * 歌曲信息
     * @param ids
     * @return
     */
    public List<SongInfoDto> songInfo(String ids){
        List<Map<String,String>> idsd = new ArrayList<>();
        String[] songIds = ids.split(",");
        for (String id:songIds) {
            Map<String, String> song = new HashMap<>();
            song.put("id", id);
            idsd.add(song);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("c", JSON.toJSONString(idsd));

        NeteaseOption option = new NeteaseOption();
        option.setCrypto("weapi");

        NeteaseResponse response = NeteaseRequestUtil.getResponse("POST",
                "https://music.163.com/api/v3/song/detail", data, option);
        checkError(response);

        List<SongInfoDto> songInfoDtoList = new ArrayList<>();
        JSONArray songs = response.getBody().getJSONArray("songs");
        if(songs != null && !songs.isEmpty()){
            for (int i = 0; i < songs.size(); i++) {
                JSONObject song = songs.getJSONObject(i);
                SongInfoDto sid = getSongInfo(song);
                songInfoDtoList.add(sid);
            }
        }
        return songInfoDtoList;
    }

    /**
     * 提取歌曲信息
     * @param song
     * @return
     */
    private SongInfoDto getSongInfo(JSONObject song) {
        SongInfoDto info = null;
        //fee 为1和4代表没有播放权限,需要用migu平台替换
        int fee = song.getInteger("fee");
        if(!canPlay(fee)){
            String songName = song.getString("name");
            String singerName = song.getJSONArray("ar").getJSONObject(0).getString("name");
            info = insteadSong(songName, singerName);
            if(info != null){
                return info;
            }
        }
        info = new SongInfoDto();
        info.setId(song.getString("id"));
        info.setDuration(song.getLong("dt"));
        info.setMusicPlatform(MusicPlatform.Netease);
        info.setName(song.getString("name"));
        List<SingerInfoDto> singerInfoDtos = new ArrayList<>();
        if(song.getJSONArray("ar") != null && !song.getJSONArray("ar").isEmpty()){
            for (Object o: song.getJSONArray("ar")) {
                JSONObject ar = (JSONObject) o;
                SingerInfoDto singer = new SingerInfoDto();
                singer.setId(ar.getString("id"));
                singer.setName(ar.getString("name"));
                singerInfoDtos.add(singer);
            }
        }
        info.setSingers(singerInfoDtos);

        List<AlbumInfoDto> albumInfoDtos = new ArrayList<>();
        if (song.containsKey("al") && song.getJSONObject("al") != null) {
            info.setPicUrl(song.getJSONObject("al").getString("picUrl"));
            AlbumInfoDto album = new AlbumInfoDto();
            album.setId(song.getJSONObject("al").getString("id"));
            album.setName(song.getJSONObject("al").getString("name"));
            albumInfoDtos.add(album);
        }
        info.setAlbums(albumInfoDtos);
        return info;
    }

    /**
     * 替换歌曲
     * @param songName 歌名
     * @param singerName 歌手名
     * @return
     */
    private SongInfoDto insteadSong(String songName, String singerName){
        log.info("网易云歌曲:{}-{}, 将使用其他平台歌曲替换.", songName, singerName);
        String keyword = StrUtil.format("{} {}", songName, singerName);
        List<SongInfoDto> list = miguService.search(keyword, 2, 30 , 1);
        if(!list.isEmpty()){
            SongInfoDto miguSong = list.get(0);
            if(songName.equals(miguSong.getName())
                    && miguSong.getSingers().stream().anyMatch(singerInfoDto -> singerInfoDto.getName().contains(singerName))){
                log.info("网易云歌曲:{}-{}, 使用{}平台歌曲替换.", songName, singerName, miguSong.getMusicPlatform().getName());
                return miguSong;
            }
        }
        log.info("网易云歌曲:{}-{}, 未找到平台歌曲替换.", songName, singerName);
        return null;
    }

    private boolean canPlay(int fee){
        return !(fee == 1 || fee == 4);
    }

    /**
     * 获取歌曲详情(包括url和歌词)
     * @param songInfo 歌曲信息
     * @return
     */
    public SongInfoDto songDetail(SongInfoDto songInfo){
        //获取url
        SongUrlDto urlDto = songUrl(songInfo.getId());
        songInfo.setUrl(urlDto.getUrl());
        songInfo.setBr(urlDto.getBr());
        //获取歌词
        String lyric = lyric(songInfo.getId());
        songInfo.setLyric(lyric);
        return songInfo;
    }

    /**
     * 歌单详情
     * @param id
     * @return
     */
    public PlayListDetailDto playListDetail(String id){
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("n", 100000);
        data.put("s", 1);

        NeteaseOption option = new NeteaseOption();
        option.setCrypto("linuxapi");

        NeteaseResponse response = NeteaseRequestUtil.getResponse("POST",
                "https://music.163.com/api/v6/playlist/detail", data, option);
        checkError(response);

        PlayListDetailDto playListDetail = new PlayListDetailDto();
        JSONObject playlist = response.getBody().getJSONObject("playlist");
        PlayListDto playListDto = new PlayListDto();
        playListDto.setId(playlist.getString("id"));
        playListDto.setName(playlist.getString("name"));
        playListDto.setMusicPlatform(MusicPlatform.Netease);
        playListDto.setPicUrl(playlist.getString("coverImgUrl"));
        playListDto.setPlayCount(playlist.getLong("playCount"));
        playListDto.setSummary(playlist.getString("description"));
        playListDto.setTrackCount(playlist.getLong("trackCount"));
        playListDetail.setPlayList(playListDto);

        JSONArray tracks = playlist.getJSONArray("trackIds");
        List<SongInfoDto> songInfoList = new ArrayList<>();
        if(tracks != null && !tracks.isEmpty()){
            List<String> ids = new ArrayList<>();
            for (int i = 0; i <tracks.size() ; i++) {
                JSONObject song = tracks.getJSONObject(i);
                ids.add(song.getString("id"));
            }
            songInfoList.addAll(songInfo(String.join(",", ids)));
        }
        playListDetail.setSongs(songInfoList);
        return playListDetail;
    }

    /**
     * 精选歌单
     * @param cat
     * @param limit
     * @param before
     * @param total
     * @return
     */
    public JSONObject topPlayListHighquality(String cat, int limit, int before, boolean total){
        Map<String, Object> data = new HashMap<>();
        data.put("cat", cat);
        data.put("limit", limit);
        data.put("lasttime", before);
        data.put("total", total);

        NeteaseOption option = new NeteaseOption();
        option.setCrypto("weapi");

        NeteaseResponse response = NeteaseRequestUtil.getResponse("POST",
                "https://music.163.com/api/playlist/highquality/list", data, option);
        checkError(response);
        return response.getBody();
    }

    /**
     * 歌单
     * @param cat
     * @param order
     * @param limit
     * @param offset
     * @param total
     * @return
     */
    public JSONObject topPlayList(String cat, String order, int limit, int offset, boolean total){
        Map<String, Object> data = new HashMap<>();
        data.put("cat", cat);
        data.put("order", order);
        data.put("limit", limit);
        data.put("offset", offset);
        data.put("total", total);

        NeteaseOption option = new NeteaseOption();
        option.setCrypto("weapi");

        NeteaseResponse response = NeteaseRequestUtil.getResponse("POST",
                "https://music.163.com/weapi/playlist/list", data, option);
        checkError(response);
        return response.getBody();
    }

    /**
     * 新歌速递
     * @param areaId
     * @param limit
     * @param offset
     * @param total
     * @return
     */
    public JSONObject topSong(int areaId, int limit, int offset, boolean total){
        Map<String, Object> data = new HashMap<>();
        data.put("areaId", areaId);
        data.put("limit", limit);
        data.put("offset", offset);
        data.put("total", total);

        NeteaseOption option = new NeteaseOption();
        option.setCrypto("weapi");

        NeteaseResponse response = NeteaseRequestUtil.getResponse("POST",
                "https://music.163.com/weapi/v1/discovery/new/songs", data, option);
        checkError(response);
        return response.getBody();
    }

    /**
     * 首页轮播图
     * @param type 'pc','android','iphone','ipad'
     * @return
     */
    public JSONObject banner(String type){
        Map<String, Object> data = new HashMap<>();
        data.put("clientType", type);

        NeteaseOption option = new NeteaseOption();
        option.setCrypto(LINUXAPI_TYPE);

        NeteaseResponse response = NeteaseRequestUtil.getResponse("POST",
                "https://music.163.com/api/v2/banner/get", data, option);
        checkError(response);
        return response.getBody();
    }

    /**
     * 推荐新歌
     * @param areaId
     * @param limit
     * @return
     */
    public List<SongInfoDto> personalizedNewSong(int limit,int areaId){
        Map<String, Object> data = new HashMap<>();
        data.put("areaId", areaId);
        data.put("limit", limit);
        data.put("type", "recommend");

        NeteaseOption option = new NeteaseOption();
        option.setCrypto("weapi");
        option.getCookie().put("os", "pc");

        NeteaseResponse response = NeteaseRequestUtil.getResponse("POST",
                "https://music.163.com/api/personalized/newsong", data, option);
        checkError(response);

        List<SongInfoDto> songInfoDtoList = new ArrayList<>();

        JSONArray result = response.getBody().getJSONArray("result");
        for (Object o : result) {
            JSONObject song = (JSONObject)o;
            SongInfoDto info = new SongInfoDto();
            int fee = song.getJSONObject("song").getInteger("fee");
            if(!canPlay(fee)){
                String name = song.getString("name");
                String singerName = song.getJSONObject("song").getJSONArray("artists").getJSONObject(0).getString("name");
                info = insteadSong(name, singerName);
                if(info != null){
                    songInfoDtoList.add(info);
                    continue;
                }
            }
            info.setMusicPlatform(MusicPlatform.Netease);
            info.setName(song.getString("name"));
            info.setId(song.getString("id"));
            info.setPicUrl(song.getString("picUrl"));
            song = song.getJSONObject("song");
            info.setDuration(song.getLong("duration"));

            List<SingerInfoDto> singerInfoDtos = new ArrayList<>();
            if(song.getJSONArray("artists") != null && !song.getJSONArray("artists").isEmpty()){
                for (Object a: song.getJSONArray("artists")) {
                    JSONObject ar = (JSONObject) a;
                    SingerInfoDto singer = new SingerInfoDto();
                    singer.setId(ar.getString("id"));
                    singer.setName(ar.getString("name"));
                    singerInfoDtos.add(singer);
                }
            }
            info.setSingers(singerInfoDtos);

            List<AlbumInfoDto> albumInfoDtos = new ArrayList<>();
            if (song.containsKey("album") && song.getJSONObject("album") != null) {
                info.setPicUrl(song.getJSONObject("album").getString("picUrl"));
                AlbumInfoDto album = new AlbumInfoDto();
                album.setId(song.getJSONObject("album").getString("id"));
                album.setName(song.getJSONObject("album").getString("name"));
                albumInfoDtos.add(album);
            }
            info.setAlbums(albumInfoDtos);
            songInfoDtoList.add(info);
        }
        return songInfoDtoList;
    }

    /**
     * 默认搜索关键词
     * @return
     */
    public JSONObject searchDefaultKeyword(){
        Map<String, Object> data = new HashMap<>();

        NeteaseOption option = new NeteaseOption();
        option.setCrypto(EAPI_TYPE);
        option.setUrl("/api/search/defaultkeyword/get");

        NeteaseResponse response = NeteaseRequestUtil.getResponse("POST",
                "https://interface3.music.163.com/eapi/search/defaultkeyword/get", data, option);
        checkError(response);
        return response.getBody();
    }

    /**
     * 全部歌单分类
     * @return
     */
    public JSONObject allTags(){
        Map<String, Object> data = new HashMap<>();

        NeteaseOption option = new NeteaseOption();
        option.setCrypto(WEAPI_TYPE);

        NeteaseResponse response = NeteaseRequestUtil.getResponse("POST",
                "https://music.163.com/weapi/playlist/catalogue", data, option);
        checkError(response);
        return response.getBody();
    }

    /**
     * 精品歌单 tags
     * @return
     */
    public JSONObject highQualityTags(){
        Map<String, Object> data = new HashMap<>();

        NeteaseOption option = new NeteaseOption();
        option.setCrypto(WEAPI_TYPE);

        NeteaseResponse response = NeteaseRequestUtil.getResponse("POST",
                "https://music.163.com/api/playlist/highquality/tags", data, option);
        checkError(response);
        return response.getBody();
    }

    /**
     * 热门歌单分类
     * @return
     */
    public JSONObject hotTags(){
        Map<String, Object> data = new HashMap<>();

        NeteaseOption option = new NeteaseOption();
        option.setCrypto(WEAPI_TYPE);

        NeteaseResponse response = NeteaseRequestUtil.getResponse("POST",
                "https://music.163.com/weapi/playlist/hottags", data, option);
        checkError(response);
        return response.getBody();
    }

    /**
     * 精品歌单
     * @param  cat 全部,华语,欧美,韩语,日语,粤语,小语种,运动,ACG,影视原声,流行,摇滚,后摇,古风,民谣,轻音乐,电子,器乐,说唱,古典,爵士
     * @return
     */
    public PlayListPage highQualityList(String cat, long limit, long lasttime){
        Map<String, Object> data = new HashMap<>();
        data.put("cat", cat);
        data.put("limit", limit);
        data.put("lasttime", lasttime);
        data.put("total", true);

        NeteaseOption option = new NeteaseOption();
        option.setCrypto(WEAPI_TYPE);

        NeteaseResponse response = NeteaseRequestUtil.getResponse("POST",
                "https://music.163.com/api/playlist/highquality/list", data, option);
        checkError(response);

        PlayListPage playListPage = new PlayListPage();
        JSONObject body = response.getBody();
        playListPage.setTotal(body.getLong("total"));
        playListPage.setOffset(body.getLong("lasttime"));
        playListPage.setCat(cat);
        playListPage.setMore(body.getBoolean("more"));
        JSONArray list = body.getJSONArray("playlists");
        if(list == null || list.isEmpty()){
            return playListPage;
        }
        List<PlayListDto> playList = new ArrayList<>();
        for (Object o : list) {
            JSONObject jo = (JSONObject)o;
            PlayListDto dto = new PlayListDto();
            dto.setMusicPlatform(MusicPlatform.Netease);
            dto.setId(jo.getString("id"));
            dto.setName(jo.getString("name"));
            dto.setPicUrl(jo.getString("coverImgUrl"));
            dto.setSummary(jo.getString("description"));
            dto.setPlayCount(jo.getLong("playCount"));
            dto.setTrackCount(jo.getLong("trackCount"));
            playList.add(dto);
        }
        playListPage.setPlayLists(playList);
        return playListPage;
    }

    /**
     * 分类歌单
     * @param cat 全部,华语,欧美,日语,韩语,粤语,小语种,流行,摇滚,民谣,电子,舞曲,说唱,轻音乐,爵士,乡村,R&B/Soul,古典,民族,英伦,金属,朋克,蓝调,雷鬼,世界音乐,拉丁,另类/独立,New Age,古风,后摇,Bossa Nova,清晨,夜晚,学习,工作,午休,下午茶,地铁,驾车,运动,旅行,散步,酒吧,怀旧,清新,浪漫,性感,伤感,治愈,放松,孤独,感动,兴奋,快乐,安静,思念,影视原声,ACG,儿童,校园,游戏,70后,80后,90后,网络歌曲,KTV,经典,翻唱,吉他,钢琴,器乐,榜单,00后
     * @param order hot,new
     * @return
     */
    public PlayListPage categoryList(String cat, String order, long limit, long offset){
        Map<String, Object> data = new HashMap<>();
        data.put("cat", cat);
        data.put("order", order);
        data.put("limit", limit);
        data.put("offset", offset);
        data.put("total", true);

        NeteaseOption option = new NeteaseOption();
        option.setCrypto(WEAPI_TYPE);

        NeteaseResponse response = NeteaseRequestUtil.getResponse("POST",
                "https://music.163.com/weapi/playlist/list", data, option);
        checkError(response);

        PlayListPage playListPage = new PlayListPage();
        JSONObject body = response.getBody();
        playListPage.setTotal(body.getLong("total"));
        playListPage.setCat(body.getString("cat"));
        playListPage.setMore(body.getBoolean("more"));
        JSONArray list = body.getJSONArray("playlists");
        if(list == null || list.isEmpty()){
            return playListPage;
        }
        playListPage.setOffset(offset + list.size());
        List<PlayListDto> playList = new ArrayList<>();
        for (Object o : list) {
            JSONObject jo = (JSONObject)o;
            PlayListDto dto = new PlayListDto();
            dto.setMusicPlatform(MusicPlatform.Netease);
            dto.setId(jo.getString("id"));
            dto.setName(jo.getString("name"));
            dto.setPicUrl(jo.getString("coverImgUrl"));
            dto.setSummary(jo.getString("description"));
            dto.setPlayCount(jo.getLong("playCount"));
            dto.setTrackCount(jo.getLong("trackCount"));
            playList.add(dto);
        }
        playListPage.setPlayLists(playList);
        return playListPage;
    }

    /**
     * 新专辑列表
     * @param area ALL:全部,ZH:华语,EA:欧美,KR:韩国,JP:日本
     * @param type new
     * @return
     */
    public JSONObject topAlbumList(String area, String type, int limit, int offset, int year, int month){
        Map<String, Object> data = new HashMap<>();
        data.put("area", area);
        data.put("type", type);
        data.put("limit", limit);
        data.put("offset", offset);
        data.put("year", year);
        data.put("month", month);
        data.put("total", false);
        data.put("rcmd", true);

        NeteaseOption option = new NeteaseOption();
        option.setCrypto(WEAPI_TYPE);

        NeteaseResponse response = NeteaseRequestUtil.getResponse("POST",
                "https://music.163.com/api/discovery/new/albums/area", data, option);
        checkError(response);

//        PlayListPage playListPage = new PlayListPage();
//        JSONObject body = response.getBody();
//        playListPage.setTotal(body.getLong("total"));
//        playListPage.setCat(body.getString("cat"));
//        playListPage.setMore(body.getBoolean("more"));
//        JSONArray list = body.getJSONArray("playlists");
//        if(list == null || list.isEmpty()){
//            return playListPage;
//        }
//        List<PlayListDto> playList = new ArrayList<>();
//        for (Object o : list) {
//            JSONObject jo = (JSONObject)o;
//            PlayListDto dto = new PlayListDto();
//            dto.setMusicPlatform(MusicPlatform.Netease);
//            dto.setId(jo.getString("id"));
//            dto.setName(jo.getString("name"));
//            dto.setPicUrl(jo.getString("coverImgUrl"));
//            dto.setSummary(jo.getString("description"));
//            dto.setPlayCount(jo.getLong("playCount"));
//            dto.setTrackCount(jo.getLong("trackCount"));
//            playList.add(dto);
//        }
//        playListPage.setPlayLists(playList);
        return response.getBody();
    }

    /**
     * 热门歌手
     * @return
     */
    public JSONObject hotArtistList(int limit, int offset){
        Map<String, Object> data = new HashMap<>();
        data.put("limit", limit);
        data.put("offset", offset);
        data.put("total", true);

        NeteaseOption option = new NeteaseOption();
        option.setCrypto(WEAPI_TYPE);

        NeteaseResponse response = NeteaseRequestUtil.getResponse("POST",
                "https://music.163.com/weapi/artist/top", data, option);
        checkError(response);
        return response.getBody();
    }

    /**
     * 排行榜
     * @return
     */
    public JSONObject rankList(String id){
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("n", 500);
        data.put("s", 0);

        NeteaseOption option = new NeteaseOption();
        option.setCrypto(WEAPI_TYPE);
        option.getCookie().put("os", "pc");

        NeteaseResponse response = NeteaseRequestUtil.getResponse("POST",
                "https://interface3.music.163.com/api/playlist/v4/detail", data, option);
        checkError(response);
        return response.getBody();
    }

    /**
     * 所有榜单介绍
     * @return
     */
    public JSONObject topListInfo(){
        Map<String, Object> data = new HashMap<>();

        NeteaseOption option = new NeteaseOption();
        option.setCrypto(LINUXAPI_TYPE);

        NeteaseResponse response = NeteaseRequestUtil.getResponse("POST",
                "https://music.163.com/api/toplist", data, option);
        checkError(response);
        return response.getBody();
    }

    /**
     * 所有榜单内容摘要
     * @return
     */
    public JSONObject topListDetail(){
        Map<String, Object> data = new HashMap<>();

        NeteaseOption option = new NeteaseOption();
        option.setCrypto(WEAPI_TYPE);

        NeteaseResponse response = NeteaseRequestUtil.getResponse("POST",
                "https://music.163.com/weapi/toplist/detail", data, option);
        checkError(response);
        return response.getBody();
    }

    /**
     * 歌手榜
     * @return
     */
    public JSONObject topArtistList(String type, int limit, int offset){
        Map<String, Object> data = new HashMap<>();
        data.put("type", type);
        data.put("limit", limit);
        data.put("offset", offset);
        data.put("total", true);

        NeteaseOption option = new NeteaseOption();
        option.setCrypto(WEAPI_TYPE);

        NeteaseResponse response = NeteaseRequestUtil.getResponse("POST",
                "https://music.163.com/weapi/toplist/artist", data, option);
        checkError(response);
        return response.getBody();
    }

    private void checkError(NeteaseResponse response){
        if(response.getCode() != 200){
            throw new BusinessException(response.getCode(), response.getMessage());
        }
    }
}
