package com.crawl.tohoku.service.sender;

import com.alibaba.fastjson.JSON;
import com.crawl.tohoku.task.TohokuProxyPageDownloadTask;
import com.github.wycm.common.CrawlerMessage;
import com.github.wycm.common.TaskQueueService;
import com.github.wycm.common.util.CrawlerUtils;
import com.github.wycm.proxy.ProxyPageProxyPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

@Slf4j
@Service
public class ProxyPageDownloadTaskSender extends BaseSender{
    @Autowired
    TaskQueueService taskQueueService;

    private static final int initQueueSize = 50;
    @Autowired
    private ProxyPageProxyPool proxyPageProxyPool;

    @Override
    @Scheduled(initialDelay = 1000, fixedDelay = 1000 * 60 * 60)
    public void send() {
        log.info("start send ProxyPageDownloadTask message");

        if (taskQueueService.queueSize(CrawlerUtils.getTaskQueueName(TohokuProxyPageDownloadTask.class)) > initQueueSize){
            log.info(CrawlerUtils.getTaskQueueName(TohokuProxyPageDownloadTask.class) + "size more than " + initQueueSize + ", not send task");
            return;
        }
        newSingleThreadExecutor().submit(() -> {
            ProxyPageProxyPool.proxyMap.keySet().forEach(url -> {
                String requestId = UUID.randomUUID().toString();
                if (redisLockUtil.lock(CrawlerUtils.getLockKeyPrefix(TohokuProxyPageDownloadTask.class) + url
                        , requestId
                        , 1000 * 60 * 50)){
                    CrawlerMessage crawlerMessage = new CrawlerMessage(url);
                    taskQueueService.sendTask(CrawlerUtils.getTaskQueueName(TohokuProxyPageDownloadTask.class), crawlerMessage, 100000);
                    log.info("ProxyPageDownloadTask message send success:{}", JSON.toJSONString(crawlerMessage));
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    log.warn("get lock failed : {}", CrawlerUtils.getLockKeyPrefix(TohokuProxyPageDownloadTask.class) + url);
                }
            });
            log.info("end send ProxyPageDownloadTask message");
        });

    }
}