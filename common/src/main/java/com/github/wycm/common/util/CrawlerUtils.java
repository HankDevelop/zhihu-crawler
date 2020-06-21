package com.github.wycm.common.util;

import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.cookie.CookieStore;
import org.asynchttpclient.cookie.ThreadSafeCookieStore;
import org.asynchttpclient.uri.Uri;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Created by wycm on 2018-12-29.
 */
@Slf4j
public class CrawlerUtils {

    public static String getLockKeyPrefix(Class<? extends Runnable> clazz) {
        return clazz.getSimpleName() + "LockKey:";
    }

    public static String getTaskQueueName(Class<? extends Runnable> clazz) {
        return clazz.getSimpleName() + "Queue";
    }

    public static CookieStore parseCookies(String originalCookies, String domain) {
        CookieStore cookieStore = new ThreadSafeCookieStore();
        //init 66ip cookie
        Stream.of(originalCookies.split("; ")).forEach(i -> {
            String name = i.split("=")[0];
            String value = i.split("=")[1];
            try {
                value = URLEncoder.encode(value, "utf-8");
            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage(), e);
            }
            Cookie cookie = new DefaultCookie(name, value);
            cookieStore.add(Uri.create(domain), cookie);
        });
        return cookieStore;
    }

    /**
     * 获取URL中的参数名和参数值的Map集合
     *
     * @param url
     * @return
     */
    public static Map<String, String> getUrlPramNameAndValue(String url) {
        String regEx = "(\\?|&+)(.+?)=([^&]*)";//匹配参数名和参数值的正则表达式
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(url);
        // LinkedHashMap是有序的Map集合，遍历时会按照加入的顺序遍历输出
        Map<String, String> paramMap = new LinkedHashMap<String, String>();
        while (m.find()) {
            String paramName = m.group(2);//获取参数名
            String paramVal = m.group(3);//获取参数值
            paramMap.put(paramName, paramVal);
        }
        return paramMap;
    }
}
