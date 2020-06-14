package com.crawl.tohoku.support;

import com.github.wycm.common.util.Constants;
import com.github.wycm.common.util.PatternUtil;
import io.netty.handler.codec.http.cookie.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.asynchttpclient.Request;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.Response;
import org.asynchttpclient.cookie.CookieStore;
import org.asynchttpclient.proxy.ProxyServer;
import org.asynchttpclient.uri.Uri;
import org.asynchttpclient.util.HttpConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import java.util.function.Predicate;

@Slf4j
@Component
public class HttpClientUtil {
    public static final String USER_AGENT = "user-agent";
    /**
     * 限制同一网站并发请求数
     * k:domain
     */
    public final static Map<String, Semaphore> semaphoreMap = new ConcurrentHashMap<>();
    private final static int MAX_CONCURRENT_COUNT = 3;

    @Autowired
    private CloseableHttpAsyncClient httpAsyncClient;
    @Autowired
    private HttpAsyncClientBuilder httpAsyncClientBuilder;

    @Autowired
    private RequestConfig requestConfig;

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
        //创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);

        //装填参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if(formParams!=null){
            for (Map.Entry<String, List<String>> entry : formParams.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue().get(0)));
            }
        }
        //设置参数到请求对象中
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, Charset.defaultCharset()));

        System.out.println("请求地址："+url);
        System.out.println("请求参数："+nvps.toString());

        //设置header信息
        //指定报文头【Content-type】、【User-Agent】
        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
        httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

//        httpAsyncClientBuilder.setProxy()

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
//            res = httpAsyncClient.execute().(request).get();
            return res;
        }
        // 对代理网站进行并发控制
        semaphoreMap.putIfAbsent(domain, new Semaphore(MAX_CONCURRENT_COUNT));
        try {
            semaphoreMap.get(domain).acquire();
//            res = httpAsyncClient.executeRequest(request).get();
        } finally {
            semaphoreMap.get(domain).release();
        }
        log.debug("execute request {} and result is {}", request.getFormParams(), res);
        return res;
    }

    /**
     * 下载图片
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
            log.debug("status:" + response.getStatusCode());
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
                log.debug("该文件存在！");
            }
        } catch (IllegalArgumentException e) {
            log.info("连接超时...");
        } catch (Exception e1) {
            e1.printStackTrace();
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