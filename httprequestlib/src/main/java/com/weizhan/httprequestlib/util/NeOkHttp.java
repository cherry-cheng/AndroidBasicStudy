package com.weizhan.httprequestlib.util;

import com.weizhan.httprequestlib.CallbackListener;
import com.weizhan.httprequestlib.HttpTask;
import com.weizhan.httprequestlib.IHttpRequest;
import com.weizhan.httprequestlib.IJsonDataListener;
import com.weizhan.httprequestlib.JsonCallbackListener;
import com.weizhan.httprequestlib.JsonHttpRequest;
import com.weizhan.httprequestlib.ThreadPoolManager;

/**
 * Created by Administrator on 2019/3/14.
 */

public class NeOkHttp {
    public static<T,M> void sendJsonRequest(T requestData, String url,
                                            Class<M> response, IJsonDataListener listener) {
        IHttpRequest httpRequest = new JsonHttpRequest();
        CallbackListener callbackListener = new JsonCallbackListener<>(response, listener);
        HttpTask httpTask = new HttpTask(requestData, url, httpRequest, callbackListener);
        ThreadPoolManager.getInstance().addTask(httpTask);
    }
}
