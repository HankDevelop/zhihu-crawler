package com.github.wycm.common.util;

import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.asynchttpclient.*;
import org.asynchttpclient.cookie.CookieStore;
import org.asynchttpclient.cookie.ThreadSafeCookieStore;
import org.asynchttpclient.proxy.ProxyServer;
import org.asynchttpclient.uri.Uri;
import org.asynchttpclient.util.HttpConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLException;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import java.util.function.Predicate;

/**
 * Created by wycm on 2018/10/28.
 */
@Slf4j
public class SimpleHttpClient {
    public static final String USER_AGENT = "user-agent";
    private AsyncHttpClient asyncHttpClient;
    /**
     * 限制同一网站并发请求数
     * k:domain
     */
    public final static Map<String, Semaphore> semaphoreMap = new ConcurrentHashMap<>();

    private final static int MAX_CONCURRENT_COUNT = 1;

    private int timeout = 60000;

    public SimpleHttpClient() {
        this(500, 2000);
    }

    public SimpleHttpClient(int maxConnectionsPerHost, int maxConnections) {
        this(new ThreadSafeCookieStore(), maxConnectionsPerHost, maxConnections);
    }

    public SimpleHttpClient(CookieStore cookieStore, int maxConnectionsPerHost, int maxConnections) {
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
        if (cookieStore != null) {
            builder.setCookieStore(cookieStore);
        }
        asyncHttpClient = new DefaultAsyncHttpClient(builder.build());
    }

    public String getBodyStr(String url) throws ExecutionException, InterruptedException {
        return getBodyStr(url, null);
    }

    public String getBodyStr(String url, ProxyServer proxyServer) throws ExecutionException, InterruptedException {
        return execGetRequest(url, proxyServer).getResponseBody();
    }

    public Response execGetRequest(String url, ProxyServer proxyServer) throws ExecutionException, InterruptedException {
        return execGetRequest(url, proxyServer, null);
    }

    public Response execGetRequest(String url, ProxyServer proxyServer, String userAgent) throws ExecutionException, InterruptedException {
        return execGetRequest(url, proxyServer, userAgent, null);
    }

    public Response execGetRequest(String url, ProxyServer proxyServer, String userAgent, Map<String, String> headers) throws ExecutionException, InterruptedException {
        RequestBuilder builder = initGetRequestBuilder(url);
        initRequestInfo(null, proxyServer, userAgent, headers, builder);
        return executeRequest(builder.build());
    }

    public Response execPostRequest(String url, Map<String, List<String>> formParams, ProxyServer proxyServer, String userAgent, Map<String, String> headers) throws ExecutionException, InterruptedException {
        RequestBuilder builder = initPostRequestBuilder(url);
        initRequestInfo(formParams, proxyServer, userAgent, headers, builder);
        return executeRequest(builder.build());
    }

    private void initRequestInfo(Map<String, List<String>> formParams, ProxyServer proxyServer, String userAgent, Map<String, String> headers, RequestBuilder builder) {
        if (Objects.nonNull(proxyServer)) {
            builder.setProxyServer(proxyServer);
        }
        if (StringUtils.isBlank(userAgent)) {
            builder.setHeader(USER_AGENT, Constants.userAgentArray[new Random().nextInt(Constants.userAgentArray.length)]);
        } else {
            builder.setHeader(USER_AGENT, userAgent);
        }
        if (Objects.nonNull(headers) && !headers.isEmpty()) {
            headers.forEach(builder::setHeader);
        }
        if (Objects.nonNull(formParams) && !formParams.isEmpty()) {
            builder.setFormParams(formParams);
        }
    }

    private RequestBuilder initGetRequestBuilder(String url) {
        RequestBuilder builder = new RequestBuilder();
        builder.resetCookies();
        builder.setUrl(url);
        return builder;
    }

    private RequestBuilder initPostRequestBuilder(String url) {
        RequestBuilder builder = new RequestBuilder();
        builder.resetCookies();
        builder.setUrl(url).setMethod(HttpConstants.Methods.POST);
        return builder;
    }

    /**
     * 调用asyncHttpClient执行请求，如果域名中不包含zhihu、baidu则添加信息量控制并发
     *
     * @param request
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public Response executeRequest(Request request) throws InterruptedException, ExecutionException {
        Response res = null;

        String domain = PatternUtil.group(request.getUrl(), "//.*?([^\\.]+)\\.(com|net|org|info|coop|int|co\\.uk|org\\.uk|ac\\.uk|uk|cn)", 1);
        if (domain.contains("tohoku") || domain.contains("zhihu") || domain.contains("baidu")) {
            res = asyncHttpClient.executeRequest(request).get();
            return res;
        }
        // 对代理网站进行并发控制
        semaphoreMap.putIfAbsent(domain, new Semaphore(MAX_CONCURRENT_COUNT));
        try {
            semaphoreMap.get(domain).acquire();
            res = asyncHttpClient.executeRequest(request).get();
        } finally {
            semaphoreMap.get(domain).release();
        }
        log.debug("execute request {} and result is {}", request.getFormParams(), res);
        return res;
    }

    /**
     * 下载图片
     *
     * @param fileURL       文件地址
     * @param path          保存路径
     * @param saveFileName  文件名，包括后缀名
     * @param isReplaceFile 若存在文件时，是否还需要下载文件
     */
    public void downloadFile(String fileURL,
                             String path,
                             String saveFileName,
                             Boolean isReplaceFile) {
        Request request = new RequestBuilder()
                .setUrl(fileURL)
                .setHeader(USER_AGENT, Constants.userAgentArray[new Random().nextInt(Constants.userAgentArray.length)])
                .build();
        downloadFile(request, path, saveFileName, isReplaceFile);
    }

    public void downloadFile(Request request,
                             String path,
                             String saveFileName,
                             Boolean isReplaceFile) {
        try {
            Response response = executeRequest(request);
            if (response.getStatusCode() != HttpStatus.OK.value()) {
                throw new IllegalStateException("http请求:" + request.getUrl() + "响应错误：" + response.getStatusCode());
            }
            File file = new File(path);
            //如果文件夹不存在则创建
            if (!file.exists() && !file.isDirectory()) {
                file.mkdirs();
            } else {
                log.debug("//目录存在");
            }
            file = new File(path + "/" + saveFileName);
            if (!file.exists() || isReplaceFile) {
                //如果文件不存在，则下载
                OutputStream os = null;
                InputStream is = null;
                try {
                    os = new FileOutputStream(file);
                    is = response.getResponseBodyAsStream();
                    byte[] buff = new byte[(int) response.getResponseBodyAsBytes().length];
                    while (true) {
                        int readed = is.read(buff);
                        if (readed == -1) {
                            break;
                        }
                        byte[] temp = new byte[readed];
                        System.arraycopy(buff, 0, temp, 0, readed);
                        os.write(temp);
                        log.debug("文件下载中....");
                    }
                    log.info(request.getUrl() + "--文件成功下载至" + path + "/" + saveFileName);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    IOUtils.closeQuietly(is);
                    IOUtils.closeQuietly(os);
                }
            } else {
                log.info("{} 文件存在！", saveFileName);
            }
        } catch (IllegalArgumentException e) {
            log.info("连接超时...");
        } catch (Exception e1) {
            log.error(e1.getMessage(), e1);
        }
    }

    public static class IgnoreCookieStore implements CookieStore {
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
