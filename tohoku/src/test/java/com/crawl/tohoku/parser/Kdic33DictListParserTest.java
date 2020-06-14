package com.crawl.tohoku.parser;

import com.crawl.BaseTest;
import com.crawl.tohoku.TohokuHttpClient;
import com.crawl.tohoku.service.TohokuComponent;
import com.github.wycm.common.Page;
import com.github.wycm.common.parser.ListPageParser;
import com.github.wycm.common.util.SimpleHttpClient;
import org.apache.http.HttpStatus;
import org.asynchttpclient.Response;
import org.asynchttpclient.proxy.ProxyServer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class Kdic33DictListParserTest extends BaseTest {

    @Autowired
    private TohokuComponent tohokuComponent;

    @Test
    @Rollback
    public void parseListPage() {
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
            Page page = tohokuComponent.getTohokuHttpClient().asyncPost(url, paramMap, proxyServer, userAgent, headers);
            Assert.assertNotNull(page);
            Assert.assertEquals(HttpStatus.SC_OK, page.getStatusCode());
            tohokuComponent.getKdic33DictListParser().parseListPage(page);
            System.out.println(page.getHtml());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}