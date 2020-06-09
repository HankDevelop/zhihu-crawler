package com.crawl.tohoku.service;

import com.crawl.tohoku.TohokuHttpClient;
import com.crawl.tohoku.parser.DictContentDetailParser;
import com.github.wycm.common.CommonProperties;
import com.github.wycm.common.LocalIPService;
import com.github.wycm.common.ProxyQueue;
import com.github.wycm.common.TaskQueueService;
import com.github.wycm.common.util.RedisLockUtil;
import com.github.wycm.proxy.ProxyHttpClient;
import com.github.wycm.proxy.ProxyPageProxyPool;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

@Component
@Data
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
    private DictContentDetailParser dictContentDetailParser;

    @Autowired
    private LocalIPService localIPService;

    @Autowired
    private CommonProperties commonProperties;

}
