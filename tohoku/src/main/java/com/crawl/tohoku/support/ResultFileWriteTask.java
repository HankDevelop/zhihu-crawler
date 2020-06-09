package com.crawl.tohoku.support;

import com.crawl.tohoku.TohokuConstants;
import com.crawl.tohoku.entity.DictItem;
import com.crawl.tohoku.entity.TransWordInfo;
import com.crawl.tohoku.service.TohokuComponent;
import com.github.wycm.common.util.ThreadPoolMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

@Component
public class ResultFileWriteTask extends Thread{

    private static Logger logger = LoggerFactory.getLogger(ResultFileWriteTask.class);
    public final static int USER_TASK_DELAY = 1000 * 60 * 10;
    private BufferedWriter bw;

    @Value("trans_ManHan_06_06.sql")
    private String fileName;

    @Autowired
    private TohokuComponent tohokuComponent;

    @Override
    @Scheduled(initialDelay = 5000, fixedDelay = USER_TASK_DELAY)
    public void run() {
        BlockingQueue<List<TransWordInfo>> exchangeQueue = tohokuComponent.getTohokuHttpClient().getExchangeQueue();

        System.out.println("开始序列化数据 " + fileName);

        File outputFile = new File(TohokuConstants.DOWNLOAD_PATH + fileName);
        if (!outputFile.exists()) {
            try {
                outputFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileOutputStream out = null;
        OutputStreamWriter writer = null;

        try {
            out = new FileOutputStream(outputFile);
            writer = new OutputStreamWriter(out);
            bw = new BufferedWriter(writer);

            StringBuilder sb = new StringBuilder();
            String preSql = "insert into trans_word_info(trans_word, translation, dict_type, source_url, real_path) values('";
            String writeStr = "";
            while (true) {
                try {
                    Thread.sleep(50);
                    if (ThreadPoolMonitor.isStopMonitor == true && exchangeQueue.isEmpty()) {
                        break;
                    }
                    if(exchangeQueue.isEmpty()){
                        Thread.sleep(new Random().nextInt(1000));
                        if(exchangeQueue.isEmpty()){
                            continue;
                        }
                    }
                    List<TransWordInfo> list = tohokuComponent.getTohokuHttpClient().getExchangeQueue().take();
                    for (TransWordInfo dictItem : list) {
//                        if(StringUtils.isAnyBlank(dictItem.getDictType(), dictItem.getRealPath(), dictItem.getSourceUrl())){
//                            logger.error(dictItem.toString());
//                            continue;
//                        }
                        writeStr = sb.append(preSql).append(dictItem.getTransWord().trim().replaceAll("\'", "\\\\'")).append("','")
                                .append(dictItem.getTranslation().trim().replaceAll("\'", "\\\\'")).append("','")
                                .append(dictItem.getDictType().trim()).append("','")
                                .append(dictItem.getSourceUrl().trim()).append("','")
                                .append(dictItem.getRealPath().trim()).append("');")
                                .toString();
                        write2Disk(writeStr);
                        sb = new StringBuilder();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void write2Disk(String str) {
        if (null != bw) {
            try {
                bw.write(str);
                bw.newLine();
                bw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
