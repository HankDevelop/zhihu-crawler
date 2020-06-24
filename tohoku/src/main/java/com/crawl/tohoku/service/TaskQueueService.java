package com.crawl.tohoku.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.crawl.tohoku.dao.DictQueryInfoDao;
import com.crawl.tohoku.entity.DictQueryInfo;
import com.crawl.tohoku.entity.DictQueryInfoExample;
import com.crawl.tohoku.task.TohokuProxyPageDownloadTask;
import com.github.wycm.common.CrawlerMessage;
import com.github.wycm.common.Proxy;
import com.github.wycm.common.util.CrawlerUtils;
import io.netty.handler.codec.http.HttpStatusClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Created by wycm on 2018/10/24.
 */
@Service
@Slf4j
public class TaskQueueService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
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
                log.debug(queryInfo.toString());
                return true;
            }
        }
        return sendTask(queueName, JSON.toJSONString(message), maxLength);
    }

    public boolean sendTask(String queueName, String message, int maxLength) {
        return sendTask(queueName, message, maxLength, null);
    }

    public boolean sendTask(String queueName, String message, int maxLength, Consumer<String> sendSuccConsumer) {
        redisTemplate.opsForList().leftPush(queueName, message);
        if (Objects.nonNull(sendSuccConsumer)) {
            sendSuccConsumer.accept(message);
        }
        return true;
    }

    public Long queueSize(String queueName) {
        return redisTemplate.opsForList().size(queueName);
    }

    public CrawlerMessage receiveTask(String queueName) throws InterruptedException {
        CrawlerMessage crawlerMessage = null;
        String s = "";
        ListOperations listOperations = redisTemplate.opsForList();
        while (true) {
            Optional<Object> targetQueue = Optional.ofNullable(listOperations.rightPop(queueName, 5, TimeUnit.SECONDS));
            if (targetQueue.isPresent() && StringUtils.isNotBlank(targetQueue.get().toString())) {
                s = targetQueue.get().toString();
                if (StringUtils.isNotBlank(s) && !StringUtils.equals(queueName, CrawlerUtils.getTaskQueueName(TohokuProxyPageDownloadTask.class))) {
                    crawlerMessage = JSON.parseObject(s, CrawlerMessage.class);
                    DictQueryInfoExample dictQueryInfoExample = new DictQueryInfoExample();
                    DictQueryInfoExample.Criteria criteria = dictQueryInfoExample.createCriteria();
                    criteria.andRequestUriEqualTo(crawlerMessage.getUrl());
                    criteria.andRequestInfoEqualTo(JSON.toJSONString(crawlerMessage.getMessageContext(), SerializerFeature.MapSortField));
                    Optional<DictQueryInfo> queryInfo = Optional.ofNullable(dictQueryInfoDao.selectUniqueByExample(dictQueryInfoExample));
                    if (queryInfo.isPresent()) {
                        Integer respCode = queryInfo.get().getRespCode();
                        if (Optional.ofNullable(respCode).isPresent() && HttpStatusClass.valueOf(respCode.intValue()) == HttpStatusClass.SUCCESS) {
                            continue;
                        }
                        log.info("receiveTask msg {} from queue {}", crawlerMessage, queueName);
                    } else {
                        DictQueryInfo dictQueryInfo = new DictQueryInfo();
                        dictQueryInfo.setRequestUri(crawlerMessage.getUrl());
                        dictQueryInfo.setRequestInfo(JSON.toJSONString(crawlerMessage.getMessageContext(), SerializerFeature.MapSortField));
                        dictQueryInfo.setCreateTime(new Date());
                        dictQueryInfoDao.insert(dictQueryInfo);
                    }
                }
                break;
            }
            Thread.sleep(1000);
        }
        crawlerMessage = JSON.parseObject(s, CrawlerMessage.class);
        return crawlerMessage;
    }

    public Proxy receiveProxyTask(String queueName) throws InterruptedException {
        String s = "";
        ListOperations listOperations = redisTemplate.opsForList();
        while (true) {
            Optional<Object> targetQueue = Optional.ofNullable(listOperations.rightPop(queueName, 5, TimeUnit.SECONDS));
            if (targetQueue.isPresent() && StringUtils.isNotBlank(targetQueue.get().toString())) {
                s = targetQueue.get().toString();
                break;
            }
            Thread.sleep(1000);
        }
        Proxy proxy = JSON.parseObject(s, Proxy.class);
        return proxy;
    }

}
