package com.crawl.tohoku.service.receiver;

import com.crawl.tohoku.service.TohokuComponent;
import com.github.wycm.common.CrawlerMessage;
import com.github.wycm.common.TaskQueueService;
import com.github.wycm.common.util.CrawlerUtils;
import com.github.wycm.common.util.ThreadPoolUtil;
import com.crawl.tohoku.support.AbstractPageTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public abstract class BaseReceiver<T extends AbstractPageTask> {
    @Autowired
    protected TaskQueueService taskQueueService;

    @Autowired
    protected TohokuComponent tohokuComponent;

    protected abstract void receive();

    protected void receive(Class<T> tClass){
        Thread.currentThread().setName(tClass.getSimpleName() + "Receiver");
        if ("test".equals(System.getProperties().getProperty("env"))){
            log.info("test env...");
            return;
        }
        log.info("start receive {} message", CrawlerUtils.getTaskQueueName(tClass));
        int corePoolSize = ThreadPoolUtil.getThreadPool(tClass).getCorePoolSize();
        for (int i = 0; i < corePoolSize; i++){
            CrawlerMessage message = null;
            try {
                message = taskQueueService.receiveTask(CrawlerUtils.getTaskQueueName(tClass));
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
                return;
            }
            ThreadPoolUtil.getThreadPool(tClass).execute(createNewTask(message));
            log.info("create {} task success", tClass);
        }
    }

    protected abstract Runnable createNewTask(CrawlerMessage crawlerMessage);

}
