package com.crawl.tohoku.service.receiver;

import com.crawl.tohoku.task.TohokuProxyPageDownloadTask;
import com.github.wycm.common.CrawlerMessage;
import com.crawl.tohoku.service.TaskQueueService;
import com.github.wycm.common.util.CrawlerUtils;
import com.github.wycm.common.util.ThreadPoolUtil;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import static java.util.concurrent.Executors.newSingleThreadExecutor;


@Slf4j
@Service
@NoArgsConstructor
public class ProxyPageDownloadTaskReceiver extends BaseReceiver {
    @Autowired
    private TaskQueueService taskQueueService;

    @PostConstruct
    @Override
    public void receive() {
        if ("test".equals(System.getProperties().getProperty("env"))) {
            log.info("test env...");
            return;
        }
        log.info("start receive ProxyPageDownloadTask message");
        int corePoolSize = ThreadPoolUtil
                .getThreadPool(TohokuProxyPageDownloadTask.class).getCorePoolSize();
        for (int i = 0; i < corePoolSize; i++) {
            CrawlerMessage message = null;
            try {
                message = taskQueueService.receiveTask(CrawlerUtils.getTaskQueueName(TohokuProxyPageDownloadTask.class));
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
                return;
            }
            ThreadPoolUtil
                    .getThreadPool(TohokuProxyPageDownloadTask.class)
                    .execute(new TohokuProxyPageDownloadTask(message, false, tohokuComponent));
        }
    }

    @Override
    protected Runnable createNewTask(CrawlerMessage crawlerMessage) {
        return null;
    }
}
