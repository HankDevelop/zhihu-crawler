package com.crawl.tohoku.service;

import com.crawl.tohoku.TohokuHttpClient;
import com.crawl.tohoku.dao.TransWordInfoDao;
import com.crawl.tohoku.parser.DictContentListParser;
import com.crawl.tohoku.parser.Kdic33DictListParser;
import com.crawl.tohoku.parser.Manchu11DictListParser;
import com.crawl.tohoku.task.*;
import com.github.wycm.common.CommonProperties;
import com.github.wycm.common.LocalIPService;
import com.github.wycm.common.ProxyQueue;
import com.github.wycm.common.TaskQueueService;
import com.github.wycm.common.util.RedisLockUtil;
import com.github.wycm.common.util.SystemUtil;
import com.github.wycm.common.util.ThreadPoolUtil;
import com.github.wycm.proxy.ProxyHttpClient;
import com.github.wycm.proxy.ProxyPageProxyPool;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;

@Data
@Component
public class TohokuComponent {

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private ProxyQueue proxyQueue;

    @Autowired
    private ProxyHttpClient proxyHttpClient;

    @Autowired
    private TohokuHttpClient tohokuHttpClient;

    @Autowired
    private TaskQueueService taskQueueService;

    @Autowired
    private RedisLockUtil redisLockUtil;

    @Autowired
    private ProxyPageProxyPool proxyPageProxyPool;

    @Autowired
    private Manchu11DictListParser manchu11DictListParser;

    @Autowired
    private Kdic33DictListParser kdic33DictListParser;

    @Autowired
    private TransWordInfoDao transWordInfoDao;

    @Autowired
    private LocalIPService localIPService;

    @Autowired
    private CommonProperties commonProperties;

    @PostConstruct
    public static void initThreadPool() {
        ThreadPoolUtil.createThreadPool(Kdic33DictQueryTask.class, SystemUtil.getRecommendThreadSize());
        ThreadPoolUtil.createThreadPool(DownloadSourceImageTask.class, SystemUtil.getRecommendThreadSize() / 2);
        ThreadPoolUtil.createThreadPool(TohokuProxyPageProxyTestTask.class, SystemUtil.getRecommendThreadSize() / 2);
        ThreadPoolUtil.createThreadPool(TohokuPageProxyTestTask.class, SystemUtil.getRecommendThreadSize() / 2);
        ThreadPoolUtil.createThreadPool(TohokuProxyPageDownloadTask.class, SystemUtil.getRecommendThreadSize() / 4);
    }

}
