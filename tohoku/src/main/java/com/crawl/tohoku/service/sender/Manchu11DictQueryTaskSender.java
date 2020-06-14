package com.crawl.tohoku.service.sender;

import com.crawl.tohoku.TohokuConstants;
import com.crawl.tohoku.task.Kdic33DictQueryTask;
import com.github.wycm.common.CrawlerMessage;
import com.github.wycm.common.ShutdownService;
import com.github.wycm.common.TaskQueueService;
import com.github.wycm.common.util.CrawlerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

@Slf4j
@Service
public class Manchu11DictQueryTaskSender extends BaseSender{
    @Autowired
    private TaskQueueService taskQueueService;
    @Autowired
    private ShutdownService shutdownService;

    private Map<String, Integer> keyMaps = new HashMap<>();

    {
        keyMaps.put("a", 1119);
        keyMaps.put("b", 870);
        keyMaps.put("c", 335);
        keyMaps.put("d", 459);
        keyMaps.put("e", 934);
        keyMaps.put("f", 306);
        keyMaps.put("g", 586);
        keyMaps.put("h", 657);
        keyMaps.put("i", 1263);
        keyMaps.put("j", 327);
        keyMaps.put("k", 507);
        keyMaps.put("l", 600);
        keyMaps.put("m", 860);
        keyMaps.put("n", 1003);
        keyMaps.put("o", 444);
        keyMaps.put("p", 18);
        keyMaps.put("q", 0);
        keyMaps.put("r", 665);
        keyMaps.put("s", 526);
        keyMaps.put("t", 440);
        keyMaps.put("u", 931);
        keyMaps.put("v", 0);
        keyMaps.put("w", 177);
        keyMaps.put("x", 0);
        keyMaps.put("y", 369);
        keyMaps.put("z", 6);
        keyMaps.put("@", 255);

    }

    @Override
//    @Scheduled(initialDelay = 5000, fixedDelay = 1000 * 60 * 60)
    public void send() {
        log.info("start send Manchu11DictQueryTask query message");
        newSingleThreadExecutor().submit(() -> {

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
                    taskQueueService.sendTask(CrawlerUtils.getTaskQueueName(Kdic33DictQueryTask.class), new CrawlerMessage(TohokuConstants.TOHOKU_KDIC_URL, paramMap), TohokuConstants.MAX_TASK_LENGTH);

                }
            }
        });
        log.info("end send Manchu11DictQueryTask query message");
    }

}
