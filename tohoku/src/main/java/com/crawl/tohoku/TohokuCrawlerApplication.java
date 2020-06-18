package com.crawl.tohoku;

import com.crawl.tohoku.support.ResultFileWriteTask;
import com.crawl.tohoku.task.*;
import com.github.wycm.common.util.SystemUtil;
import com.github.wycm.common.util.ThreadPoolUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableScheduling
@ComponentScan({"com.github.wycm", "com.crawl.tohoku"})
@EnableAspectJAutoProxy   //开启aop
@EnableTransactionManagement()  //开启spring事务管理
@PropertySource(value = "classpath:/prop/jdbc.properties", encoding = "utf-8")
@MapperScan("com.crawl.tohoku.dao")
@SpringBootApplication
public class TohokuCrawlerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TohokuCrawlerApplication.class, args);
	}

}

