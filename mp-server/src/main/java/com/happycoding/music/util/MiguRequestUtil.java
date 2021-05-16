package com.happycoding.music.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.happycoding.music.common.model.ResponseCode;
import com.happycoding.music.model.MiguOption;
import com.happycoding.music.model.MiguResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2021/5/13 13:45
 */
@Slf4j
public class MiguRequestUtil {

    private static String[] userAgentList = {
            // iOS 13.5.1 14.0 beta with safari
            "Mozilla/5.0 (iPhone; CPU iPhone OS 13_5_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.1.1 Mobile/15E148 Safari/604.1",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 14_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Mobile/15E148 Safari/604.",
            // iOS with qq micromsg
            "Mozilla/5.0 (iPhone; CPU iPhone OS 13_5_1 like Mac OS X) AppleWebKit/602.1.50 (KHTML like Gecko) Mobile/14A456 QQ/6.5.7.408 V1_IPH_SQ_6.5.7_1_APP_A Pixel/750 Core/UIWebView NetType/4G Mem/103",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 13_5_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/7.0.15(0x17000f27) NetType/WIFI Language/zh",
            // Android -> Huawei Xiaomi
            "Mozilla/5.0 (Linux; Android 9; PCT-AL10) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.64 HuaweiBrowser/10.0.3.311 Mobile Safari/537.36",
            "Mozilla/5.0 (Linux; U; Android 9; zh-cn; Redmi Note 8 Build/PKQ1.190616.001) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/71.0.3578.141 Mobile Safari/537.36 XiaoMi/MiuiBrowser/12.5.22",
            // Android + qq micromsg
            "Mozilla/5.0 (Linux; Android 10; YAL-AL00 Build/HUAWEIYAL-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/78.0.3904.62 XWEB/2581 MMWEBSDK/200801 Mobile Safari/537.36 MMWEBID/3027 MicroMessenger/7.0.18.1740(0x27001235) Process/toolsmp WeChat/arm64 NetType/WIFI Language/zh_CN ABI/arm64",
            "Mozilla/5.0 (Linux; U; Android 8.1.0; zh-cn; BKK-AL10 Build/HONORBKK-AL10) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/10.6 Mobile Safari/537.36",
            // macOS 10.15.6  Firefox / Chrome / Safari
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:80.0) Gecko/20100101 Firefox/80.0",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.30 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.1.2 Safari/605.1.15",
            // Windows 10 Firefox / Chrome / Edge
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:80.0) Gecko/20100101 Firefox/80.0",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.30 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/13.10586"
            // Linux 就算了
    };

    public static MiguResponse getResoponse(String method, String url, Map data){
        return getResoponse(method, url, data, false);
    }

    public static MiguResponse getResoponse(String method, String url, Map data, boolean isRaw){
        Map<String, String> header = new HashMap<>();
        Map<String, Object> param = data;
        MiguResponse response = new MiguResponse();
        try {
            header.put("User-Agent", userAgentList[RandomUtil.randomInt(userAgentList.length)]);
            if(!header.containsKey("Referer")){
                header.put("Referer", "https://m.music.migu.cn/");
            }

            String finalUrl = handleUrlParameters(url, param);
            ResponseEntity<String> responseEntity = null;
            responseEntity = RestTemplateUtils.get(finalUrl, header, String.class, param);

            String body = responseEntity.getBody();
//            body = body.replaceAll("/callback\\(|MusicJsonCallback\\(|jsonCallback\\(|\\)$/g", "");
            JSONObject bodyJson = null;
            if(isRaw){
                bodyJson = new JSONObject();
                bodyJson.put("code", 200);
                bodyJson.put("html", body);
            } else {
                bodyJson = JSON.parseObject(body);
            }

            response.setCode(200);
            response.setBody(bodyJson);
            return response;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response.setCode(ResponseCode.INTERNAL_SERVER_ERROR.getCode());
            response.setMessage(e.getMessage());
            return response;
        }
    }

    /**
     * 拼接get参数
     *
     * @param baseUrl      baseUrl
     * @param parameterMap parameter map
     **/
    private static String handleUrlParameters(String baseUrl, Map<String, Object> parameterMap) {
        Preconditions.checkArgument(StringUtils.hasText(baseUrl), "Error: url不能为空");
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        if (CollectionUtil.isNotEmpty(parameterMap)) {
            parameterMap.forEach((key, value) -> {
                if(Objects.nonNull(value)){
                    params.put(key, Collections.singletonList(String.valueOf(value)));
                }
            });
        }
        return builder.queryParams(params).build().encode().toUriString();
    }
}
