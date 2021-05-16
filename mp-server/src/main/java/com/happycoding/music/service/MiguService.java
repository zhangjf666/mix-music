package com.happycoding.music.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.happycoding.music.common.exception.BusinessException;
import com.happycoding.music.dto.PlayListDto;
import com.happycoding.music.dto.SongInfoDto;
import com.happycoding.music.model.MiguOption;
import com.happycoding.music.model.MiguResponse;
import com.happycoding.music.model.MusicPlatform;
import com.happycoding.music.util.MiguRequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public JSONObject search(String keyword, int type, int rows, int pgc){
        Map<String, Object> param = new HashMap<>();
        param.put("keyword", keyword);
        param.put("rows", rows);
        param.put("type", type);
        param.put("pgc", pgc);

        MiguResponse response = MiguRequestUtil.getResoponse("GET",
                "https://m.music.migu.cn/migu/remoting/scr_search_tag", param);
        checkError(response);
        return response.getBody();
    }

    /**
     * 获取歌曲url
     * @param cid 歌曲cid
     * @param br 品质(LQ,HQ,SQ,ZQ)
     * @return
     */
    public JSONObject songUrl(String cid, String br){
        Map<String, Object> param = new HashMap<>();
        param.put("copyrightId", cid);
        param.put("resourceType", 2);

        MiguResponse response = MiguRequestUtil.getResoponse("GET",
                "https://c.musicapp.migu.cn/MIGUM2.0/v1.0/content/resourceinfo.do", param);
        checkError(response);

        JSONArray data = response.getBody().getJSONArray("resource");
        JSONObject res = data.getJSONObject(0);
        String url = "";
        JSONArray rateFormats = res.getJSONArray("newRateFormats");
        if(rateFormats == null || rateFormats.isEmpty()){
            rateFormats = res.getJSONArray("rateFormats");
        }
        url = findUrl(rateFormats, br);
        System.out.println(url);
        return response.getBody();
    }

    private String findUrl(JSONArray rateFormats, String br){
        String url = "";
        String insteadUrl = "https://freetyst.nf.migu.cn";
        String reg = "ftp://(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d):\\d+";
        for (Object rate: rateFormats) {
            JSONObject obj = (JSONObject)rate;
            String formatType = obj.getString("formatType");
            if(br.equals(formatType)){
                url = obj.getString("url").replaceAll(reg,insteadUrl);
            }
        }
        if(StringUtils.isBlank(url) && !"LQ".equals(br)){
            url = findUrl(rateFormats, getPreBr(br));
        }
        return url;
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
    public List<SongInfoDto> song(String cids){
        Map<String, Object> param = new HashMap<>();
        param.put("copyrightId", cids);
        param.put("type", 1);

        MiguResponse response = MiguRequestUtil.getResoponse("GET",
                "https://music.migu.cn/v3/api/music/audioPlayer/songs", param);
        checkError(response);

        JSONArray songs = response.getBody().getJSONArray("items");
        if(songs != null && !songs.isEmpty()){
            List<SongInfoDto> songInfos = new ArrayList<>();
            for (int i = 0; i <songs.size() ; i++) {
                JSONObject song = songs.getJSONObject(i);
                SongInfoDto info = new SongInfoDto();
                info.setId(song.getString("copyrightId"));
                DateTime length = DateUtil.parseTime(song.getString("length"));
                int duration = length.minute() * 60000 + length.second() * 1000 + length.millsecond();
                info.setDuration(duration);
                info.setMusicPlatform(MusicPlatform.Migu);
                info.setName(song.getString("songName"));

                JSONObject singer = song.getJSONArray("singers").getJSONObject(0);
                info.setSingerId(singer.getString("artistId"));
                info.setSingerName(singer.getString("artistName"));

                if(song.containsKey("albums") && song.getJSONArray("albums") != null && !song.getJSONArray("albums").isEmpty()){
                    JSONObject albums = song.getJSONArray("albums").getJSONObject(0);
                    info.setAlbumId(albums.getString("albumId"));
                    info.setAlbumName(albums.getString("albumName"));
                }
                songInfos.add(info);
            }
            return songInfos;
        }
        return null;
    }

    /**
     * 获取歌单中歌曲信息
     * @return
     */
    public JSONObject playListDetails(String id, int limit){
        Map<String, Object> param = new HashMap<>();
        param.put("playListType", 2);
        param.put("playListId", id);
        param.put("contentCount", limit);
        String url = "https://m.music.migu.cn/migu/remoting/playlistcontents_query_tag";

        return null;
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
    public JSONObject playList(String id){
        Map<String,Object> param = new HashMap<>();
        param.put("playListId", id);

        MiguResponse response = MiguRequestUtil.getResoponse("GET",
                "http://m.music.migu.cn/migu/remoting/query_playlist_by_id_tag?onLine=1&queryChannel=0&createUserId=migu&contentCountMin=5", param);
        checkError(response);

        String code = response.getBody().getJSONObject("rsp").getString("code");
        if("000000".equals(code)){
            JSONObject playList = response.getBody().getJSONObject("rsp").getJSONArray("playList").getJSONObject(0);
            int contentCount = playList.getInteger("contentCount");
            List<String> cids = songCidInPlayList(id, contentCount);
            String cidsStr = String.join(",", cids);
            List<SongInfoDto> songInfos = song(cidsStr);
            return response.getBody();
        }
        return response.getBody();
    }

    /**
     * 获取歌词
     * @return
     */
    public JSONObject lyric(String cid){
        Map<String,Object> param = new HashMap<>();
        param.put("copyrightId", cid);

        MiguResponse response = MiguRequestUtil.getResoponse("GET",
                "http://music.migu.cn/v3/api/music/audioPlayer/getLyric", param);
        checkError(response);
        return response.getBody();
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
            PlayListDto dto = new PlayListDto();
            dto.setMusicPlatform(MusicPlatform.Migu);
            dto.setId(jo.getString("playListId"));
            dto.setName(jo.getString("playListName"));
            dto.setPicUrl(jo.getString("image"));
            dto.setSummary(jo.getString("summary"));
            dto.setPlayCount(jo.getLong("playCount"));
            dto.setTrackCount(jo.getLong("contentCount"));
            playList.add(dto);
        }
        return playList;
    }
}
