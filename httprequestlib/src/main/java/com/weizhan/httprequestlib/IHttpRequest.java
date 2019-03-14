package com.weizhan.httprequestlib;

/**
 * Created by Administrator on 2019/3/14.
 */
// 封装请求接口
public interface IHttpRequest {
    void setUrl(String url);
    void setData(byte[] data);
    void setListener(CallbackListener callbackListener);
    void execute();
}
