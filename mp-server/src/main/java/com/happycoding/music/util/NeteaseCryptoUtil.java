package com.happycoding.music.util;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2021/5/10 13:47
 */
@Slf4j
public class NeteaseCryptoUtil {
    public final static String WEAPI_TYPE = "weapi";
    public final static String EAPI_TYPE = "eapi";
    public final static String LINUXAPI_TYPE = "linuxapi";

    private final static String PUBLIC_KEY = "00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7" +
            "b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280" +
            "104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932" +
            "575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b" +
            "3ece0462db0a22b8e7";

    private final static String PRESET_KEY = "0CoJUm6Qyw8W8jud";

    private final static String PUB_KEY = "010001";

    private final static String IV_STR = "0102030405060708";

    private static final String PARAMS = "params";

    private static final String ENC_SEC_KEY = "encSecKey";

    private static final String LINUX_API_KEY = "rFgB&h#%2?^eDg:Q";

    private static final String EAPI_KEY = "e82ckenh8dichen8";

    private static final String RANDOM_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static Map weapiEncrypt(Map data) {
        String text = JSONUtil.toJsonStr(data);
        String secKey = RandomStringUtils.random(16, RANDOM_CHARS);
        String encText = aesEncrypt(aesEncrypt(text, PRESET_KEY), secKey);
        String encSecKey = rsaEncrypt(secKey, PUB_KEY, PUBLIC_KEY);
        Map map = new HashMap();
        map.put(PARAMS, encText);
        map.put(ENC_SEC_KEY, encSecKey);
        return map;
    }

    public static Map linuxapi(Map data) {
        try {
            String text = JSONUtil.toJsonStr(data);
            SecretKeySpec skeySpec = new SecretKeySpec(LINUX_API_KEY.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] out = cipher.doFinal(text.getBytes());
            String encText = HexUtil.encodeHexStr(out, false);
            Map map = new HashMap();
            map.put("eparams", encText);
            return map;
        } catch (Exception e) {
            log.error("linuxapi参数加密出错:{}",e.getMessage());
            return new HashMap();
        }
    }

    public static Map eapi(String url, Map data) {
        try {
            String text = JSONUtil.toJsonStr(data);
            String message = StrUtil.format("nobody{}use{}md5forencrypt", url, text);
            message = MD5.create().digestHex(message);
            message = StrUtil.format("{}-36cd479b6b5-{}-36cd479b6b5-{}",url, text, message);
            SecretKeySpec skeySpec = new SecretKeySpec(EAPI_KEY.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] out = cipher.doFinal(message.getBytes());
            String encText = HexUtil.encodeHexStr(out, false);
            Map map = new HashMap();
            map.put(PARAMS, encText);
            return map;
        } catch (Exception e) {
            log.error("eapi参数加密出错:{}",e.getMessage());
            return new HashMap();
        }
    }

    public static String eapiDecrypt(String body){
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(EAPI_KEY.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] out = cipher.doFinal(body.getBytes());
            return new String(out);
        } catch (Exception e) {
            log.error("eapi解密出错:{}",e.getMessage());
            return "";
        }
    }

    private static String aesEncrypt(String text, String key) {
        try {
            IvParameterSpec iv = new IvParameterSpec(IV_STR.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(text.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            log.error("weapi参数加密出错:{}",e.getMessage());
            return "";
        }
    }

    private static String rsaEncrypt(String text, String pubKey, String modulus) {
        text = StrUtil.reverse(text);
        BigInteger rs = new BigInteger(String.format("%x", new BigInteger(1, text.getBytes())), 16)
                .modPow(new BigInteger(pubKey, 16), new BigInteger(modulus, 16));
        String r = rs.toString(16);
        if (r.length() >= 256) {
            return r.substring(r.length() - 256, r.length());
        } else {
            while (r.length() < 256) {
                r = 0 + r;
            }
            return r;
        }
    }
}

