package com.cyh.qrcodedemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cyh.qrcodedemo.utils.Utils;
import com.weizhan.httprequestlib.IJsonDataListener;
import com.weizhan.httprequestlib.util.NeOkHttp;
import com.weizhan.httprequestlib.util.ResponseBean;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_SCAN = 0;
    private String url = "http://v.juhe.cn/historyWeather/citys?province_id=2&key=bb52107206585ab074f5e59a8c73875b";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NeOkHttp.sendJsonRequest(null, url, ResponseBean.class, new IJsonDataListener<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean rb) {
                        Log.e("===> ",rb.toString());
                    }

                    @Override
                    public void onFailure() {

                    }
                });
            }
        });
        findViewById(R.id.ll_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRuntimePermission();
            }
        });
    }
    // 获得运行时权限
    private void getRuntimePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = {Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            if (checkSelfPermission(perms[0]) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(perms[1]) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(perms, 200);
            } else {
                jumpScanPage();
            }
        }
    }

    // 跳转到扫码页
    private void jumpScanPage() {
        startActivityForResult(new Intent(this, CaptureActivity.class), REQUEST_SCAN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SCAN && resultCode == RESULT_OK) {
            Toast.makeText(this, data.getStringExtra(Utils.BAR_CODE), Toast.LENGTH_LONG).show();
        }
    }

}
