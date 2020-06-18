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
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
    public boolean sendTask(String queueName, CrawlerMessage message, int maxLength) {
        DictQueryInfoExample dictQueryInfoExample = new DictQueryInfoExample();
        DictQueryInfoExample.Criteria criteria = dictQueryInfoExample.createCriteria();
        criteria.andRequestUriEqualTo(message.getUrl());
        criteria.andRequestInfoEqualTo(JSON.toJSONString(message.getMessageContext(), SerializerFeature.MapSortField));
        Optional<DictQueryInfo> queryInfo = Optional.ofNullable(dictQueryInfoDao.selectUniqueByExample(dictQueryInfoExample));
        if (queryInfo.isPresent()) {
            Optional<Integer> respCode = Optional.ofNullable(queryInfo.get().getRespCode());
            if (respCode.isPresent() && HttpStatus.SC_OK == respCode.get()) {
                log.info(queryInfo.toString());
            }
        } else {
            return sendTask(queueName, JSON.toJSONString(message), maxLength);
        }
        return true;
    }

    public boolean sendTask(String queueName, String message, int maxLength) {
        return sendTask(queueName, message, maxLength, null);
    }

    public boolean sendTask(String queueName, String message, int maxLength, Consumer<String> sendSuccConsumer) {
        Jedis jedis = jedisPool.getResource();
        try {
            if (jedis.llen(queueName) < maxLength) {
                jedis.lpush(queueName, message);
                if (Objects.nonNull(sendSuccConsumer)) {
                    sendSuccConsumer.accept(message);
                }
            } else {
                log.debug("queue is full, queueName:{}", queueName);
                return false;
            }
        } finally {
            jedis.close();
        }
        return true;
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
                    Optional<DictQueryInfo> queryInfo = Optional.ofNullable(dictQueryInfoDao.selectUniqueByExample(dictQueryInfoExample));
                    if (queryInfo.isPresent() && HttpStatus.SC_OK != queryInfo.get().getRespCode()) {
                        continue;
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
