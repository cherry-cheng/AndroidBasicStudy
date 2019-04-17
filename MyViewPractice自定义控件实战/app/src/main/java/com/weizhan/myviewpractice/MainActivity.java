package com.weizhan.myviewpractice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.weizhan.myviewpractice.layout.FlowLayout;
import com.weizhan.myviewpractice.svg.SvgActivity;
import com.weizhan.myviewpractice.toolbar.ToolbarActivity;
import com.weizhan.myviewpractice.vlayout.VlayoutActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FlowLayout flowLayout;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flowLayout = findViewById(R.id.flow);
        List<String> tags = new ArrayList<>();
        tags.add("网易");
        tags.add("网易");
        tags.add("网易课堂");
        tags.add("网易云音乐");
        tags.add("有道云");
        tags.add("高级UI自定义控件");
        tags.add("继承控件");
        tags.add("今天天气真的好好");
        tags.add("杭州天气也不错~");
        tags.add("好好学习   天天向上");
        tags.add("你是最棒的");
        tags.add("加油");
        flowLayout.addTag(tags);

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, ToolbarActivity.class));
//                startActivity(new Intent(MainActivity.this, SvgActivity.class));
                startActivity(new Intent(MainActivity.this, VlayoutActivity.class));
            }
        });
    }
}
