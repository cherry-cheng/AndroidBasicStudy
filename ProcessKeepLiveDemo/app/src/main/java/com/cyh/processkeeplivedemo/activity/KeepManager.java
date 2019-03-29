package com.cyh.processkeeplivedemo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.lang.ref.WeakReference;

public class KeepManager {
    private static final KeepManager outInstance = new KeepManager();
    public static KeepManager getInstance() {
        return outInstance;
    }
    private KeepManager() {

    }

    private KeepReceiver keepReceiver;
    private WeakReference<Activity> mKeepAct;

    /**
     * 注册  关屏  开屏
     * @param context
     */
    public void registerKeep(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        keepReceiver = new KeepReceiver();
        context.registerReceiver(keepReceiver, filter);
    }

    public void unregisterKeep(Context context) {
        if (null != keepReceiver) {
            context.unregisterReceiver(keepReceiver);
        }
    }

    /**
     * 开启activity
     * @param context
     */
    public void startKeep(Context context) {
        Intent intent = new Intent(context, KeepActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void finishKeep() {
        if (null != mKeepAct) {
            Activity activity = mKeepAct.get();
            if (null != activity) {
                activity.finish();
            }
            mKeepAct = null;
        }
    }
    public void setKeep(KeepActivity keep) {
        mKeepAct = new WeakReference<Activity>(keep);
    }
}
