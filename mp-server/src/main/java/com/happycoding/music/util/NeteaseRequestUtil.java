package com.happycoding.music.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.happycoding.music.model.NeteaseOption;
import com.happycoding.music.model.NeteaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2021/5/10 15:28
 */
@Slf4j
public class NeteaseRequestUtil {

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

    public static NeteaseResponse getResponse(String method, String url, Map data, NeteaseOption options){
        String finalUrl = url;
        Map<String, Object> header = new HashMap<>();
        Map<String, Object> param = new HashMap<>();
        header.put("User-Agent", userAgentList[RandomUtil.randomInt(userAgentList.length)]);
        if("POST".equals(method.toUpperCase())){
            header.put("Content-Type", "application/x-www-form-urlencoded");
        }
        if(url.contains("music.163.com")){
            header.put("Referer", "https://music.163.com");
        }
        if(options.getCookie() != null && !options.getCookie().isEmpty()){
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String,String> entry: options.getCookie().entrySet()) {
                sb.append(entry.getKey());
                sb.append("=");
                sb.append(entry.getValue());
                sb.append(";");
            }
            header.put("Cookie", sb.substring(0, sb.length() - 1));
        }
        if(!header.containsKey("Cookie")){
            header.put("Cookie", options.getToken());
        }
        if(NeteaseCryptoUtil.WEAPI_TYPE.equals(options.getCrypto())){
            List<String> csrfTokens = ReUtil.findAll("_csrf=([^(;|$)]+)", (String) header.get("Cookie"),0);
            data.put("csrf_token", csrfTokens.isEmpty() ? "" : csrfTokens.get(1));
            param = NeteaseCryptoUtil.weapiEncrypt(data);
            finalUrl = finalUrl.replaceAll("\\w*api","weapi");
        } else if(NeteaseCryptoUtil.LINUXAPI_TYPE.equals(options.getCrypto())){
            finalUrl = finalUrl.replaceAll("\\w*api","api");
            Map newdata = new HashMap();
            newdata.put("method", method);
            newdata.put("url", finalUrl);
            newdata.put("params", data);
            param = NeteaseCryptoUtil.linuxapi(newdata);
            header.put("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) " +
                    "Chrome/60.0.3112.90 Safari/537.36");
            finalUrl = "https://music.163.com/api/linux/forward";
        } else if(NeteaseCryptoUtil.EAPI_TYPE.equals(options.getCrypto())){
            Map<String,String> cookie = options.getCookie();
            Map<String, Object> eHeader = new HashMap<>();
            header.put("osver", cookie.get("osver"));
            header.put("deviceId", cookie.get("deviceId"));
            header.put("appver", cookie.getOrDefault("appver", "8.0.0"));
            header.put("versioncode", cookie.getOrDefault("versioncode", "140"));
            header.put("mobilename", cookie.get("mobilename"));
            header.put("buildver", cookie.getOrDefault("buildver", DateUtil.today()));
            header.put("resolution", cookie.getOrDefault("resolution", "1920x1080"));
            header.put("__csrf", cookie.get("__csrf"));
            header.put("os", cookie.getOrDefault("os", "android"));
            header.put("channel", cookie.get("channel"));
            header.put("requestId", StrUtil.format("{}_{}",DateUtil.now(),
                    StrUtil.padPre(String.valueOf(RandomUtil.randomInt(1000)), 4, '0')));
            if(cookie.containsKey("MUSIC_U")){
                header.put("MUSIC_U", cookie.get("MUSIC_U"));
            }
            if(cookie.containsKey("MUSIC_A")){
                header.put("MUSIC_A", cookie.get("MUSIC_A"));
            }
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String,String> entry: options.getCookie().entrySet()) {
                sb.append(entry.getKey());
                sb.append("=");
                sb.append(entry.getValue());
                sb.append(";");
            }
            header.put("Cookie", sb.substring(0, sb.length() - 1));
            data.put("header", header);
            param = NeteaseCryptoUtil.eapi(options.getUrl(), data);
            finalUrl = finalUrl.replaceAll("\\w*api","eapi");
        }

        NeteaseResponse response = new NeteaseResponse();
        try {
            ResponseEntity responseEntity = null;
            HttpResponse httpResponse = null;
            if("POST".equals(method.toUpperCase())){
                httpResponse = HttpClientUtil.httpPostRequestResponse(finalUrl, header, param);
            } else {
                httpResponse = HttpClientUtil.httpGetRequestResponse(finalUrl, header, param);
            }
            if(httpResponse.getEntity() == null){
                throw new Exception("response is null");
            }
            String body = EntityUtils.toString(httpResponse.getEntity());
            Header[] headers = httpResponse.getHeaders("set-cookie");
            String[] cookie = new String[headers.length];
            for (int i = 0; i < headers.length; i++) {
                cookie[i] = headers[i].getValue().replaceAll("\\s*Domain=[^(;|$)]+;*", "");
            }
            response.setCookie(cookie);

            JSONObject bodyJson = null;
            if(NeteaseCryptoUtil.EAPI_TYPE.equals(options.getCrypto())){
                bodyJson = JSONUtil.parseObj(NeteaseCryptoUtil.eapiDecrypt(body));
            } else {
                bodyJson = JSONUtil.parseObj(body);
            }
            response.setBody(bodyJson);

            response.setCode(bodyJson.containsKey("code")? bodyJson.getInt("code") :
                    httpResponse.getStatusLine().getStatusCode());
            if("201,302,400,800,801,802,803".contains(bodyJson.getStr("code"))){
                response.setCode(200);
            }

            response.setCode(100 < response.getCode() && response.getCode() < 600 ? response.getCode() : 400);
            return response;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response.setCode(502);
            response.setMsg(e.getMessage());
            return response;
        }
    }
}
