package com.sz.transformation.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Sean Zhang
 * Date: 2021/11/12
 */
public class ThreadPool {
    private static ExecutorService executorService=Executors.newFixedThreadPool(Math.max(2, Runtime.getRuntime().availableProcessors() / 2));
    public static void execute(Runnable runnable){
        executorService.execute(runnable);
    }
}
