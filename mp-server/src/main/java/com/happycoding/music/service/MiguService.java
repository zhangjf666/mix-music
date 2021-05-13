package com.happycoding.music.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.happycoding.music.common.exception.BusinessException;
import com.happycoding.music.model.MiguOption;
import com.happycoding.music.model.MiguResponse;
import com.happycoding.music.util.MiguRequestUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
    public Object search(String keyword, int type, int rows, int pgc){
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
     * @return
     */
    public Object songUrl(String cid){
        Map<String, Object> param = new HashMap<>();
        param.put("copyrightId", cid);
        param.put("resourceType", 2);

        MiguResponse response = MiguRequestUtil.getResoponse("GET",
                "https://c.musicapp.migu.cn/MIGUM2.0/v1.0/content/resourceinfo.do", param);
        checkError(response);

        String insteadUrl = "https://freetyst.nf.migu.cn";
        String reg = "ftp://(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d):\\d+";
        JSONArray data = response.getBody().getJSONArray("resource");
        JSONObject res = data.getJSONObject(0);
        String url = "";
        JSONArray rateFormats = res.getJSONArray("rateFormats");
        for (Object rate: rateFormats) {
            JSONObject obj = (JSONObject)rate;
            String formatType = obj.getString("formatType");
            if("HQ".equals(formatType)){
                url = obj.getString("url").replaceAll(reg,insteadUrl);
            }
        }
        System.out.println(url);
        return response.getBody();
    }

    /**
     * 获取歌曲信息
     * @return
     */
    public Object song(String cids){
        Map<String, Object> param = new HashMap<>();
        param.put("copyrightId", cids);
        param.put("type", 1);

        MiguResponse response = MiguRequestUtil.getResoponse("GET",
                "https://music.migu.cn/v3/api/music/audioPlayer/songs", param);
        checkError(response);
        return response.getBody();
    }

    /**
     * 获取歌单中歌曲信息
     * @return
     */
    public Object playListSongs(String id, int limit){
        Map<String, Object> param = new HashMap<>();
        param.put("playListType", 2);
        param.put("playListId", id);
        param.put("contentCount", limit);
        String url = "https://m.music.migu.cn/migu/remoting/playlistcontents_query_tag";

        MiguResponse response = MiguRequestUtil.getResoponse("GET",
                url, param);
        checkError(response);
        return response.getBody();
    }

    /**
     * 获取歌单信息
     * @return
     */
    public Object playList(String id){
        Map<String,Object> param = new HashMap<>();
        param.put("playListId", id);

        MiguResponse response = MiguRequestUtil.getResoponse("GET",
                "http://m.music.migu.cn/migu/remoting/query_playlist_by_id_tag?onLine=1&queryChannel=0&createUserId=migu&contentCountMin=5", param);
        checkError(response);
        return response.getBody();
    }

    /**
     * 获取歌词
     * @return
     */
    public Object lyric(String cid){
        Map<String,Object> param = new HashMap<>();
        param.put("copyrightId", cid);

        MiguResponse response = MiguRequestUtil.getResoponse("GET",
                "http://music.migu.cn/v3/api/music/audioPlayer/getLyric", param);
        checkError(response);
        return response.getBody();
    }

    /**
     * 推荐歌单
     * @return
     */
    public Object personalized(int pageNo, int pageSize, String type){
        Map<String,Object> param = new HashMap<>();
        param.put("playListType", 2);
        param.put("type", 1);
        param.put("columnId", "1".equals(type) ? "15127315":"15127272");
        param.put("startIndex", (pageNo-1) * pageSize);

        MiguResponse response = MiguRequestUtil.getResoponse("GET", "http://m.music.migu.cn/migu/remoting/playlist_bycolumnid_tag", param);
        checkError(response);
        return response.getBody();
    }
}
