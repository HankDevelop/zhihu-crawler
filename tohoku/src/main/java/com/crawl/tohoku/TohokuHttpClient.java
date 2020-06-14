package com.crawl.tohoku;

import com.crawl.tohoku.entity.TransWordInfo;
import com.github.wycm.common.LocalIPService;
import com.github.wycm.common.util.SimpleHttpClient;
import com.github.wycm.proxy.AbstractHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 用户抓取HttpClient
 */
@Slf4j
@Component
public class TohokuHttpClient extends AbstractHttpClient {

    @Autowired
    private LocalIPService localIPService;

    public TohokuHttpClient(){
        super();
        super.httpClient = new SimpleHttpClient(new SimpleHttpClient.IgnoreCookieStore(), 500, 2000);
    }

    @Override
    public LocalIPService getLocalIPService() {
        return localIPService;
    }
   /* public static AtomicInteger parseWordCount = new AtomicInteger(0);
    public static AtomicInteger parseWordDetailCount = new AtomicInteger(0);
    public static AtomicInteger parseWordNotFountCount = new AtomicInteger(0);
    private BlockingQueue<List<TransWordInfo>> exchangeQueue = new ArrayBlockingQueue<>(100);

    public BlockingQueue<List<TransWordInfo>> getExchangeQueue() {
        return exchangeQueue;
    }*/
    /*
    private static long startTime = System.currentTimeMillis();
    public static volatile boolean isStop = false;

    public static TohokuHttpClient getInstance() {
        if (instance == null) {
            synchronized (TohokuHttpClient.class) {
                if (instance == null) {
                    instance = new TohokuHttpClient();
                }
            }
        }
        return instance;
    }

    *//**
     * 列表页下载线程池
     *//*
    private ThreadPoolExecutor listPageThreadPool;
    *//**
     * 来源文件图片下载线程池
     *//*
    private ThreadPoolExecutor detailImageThreadPool;

    private TohokuHttpClient() {
    }

    *//**
     * 初始化HttpClient
     *//*
    @Override
    public void initHttpClient() {
        if (Config.dbEnable) {
            ZhiHuDao1Imp.DBTablesInit();
        }
    }

    *//**
     * 初始化线程池
     *//*
    private void intiThreadPool() {
        listPageThreadPool = new SimpleThreadPoolExecutor(20, 30,
                0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(200),
                new ThreadPoolExecutor.DiscardPolicy(), "listPageThreadPool");
        new Thread(new ThreadPoolMonitor(listPageThreadPool, "ListPageDownloadThreadPool")).start();
        detailImageThreadPool = new SimpleThreadPoolExecutor(Config.downloadThreadSize,
                Config.downloadThreadSize,
                0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(500),
                new ThreadPoolExecutor.DiscardPolicy(),
                "detailImageThreadPool");
        new Thread(new ThreadPoolMonitor(detailImageThreadPool, "DetailImageThreadPool")).start();
    }

    @Override
    public void startCrawl() {
        initHttpClient();
        intiThreadPool();

        new ResultFileWriteTask("trans_ManHan.sql").start();



        String startUrl = "http://hkuri.cneas.tohoku.ac.jp/project1/manchu/listAction";
        Map<String, String> params = new HashMap<>();
        params.put("searchRange", "2");
        params.put("searchMethod", "2");
        params.put("groupId", "11");
        params.put("pageSize", "50");
        params.put("dicIds", "6,72");

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
                params.put("currentPage", currentPage);
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
                }
            }
            rows += keyRows;
            logger.info("以{}为关键字，每页50行，共查询{}页", keyWord, keyRows);
        }

        File tmpFile = new File("E:\\temp\\Dictionary\\dic\\manchulist_1.txt");
        if (tmpFile.exists()) {
            try {
                BufferedReader fileReader = new BufferedReader(new FileReader(tmpFile));
                String contentLine;
                while ((contentLine = fileReader.readLine()) != null) {
                    if (contentLine.trim().length() > 0) {
                        // 防止写文件数据阻塞，导致生产线程阻塞
                        if (listPageThreadPool.getActiveCount() > 40) {
                            Thread.sleep(new Random().nextInt(10) * 4000);
                        }
                        params.put("currentPage", "1");
                        params.put("keyword", contentLine.trim());
                        HttpPost postRequest = new HttpPost(startUrl);
                        HttpClientUtil.setHttpPostParams(postRequest, params);
                        listPageThreadPool.execute(new TohokuDictQueryTask(postRequest, contentLine.trim(), Config.isProxy));
                        Thread.sleep(new Random().nextInt(5) * 1000);
                        rows++;
                        if (rows % 100 == 0) {
                            logger.info("已提交任务{}项,查询成功{}项,明细数据{}项，有{}项未查到", rows, parseWordCount.get(), parseWordDetailCount.get(), parseWordNotFountCount.get());
                        }
                    } else {
                        break;
                    }
                }
                logger.info("已全部提交任务{}项,查询成功{}项,明细数据{}项，有{}项未查到", rows, parseWordCount.get(), parseWordDetailCount.get(), parseWordNotFountCount.get());
            } catch (Exception e) {
                return;
            }
        }
        manageHttpClient();
    }

    *//**
     * 管理知乎客户端
     * 关闭整个爬虫
     *//*
    public void manageHttpClient() {
        while (true) {
            *//**
             * 下载网页数
             *//*
//            long downloadPageCount = detailImageThreadPool.getTaskCount();

            if (listPageThreadPool.getQueue().size() == 0 && listPageThreadPool.getActiveCount() == 0) {
                listPageThreadPool.shutdown();
            }

            if (listPageThreadPool.isTerminated() &&
                    detailImageThreadPool.getActiveCount() == 0 && detailImageThreadPool.getQueue().size() == 0) {
                isStop = true;
                ThreadPoolMonitor.isStopMonitor = true;
                detailImageThreadPool.shutdown();
            }
            if (detailImageThreadPool.isTerminated()) {
                if (Config.isProxy) {
                    //关闭代理检测线程池
                    ProxyHttpClient.getInstance().getProxyTestThreadExecutor().shutdownNow();
                    //关闭代理下载页线程池
                    ProxyHttpClient.getInstance().getProxyDownloadThreadExecutor().shutdownNow();
                }
                break;
            }
            double costTime = (System.currentTimeMillis() - startTime) / 1000.0;//单位s
            logger.info("耗时{}秒，抓取{}数据，抓取速率：{}个/s",costTime, parseWordCount.get(), parseWordCount.get() / costTime);
//            logger.info("downloadFailureProxyPageSet size:" + ProxyHttpClient.downloadFailureProxyPageSet.size());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public ThreadPoolExecutor getListPageThreadPool() {
        return listPageThreadPool;
    }


    public ThreadPoolExecutor getDetailImageThreadPool() {
        return detailImageThreadPool;
    }

    */
}
