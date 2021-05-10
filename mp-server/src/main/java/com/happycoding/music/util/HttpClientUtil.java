package com.happycoding.music.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2019/8/9 14:48
 */
@Slf4j
public class HttpClientUtil {
    private static HttpClientBuilder builder;
    private static String EMPTY_STR = "";
    private static String UTF_8 = "UTF-8";

    /**
     * 通过连接池获取HttpClient
     *
     * @return
     */
    private static CloseableHttpClient getHttpClient() {
        if (builder == null) {
            synchronized (HttpClientUtil.class){
                if (builder == null) {
                    PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
                    // 整个连接池最大连接数
                    cm.setMaxTotal(50);
                    // 每路由最大连接数，默认值是2
                    cm.setDefaultMaxPerRoute(10);
                    builder = HttpClients.custom().setConnectionManager(cm);
                }
            }
        }
        return builder.build();
    }

    /**
     * @param url
     * @return
     */
    public static String httpGetRequest(String url)throws URISyntaxException {
        return httpGetRequest(url,null,null);
    }

    public static String httpGetRequest(String url, Map<String, Object> params) throws URISyntaxException {
        return httpGetRequest(url,null,params);
    }

    public static String httpGetRequest(String url, Map<String, Object> headers, Map<String, Object> params)
            throws URISyntaxException {
        URIBuilder ub = new URIBuilder();
        ub.setPath(url);
        if(params != null && !params.isEmpty()){
            ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
            ub.setParameters(pairs);
        }
        HttpGet httpGet = new HttpGet(ub.build());
        if(headers != null && !headers.isEmpty()){
            for (Map.Entry<String, Object> header : headers.entrySet()) {
                httpGet.addHeader(header.getKey(), String.valueOf(header.getValue()));
            }
        }
        return getResult(httpGet);
    }

    public static String httpPostRequest(String url, String json) {
        return httpPostRequest(url,null, json);
    }

    public static String httpPostRequest(String url, Map<String, Object> headers,String json) {
        HttpPost httpPost = new HttpPost(url);
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, Object> param : headers.entrySet()) {
                httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
            }
        }
        StringEntity entity = new StringEntity(json,"utf-8");//解决中文乱码问题
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        return getResult(httpPost);
    }

    public static String httpPostRequest(String url, Map<String, Object> params) throws UnsupportedEncodingException {
        return httpPostRequest(url,null, params);
    }

    public static String httpPostRequest(String url, Map<String, Object> headers, Map<String, Object> params)
            throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);
        if(params != null && !params.isEmpty()){
            ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
            httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));
        }
        if(headers != null && !headers.isEmpty()){
            for (Map.Entry<String, Object> param : headers.entrySet()) {
                httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
            }
        }
        return getResult(httpPost);
    }

    private static ArrayList<NameValuePair> covertParams2NVPS(Map<String, Object> params) {
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            pairs.add(new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue())));
        }
        return pairs;
    }

    /**
     * 处理Http请求
     */
    private static String getResult(HttpRequestBase request) {
        CloseableHttpClient httpClient = getHttpClient();
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            request.releaseConnection();
            try {
                if (null != response) {
                    response.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        return EMPTY_STR;
    }

    public static void downLoadFile(String url, Map<String, Object> headers, String filePath, String fileName) {
        CloseableHttpClient httpClient = getHttpClient();
        OutputStream out = null;
        InputStream in = null;

        try {
            HttpGet httpGet = new HttpGet(url);
            if(headers != null && !headers.isEmpty()){
                for (Map.Entry<String, Object> param : headers.entrySet()) {
                    httpGet.addHeader(param.getKey(), String.valueOf(param.getValue()));
                }
            }
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            in = entity.getContent();

            long length = entity.getContentLength();
            if (length <= 0) {
                log.error("下载文件不存在!");
                return;
            }

            File file = new File(filePath);
            if(!file.exists()){
                file.mkdirs();
            }

            String realfile = "";
            if(!filePath.endsWith(File.separator)){
                realfile = filePath.concat(File.separator).concat(fileName);
            }else{
                realfile = filePath.concat(fileName);
            }
            File rfile = new File(realfile);
            if(!rfile.isFile()){
                rfile.createNewFile();
            }
            out = new FileOutputStream(rfile);
            byte[] buffer = new byte[4096];
            int readLength = 0;
            while ((readLength=in.read(buffer)) > 0) {
                byte[] bytes = new byte[readLength];
                System.arraycopy(buffer, 0, bytes, 0, readLength);
                out.write(bytes);
            }
            out.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        }finally{
            try {
                if(in != null){
                    in.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }

            try {
                if(out != null){
                    out.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }
}
