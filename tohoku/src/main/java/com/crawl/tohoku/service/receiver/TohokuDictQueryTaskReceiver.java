package com.crawl.tohoku.service.receiver;

import com.crawl.tohoku.task.TohokuDictQueryTask;
import com.github.wycm.common.CrawlerMessage;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
@NoArgsConstructor
public class TohokuDictQueryTaskReceiver extends BaseReceiver{

    @PostConstruct
    @Override
    public void receive() {
        new Thread(() -> receive(TohokuDictQueryTask.class)).start();
    }

    @Override
    protected Runnable createNewTask(CrawlerMessage crawlerMessage) {
        TohokuDictQueryTask task = new TohokuDictQueryTask(crawlerMessage, tohokuComponent);
        task.setUrl(crawlerMessage.getUrl());
        task.setCurrentRetryTimes(crawlerMessage.getCurrentRetryTimes());
        task.setProxyFlag(true);
        return task;
    }
}
