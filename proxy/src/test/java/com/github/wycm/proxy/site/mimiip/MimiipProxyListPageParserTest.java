package com.github.wycm.proxy.site.mimiip;

import com.github.wycm.common.Proxy;
import com.github.wycm.common.util.SimpleHttpClient;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by wycm on 2019-03-06.
 */
public class MimiipProxyListPageParserTest {

    @Test
    public void parse() throws ExecutionException, InterruptedException {
        SimpleHttpClient simpleHttpClient = new SimpleHttpClient();
        String r = simpleHttpClient.getBodyStr("http://www.mimiip.com/gngao/1");
        MimiipProxyListPageParser pageParser = new MimiipProxyListPageParser();
        List<Proxy> proxyList =pageParser.parse(r);
//        Assert.assertTrue(proxyList.size() > 1);
    }
}