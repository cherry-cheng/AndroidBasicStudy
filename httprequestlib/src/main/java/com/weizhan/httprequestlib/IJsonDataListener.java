package com.weizhan.httprequestlib;

/**
 * Created by Administrator on 2019/3/14.
 */

public interface IJsonDataListener<T> {
    void onSuccess(T m);
    void onFailure();
}
