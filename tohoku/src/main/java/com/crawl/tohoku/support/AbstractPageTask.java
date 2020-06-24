package com.crawl.tohoku.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.crawl.tohoku.TohokuConstants;
import com.crawl.tohoku.dao.DictQueryInfoDao;
import com.crawl.tohoku.entity.DictQueryInfo;
import com.crawl.tohoku.entity.DictQueryInfoExample;
import com.crawl.tohoku.service.TaskQueueService;
import com.github.wycm.common.*;
import com.github.wycm.common.util.Constants;
import com.github.wycm.common.util.CrawlerUtils;
import com.github.wycm.common.util.ThreadPoolUtil;
import com.github.wycm.proxy.AbstractHttpClient;
import com.github.wycm.proxy.util.ProxyUtil;
import io.netty.handler.codec.http.HttpStatusClass;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.asynchttpclient.proxy.ProxyServer;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Function;


/**
 * page task
 * 下载网页并解析，具体解析由子类实现
 * 若使用代理，从ProxyPool中取
 */
@Slf4j
public abstract class AbstractPageTask implements Runnable, RetryHandler, SinglePool {
    @Getter
    @Setter
    protected String url;

    @Getter
    protected final static Function<String, Boolean> jsonPageBannedFunction = (String s) -> !s.contains("login_status");

    @Getter
    protected final static Function<String, Boolean> htmlPageBannedFunction = (String s) -> s.equals("<html><header></header><body></body></html>");

    @Getter
    @Setter
    protected boolean proxyFlag;//是否通过代理下载

    protected Proxy currentProxy;//当前线程使用的代理

    @Getter
    @Setter
    protected int currentRetryTimes = 0;

    private String currentPoolName = null;


    //当前任务是否需要重试
    @Getter
    @Setter
    boolean isRetry = false;

    @Getter
    @Setter
    protected CrawlerMessage crawlerMessage;


    public AbstractPageTask() {

    }

    public AbstractPageTask(String url, boolean proxyFlag) {
        this.url = url;
        this.proxyFlag = proxyFlag;
    }

    public AbstractPageTask(String url, boolean proxyFlag, int currentRetryTimes) {
        this.url = url;
        this.proxyFlag = proxyFlag;
        this.currentRetryTimes = currentRetryTimes;
    }

    public AbstractPageTask(CrawlerMessage message) {
        this.crawlerMessage = message;
    }

    public void run() {
        long requestStartTime = 0l;
        try {
            Page page = null;
            boolean useProxy = false;
            if (url != null) {
                if (proxyFlag && crawlerMessage.getCurrentRetryTimes() > 0) {
                    // && crawlerMessage.getCurrentRetryTimes() > 0
                    currentProxy = getProxyQueue().takeProxy(getProxyQueueName());
                    if (Objects.nonNull(currentProxy) && !(currentProxy.getIp().equals(LocalIPService.getLocalIp()))) {
                        useProxy = true;
                    }
                }
                requestStartTime = System.currentTimeMillis();
                if (useProxy) {
                    if (Objects.isNull(crawlerMessage.getMessageContext()) || crawlerMessage.getMessageContext().isEmpty()) {
                        //代理
                        page = getHttpClient().asyncGet(url, new ProxyServer.Builder(currentProxy.getIp(), currentProxy.getPort()).build(), crawlerMessage.getUserAgent(), crawlerMessage.getHeaders());
                    } else {
                        page = getHttpClient().asyncPost(url, crawlerMessage.getMessageContext(), new ProxyServer.Builder(currentProxy.getIp(), currentProxy.getPort()).build(), crawlerMessage.getUserAgent(), crawlerMessage.getHeaders());
                    }
                    page.setProxy(currentProxy);
                } else {
                    if (Objects.isNull(crawlerMessage.getMessageContext()) || crawlerMessage.getMessageContext().isEmpty()) {
                        page = getHttpClient().asyncGet(url, crawlerMessage.getUserAgent(), crawlerMessage.getHeaders());
                    } else {
                        page = getHttpClient().asyncPost(url, crawlerMessage.getMessageContext(), crawlerMessage.getUserAgent(), crawlerMessage.getHeaders());
                    }
                }
            }
            long requestEndTime = System.currentTimeMillis();
            if (Objects.nonNull(page)) {
                int status = page.getStatusCode();
                String logStr = Thread.currentThread().getName() + " " + currentProxy +
                        "  executing request " + page.getUrl() + crawlerMessage.getMessageContext().toString() + " response statusCode:" + status +
                        "  request cost time:" + (requestEndTime - requestStartTime) + "ms";
                log.info(logStr);
                if (HttpStatusClass.valueOf(status) == HttpStatusClass.SUCCESS && !responseError(page)) {
                    ProxyUtil.handleResponseSuccProxy(currentProxy);
                    handle(page);
                    if(page.getUrl().startsWith(TohokuConstants.TOHOKU_KDIC_URL) || page.getUrl().startsWith(TohokuConstants.TOHOKU_MANCHU_URL)) {
                        DictQueryInfoExample dictQueryInfoExample = new DictQueryInfoExample();
                        DictQueryInfoExample.Criteria criteria = dictQueryInfoExample.createCriteria();
                        criteria.andRequestUriEqualTo(crawlerMessage.getUrl());
                        criteria.andRequestInfoEqualTo(JSON.toJSONString(crawlerMessage.getMessageContext(), SerializerFeature.MapSortField));
                        Optional<DictQueryInfo> queryInfo = Optional.ofNullable(getDictQueryInfoDao().selectUniqueByExample(dictQueryInfoExample));
                        if (queryInfo.isPresent()) {
                            DictQueryInfo dictQueryInfo = queryInfo.get();
                            Optional<Integer> respCode = Optional.ofNullable(dictQueryInfo.getRespCode());
                            if (respCode.isPresent() && HttpStatus.SC_OK == respCode.get()) {
                                dictQueryInfo.setCalls(Optional.ofNullable(dictQueryInfo.getRespCode()).isPresent() ? dictQueryInfo.getCalls() + 1 : 1);
                                dictQueryInfo.setModifyTime(new Date());
                                getDictQueryInfoDao().updateByPrimaryKeySelective(dictQueryInfo);
                            } else {
                                dictQueryInfo.setCalls(1);
                                dictQueryInfo.setRespCode(HttpStatus.SC_OK);
                                dictQueryInfo.setRespTxt(page.getHtml());
                                dictQueryInfo.setModifyTime(new Date());
                                getDictQueryInfoDao().updateByExampleWithBLOBs(dictQueryInfo, dictQueryInfoExample);
                            }
                        } else {
                            DictQueryInfo dictQueryInfo = new DictQueryInfo();
                            dictQueryInfo.setRequestUri(crawlerMessage.getUrl());
                            dictQueryInfo.setRequestInfo(JSON.toJSONString(crawlerMessage.getMessageContext(), SerializerFeature.MapSortField));
                            dictQueryInfo.setCreateTime(new Date());
                            dictQueryInfo.setCalls(1);
                            dictQueryInfo.setRespCode(HttpStatus.SC_OK);
                            dictQueryInfo.setRespTxt(page.getHtml());
                            dictQueryInfo.setModifyTime(new Date());
                            getDictQueryInfoDao().insert(dictQueryInfo);
                        }
                    }
                } else {
                    ProxyUtil.handleResponseFailedProxy(currentProxy);
                    retry();
                }
            } else {
                log.error("请求失败，未返回结果，请求信息：{}", crawlerMessage.toString());
            }
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        } catch (ExecutionException e) {
            log.debug(e.getMessage(), e);
            ProxyUtil.handleResponseFailedProxy(currentProxy);
            try {
                retry();
            } catch (Exception e1) {
                log.error(e1.getMessage(), e1);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (currentProxy != null && !ProxyUtil.isDiscardProxy(currentProxy)) {
                getProxyQueue().addProxy(getProxyQueueName(), currentProxy);
            }
            try {
                receiveNewTask();
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
            log.info("当前请求执行完成，请求信息：{}", crawlerMessage);
        }
    }


    /**
     * 子类实现page的处理
     *
     * @param page
     */
    protected abstract void handle(Page page);


    protected void receiveNewTask() throws InterruptedException {
        if (crawlerMessage != null && !Constants.stopService && !isRetry) {
            CrawlerMessage message = getTaskQueueService().receiveTask(CrawlerUtils.getTaskQueueName(this.getClass()));
            createNewTask(message);
        }
    }

    protected abstract void createNewTask(CrawlerMessage crawlerMessage);

    @Override
    public String getCurrentThreadPoolName() {
        if (this.currentPoolName == null) {
            this.currentPoolName = ThreadPoolUtil.getThreadPoolName(this.getClass());
        }
        return this.currentPoolName;
    }

    @Override
    public ThreadPoolExecutor getCurrentThreadPool() {
        return ThreadPoolUtil.getThreadPoolExecutorMap().get(getCurrentThreadPoolName());
    }

    private String getProxyStr(Proxy proxy) {
        if (proxy == null) {
            return "";
        }
        return proxy.getIp() + ":" + proxy.getPort();
    }

    @Override
    public void retry() throws InterruptedException {
        if (getCurrentRetryTimes() < getMaxRetryTimes()) {
            crawlerMessage.setCurrentRetryTimes(crawlerMessage.getCurrentRetryTimes() + 1);
            createNewTask(crawlerMessage);
        } else {
            log.warn(this.getClass().getSimpleName() + "maxRetryTimes:{}, currentRetryTimes:{}", getMaxRetryTimes(), getCurrentRetryTimes());
            receiveNewTask();
        }
        isRetry = true;
    }

    protected abstract AbstractHttpClient getHttpClient();

    protected abstract String getProxyQueueName();

    protected abstract ProxyQueue getProxyQueue();

    protected abstract TaskQueueService getTaskQueueService();

    protected abstract DictQueryInfoDao getDictQueryInfoDao();

    protected abstract LocalIPService getLocalIPService();

    protected boolean responseError(Page page) {
        return false;
    }

}
