package com.crawl.tohoku.task;

import com.crawl.tohoku.TohokuConstants;
import com.crawl.tohoku.dao.DictQueryInfoDao;
import com.crawl.tohoku.service.TaskQueueService;
import com.crawl.tohoku.service.TohokuComponent;
import com.github.wycm.common.*;
import com.github.wycm.common.util.SimpleHttpClient;
import com.github.wycm.proxy.AbstractHttpClient;
import com.crawl.tohoku.support.AbstractPageTask;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 下载翻译来源图片
 */
public class DownloadSourceImageTask extends AbstractPageTask {

    private static Logger logger = LoggerFactory.getLogger(DownloadSourceImageTask.class);
    private String fileName;

    private TohokuComponent tohokuComponent;

    public DownloadSourceImageTask(String url, String fileName, boolean proxyFlag) {
        super(url, proxyFlag);
        this.fileName = fileName;
    }

    public DownloadSourceImageTask(CrawlerMessage crawlerMessage, boolean b, TohokuComponent tohokuComponent) {
        this.crawlerMessage = crawlerMessage;
        this.tohokuComponent = tohokuComponent;
    }

    @Override
    protected void createNewTask(CrawlerMessage crawlerMessage) {

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
        Document dc = Jsoup.parse(page.getHtml());
        Elements imgs = dc.select("img#dicImage");
        String realUrl = TohokuConstants.TOHOKU_START_URL + imgs.first().attr("src");
        String path = TohokuConstants.DOWNLOAD_PATH + fileName;
        if (realUrl.length() - realUrl.lastIndexOf(".") < 5) {
            path += realUrl.substring(realUrl.lastIndexOf("."));
        } else {
            path += ".jpg";
        }
        File saveFile = new File(path);

        new SimpleHttpClient().downloadFile(realUrl,
                saveFile.getParent(),
                saveFile.getName(),
                false);
    }
}
