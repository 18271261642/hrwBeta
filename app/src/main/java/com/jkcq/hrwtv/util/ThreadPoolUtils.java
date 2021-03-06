package com.jkcq.hrwtv.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolUtils {
    private ExecutorService service;

    private ThreadPoolUtils() {
        int num = Runtime.getRuntime().availableProcessors();
        service = Executors.newFixedThreadPool(num * 2);
    }

    private static final ThreadPoolUtils manager = new ThreadPoolUtils();

    public static ThreadPoolUtils getInstance() {
        return manager;
    }

    public void addTask(Runnable runnable) {
        service.execute(runnable);
    }

    public void onTerminate() {
        if (service != null) {
            service.shutdown();
        }
    }
}
