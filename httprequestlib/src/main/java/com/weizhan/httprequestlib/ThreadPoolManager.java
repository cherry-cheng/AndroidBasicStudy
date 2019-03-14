package com.weizhan.httprequestlib;

import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2019/3/14.
 */

public class ThreadPoolManager {
    // 1创建队列，用来保存异步请求任务
    private LinkedBlockingDeque<Runnable> mQueue = new LinkedBlockingDeque<>();

    // 2 添加异步任务到队列
    public void addTask(Runnable runnable) {
        if (runnable != null) {
            try {
                mQueue.put(runnable);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // 创建延迟队列
    private DelayQueue<HttpTask> mDelayQueue = new DelayQueue<>();

    public void addDelayTask(HttpTask ht) {
        if (ht != null) {
            ht.setDelayTime(3000);
            mDelayQueue.offer(ht);
        }
    }

    public Runnable delayThread = new Runnable() {
        @Override
        public void run() {
            HttpTask ht = null;
            while (true) {
                try {
                    ht = mDelayQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (ht.getRetryCount() < 3) {
                    mThreadPoolExecutor.execute(ht);
                    ht.setRetryCount(ht.getRetryCount()+1);
                    Log.e("======重试机制 ====", ht.getRetryCount() + "");
                } else {
                    Log.e("=====重试机制=====", "执行次数超限，放弃");
                }
            }
        }
    };

    // 3 创建线程池
    private ThreadPoolExecutor mThreadPoolExecutor;
    private ThreadPoolManager() {
        mThreadPoolExecutor = new ThreadPoolExecutor(3, 10, 15, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(4), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
                // 处理抛出来的任务
                addTask(runnable);
            }
        });
        mThreadPoolExecutor.execute(communicateThread);
        mThreadPoolExecutor.execute(delayThread);
    }

    // 创建队列与线程池的“交互”线程
    public Runnable communicateThread = new Runnable() {
        @Override
        public void run() {
            Runnable ruun = null;
            while (true) {
                try {
                    ruun = mQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mThreadPoolExecutor.execute(ruun);
            }
        }
    };

    //单例模式
    private static ThreadPoolManager threadPoolManager = new ThreadPoolManager();
    public static ThreadPoolManager getInstance() {
        return threadPoolManager;
    }
}
