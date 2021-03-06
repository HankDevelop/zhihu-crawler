package com.crawl.tohoku.task;

import com.crawl.tohoku.TohokuConstants;
import com.crawl.tohoku.dao.DictQueryInfoDao;
import com.crawl.tohoku.entity.TransWordInfo;
import com.crawl.tohoku.service.TaskQueueService;
import com.crawl.tohoku.service.TohokuComponent;
import com.github.wycm.common.*;
import com.github.wycm.common.util.Constants;
import com.github.wycm.common.util.CrawlerUtils;
import com.github.wycm.common.util.ThreadPoolUtil;
import com.github.wycm.proxy.AbstractHttpClient;
import com.crawl.tohoku.support.AbstractPageTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Random;

/**
 * 根据输入keyword查询在线字典结果
 */
@Slf4j
public class Kdic33DictQueryTask extends AbstractPageTask {

    private static Logger logger = LoggerFactory.getLogger(Kdic33DictQueryTask.class);
    private String queryParams;
    private TohokuComponent tohokuComponent;

    public Kdic33DictQueryTask(CrawlerMessage crawlerMessage, TohokuComponent tohokuComponent) {
        this.crawlerMessage = crawlerMessage;
        if (crawlerMessage != null && !crawlerMessage.getMessageContext().isEmpty() && crawlerMessage.getMessageContext().containsKey("keyword")) {
            this.queryParams = crawlerMessage.getMessageContext().get("keyword").get(0);
//            crawlerMessage.getHeaders().put("Content-Type", "application/x-www-form-urlencoded");
        }
        this.url = crawlerMessage.getUrl();
        this.tohokuComponent = tohokuComponent;
        this.proxyFlag = true;
    }

    @Override
    protected void createNewTask(CrawlerMessage crawlerMessage) {
        if (Constants.stopService) {
            return;
        }
        Kdic33DictQueryTask task = new Kdic33DictQueryTask(crawlerMessage, tohokuComponent);
        ThreadPoolUtil.getThreadPool(this.getClass()).execute(task);
        log.info("create new {} success", this.getClass().getSimpleName());
    }

    @Override
    protected AbstractHttpClient getHttpClient() {
        return tohokuComponent.getTohokuHttpClient();
    }

    @Override
    protected String getProxyQueueName() {
        return tohokuComponent.getCommonProperties().getTargetPageProxyQueueName();
    }

    @Override
    protected ProxyQueue getProxyQueue() {
        return tohokuComponent.getProxyQueue();
    }

    @Override
    protected TaskQueueService getTaskQueueService() {
        return tohokuComponent.getTaskQueueService();
    }

    @Override
    protected DictQueryInfoDao getDictQueryInfoDao() {
        return tohokuComponent.getDictQueryInfoDao();
    }

    @Override
    protected LocalIPService getLocalIPService() {
        return tohokuComponent.getLocalIPService();
    }

    @Override
    public int getMaxRetryTimes() {
        return 3;
    }

    @Override
    protected void handle(Page page) {
        List<TransWordInfo> transWordInfos = tohokuComponent.getKdic33DictListParser().parseListPage(page);
        if (null == transWordInfos || transWordInfos.isEmpty()) {
//            parseWordNotFountCount.getAndIncrement();
            logger.warn("转换字{}查询解析结果为空", this.queryParams);
            return;
        }
//        logger.debug(transWordInfos.toString());
//        parseWordCount.getAndAdd(transWordInfos.size());
        /*try {
            tohokuComponent.getTohokuHttpClient().getExchangeQueue().put(dictItems);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        for (TransWordInfo transWordInfo : transWordInfos) {
            if (StringUtils.isNotBlank(transWordInfo.getRealPath())) {
                String path = TohokuConstants.DOWNLOAD_PATH + transWordInfo.getDictType() + "/" + transWordInfo.getRealPath() + ".png";
                File saveFile = new File(path);
                if (saveFile.exists()) {
                    logger.debug("词条出处图片已下载，图片地址:{}", path);
                    continue;
                } else {
                    logger.debug("下载词条出处:{} ; url: {};", transWordInfo.getDictType(), transWordInfo.getSourceUrl());
//                    parseWordDetailCount.getAndIncrement();
                    /*if (dictItem.getSourceUrl().length() > 0) {
                        tohokuHttpClient.getDetailImageThreadPool().execute(new DownloadSourceImageTask(dictItem.getSourceUrl(), dictItem.getDictType() + "/" + dictItem.getRealPath(), Config.isProxy));
                    }*/
                }
            }

        }
    }

    @Override
    public void retry() {
        if (getCurrentRetryTimes() <= getMaxRetryTimes()) {
            CrawlerMessage crawlerMessage = new CrawlerMessage(url, getCurrentRetryTimes() + 1);
            boolean isSend = true;
            do {
                isSend = getTaskQueueService().sendTask(CrawlerUtils.getTaskQueueName(Kdic33DictQueryTask.class), crawlerMessage, TohokuConstants.MAX_TASK_LENGTH);
                if (!isSend) {
                    try {
                        Thread.sleep(new Random().nextInt(1000));
                    } catch (InterruptedException e) {
                        log.error(e.getMessage());
                    }
                }
            } while (!isSend);
        } else {
            log.warn(this.getClass().getSimpleName() + "maxRetryTimes:{}, currentRetryTimes:{}", getMaxRetryTimes(), getCurrentRetryTimes());
        }
    }

}

