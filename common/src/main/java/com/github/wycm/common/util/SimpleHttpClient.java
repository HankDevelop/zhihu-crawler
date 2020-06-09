package com.github.wycm.common.util;

import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.*;
import org.asynchttpclient.cookie.CookieStore;
import org.asynchttpclient.cookie.ThreadSafeCookieStore;
import org.asynchttpclient.proxy.ProxyServer;
import org.asynchttpclient.uri.Uri;
import org.asynchttpclient.util.HttpConstants;
import redis.clients.jedis.JedisPool;

import javax.net.ssl.SSLException;
import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import java.util.function.Predicate;

/**
 * Created by wycm on 2018/10/28.
 */
@Slf4j
public class SimpleHttpClient {
    private AsyncHttpClient asyncHttpClient;
    /**
     * 限制同一网站并发请求数
     * k:domain
     */
    public final static Map<String, Semaphore> semaphoreMap = new ConcurrentHashMap<>();

    private final static int MAX_CONCURRENT_COUNT = 3;

    protected JedisPool jedisPool = null;

    private int timeout = 10000;

    public SimpleHttpClient(){
        this(null);
    }

    public SimpleHttpClient(JedisPool jedisPool){
        this(500, 2000, jedisPool);
    }

    public SimpleHttpClient(int maxConnectionsPerHost, int maxConnections, JedisPool jedisPool){
        this(new ThreadSafeCookieStore(), maxConnectionsPerHost, maxConnections, jedisPool);
    }
    public SimpleHttpClient(CookieStore cookieStore, int maxConnectionsPerHost, int maxConnections, JedisPool jedisPool){
        SslContext sslCtx = null;
        try {
            sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        } catch (SSLException e) {
            e.printStackTrace();
        }
        DefaultAsyncHttpClientConfig.Builder builder = Dsl.config()
                .setConnectTimeout(timeout)
                .setHandshakeTimeout(timeout)
                .setReadTimeout(timeout)
                .setRequestTimeout(timeout)
                .setShutdownTimeout(timeout)
                .setSslSessionTimeout(timeout)
                .setPooledConnectionIdleTimeout(timeout)
                .setMaxConnectionsPerHost(maxConnectionsPerHost)
                .setMaxConnections(maxConnections)
                .setSslContext(sslCtx)
                .setMaxRedirects(3)
                .setFollowRedirect(true);
        if (cookieStore != null){
            builder.setCookieStore(cookieStore);
        }
        asyncHttpClient = new DefaultAsyncHttpClient(builder.build());
        this.jedisPool = jedisPool;
    }
    public String get(String url) throws ExecutionException, InterruptedException {
        Request request = new RequestBuilder()
                .setUrl(url)
                .setHeader("user-agent", Constants.userAgentArray[new Random().nextInt(Constants.userAgentArray.length)])
                .build();
        return execute(request);
    }

    public String get(String url, ProxyServer proxyServer) throws ExecutionException, InterruptedException {
        Request request = new RequestBuilder()
                .setUrl(url)
                .setProxyServer(proxyServer)
                .setHeader("user-agent", Constants.userAgentArray[new Random().nextInt(Constants.userAgentArray.length)])
                .build();
        return execute(request);
    }

    public String execute(Request request) throws ExecutionException, InterruptedException {
        return executeRequest(request).getResponseBody();
    }

    public Response getResponse(String url, ProxyServer proxyServer) throws ExecutionException, InterruptedException {
//        proxyServer = new ProxyServer.Builder("127.0.0.1", 8888).build();
        Request request = new RequestBuilder()
                .setUrl(url)
                .setProxyServer(proxyServer)
                .setHeader("user-agent", Constants.userAgentArray[new Random().nextInt(Constants.userAgentArray.length)])
                .build();
        return executeRequest(request);
    }
    public Response getResponse(String url, ProxyServer proxyServer, String userAgent) throws ExecutionException, InterruptedException {
//        proxyServer = new ProxyServer.Builder("127.0.0.1", 8888).build();
        RequestBuilder builder = new RequestBuilder();
        builder.resetCookies();
        Request request = builder
                .setUrl(url)
                .setProxyServer(proxyServer)
                .setHeader("user-agent", userAgent)
                .build();
        return executeRequest(request);
    }
    public Response getResponse(String url, ProxyServer proxyServer, String userAgent, Map<String, String> headers) throws ExecutionException, InterruptedException {
//        proxyServer = new ProxyServer.Builder("127.0.0.1", 8888).build();
        RequestBuilder builder = new RequestBuilder();
        builder.resetCookies();
        builder.setUrl(url)
                .setProxyServer(proxyServer)
                .setHeader("user-agent", userAgent);
        headers.forEach(builder::setHeader);
        return executeRequest(builder.build());
    }

    public Response getResponse(String url, Map<String, List<String>> paramMap, ProxyServer proxyServer, String userAgent, Map<String, String> headers) throws ExecutionException, InterruptedException {
//        proxyServer = new ProxyServer.Builder("127.0.0.1", 8888).build();
        RequestBuilder builder = new RequestBuilder();
        builder.resetCookies();
        builder.setUrl(url)
                .setMethod(HttpConstants.Methods.POST)
                .setProxyServer(proxyServer)
                .setHeader("user-agent", userAgent)
                .setFormParams(paramMap);
        headers.forEach(builder::setHeader);
        return executeRequest(builder.build());
    }

    public Response executeRequest(Request request) throws InterruptedException, ExecutionException {
        Response res = null;

        String domain = PatternUtil.group(request.getUrl(), "//.*?([^\\.]+)\\.(com|net|org|info|coop|int|co\\.uk|org\\.uk|ac\\.uk|uk|cn)", 1);
        if (domain.contains("zhihu") || domain.contains("baidu")){
            res = asyncHttpClient.executeRequest(request).get();
            return res;
        }
        semaphoreMap.putIfAbsent(domain, new Semaphore(MAX_CONCURRENT_COUNT));
        try {
            semaphoreMap.get(domain).acquire();
            res = asyncHttpClient.executeRequest(request).get();
        } finally {
            semaphoreMap.get(domain).release();
        }
        return res;
    }

    /**
     * 下载图片
     * @param fileURL 文件地址
     * @param path 保存路径
     * @param saveFileName 文件名，包括后缀名
     * @param isReplaceFile 若存在文件时，是否还需要下载文件
     */
    public void downloadFile(String fileURL,
                                    String path,
                                    String saveFileName,
                                    Boolean isReplaceFile){
        Request request = new RequestBuilder()
                .setUrl(fileURL)
                .setHeader("user-agent", Constants.userAgentArray[new Random().nextInt(Constants.userAgentArray.length)])
                .build();
        downloadFile(request, path, saveFileName, isReplaceFile);
    }

    public void downloadFile(Request request,
                                    String path,
                                    String saveFileName,
                                    Boolean isReplaceFile){
        try{
            Response response = executeRequest(request);
            log.debug("status:" + response.getStatusCode());
            File file =new File(path);
            //如果文件夹不存在则创建
            if  (!file .exists()  && !file .isDirectory()){
                file.mkdirs();
            } else{
                log.debug("//目录存在");
            }
            file = new File(path + "/" + saveFileName);
            if(!file.exists() || isReplaceFile){
                //如果文件不存在，则下载
                try {
                    OutputStream os = new FileOutputStream(file);
                    InputStream is = response.getResponseBodyAsStream();
                    byte[] buff = new byte[(int) response.getResponseBodyAsBytes().length];
                    while(true) {
                        int readed = is.read(buff);
                        if(readed == -1) {
                            break;
                        }
                        byte[] temp = new byte[readed];
                        System.arraycopy(buff, 0, temp, 0, readed);
                        os.write(temp);
                        log.debug("文件下载中....");
                    }
                    is.close();
                    os.close();
                    log.info(request.getUrl() + "--文件成功下载至" + path + "/" + saveFileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                log.debug("该文件存在！");
            }
        } catch(IllegalArgumentException e){
            log.info("连接超时...");
        } catch(Exception e1){
            e1.printStackTrace();
        }
    }

    public static class IgnoreCookieStore implements CookieStore{
        List<Cookie> empty = Collections.emptyList();

        @Override
        public void add(Uri uri, Cookie cookie) {

        }

        @Override
        public List<Cookie> get(Uri uri) {
            return empty;
        }

        @Override
        public List<Cookie> getAll() {
            return empty;
        }

        @Override
        public boolean remove(Predicate<Cookie> predicate) {
            return true;
        }

        @Override
        public boolean clear() {
            return true;
        }
    }
}
