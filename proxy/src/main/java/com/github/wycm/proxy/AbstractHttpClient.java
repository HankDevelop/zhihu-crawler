package com.github.wycm.proxy;

import com.github.wycm.common.*;
import com.github.wycm.common.util.SimpleHttpClient;
import com.github.wycm.proxy.util.ProxyUtil;
import io.netty.handler.codec.http.HttpStatusClass;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.Response;
import org.asynchttpclient.proxy.ProxyServer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

@Slf4j
public abstract class AbstractHttpClient {
    @Autowired
    protected ProxyQueue proxyQueue;

    @Autowired
    protected CommonProperties commonProperties;

    protected SimpleHttpClient httpClient;


    public Page asyncGet(String url) throws ExecutionException, InterruptedException {
        return asyncGet(url, null);
    }

    public Page asyncGet(String url, ProxyServer proxyServer) throws ExecutionException, InterruptedException {
        return asyncGet(url, proxyServer, null, null);
    }

    public Page asyncGet(String url, String userAgent, Map<String, String> headers) throws ExecutionException, InterruptedException {
        return asyncGet(url, null, userAgent, headers);
    }

    public Page asyncGet(String url, ProxyServer proxyServer, String userAgent, Map<String, String> headers) throws ExecutionException, InterruptedException {
        Response response = httpClient.execGetRequest(url, proxyServer, userAgent, headers);
        return transformResponseToPage(url, response);
    }

    public Page asyncPost(String url, Map<String, List<String>> formParams, String userAgent, Map<String, String> headers) throws ExecutionException, InterruptedException {
        return asyncPost(url, formParams, null, userAgent, headers);
    }

    public Page asyncPost(String url, Map<String, List<String>> formParams, ProxyServer proxyServer, String userAgent, Map<String, String> headers) throws ExecutionException, InterruptedException {
        Response response = httpClient.execPostRequest(url, formParams, proxyServer, userAgent, headers);
        return transformResponseToPage(url, response);
    }

    public Page transformResponseToPage(String url, Response response) throws ExecutionException, InterruptedException {
        Page page = new Page();
        page.setStatusCode(response.getStatusCode());
        page.setHtml(response.getResponseBody());
        page.setUrl(url);
        return page;
    }

    public Page executeGetWithRetry(CrawlerMessage crawlerMessage, Function<String, Boolean> ipBannedFunction) throws InterruptedException {
        int currentRetryTimes = 0;
        Proxy currentProxy = null;
        while (currentRetryTimes < 3){
            Page page = null;
            try {
                long requestStartTime = 0L;
                currentProxy = proxyQueue.takeProxy(commonProperties.getTargetPageProxyQueueName());
                requestStartTime = System.currentTimeMillis();

                if(!(currentProxy.getIp().equals(LocalIPService.getLocalIp()))){
                    //代理
                    page = this.asyncGet(crawlerMessage.getUrl(), new ProxyServer.Builder(currentProxy.getIp(), currentProxy.getPort()).build(), crawlerMessage.getUserAgent(), crawlerMessage.getHeaders());
                } else {
                    page = this.asyncGet(crawlerMessage.getUrl(), crawlerMessage.getUserAgent(), crawlerMessage.getHeaders());
                }
                long requestEndTime = System.currentTimeMillis();
                page.setProxy(currentProxy);
                int status = page.getStatusCode();

                if(HttpStatusClass.valueOf(status) == HttpStatusClass.SUCCESS){
                    if (ipBannedFunction.apply(page.getHtml())){
                        log.warn("response error, url:{}, response:{}, discard ip:{}, port:{}", crawlerMessage.getUrl(), page.getHtml(), currentProxy.getIp(), currentProxy.getPort());
                        throw new ExecutionException(new Throwable("response error"));
                    }
                    ProxyUtil.handleResponseSuccProxy(currentProxy);
                    return page;
                } else {
                    log.error("{}, retryTimes:{},{}, url:{}, response Code:{}, cost time:{}ms"
                            , Thread.currentThread().getName()
                            , currentRetryTimes
                            , currentProxy
                            , page.getUrl()
                            , status
                            , requestEndTime - requestStartTime);
                    ProxyUtil.handleResponseFailedProxy(currentProxy);
                }
            } catch (ExecutionException e) {
                log.debug(e.getMessage(), e);
                ProxyUtil.handleResponseFailedProxy(currentProxy);
            } finally {
                if (currentProxy != null && !ProxyUtil.isDiscardProxy(currentProxy)){
                    proxyQueue.addProxy(commonProperties.getTargetPageProxyQueueName(), currentProxy);
                }
            }
            currentRetryTimes = currentRetryTimes + 1;
        }
        return null;
    }

    public Page executePostWithRetry(CrawlerMessage crawlerMessage, Function<String, Boolean> ipBannedFunction) throws InterruptedException {
        int currentRetryTimes = 0;
        Proxy currentProxy = null;
        while (currentRetryTimes < 3){
            Page page = null;
            try {
                long requestStartTime = 0l;
                currentProxy = proxyQueue.takeProxy(commonProperties.getTargetPageProxyQueueName());
                requestStartTime = System.currentTimeMillis();

                //page = execRequest(crawlerMessage, currentProxy);

                if(!(currentProxy.getIp().equals(LocalIPService.getLocalIp()))){
                    //代理 TODO  form params
                    page = this.asyncPost(crawlerMessage.getUrl(), null, new ProxyServer.Builder(currentProxy.getIp(), currentProxy.getPort()).build(), crawlerMessage.getUserAgent(), crawlerMessage.getHeaders());
                } else {
                    page = this.asyncPost(crawlerMessage.getUrl(), null, crawlerMessage.getUserAgent(), crawlerMessage.getHeaders());
                }
                long requestEndTime = System.currentTimeMillis();
                page.setProxy(currentProxy);
                int status = page.getStatusCode();

                if(HttpStatusClass.valueOf(status) == HttpStatusClass.SUCCESS){
                    if (ipBannedFunction.apply(page.getHtml())){
                        log.warn("response error, url:{}, response:{}, discard ip:{}, port:{}", crawlerMessage.getUrl(), page.getHtml(), currentProxy.getIp(), currentProxy.getPort());
                        throw new ExecutionException(new Throwable("response error"));
                    }
                    ProxyUtil.handleResponseSuccProxy(currentProxy);
                    return page;
                } else {
                    log.error("{}, retryTimes:{},{}, url:{}, response Code:{}, cost time:{}ms"
                            , Thread.currentThread().getName()
                            , currentRetryTimes
                            , currentProxy
                            , page.getUrl()
                            , status
                            , requestEndTime - requestStartTime);
                    ProxyUtil.handleResponseFailedProxy(currentProxy);
                }
            } catch (ExecutionException e) {
                log.debug(e.getMessage(), e);
                ProxyUtil.handleResponseFailedProxy(currentProxy);
            } finally {
                if (currentProxy != null && !ProxyUtil.isDiscardProxy(currentProxy)){
                    proxyQueue.addProxy(commonProperties.getTargetPageProxyQueueName(), currentProxy);
                }
            }
            currentRetryTimes = currentRetryTimes + 1;
        }
        return null;
    }

    public abstract LocalIPService getLocalIPService();

}
