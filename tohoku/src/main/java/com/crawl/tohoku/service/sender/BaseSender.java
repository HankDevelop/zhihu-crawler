package com.crawl.tohoku.service.sender;

import com.github.wycm.common.util.RedisLockUtil;
import org.springframework.beans.factory.annotation.Autowired;

public interface BaseSender {

    abstract void send();
}
