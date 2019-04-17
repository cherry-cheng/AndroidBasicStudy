package com.weizhan.myviewpractice.toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.weizhan.myviewpractice.MainActivity;
import com.weizhan.myviewpractice.R;
import com.weizhan.myviewpractice.myrecyclerview.RecyclerViewActivity;
import com.weizhan.myviewpractice.view.CarActivity;

/**
 * Created by Administrator on 2019/4/16.
 */

public class ToolbarActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar_layout);
        Toolbar toolBar = findViewById(R.id.toolBar);
        toolBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ToolbarActivity.this, "点击返回", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ToolbarActivity.this, RecyclerViewActivity.class));
            }
        });
        toolBar.setRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ToolbarActivity.this, "点击右键", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ToolbarActivity.this, CarActivity.class));
            }
        });
    }
}
