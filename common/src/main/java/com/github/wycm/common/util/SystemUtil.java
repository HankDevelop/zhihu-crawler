package com.github.wycm.common.util;

/**
 * Created by wycm on 2018/10/15.
 */
public class SystemUtil {
    public static int getCpuCoreSize(){
        return Runtime.getRuntime().availableProcessors();
    }

    public static int getRecommendThreadSize(){
        int calcTaskTime = 200;
        int waitTime = 1000;
        return getCpuCoreSize() * (calcTaskTime + waitTime) / calcTaskTime;
    }

    public static void main(String[] args) {
        System.out.println(getCpuCoreSize());
        System.out.println(getRecommendThreadSize());
    }
}
