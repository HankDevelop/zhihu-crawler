package com.crawl.tohoku.task;

import com.crawl.tohoku.TohokuConstants;
import com.crawl.tohoku.service.TohokuComponent;
import com.github.wycm.common.Proxy;
import com.github.wycm.common.ProxyQueue;
import com.github.wycm.common.TaskQueueService;
import com.github.wycm.common.util.Constants;
import com.github.wycm.common.util.ThreadPoolUtil;
import com.github.wycm.proxy.AbstractHttpClient;
import com.github.wycm.proxy.AbstractPageProxyTestTask;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 代理检测task
 * 通过访问首页，能否正确响应
 * 将可用代理添加到ProxyDelayQueue延时队列中
 */
@Slf4j
@NoArgsConstructor
public class TohokuProxyPageProxyTestTask extends AbstractPageProxyTestTask {
    private TohokuComponent tohokuComponent;

    public TohokuProxyPageProxyTestTask(Proxy proxy, String url, TohokuComponent tohokuComponent) {
        super(proxy, url);
        this.tohokuComponent = tohokuComponent;
    }

    @Override
    protected String getTestUrl() {
        return TohokuConstants.TOHOKU_START_URL;
    }

    @Override
    protected String getProxyQueueName() {
        return tohokuComponent.getCommonProperties().getProxyPageProxyQueueName();
    }

    @Override
    protected void createNewTask(Proxy proxy, String url) {
        if (Constants.stopService){
            return;
        }
        TohokuProxyPageProxyTestTask task = new TohokuProxyPageProxyTestTask(proxy, url, tohokuComponent);
        ThreadPoolUtil.getThreadPool(this.getClass()).execute(task);
        log.info("create new {} success", this.getClass().getSimpleName());
    }

    @Override
    protected AbstractHttpClient getHttpClient() {
        return tohokuComponent.getProxyHttpClient();
    }

    @Override
    protected ProxyQueue getProxyQueue() {
        return tohokuComponent.getProxyQueue();
    }

    @Override
    protected TaskQueueService getTaskQueueService() {
        return tohokuComponent.getTaskQueueService();
    }
}
