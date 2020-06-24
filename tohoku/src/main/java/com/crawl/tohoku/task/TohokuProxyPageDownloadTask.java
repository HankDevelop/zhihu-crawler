package com.crawl.tohoku.task;

import com.alibaba.fastjson.JSON;
import com.crawl.tohoku.TohokuConstants;
import com.crawl.tohoku.dao.DictQueryInfoDao;
import com.crawl.tohoku.service.TaskQueueService;
import com.crawl.tohoku.service.TohokuComponent;
import com.crawl.tohoku.support.AbstractPageTask;
import com.github.wycm.common.*;
import com.github.wycm.common.util.Constants;
import com.github.wycm.common.util.CrawlerUtils;
import com.github.wycm.common.util.RedisLockUtil;
import com.github.wycm.common.util.ThreadPoolUtil;
import com.github.wycm.proxy.AbstractHttpClient;
import com.github.wycm.proxy.ProxyListPageParser;
import com.github.wycm.proxy.ProxyPageProxyPool;
import com.github.wycm.proxy.site.ProxyListPageParserFactory;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collections;
import java.util.List;
import java.util.UUID;


/**
 * 下载代理网页并解析
 * 若下载失败，通过代理去下载代理网页
 */
@Slf4j
@NoArgsConstructor
public class TohokuProxyPageDownloadTask extends AbstractPageTask implements Runnable, RetryHandler {
    private TohokuComponent tohokuComponent;


    public TohokuProxyPageDownloadTask(CrawlerMessage crawlerMessage, boolean proxyFlag, TohokuComponent tohokuComponent) {
        this.crawlerMessage = crawlerMessage;
        this.url = crawlerMessage.getUrl();
        this.proxyFlag = proxyFlag;
        this.currentRetryTimes = crawlerMessage.getCurrentRetryTimes();
        this.tohokuComponent = tohokuComponent;
    }


    @Override
    protected AbstractHttpClient getHttpClient() {
        return tohokuComponent.getProxyHttpClient();
    }

    @Override
    protected String getProxyQueueName() {
        return tohokuComponent.getCommonProperties().getProxyPageProxyQueueName();
    }

    @Override
    protected ProxyQueue getProxyQueue() {
        return tohokuComponent.getProxyQueue();
    }

    @Override
    protected TaskQueueService getTaskQueueService() {
        return tohokuComponent.getTaskQueueService();
    }

    @Override
    protected DictQueryInfoDao getDictQueryInfoDao() {
        return tohokuComponent.getDictQueryInfoDao();
    }

    @Override
    protected LocalIPService getLocalIPService() {
        return tohokuComponent.getLocalIPService();
    }

    @Override
    public int getMaxRetryTimes() {
        return 3;
    }

    @Override
    public int getCurrentRetryTimes() {
        return this.currentRetryTimes;
    }

    @Override
    public void handle(Page page) {
        if (page.getHtml() == null || page.getHtml().equals("")) {
            return;
        }

        ProxyListPageParser parser = ProxyListPageParserFactory.
                getProxyListPageParser(ProxyPageProxyPool.proxyMap.get(url));
        List<Proxy> proxyList = parser.parse(page.getHtml());
        for (Proxy p : proxyList) {
            String proxyPagekey = TohokuConstants.PROXY_PAGE_REDIS_KEY_PREFIX + getProxyStr(p);
            boolean containFlag = getRedisTemplate().countExistingKeys(Collections.singleton(proxyPagekey)) > 0;
            if (!containFlag) {
                String requestId = UUID.randomUUID().toString();
                if (getRedisLockUtil().lock(proxyPagekey, requestId, Constants.REDIS_TIMEOUT * 1000)) {
                    getTaskQueueService().sendTask(CrawlerUtils.getTaskQueueName(TohokuProxyPageProxyTestTask.class), JSON.toJSONString(p), 100000);
                }
            }
            String tohokuPageKey = TohokuConstants.TOHOKU_PAGE_REDIS_KEY_PREFIX + getProxyStr(p);
            Proxy copyProxy = new Proxy();
            BeanUtils.copyProperties(p, copyProxy);
            p = copyProxy;
            containFlag = getRedisTemplate().countExistingKeys(Collections.singleton(tohokuPageKey)) > 0;
            if (!containFlag) {
                String requestId = UUID.randomUUID().toString();
                if (getRedisLockUtil().lock(tohokuPageKey, requestId, Constants.REDIS_TIMEOUT * 1000)) {
                    getTaskQueueService().sendTask(CrawlerUtils.getTaskQueueName(TohokuPageProxyTestTask.class), JSON.toJSONString(p), 100000);
                }
            }
        }
    }

    @Override
    protected void createNewTask(CrawlerMessage crawlerMessage) {
        if (Constants.stopService){
            return;
        }
        TohokuProxyPageDownloadTask task = new TohokuProxyPageDownloadTask(crawlerMessage, true, tohokuComponent);
        task.crawlerMessage = crawlerMessage;
        task.url = crawlerMessage.getUrl();
        task.currentRetryTimes = crawlerMessage.getCurrentRetryTimes();
        task.proxyFlag = true;
        ThreadPoolUtil.getThreadPool(this.getClass()).execute(task);
        log.info("create new {} success", this.getClass().getSimpleName());
    }

    private RedisLockUtil getRedisLockUtil(){
        return tohokuComponent.getRedisLockUtil();
    }

    private RedisTemplate getRedisTemplate(){
        return tohokuComponent.getRedisTemplate();
    }

    private String getProxyStr(Proxy proxy) {
        if (proxy == null) {
            return "";
        }
        return proxy.getIp() + ":" + proxy.getPort();
    }
}
