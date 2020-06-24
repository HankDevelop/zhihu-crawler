package com.crawl.tohoku;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableScheduling
@ComponentScan({"com.github.wycm", "com.crawl.tohoku"})
//@EnableAspectJAutoProxy   //开启aop
@EnableAsync(proxyTargetClass=true)
@EnableTransactionManagement  //开启spring事务管理
@PropertySource(value = "classpath:/prop/jdbc.properties", encoding = "utf-8")
@MapperScan("com.crawl.tohoku.dao")
@SpringBootApplication
public class TohokuCrawlerApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(TohokuCrawlerApplication.class);
		application.setBannerMode(Banner.Mode.OFF);
		application.run(args);
	}

}

