package com.crawl.tohoku.service.receiver;

import com.crawl.tohoku.service.sender.Kdic33DictQueryTaskSender;
import com.crawl.tohoku.task.Kdic33DictQueryTask;
import com.crawl.tohoku.task.TohokuProxyPageDownloadTask;
import com.github.wycm.common.CrawlerMessage;
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
public class Kdic33DictQueryTaskReceiver extends BaseReceiver{

    @Autowired
    private Kdic33DictQueryTaskSender taskSender;

    @Override
//    @PostConstruct
    public void receive() {
        ThreadPoolUtil
                .getThreadPool(Kdic33DictQueryTask.class)
                .execute(() -> {
                    taskSender.send();
                });
        receive(Kdic33DictQueryTask.class);
    }

    @Override
    protected Runnable createNewTask(CrawlerMessage crawlerMessage) {
        Kdic33DictQueryTask task = new Kdic33DictQueryTask(crawlerMessage, tohokuComponent);
        task.setUrl(crawlerMessage.getUrl());
        task.setCurrentRetryTimes(crawlerMessage.getCurrentRetryTimes());
        task.setProxyFlag(true);
        return task;
    }
}
