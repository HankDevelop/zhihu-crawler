package com.github.wycm.common;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

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

    /**
     * @param queueName
     * @param maxLength
     */
    public void sendTask(String queueName, CrawlerMessage message, int maxLength) {
        sendTask(queueName, JSON.toJSONString(message), maxLength);
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
        CrawlerMessage crawlerMessage = JSON.parseObject(s, CrawlerMessage.class);
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
