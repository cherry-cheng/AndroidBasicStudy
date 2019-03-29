package com.cyh.processkeeplivedemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cyh.processkeeplivedemo.service.ForegroundService;
import com.cyh.processkeeplivedemo.service.LocalService;
import com.cyh.processkeeplivedemo.service.RemoteService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 通过 1像素activity 进行提权(保活)
//        KeepManager.getInstance().registerKeep(this);

        // 2 前台服务（保活）
//        startService(new Intent(this, ForegroundService.class));

        //双进程守护（拉活）
        startService(new Intent(this, LocalService.class));
        startService(new Intent(this, RemoteService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        KeepManager.getInstance().unregisterKeep(this);
    }
}
