package com.github.wycm.proxy.site.xicidaili;

import com.github.wycm.common.Proxy;
import com.github.wycm.common.util.SimpleHttpClient;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by wycm on 2019-03-06.
 */
public class XicidailiProxyListPageParserTest {

    @Test
    public void parse() throws ExecutionException, InterruptedException {
        SimpleHttpClient simpleHttpClient = new SimpleHttpClient(null);
        String r = simpleHttpClient.getBodyStr("http://www.xicidaili.com/wt/1.html");
        XicidailiProxyListPageParser pageParser = new XicidailiProxyListPageParser();
        List<Proxy> proxyList =pageParser.parse(r);
        Assert.assertTrue(proxyList.size() > 1);
    }
}