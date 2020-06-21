package com.github.wycm.common.util;

import io.netty.handler.codec.http.HttpStatusClass;
import org.asynchttpclient.Response;
import org.asynchttpclient.proxy.ProxyServer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SimpleHttpClientTest {

    SimpleHttpClient simpleHttpClient;

    @Before
    public void before(){
        simpleHttpClient = new SimpleHttpClient(new SimpleHttpClient.IgnoreCookieStore(), 500, 2000);
    }

    @Test
    public void get() {
    }

    @Test
    public void testGet() {
    }

    @Test
    public void execute() {
    }

    @Test
    public void getResponse() {
    }

    @Test
    public void testGetResponse() {
    }

    @Test
    public void testGetResponse1() {
    }

    @Test
    public void testGetResponse2() {
        Map<String, List<String>> paramMap = new HashMap<>();
        paramMap.put("searchRange", Arrays.asList("1"));
        paramMap.put("searchMethod", Arrays.asList("4"));
        paramMap.put("groupId", Arrays.asList("33"));
        paramMap.put("pageSize", Arrays.asList("50"));
        paramMap.put("dicIds", Arrays.asList("73,74"));
        paramMap.put("currentPage", Arrays.asList("1"));
        paramMap.put("keyword", Arrays.asList("a"));
        String url = "http://hkuri.cneas.tohoku.ac.jp/project1/kdic/listAction";
        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36";
        Map<String, String> headers = new HashMap<>();

        ProxyServer proxyServer = new ProxyServer.Builder("183.195.106.118", 8118).build();
        try {
            Response response = simpleHttpClient.execPostRequest(url, paramMap, proxyServer, userAgent, headers);
            Assert.assertNotNull(response);
            Assert.assertEquals(HttpStatusClass.valueOf(response.getStatusCode()), HttpStatusClass.SUCCESS);
            System.out.println(response.getResponseBody());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void executeRequest() {
    }

    @Test
    public void downloadFile() {
    }

    @Test
    public void testDownloadFile() {
    }
}