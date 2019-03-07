package com.cyh.qrcodelibrary.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;

/**
 * Created by Administrator on 2019/3/7.
 */

public class InactivityTimer {
    public static final String TAG = InactivityTimer.class.getSimpleName();
    public static final long INACTIVITY_DELAY_MS = 5 * 60 * 1000L;

    private Activity activity;
    private BroadcastReceiver powerStatusReceiver;
    private boolean registtered;
    private AsyncTask<Object, Object, Object> inactivityTask;

    public InactivityTimer(Activity activity) {
        this.activity = activity;
        powerStatusReceiver = new PowerStatusReceiver();
        registtered = false;
        onActivity();
    }

    @SuppressLint("NewApi")
    public synchronized void onActivity() {
        cancel();
        inactivityTask = new InactivityAsyncTask();
        if (Build.VERSION.SDK_INT >= 11) {
            inactivityTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            inactivityTask.execute();
        }
    }

    public synchronized void onPause() {
        cancel();
        if (registtered) {
            activity.unregisterReceiver(powerStatusReceiver);
            registtered = false;
        } else {

        }
    }

    public synchronized void onResume() {
        if (registtered) {

        } else {
            activity.registerReceiver(powerStatusReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            registtered = true;
        }
        onActivity();
    }

    private synchronized void cancel() {
        AsyncTask<?, ?, ?> task = inactivityTask;
        if (task != null) {
            task.cancel(true);
            inactivityTask = null;
        }
    }

    public void shutdown() {
        cancel();
    }

    private class PowerStatusReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                boolean onBatteryNow = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) <= 0;
                if (onBatteryNow) {
                    InactivityTimer.this.onActivity();
                } else {
                    InactivityTimer.this.cancel();
                }
            }
        }
    }

    private class InactivityAsyncTask extends AsyncTask<Object, Object, Object> {

        @Override
        protected Object doInBackground(Object... objects) {
            try {
                Thread.sleep(INACTIVITY_DELAY_MS);
                activity.finish();
            } catch (InterruptedException e) {

            }
            return null;
        }
    }
}
