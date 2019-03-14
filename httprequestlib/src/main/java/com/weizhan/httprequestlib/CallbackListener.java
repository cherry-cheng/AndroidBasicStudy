package com.weizhan.httprequestlib;

import java.io.InputStream;

/**
 * Created by Administrator on 2019/3/14.
 */

public interface CallbackListener {
    void onSuccess(InputStream inputStream);
    void onFailure();
}
