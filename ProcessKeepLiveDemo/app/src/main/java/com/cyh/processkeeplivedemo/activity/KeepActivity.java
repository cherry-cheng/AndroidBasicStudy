package com.cyh.processkeeplivedemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

public class KeepActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置这个1像素在act 左上角
        Window window = getWindow();
        window.setGravity(Gravity.START | Gravity.TOP);
        WindowManager.LayoutParams attr = window.getAttributes();
        attr.width = 1;
        attr.height = 1;
        attr.x = 0;
        attr.y = 0;
        window.setAttributes(attr);
        KeepManager.getInstance().setKeep(this);
    }
}
