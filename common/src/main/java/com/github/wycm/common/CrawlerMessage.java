package com.github.wycm.common;

import lombok.Data;
import lombok.ToString;
import sun.plugin2.os.windows.Windows;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wycm on 2018/10/24.
 */
@Data
@ToString
public class CrawlerMessage {
    private String url;
    private int currentRetryTimes = 0;
    private String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:82.0) Gecko/20100101 Firefox/82.0";

    private Map<String, String> headers = new HashMap<>();
    /**
     * 消息上下文
     */
    private Map<String, List<String>> messageContext = new HashMap<>();
    private String sendTime;

    public CrawlerMessage(){

    }

    public CrawlerMessage(String url) {
        this.url = url;
    }

    public CrawlerMessage(String url, Map<String, List<String>> messageContext) {
        this.url = url;
        this.messageContext = messageContext;
    }

    public CrawlerMessage(String url, int currentRetryTimes) {
        this.url = url;
        this.currentRetryTimes = currentRetryTimes;
    }

    public CrawlerMessage(String url, String userAgent) {
        this.url = url;
        this.userAgent = userAgent;
    }

    public CrawlerMessage(String url, int currentRetryTimes, String userAgent) {
        this.url = url;
        this.currentRetryTimes = currentRetryTimes;
        this.userAgent = userAgent;
    }

}
