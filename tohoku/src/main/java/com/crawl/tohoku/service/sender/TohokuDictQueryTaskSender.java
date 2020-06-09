package com.crawl.tohoku.service.sender;

import com.crawl.tohoku.TohokuConstants;
import com.crawl.tohoku.support.ResultFileWriteTask;
import com.crawl.tohoku.task.TohokuDictQueryTask;
import com.github.wycm.common.CrawlerMessage;
import com.github.wycm.common.TaskQueueService;
import com.github.wycm.common.util.CrawlerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

@Service
@Slf4j
public class TohokuDictQueryTaskSender extends BaseSender {
    public final static int USER_TASK_DELAY = 1000 * 60 * 10;
    @Autowired
    private TaskQueueService taskQueueService;
    @Autowired
    private ResultFileWriteTask fileWriteTask;

    private Map<String, Integer> keyMaps = new HashMap<>();

    {
//        keyMaps.put("a", 1119);
//        keyMaps.put("b", 870);
//        keyMaps.put("c", 335);
//        keyMaps.put("d", 459);
//        keyMaps.put("e", 934);
//        keyMaps.put("f", 306);
//        keyMaps.put("g", 586);
//        keyMaps.put("h", 657);
//        keyMaps.put("i", 1263);
//        keyMaps.put("j", 327);
//        keyMaps.put("k", 507);
//        keyMaps.put("l", 600);
//        keyMaps.put("m", 860);
//        keyMaps.put("n", 1003);
//        keyMaps.put("o", 444);
//        keyMaps.put("p", 18);
//        keyMaps.put("q", 0);
//        keyMaps.put("r", 665);
//        keyMaps.put("s", 526);
//        keyMaps.put("t", 440);
//        keyMaps.put("u", 931);
//        keyMaps.put("v", 0);
//        keyMaps.put("w", 177);
//        keyMaps.put("x", 0);
//        keyMaps.put("y", 369);
//        keyMaps.put("z", 6);
//        keyMaps.put("@", 255);

        keyMaps.put("a", 1133);
        keyMaps.put("b", 874);
        keyMaps.put("c", 360);
        keyMaps.put("d", 462);
        keyMaps.put("e", 942);
        keyMaps.put("f", 308);
        keyMaps.put("g", 646);
        keyMaps.put("h", 663);
        keyMaps.put("i", 1278);
        keyMaps.put("j", 330);
        keyMaps.put("k", 514);
        keyMaps.put("l", 607);
        keyMaps.put("m", 864);
        keyMaps.put("n", 1018);
        keyMaps.put("o", 451);
        keyMaps.put("p", 18);
        keyMaps.put("q", 0);
        keyMaps.put("r", 677);
        keyMaps.put("s", 665);
        keyMaps.put("t", 447);
        keyMaps.put("u", 944);
        keyMaps.put("v", 0);
        keyMaps.put("w", 179);
        keyMaps.put("x", 0);
        keyMaps.put("y", 373);
        keyMaps.put("z", 6);
        keyMaps.put("@", 257);
    }


    //每天01:01:10时执行，抓取
    @Scheduled(initialDelay = 5000, fixedDelay = USER_TASK_DELAY)
    @Override
    public void send() {
        newSingleThreadExecutor().submit(() -> {

            log.info("start send query dict message");
            Map<String, List<String>> paramMap = new HashMap<>();
            paramMap.put("searchRange", Arrays.asList("1"));
            paramMap.put("searchMethod", Arrays.asList("4"));
            paramMap.put("groupId", Arrays.asList("33"));
            paramMap.put("pageSize", Arrays.asList("50"));
            paramMap.put("dicIds", Arrays.asList("73,74"));
            for (String keyWord : keyMaps.keySet()) {
                int keyRows = keyMaps.get(keyWord).intValue();
                List<Integer> list = new ArrayList();
                for (int i = 1; i <= keyRows; i++) {
                    list.add(i);
                }
                Collections.shuffle(list);

                Iterator ite = list.iterator();
                while (ite.hasNext()) {
                    String currentPage = ite.next().toString();
                    paramMap.put("currentPage", Arrays.asList(currentPage));
                    paramMap.put("keyword", Arrays.asList(keyWord));
                    taskQueueService.sendTask(CrawlerUtils.getTaskQueueName(TohokuDictQueryTask.class), new CrawlerMessage(TohokuConstants.TOHOKU_DICT_URL, paramMap), 10);

                    /*params.put("currentPage", currentPage);
                    params.put("keyword", keyWord);
                    HttpPost postRequest = new HttpPost(startUrl);
                    HttpClientUtil.setHttpPostParams(postRequest, params);
                    // 防止写文件数据阻塞，导致生产线程阻塞
                    if (listPageThreadPool.getActiveCount() > 40 || listPageThreadPool.getQueue().size() > 150) {
                        try {
                            Thread.sleep(new Random().nextInt(10) * 4000);
                        } catch (InterruptedException e) {
                            logger.error("主线程sleep出现错误：", e);
                        }
                    }
                    logger.info("查询参数：{}", params.toString());
                    listPageThreadPool.execute(new TohokuDictQueryTask(postRequest, keyWord + "_" + currentPage, Config.isProxy));
                    try {
                        Thread.sleep(new Random().nextInt(5) * 1000);
                    } catch (InterruptedException e) {
                        logger.error("主线程sleep出现错误：", e);
                    }*/
                }
//                rows += keyRows;
//                logger.info("以{}为关键字，每页50行，共查询{}页", keyWord, keyRows);
            }
//            String startUrl = TohokuConstants.TOHOKU_DICT_URL;
////            String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36";
//            taskQueueService.sendTask(CrawlerUtils.getTaskQueueName(TohokuDictQueryTask.class), new CrawlerMessage(startUrl, paramMap), 100000);
//            log.info("end send ZhihuUser message, sendSize:{}", sendSize.get());
        });

    }


}
