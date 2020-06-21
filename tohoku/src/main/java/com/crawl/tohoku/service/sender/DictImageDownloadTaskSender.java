package com.crawl.tohoku.service.sender;

import com.crawl.tohoku.TohokuConstants;
import com.crawl.tohoku.entity.TransWordInfo;
import com.crawl.tohoku.service.TaskQueueService;
import com.crawl.tohoku.service.TohokuComponent;
import com.crawl.tohoku.task.DictImageDownloadTask;
import com.github.wycm.common.CrawlerMessage;
import com.github.wycm.common.util.CrawlerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author Hank
 * @Date 2020-11-09 23:23
 */
@Slf4j
@Service
public class DictImageDownloadTaskSender implements BaseSender {

    @Autowired
    private TaskQueueService taskQueueService;
    @Autowired
    TohokuComponent tohokuComponent;

    @Override

    public void send() {
        log.info("start send DictImageDownloadTask dict message");
        List<String> imgList = tohokuComponent.getTransWordInfoDao().selectImageSource("66");
        AtomicBoolean isSend = new AtomicBoolean(true);
        if (!CollectionUtils.isEmpty(imgList)) {
            taskQueueService.clearQueue(CrawlerUtils.getTaskQueueName(DictImageDownloadTask.class));
            imgList.stream().forEach(realPath -> {
                        do {
                            isSend.set(taskQueueService.sendTask(CrawlerUtils.getTaskQueueName(DictImageDownloadTask.class), new CrawlerMessage(realPath), TohokuConstants.MAX_TASK_LENGTH));
                            if (!isSend.get()) {
                                try {
                                    Thread.sleep(new Random().nextInt(1000));
                                } catch (InterruptedException e) {
                                    log.error(e.getMessage());
                                }
                            }
                        } while (!isSend.get());
                    }

            );
        }
        log.info("end send DictImageDownloadTask message");
    }
}
