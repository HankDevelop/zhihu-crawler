package com.crawl.tohoku.service.sender;

import com.crawl.tohoku.TohokuConstants;
import com.crawl.tohoku.task.Kdic33DictQueryTask;
import com.github.wycm.common.CrawlerMessage;
import com.crawl.tohoku.service.TaskQueueService;
import com.github.wycm.common.util.CrawlerUtils;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

@Slf4j
@Service
public class Kdic33DictQueryTaskSender implements BaseSender {
    @Autowired
    private TaskQueueService taskQueueService;

    public void send() {
        log.info("start send Kdic33DictQueryTask dict message");
        Map<String, Integer> keyMaps = new HashMap<>();
        keyMaps.put("a", 187);
        keyMaps.put("b", 131);
        keyMaps.put("c", 60);
        keyMaps.put("d", 76);
        keyMaps.put("e", 157);
        keyMaps.put("f", 55);
        keyMaps.put("g", 93);
        keyMaps.put("h", 124);
        keyMaps.put("i", 200);
        keyMaps.put("j", 56);
        keyMaps.put("k", 90);
        keyMaps.put("l", 96);
        keyMaps.put("m", 128);
        keyMaps.put("n", 153);
        keyMaps.put("o", 74);
        keyMaps.put("p", 4);
        keyMaps.put("q", 0);
        keyMaps.put("r", 194);
        keyMaps.put("s", 159);
        keyMaps.put("t", 109);
        keyMaps.put("u", 239);
        keyMaps.put("v", 89);
        keyMaps.put("w", 45);
        keyMaps.put("x", 47);
        keyMaps.put("y", 89);
        keyMaps.put("z", 2);
        //keyMaps.put("@", 257);

        /*
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
         */
        Map<String, List<String>> paramMap = new HashMap<>();
        paramMap.put("searchRange", Arrays.asList("1"));
        paramMap.put("searchMethod", Arrays.asList("4"));
        paramMap.put("groupId", Arrays.asList("33"));
        paramMap.put("pageSize", Arrays.asList("50"));
        paramMap.put("dicIds", Arrays.asList("66")); // 73,74
        boolean isSend = true;
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
                do {
                    isSend = taskQueueService.sendTask(CrawlerUtils.getTaskQueueName(Kdic33DictQueryTask.class), new CrawlerMessage(TohokuConstants.TOHOKU_KDIC_URL, paramMap), TohokuConstants.MAX_TASK_LENGTH);
                    if (!isSend) {
                        try {
                            Thread.sleep(new Random().nextInt(1000));
                        } catch (InterruptedException e) {
                            log.error(e.getMessage());
                        }
                    }
                } while (!isSend);
            }
        }
        log.info("end send Kdic33DictQueryTask message");
    }

}
