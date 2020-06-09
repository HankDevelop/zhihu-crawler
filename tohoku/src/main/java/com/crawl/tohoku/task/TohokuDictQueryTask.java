package com.crawl.tohoku.task;

import com.crawl.tohoku.TohokuConstants;
import com.crawl.tohoku.entity.DictItem;
import com.crawl.tohoku.entity.TransWordInfo;
import com.crawl.tohoku.parser.DictContentDetailParser;
import com.crawl.tohoku.service.TohokuComponent;
import com.github.wycm.common.*;
import com.github.wycm.common.parser.ListPageParser;
import com.github.wycm.common.util.Constants;
import com.github.wycm.common.util.CrawlerUtils;
import com.github.wycm.common.util.ThreadPoolUtil;
import com.github.wycm.proxy.AbstractHttpClient;
import com.github.wycm.proxy.AbstractPageTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import sun.util.resources.cldr.rw.CalendarData_rw_RW;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

import static com.crawl.tohoku.TohokuHttpClient.*;

/**
 * 根据输入keyword查询在线字典结果
 */
@Slf4j
public class TohokuDictQueryTask extends AbstractPageTask {

    private static Logger logger = LoggerFactory.getLogger(TohokuDictQueryTask.class);

    @Resource(name = "dictContentDetailParser")
    private ListPageParser listPageParser;
    private String queryParams;
    @Autowired
    private TohokuComponent tohokuComponent;

    public TohokuDictQueryTask(CrawlerMessage crawlerMessage) {
        this.crawlerMessage = crawlerMessage;
        if (crawlerMessage != null && !crawlerMessage.getMessageContext().isEmpty() && crawlerMessage.getMessageContext().containsKey("keyword")) {
            this.queryParams = crawlerMessage.getMessageContext().get("keyword").get(0);
        }
        this.proxyFlag = true;
//        request.addHeader("Content-Type", "application/x-www-form-urlencoded");
    }

    @Override
    protected void createNewTask(CrawlerMessage crawlerMessage) {
        if (Constants.stopService){
            return;
        }
        TohokuDictQueryTask task = new TohokuDictQueryTask(crawlerMessage);
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
    protected LocalIPService getLocalIPService() {
        return tohokuComponent.getLocalIPService();
    }

    @Override
    public int getMaxRetryTimes() {
        return 3;
    }

    @Override
    protected void handle(Page page) {
        List<TransWordInfo> dictItems = listPageParser.parseListPage(page);
        if (null == dictItems || dictItems.isEmpty()) {
            parseWordNotFountCount.getAndIncrement();
            logger.warn("转换字{}查询解析结果为空", this.queryParams);
            return;
        }
        logger.debug(dictItems.toString());
        parseWordCount.getAndAdd(dictItems.size());
        try {
            tohokuComponent.getTohokuHttpClient().getExchangeQueue().put(dictItems);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (TransWordInfo dictItem : dictItems) {
            if (StringUtils.isNotBlank(dictItem.getRealPath())) {
                String path = TohokuConstants.DOWNLOAD_PATH + dictItem.getDictType()+ "/"+dictItem.getRealPath()+".png";
                File saveFile = new File(path);
                if(saveFile.exists()){
                    logger.debug("词条出处图片已下载，图片地址:{}", path);
                    continue;
                } else {
                    logger.debug("下载词条出处:{} ; url: {};", dictItem.getDictType(), dictItem.getSourceUrl());
                    parseWordDetailCount.getAndIncrement();
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
            getTaskQueueService().sendTask(CrawlerUtils.getTaskQueueName(TohokuDictQueryTask.class), crawlerMessage, 100000);
        } else {
            log.warn(this.getClass().getSimpleName() + "maxRetryTimes:{}, currentRetryTimes:{}", getMaxRetryTimes(), getCurrentRetryTimes());
        }
    }
}

