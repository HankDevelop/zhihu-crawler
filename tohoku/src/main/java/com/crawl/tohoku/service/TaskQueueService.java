package com.crawl.tohoku.service;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.crawl.tohoku.dao.DictQueryInfoDao;
import com.crawl.tohoku.entity.DictQueryInfo;
import com.crawl.tohoku.entity.DictQueryInfoExample;
import com.crawl.tohoku.entity.TransWordInfoExample;
import com.crawl.tohoku.task.TohokuProxyPageDownloadTask;
import com.github.wycm.common.CrawlerMessage;
import com.github.wycm.common.Proxy;
import com.github.wycm.common.util.CrawlerUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Created by wycm on 2018/10/24.
 */
@Service
@Slf4j
public class TaskQueueService {

    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private DictQueryInfoDao dictQueryInfoDao;

    /**
     * @param queueName
     * @param maxLength
     */
    public void sendTask(String queueName, CrawlerMessage message, int maxLength) {
        DictQueryInfoExample dictQueryInfoExample = new DictQueryInfoExample();
        DictQueryInfoExample.Criteria criteria = dictQueryInfoExample.createCriteria();
        criteria.andRequestUriEqualTo(message.getUrl());
        criteria.andRequestInfoEqualTo(JSON.toJSONString(message.getMessageContext(), SerializerFeature.MapSortField));
        List<DictQueryInfo> dictQueryInfos = dictQueryInfoDao.selectByExample(dictQueryInfoExample);
        if (dictQueryInfos == null || dictQueryInfos.isEmpty()) {
            sendTask(queueName, JSON.toJSONString(message), maxLength);
        }
    }

    public void sendTask(String queueName, String message, int maxLength) {
        sendTask(queueName, message, maxLength, null);
    }

    public void sendTask(String queueName, String message, int maxLength, Consumer<String> sendSuccConsumer) {
        Jedis jedis = jedisPool.getResource();
        try {
            if (jedis.llen(queueName) < maxLength) {
                jedis.lpush(queueName, message);
                if (Objects.nonNull(sendSuccConsumer)) {
                    sendSuccConsumer.accept(message);
                }
            } else {
                log.debug("queue is full, queueName:{}", queueName);
            }
        } finally {
            jedis.close();
        }
    }

    public Long queueSize(String queueName) {
        Long s = 0L;
        Jedis jedis = jedisPool.getResource();
        try {
            s = jedis.llen(queueName);
        } finally {
            jedis.close();
        }
        return s;
    }

    public CrawlerMessage receiveTask(String queueName) throws InterruptedException {
        CrawlerMessage crawlerMessage = null;
        String s = "";
        while (true) {
            Jedis jedis = jedisPool.getResource();
            try {
                s = jedis.rpop(queueName);
                if (StringUtils.isNotBlank(s) && !StringUtils.equals(queueName, CrawlerUtils.getTaskQueueName(TohokuProxyPageDownloadTask.class))) {
                    crawlerMessage = JSON.parseObject(s, CrawlerMessage.class);
                    DictQueryInfoExample dictQueryInfoExample = new DictQueryInfoExample();
                    DictQueryInfoExample.Criteria criteria = dictQueryInfoExample.createCriteria();
                    criteria.andRequestUriEqualTo(crawlerMessage.getUrl());
                    criteria.andRequestInfoEqualTo(JSON.toJSONString(crawlerMessage.getMessageContext(), SerializerFeature.MapSortField));
                    List<DictQueryInfo> dictQueryInfos = dictQueryInfoDao.selectByExample(dictQueryInfoExample);
                    if (dictQueryInfos != null && !dictQueryInfos.isEmpty()) {
                        DictQueryInfo dictQueryInfo = dictQueryInfos.get(0);
                        if (dictQueryInfo.getRespCode() != null && dictQueryInfo.getRespCode() == HttpStatus.SC_OK) {
                            continue;
                        }
                    } else {
                        DictQueryInfo dictQueryInfo = new DictQueryInfo();
                        dictQueryInfo.setRequestUri(crawlerMessage.getUrl());
                        dictQueryInfo.setRequestInfo(JSON.toJSONString(crawlerMessage.getMessageContext(), SerializerFeature.MapSortField));
                        dictQueryInfo.setCreateTime(new Date());
                        dictQueryInfoDao.insert(dictQueryInfo);
                    }
                }
            } finally {
                jedis.close();
            }
            if (StringUtils.isNotBlank(s)) {
                break;
            }
            Thread.sleep(1000);
        }
        crawlerMessage = JSON.parseObject(s, CrawlerMessage.class);
        return crawlerMessage;
    }

    public Proxy receiveProxyTask(String queueName) throws InterruptedException {
        String s = "";
        while (true) {
            Jedis jedis = jedisPool.getResource();
            try {
                s = jedis.rpop(queueName);
            } finally {
                jedis.close();
            }
            if (StringUtils.isNotBlank(s)) {
                break;
            }
            Thread.sleep(1000);
        }
        Proxy proxy = JSON.parseObject(s, Proxy.class);
        return proxy;
    }

}
