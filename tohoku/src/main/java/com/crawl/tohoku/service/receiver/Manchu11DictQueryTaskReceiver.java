package com.crawl.tohoku.service.receiver;

import com.crawl.tohoku.task.Manchu11DictQueryTask;
import com.github.wycm.common.CrawlerMessage;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
@NoArgsConstructor
public class Manchu11DictQueryTaskReceiver extends BaseReceiver{

//    @PostConstruct
    @Override
    public void receive() {
       receive(Manchu11DictQueryTask.class);
    }

    @Override
    protected Runnable createNewTask(CrawlerMessage crawlerMessage) {
        Manchu11DictQueryTask task = new Manchu11DictQueryTask(crawlerMessage, tohokuComponent);
        task.setUrl(crawlerMessage.getUrl());
        task.setCurrentRetryTimes(crawlerMessage.getCurrentRetryTimes());
        task.setProxyFlag(true);
        return task;
    }
}
