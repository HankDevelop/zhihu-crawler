package com.github.wycm.common;

import com.alibaba.fastjson.JSON;
import com.github.wycm.common.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ProxyQueue {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 从队列中获取一个代理
     *
     * @param queueName
     * @return
     */
    public Proxy takeProxy(String queueName) throws InterruptedException {
        String s = null;
        Proxy currentProxy = null;
        while (s == null) {
            Optional currentEle = Optional.ofNullable(redisTemplate.opsForList().rightPop(queueName));
            if (currentEle.isPresent() && StringUtils.isNotBlank(s = currentEle.get().toString())) {
                currentProxy = JSON.parseObject(s, Proxy.class);
                if (currentProxy.getPort() == LocalIPService.DIRECT_CONNECT_PORT) {
                    //direct connect
                    if (!currentProxy.getIp().equals(LocalIPService.getLocalIp())) {
                        //not local, add to queue
                        addProxy(queueName, currentProxy);
                        s = null;
                    }
                }
            }

            Thread.sleep(100);
        }
        long serverTime = getRedisServerTime();
        String queueSetName = queueName + "-set";
        redisTemplate.opsForSet().remove(queueSetName, currentProxy.getProxyStr());
        if ((serverTime - currentProxy.getLastUseTime()) * 1000 < Constants.TIME_INTERVAL) {
            long sleepTime = Constants.TIME_INTERVAL - ((serverTime - currentProxy.getLastUseTime()) * 1000);
            logger.info("proxy failed to reach the specified delay, queueName:{}, sleep:{}", queueName, sleepTime);
            Thread.sleep(sleepTime);
        } else {
            logger.info("proxy reaches the specified delay, queueName:{}, {}ms", queueName, Constants.TIME_INTERVAL);
        }
        return currentProxy;
    }

    public void addProxy(String queueName, Proxy proxy) {
        String queueSetName = queueName + "-set";
        if (redisTemplate.opsForSet().isMember(queueSetName, proxy.getProxyStr())) {
            redisTemplate.opsForSet().add(queueSetName, proxy.getProxyStr());
        } else {
            log.info("proxy in queue {} exist, {}", queueSetName, proxy.getProxyStr());
            return;
        }
        proxy.setLastUseTime(getRedisServerTime());
        redisTemplate.opsForList().leftPush(queueName, JSON.toJSONString(proxy));
        redisTemplate.expire(queueName, Constants.REDIS_TIMEOUT, TimeUnit.SECONDS);
        redisTemplate.expire(queueSetName, Constants.REDIS_TIMEOUT, TimeUnit.SECONDS);
    }

    /**
     * 获取server时间戳
     *
     * @return
     */
    public long getRedisServerTime() {
        RedisCallback<String> callback = (connection) -> {
            return connection.eval("return redis.call('time')[1]".getBytes(), ReturnType.STATUS, 0);
        };
        return Long.valueOf(redisTemplate.execute(callback).toString());
    }
}
