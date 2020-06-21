package com.crawl.tohoku.service.receiver;

import com.crawl.tohoku.service.sender.DictImageDownloadTaskSender;
import com.crawl.tohoku.service.sender.Kdic33DictQueryTaskSender;
import com.crawl.tohoku.task.DictImageDownloadTask;
import com.crawl.tohoku.task.Kdic33DictQueryTask;
import com.github.wycm.common.CrawlerMessage;
import com.github.wycm.common.util.ThreadPoolUtil;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
@NoArgsConstructor
public class DictImageDownloadTaskReceiver extends BaseReceiver{

    @Autowired
    private DictImageDownloadTaskSender taskSender;

    @Override
    @PostConstruct
    public void receive() {
        ThreadPoolUtil
                .getThreadPool(DictImageDownloadTask.class)
                .execute(() -> {
                    taskSender.send();
                });
        receive(DictImageDownloadTask.class);
    }

    @Override
    protected Runnable createNewTask(CrawlerMessage crawlerMessage) {
        DictImageDownloadTask task = new DictImageDownloadTask(crawlerMessage, tohokuComponent);
        task.setUrl(crawlerMessage.getUrl());
        task.setCurrentRetryTimes(crawlerMessage.getCurrentRetryTimes());
        task.setProxyFlag(true);
        return task;
    }
}
