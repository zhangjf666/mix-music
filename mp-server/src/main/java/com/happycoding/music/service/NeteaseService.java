package com.happycoding.music.service;

import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONUtil;
import com.happycoding.music.common.exception.BusinessException;
import com.happycoding.music.model.NeteaseOption;
import com.happycoding.music.model.NeteaseResponse;
import com.happycoding.music.util.NeteaseRequestUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2021/5/11 14:09
 */
@Service
public class NeteaseService {

    public Object getTopSong(int areaId, int limit, int offset, boolean total){
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

    public Object getHotSearchList(){
        Map<String, Object> data = new HashMap<>();
        NeteaseOption option = new NeteaseOption();
        option.setCrypto("weapi");

        NeteaseResponse response = NeteaseRequestUtil.getResponse("POST",
                "https://music.163.com/weapi/hotsearchlist/get", data, option);
        checkError(response);
        return response.getBody();
    }

    private void checkError(NeteaseResponse response){
        if(response.getCode() != 200){
            throw new BusinessException(response.getCode(), response.getMessage());
        }
    }
}
