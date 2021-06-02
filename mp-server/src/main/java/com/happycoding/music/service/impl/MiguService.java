package com.happycoding.music.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.happycoding.music.common.exception.BusinessException;
import com.happycoding.music.dto.*;
import com.happycoding.music.model.MiguResponse;
import com.happycoding.music.model.MusicPlatform;
import com.happycoding.music.util.MiguRequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2021/5/13 14:20
 */
@Service
public class MiguService {

    private void checkError(MiguResponse response){
        if(response.getCode() != 200){
            throw new BusinessException(response.getCode(), response.getMessage());
        }
    }

    /**
     * 搜索
     * @return
     */
    public List<SongInfoDto> search(String keyword, int type, int rows, int pgc){
        Map<String, Object> param = new HashMap<>();
        param.put("keyword", keyword);
        param.put("rows", rows);
        param.put("type", type);
        param.put("pgc", pgc);

        MiguResponse response = MiguRequestUtil.getResoponse("GET",
                "https://m.music.migu.cn/migu/remoting/scr_search_tag", param);
        checkError(response);

        JSONArray musics = response.getBody().getJSONArray("musics");
        List<SongInfoDto> songInfoDtoList = new ArrayList<>();
        if(musics != null && !musics.isEmpty()){
            for (int i = 0; i < musics.size(); i++) {
                JSONObject song = musics.getJSONObject(i);
                SongInfoDto info = new SongInfoDto();
                info.setId(song.getString("copyrightId"));
                info.setName(song.getString("songName"));
                //专辑信息
                List<AlbumInfoDto> albumInfoDtos = new ArrayList<>();
                if(song.getString("albumId") != null && song.getString("albumName") != null){
                    String[] albumIds = song.getString("albumId").split(",");
                    String[] albumNames = song.getString("albumName").split(",");
                    for (int ia = 0; ia < albumIds.length; ia++) {
                        AlbumInfoDto album = new AlbumInfoDto();
                        album.setId(albumIds[ia]);
                        album.setName(albumNames[ia]);
                        albumInfoDtos.add(album);
                    }
                }
                info.setAlbums(albumInfoDtos);
                // 歌手信息
                List<SingerInfoDto> singerInfoDtos = new ArrayList<>();
                if(song.getString("singerId") != null && song.getString("singerName") != null){
                    String[] singerIds = song.getString("singerId").split(",");
                    String[] singerNames = song.getString("singerName").split(",");
                    for (int is = 0; is < singerIds.length; is++) {
                        SingerInfoDto singer = new SingerInfoDto();
                        singer.setId(singerIds[is]);
                        singer.setName(singerNames[is]);
                        singerInfoDtos.add(singer);
                    }
                }
                info.setSingers(singerInfoDtos);

                info.setPicUrl(song.getString("cover"));
                info.setMusicPlatform(MusicPlatform.Migu);
                songInfoDtoList.add(info);
            }
        }
        return songInfoDtoList;
    }

    /**
     * 获取歌曲url
     * @param cid 歌曲cid
     * @param br 品质(LQ,HQ,SQ,ZQ)
     * @return
     */
    public SongUrlDto songUrl(String cid, String br){
        Map<String, Object> param = new HashMap<>();
        param.put("copyrightId", cid);
        param.put("resourceType", 2);

        MiguResponse response = MiguRequestUtil.getResoponse("GET",
                "https://c.musicapp.migu.cn/MIGUM2.0/v1.0/content/resourceinfo.do", param);
        checkError(response);

        JSONArray data = response.getBody().getJSONArray("resource");
        JSONObject res = data.getJSONObject(0);
        SongUrlDto dto = new SongUrlDto();
        dto.setBr(br);
        JSONArray rateFormats = res.getJSONArray("newRateFormats");
        if(rateFormats == null || rateFormats.isEmpty()){
            rateFormats = res.getJSONArray("rateFormats");
        }
        findUrl(rateFormats, dto);
        return dto;
    }

    private void findUrl(JSONArray rateFormats, SongUrlDto dto){
        String url = "";
        String insteadUrl = "https://freetyst.nf.migu.cn";
        String reg = "ftp://(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d):\\d+";
        for (Object rate: rateFormats) {
            JSONObject obj = (JSONObject)rate;
            String formatType = obj.getString("formatType");
            if(dto.getBr().equals(formatType)){
                url = obj.getString("url").replaceAll(reg,insteadUrl);
                dto.setUrl(url);
                break;
            }
        }
        if(StringUtils.isBlank(url) && !"LQ".equals(dto.getBr())){
            dto.setBr(getPreBr(dto.getBr()));
            findUrl(rateFormats, dto);
        }
    }

    private String getPreBr(String br){
        String preBr = "";
        switch (br){
            case "ZQ":
                preBr = "SQ";
                break;
            case "SQ":
                preBr = "HQ";
                break;
            case "HQ":
                preBr = "LQ";
                break;
            default:
                preBr = "LQ";
                break;
        }
        return preBr;
    }

    /**
     * 获取歌曲信息
     * @return
     */
    public List<SongInfoDto> songInfo(String cids){
        Map<String, Object> param = new HashMap<>();
        param.put("copyrightId", cids);
        param.put("type", 1);

        MiguResponse response = MiguRequestUtil.getResoponse("GET",
                "https://music.migu.cn/v3/api/music/audioPlayer/songs", param);
        checkError(response);

        JSONArray msongs =  response.getBody().getJSONArray("items");
        if(msongs != null && !msongs.isEmpty()) {
            List<SongInfoDto> songInfos = new ArrayList<>();
            for (int i = 0; i < msongs.size(); i++) {
                JSONObject song = msongs.getJSONObject(i);
                SongInfoDto sid = new SongInfoDto();
                sid.setId(song.getString("copyrightId"));
                DateTime length = DateUtil.parseTime(song.getString("length"));
                int duration = length.minute() * 60000 + length.second() * 1000 + length.millsecond();
                sid.setDuration(duration);
                sid.setMusicPlatform(MusicPlatform.Migu);
                sid.setName(song.getString("songName"));

                if(song.getJSONArray("singers") != null && !song.getJSONArray("singers").isEmpty()){
                    List<SingerInfoDto> singerInfoDtos = new ArrayList<>();
                    for (Object si : song.getJSONArray("singers")) {
                        JSONObject sin = (JSONObject) si;
                        SingerInfoDto singer = new SingerInfoDto();
                        singer.setId(sin.getString("artistId"));
                        singer.setName(sin.getString("artistName"));
                        singerInfoDtos.add(singer);
                    }
                    sid.setSingers(singerInfoDtos);
                }

                if (song.containsKey("albums") && song.getJSONArray("albums") != null && !song.getJSONArray("albums").isEmpty()) {
                    List<AlbumInfoDto> albumInfoDtos = new ArrayList<>();
                    for (Object al:song.getJSONArray("albums")) {
                        JSONObject alb = (JSONObject) al;
                        AlbumInfoDto album = new AlbumInfoDto();
                        album.setId(alb.getString("albumId"));
                        album.setName(alb.getString("albumName"));
                        albumInfoDtos.add(album);
                    }
                    sid.setAlbums(albumInfoDtos);
                }
                songInfos.add(sid);
            }
            return songInfos;
        }
        return new ArrayList<>();
    }

    /**
     * 获取歌曲详情(包括url和歌词)
     * @param songInfo 歌曲信息
     * @return
     */
    public SongInfoDto songDetail(SongInfoDto songInfo){
        //获取url
        SongUrlDto urlDto = songUrl(songInfo.getId(), "HQ");
        songInfo.setUrl(urlDto.getUrl());
        songInfo.setBr(urlDto.getBr());
        //获取歌词
        String lyric = lyric(songInfo.getId());
        songInfo.setLyric(lyric);
        return songInfo;
    }

    private List<String> songCidInPlayList(String playListId, int contentCount){
        List<String> cids = new ArrayList<>();
        int pageNo = 1;
        while ((pageNo -1) * 20 < contentCount){
            String url = StrUtil.format("https://music.migu.cn/v3/music/playlist/{}?page={}", playListId, pageNo);
            MiguResponse response = MiguRequestUtil.getResoponse("GET", url, new HashMap(), true);
            String html = response.getBody().getString("html");
            Document document = Jsoup.parse(html);
            Elements elements = document.select("[class=songlist-body]");
            Elements children = elements.get(0).children();
            for (Element child: children) {
                String cid = child.attr("data-cid");
                cids.add(cid);
            }
            pageNo++;
        }
        return cids;
    }

    /**
     * 获取歌单信息
     * @return
     */
    public PlayListDetailDto playListDetail(String id){
        Map<String,Object> param = new HashMap<>();
        param.put("playListId", id);

        MiguResponse response = MiguRequestUtil.getResoponse("GET",
                "http://m.music.migu.cn/migu/remoting/query_playlist_by_id_tag?onLine=1&queryChannel=0&createUserId=migu&contentCountMin=5", param);
        checkError(response);

        String code = response.getBody().getJSONObject("rsp").getString("code");
        if("000000".equals(code)){
            PlayListDetailDto playListDetailDto = new PlayListDetailDto();
            JSONObject playList = response.getBody().getJSONObject("rsp").getJSONArray("playList").getJSONObject(0);
            PlayListDto playListDto = getPlayListDto(playList);
            playListDetailDto.setPlayList(playListDto);

            int contentCount = playList.getInteger("contentCount");
            List<String> cids = songCidInPlayList(id, contentCount);
            String cidsStr = String.join(",", cids);
            List<SongInfoDto> songInfos = songInfo(cidsStr);
            playListDetailDto.setSongs(songInfos);
            return playListDetailDto;
        }
        return null;
    }

    /**
     * 获取歌词
     * @return
     */
    public String lyric(String cid){
        Map<String,Object> param = new HashMap<>();
        param.put("copyrightId", cid);

        MiguResponse response = MiguRequestUtil.getResoponse("GET",
                "http://music.migu.cn/v3/api/music/audioPlayer/getLyric", param);
        checkError(response);
        String lyric = response.getBody().getString("lyric");
        return lyric;
    }

    /**
     * 推荐歌单
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @param type 1:推荐,2:最新
     * @return
     */
    public List<PlayListDto> personalized(int pageNo, int pageSize, String type){
        Map<String,Object> param = new HashMap<>();
        param.put("playListType", 2);
        param.put("type", 1);
        param.put("columnId", "1".equals(type) ? "15127315":"15127272");
        param.put("startIndex", (pageNo-1) * pageSize);

        MiguResponse response = MiguRequestUtil.getResoponse("GET", "http://m.music.migu.cn/migu/remoting/playlist_bycolumnid_tag", param);
        checkError(response);

        JSONArray list = response.getBody().getJSONObject("retMsg").getJSONArray("playlist");
        List<PlayListDto> playList = new ArrayList<>();
        if(list == null || list.isEmpty()){
            return playList;
        }
        for (Object o : list) {
            JSONObject jo = (JSONObject)o;
            PlayListDto dto = getPlayListDto(jo);
            playList.add(dto);
        }
        return playList;
    }

    /**
     * 提取歌单信息
     * @param jo
     * @return
     */
    private PlayListDto getPlayListDto(JSONObject jo) {
        PlayListDto dto = new PlayListDto();
        dto.setMusicPlatform(MusicPlatform.Migu);
        dto.setId(jo.getString("playListId"));
        dto.setName(jo.getString("playListName"));
        dto.setPicUrl(jo.getString("image"));
        dto.setSummary(jo.getString("summary"));
        dto.setPlayCount(jo.getLong("playCount"));
        dto.setTrackCount(jo.getLong("contentCount"));
        return dto;
    }
}
