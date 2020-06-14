package com.crawl.tohoku;


import com.crawl.tohoku.task.TohokuProxyPageDownloadTask;

public class TohokuConstants {

    public static final int MAX_TASK_LENGTH = 10000;

    public static final String PROXY_PAGE_REDIS_KEY_PREFIX = "tohoku:proxy:";

    public static final String TOHOKU_PAGE_REDIS_KEY_PREFIX = "tohoku:tohoku:";


    public final static String PROXY_PAGE_DOWNLOAD_TASK_LOCK_KEY_PREFIX = TohokuProxyPageDownloadTask.class.getSimpleName() + "LockKey:";

    public final static String PROXY_PAGE_DOWNLOAD_TASK_QUEUE_NAME = TohokuProxyPageDownloadTask.class.getSimpleName() + "Queue";

    /**
     * tohoku 字典首页
     */
    public final static String TOHOKU_START_URL = "http://hkuri.cneas.tohoku.ac.jp";

    /**
     * <p>浏览器页面地址：http://hkuri.cneas.tohoku.ac.jp/project1/manchu/list?groupId=11</p>
     */
    public final static String TOHOKU_MANCHU_URL = "http://hkuri.cneas.tohoku.ac.jp/project1/manchu/listAction";
    /**
     * <p>浏览器页面地址：http://hkuri.cneas.tohoku.ac.jp/project1/kdic/list?groupId=33</p>
     */
    public final static String TOHOKU_KDIC_URL = "http://hkuri.cneas.tohoku.ac.jp/project1/kdic/listAction";
    /**
     * 图片下载地址URL
     */
    public static String DOWNLOAD_URL = TOHOKU_START_URL + "/project1/imageviewer/detail?dicId=%s&imageFileName=%s";
    /**
     * 图片存储地址
     */
    public static String DOWNLOAD_PATH = "E:/temp/tohoku/";
}
