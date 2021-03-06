package com.crawl.tohoku.service.sender;

import com.alibaba.fastjson.JSON;
import com.crawl.tohoku.service.TaskQueueService;
import com.crawl.tohoku.task.TohokuProxyPageDownloadTask;
import com.github.wycm.common.CrawlerMessage;
import com.github.wycm.common.util.CrawlerUtils;
import com.github.wycm.common.util.RedisLockUtil;
import com.github.wycm.proxy.ProxyPageProxyPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class ProxyPageDownloadTaskSender implements BaseSender {
    @Autowired
    RedisLockUtil redisLockUtil;
    @Autowired
    TaskQueueService taskQueueService;

    private static final int initQueueSize = 1000;

    //    @Scheduled(initialDelay = 1000, fixedDelay = 1000)
    public void send() {
        log.debug("start send ProxyPageDownloadTask message");

        if (taskQueueService.queueSize(CrawlerUtils.getTaskQueueName(TohokuProxyPageDownloadTask.class)) > initQueueSize) {
            log.debug(CrawlerUtils.getTaskQueueName(TohokuProxyPageDownloadTask.class) + "size more than " + initQueueSize + ", not send task");
            return;
        }
        ProxyPageProxyPool.proxyMap.keySet().forEach(url -> {
            String requestId = UUID.randomUUID().toString();
            if (redisLockUtil.lock(CrawlerUtils.getLockKeyPrefix(TohokuProxyPageDownloadTask.class) + url
                    , requestId
                    , 1000 * 60 * 50)) {
                CrawlerMessage crawlerMessage = new CrawlerMessage(url);
                taskQueueService.sendTask(CrawlerUtils.getTaskQueueName(TohokuProxyPageDownloadTask.class), crawlerMessage, initQueueSize);
                log.debug("ProxyPageDownloadTask message send success:{}", JSON.toJSONString(crawlerMessage));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                log.warn("get lock failed : {}", CrawlerUtils.getLockKeyPrefix(TohokuProxyPageDownloadTask.class) + url);
            }
        });
        log.debug("end send ProxyPageDownloadTask message");
    }

}
