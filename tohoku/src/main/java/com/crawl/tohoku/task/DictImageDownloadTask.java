package com.crawl.tohoku.task;

import com.crawl.tohoku.TohokuConstants;
import com.crawl.tohoku.dao.DictQueryInfoDao;
import com.crawl.tohoku.service.TaskQueueService;
import com.crawl.tohoku.service.TohokuComponent;
import com.crawl.tohoku.support.AbstractPageTask;
import com.github.wycm.common.CrawlerMessage;
import com.github.wycm.common.LocalIPService;
import com.github.wycm.common.Page;
import com.github.wycm.common.ProxyQueue;
import com.github.wycm.common.util.Constants;
import com.github.wycm.common.util.CrawlerUtils;
import com.github.wycm.common.util.SimpleHttpClient;
import com.github.wycm.common.util.ThreadPoolUtil;
import com.github.wycm.proxy.AbstractHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 下载翻译来源图片
 */
@Slf4j
public class DictImageDownloadTask extends AbstractPageTask {

    private TohokuComponent tohokuComponent;

    public DictImageDownloadTask(CrawlerMessage crawlerMessage, TohokuComponent tohokuComponent) {
        this.crawlerMessage = crawlerMessage;
        this.url = crawlerMessage.getUrl();
        this.setProxyFlag(false);
        this.tohokuComponent = tohokuComponent;
    }

    @Override
    protected void createNewTask(CrawlerMessage crawlerMessage) {
        if (Constants.stopService) {
            return;
        }
        DictImageDownloadTask task = new DictImageDownloadTask(crawlerMessage, tohokuComponent);
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
    protected void handle(Page page) throws FileNotFoundException {
        Document dc = Jsoup.parse(page.getHtml());
        Elements imgs = dc.select("img#dicImage");
        String realUrl = TohokuConstants.TOHOKU_START_URL + imgs.first().attr("src");
        String imgPath = TohokuConstants.DOWNLOAD_PATH + CrawlerUtils.getUrlPramNameAndValue(realUrl).get("imagePath");
        File saveFile = new File(imgPath);

        if (!saveFile.exists()) {
            tohokuComponent.getRestTemplate().execute(realUrl, HttpMethod.GET, null, clientHttpResponse -> {
                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(saveFile);
                    StreamUtils.copy(clientHttpResponse.getBody(), outputStream);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                } finally {
                    IOUtils.closeQuietly(outputStream);
                }
                return null;
            });
        }
    }
}
