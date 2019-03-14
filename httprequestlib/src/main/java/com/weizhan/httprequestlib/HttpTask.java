package com.weizhan.httprequestlib;

import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2019/3/14.
 */

public class HttpTask<T> implements Runnable, Delayed {

    private IHttpRequest mIHttpRequest;

    public HttpTask(T requestData, String url, IHttpRequest httpRequest, CallbackListener callbackListener) {
        this.mIHttpRequest = httpRequest;
        httpRequest.setUrl(url);
        httpRequest.setListener(callbackListener);
        String content = JSON.toJSONString(requestData);
        try {
            httpRequest.setData(content.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private long delayTime;
    private int retryCount;

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        // 设置延迟时间  3000
        this.delayTime = System.currentTimeMillis() + delayTime;
    }

    @Override
    public long getDelay(@NonNull TimeUnit timeUnit) {
        return timeUnit.convert(this.delayTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(@NonNull Delayed delayed) {
        return 0;
    }

    @Override
    public void run() {
        try {
            mIHttpRequest.execute();
        } catch (Exception e) {
            // 将失败的任务添加到重试队列中
            ThreadPoolManager.getInstance().addDelayTask(this);
        }
    }
}
