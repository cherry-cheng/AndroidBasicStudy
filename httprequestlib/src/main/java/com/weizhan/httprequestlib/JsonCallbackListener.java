package com.weizhan.httprequestlib;

import android.os.Handler;
import android.os.Looper;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2019/3/14.
 */

public class JsonCallbackListener<T> implements CallbackListener {
    private Class<T> responseClass;
    Handler handler = new Handler(Looper.getMainLooper());
    private IJsonDataListener mIJsonDataListener;

    public JsonCallbackListener(Class<T> responseClass, IJsonDataListener listener) {
        this.responseClass = responseClass;
        mIJsonDataListener = listener;
    }

    @Override
    public void onSuccess(InputStream inputStream) {
        String response = getContent(inputStream);
        final T clazz = JSON.parseObject(response, responseClass);
        handler.post(new Runnable() {
            @Override
            public void run() {
                mIJsonDataListener.onSuccess(clazz);
            }
        });
    }

    private String getContent(InputStream inputStream) {
        String content = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder sb = new StringBuilder();

            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                System.out.println("Error = " + e.toString());
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    System.out.println("Error = " + e.toString());
                }
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    @Override
    public void onFailure() {

    }
}
