package com.happycoding.music.util;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2021/5/10 15:28
 */
public class NeteaseRequestUtil {

    public static String testRequest(String id){
        Map data = new HashMap();
//        data.put("s", "吻别");
//        data.put("type", "1");
//        data.put("limit", 30);
//        data.put("offset", 0);
        data.put("csrf_token", "");

        Map edata = NeteaseCryptoUtil.encrypt(JSONUtil.toJsonStr(data));

        String res =
                HttpRequest.post("https://music.163.com/weapi/hotsearchlist/get")
                        .header(Header.USER_AGENT,
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.30" +
                        " Safari/537.36")
                        .header(Header.REFERER,"https://music.163.com")
                        .header(Header.CONTENT_TYPE,"application/x-www-form-urlencoded")
                        .form(edata)
                        .execute()
                        .body();
        return res;
    }
}
