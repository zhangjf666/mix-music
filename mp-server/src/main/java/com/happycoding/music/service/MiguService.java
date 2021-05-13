package com.happycoding.music.service;

import cn.hutool.core.util.StrUtil;
import com.happycoding.music.common.exception.BusinessException;
import com.happycoding.music.model.MiguOption;
import com.happycoding.music.model.MiguResponse;
import com.happycoding.music.util.MiguRequestUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;

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
     * 推荐歌单
     * @return
     */
    public Object personalized(int pageNo, int pageSize, String type){
        type = "1".equals(type) ? "15127315":"15127272";
        int startIndex = (pageNo-1) * pageSize;
        String url = StrUtil.format("http://m.music.migu.cn/migu/remoting/playlist_bycolumnid_tag?playListType=2&type" +
                "=1&columnId={}&tagId=&startIndex={}",type, startIndex);

        MiguResponse response = MiguRequestUtil.getResoponse("GET",
                url, new HashMap(), new MiguOption());
        checkError(response);
        return response.getBody();
    }
}
