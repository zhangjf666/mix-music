package com.happycoding.music.service;

import com.alibaba.fastjson.JSON;
import com.happycoding.music.common.exception.BusinessException;
import com.happycoding.music.model.NeteaseOption;
import com.happycoding.music.model.NeteaseResponse;
import com.happycoding.music.util.NeteaseRequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2021/5/11 14:09
 */
@Service
public class NeteaseService {

    /**
     * 推荐歌单
     * @param limit
     * @return
     */
    public Object personalized(Integer limit){
        Map<String, Object> data = new HashMap<>();
        data.put("limit", limit != null ? limit : 30);
        data.put("total", true);
        data.put("n", 1000);

        NeteaseOption option = new NeteaseOption();
        option.setCrypto("weapi");

        NeteaseResponse response = NeteaseRequestUtil.getResponse("POST",
                "https://music.163.com/weapi/personalized/playlist", data, option);
        checkError(response);
        return response.getBody();
    }

    /**
     * 歌词
     * @param id
     * @return
     */
    public Object lyric(Long id){
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
        return response.getBody();
    }

    /**
     * 多类型搜索
     * @param keywords
     * @param type
     * @return
     */
    public Object searchMultiMatch(String keywords,String type){
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
    public Object searchSuggest(String keywords,String type){
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
    public Object searchHotDetail(){
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
    public Object cloudSearch(String keywords, String type, long limit, long offset, boolean total){
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
        return response.getBody();
    }

    /**
     * 歌曲url
     * @param ids
     * @return
     */
    public Object songUrl(Long[] ids){
        Map<String, Object> data = new HashMap<>();
        data.put("ids", ids);
        data.put("br", 999000);

        NeteaseOption option = new NeteaseOption();
        option.setCrypto("eapi");
        option.setUrl("/api/song/enhance/player/url");

        NeteaseResponse response = NeteaseRequestUtil.getResponse("POST",
                "https://interface3.music.163.com/eapi/song/enhance/player/url", data, option);
        checkError(response);
        return response.getBody();
    }

    /**
     * 歌曲详情
     * @param ids
     * @return
     */
    public Object songDetail(Long[] ids){
        List<Map<String,Long>> idsd = new ArrayList<>();
        for (Long id:ids) {
            Map<String, Long> song = new HashMap<>();
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
        return response.getBody();
    }

    /**
     * 检查歌曲是否可用
     * @param id
     * @return
     */
    public Object checkMusic(Long[] id){
        Map<String, Object> data = new HashMap<>();
        data.put("ids", id);
        data.put("br", 999000);

        NeteaseOption option = new NeteaseOption();
        option.setCrypto("weapi");

        NeteaseResponse response = NeteaseRequestUtil.getResponse("POST",
                "https://music.163.com/weapi/song/enhance/player/url", data, option);
        checkError(response);
        return response.getBody();
    }

    /**
     * 歌单详情
     * @param id
     * @return
     */
    public Object playListDetail(Long id){
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("n", 100000);
        data.put("s", 1);

        NeteaseOption option = new NeteaseOption();
        option.setCrypto("linuxapi");

        NeteaseResponse response = NeteaseRequestUtil.getResponse("POST",
                "https://music.163.com/api/v6/playlist/detail", data, option);
        checkError(response);
        return response.getBody();
    }

    /**
     * 精选歌单
     * @param cat
     * @param limit
     * @param before
     * @param total
     * @return
     */
    public Object topPlayListHighquality(String cat, int limit, int before, boolean total){
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
    public Object topPlayList(String cat, String order, int limit, int offset, boolean total){
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
    public Object topSong(int areaId, int limit, int offset, boolean total){
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

    private void checkError(NeteaseResponse response){
        if(response.getCode() != 200){
            throw new BusinessException(response.getCode(), response.getMessage());
        }
    }
}
