package com.crawl.tohoku.task;

import com.crawl.tohoku.TohokuConstants;
import com.crawl.tohoku.service.TohokuComponent;
import com.github.wycm.common.Page;
import com.github.wycm.common.Proxy;
import com.github.wycm.common.ProxyQueue;
import com.github.wycm.common.TaskQueueService;
import com.github.wycm.common.util.Constants;
import com.github.wycm.common.util.ThreadPoolUtil;
import com.github.wycm.proxy.AbstractHttpClient;
import com.github.wycm.proxy.AbstractPageProxyTestTask;
import com.crawl.tohoku.support.AbstractPageTask;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 代理检测task
 * 通过访问头条首页，能否正确响应
 * 将可用代理添加到DelayQueue延时队列中
 */
@Slf4j
@NoArgsConstructor
public class TohokuPageProxyTestTask extends AbstractPageProxyTestTask {
    private TohokuComponent tohokuComponent;

    public TohokuPageProxyTestTask(Proxy proxy, String url, TohokuComponent tohokuComponent) {
        super(proxy, url);
        this.tohokuComponent = tohokuComponent;
    }

    @Override
    protected String getTestUrl() {
        return TohokuConstants.TOHOKU_START_URL;
    }

    @Override
    protected String getProxyQueueName() {
        return tohokuComponent.getCommonProperties().getTargetPageProxyQueueName();
    }

    @Override
    protected AbstractHttpClient getHttpClient() {
        return tohokuComponent.getTohokuHttpClient();
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
    protected void createNewTask(Proxy proxy, String url) {
        if (Constants.stopService){
            return;
        }
        TohokuPageProxyTestTask task = new TohokuPageProxyTestTask(proxy, url, tohokuComponent);
        ThreadPoolUtil.getThreadPool(this.getClass()).execute(task);
        log.info("create new {} success", this.getClass().getSimpleName());
    }

    @Override
    protected boolean responseError(Page page){
        boolean flag = page == null || page.getStatusCode() != 200 || AbstractPageTask.getHtmlPageBannedFunction().apply(page.getHtml());
        if (flag){
            log.warn("tohoku proxy test, ip banned, url:{}, response:{}, discard ip:{}, port:{}", page.getUrl(), page.getHtml(), proxy.getIp(), proxy.getPort());
        }
         return flag;
    }
}
