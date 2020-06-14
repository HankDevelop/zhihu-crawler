package com.crawl.tohoku.service.receiver;

import com.crawl.tohoku.TohokuConstants;
import com.crawl.tohoku.task.TohokuProxyPageProxyTestTask;
import com.github.wycm.common.CrawlerMessage;
import com.github.wycm.common.Proxy;
import com.github.wycm.common.TaskQueueService;
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
public class ProxyPageProxyTestReceiver extends BaseReceiver {
    @Autowired
    private TaskQueueService taskQueueService;

    @PostConstruct
    @Override
    public void receive() {
        if ("test".equals(System.getProperties().getProperty("env"))) {
            log.info("test env...");
            return;
        }
        log.info("start receive {} message", CrawlerUtils.getTaskQueueName(TohokuProxyPageProxyTestTask.class));
        int corePoolSize = ThreadPoolUtil
                .getThreadPool(TohokuProxyPageProxyTestTask.class).getCorePoolSize();
        for (int i = 0; i < corePoolSize; i++) {
            Proxy proxy = null;
            try {
                proxy = taskQueueService.receiveProxyTask(CrawlerUtils.getTaskQueueName(TohokuProxyPageProxyTestTask.class));
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
                return;
            }
            ThreadPoolUtil.getThreadPool(TohokuProxyPageProxyTestTask.class)
                    .execute(new TohokuProxyPageProxyTestTask(proxy, TohokuConstants.TOHOKU_START_URL, tohokuComponent));
        }
    }

    @Override
    protected Runnable createNewTask(CrawlerMessage crawlerMessage) {
        return null;
    }
}
