package com.cyh.animationdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PieView pieView;
    private AnimCheckView animCheckView;
    private List<PieData> mData;
    private int[] mColors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
            0xFFE6B800, 0xFF7CFC00};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pieView = findViewById(R.id.pieView);
        animCheckView = findViewById(R.id.animCheckView);
        pieView.setmList(initData());

        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pieView.setStartAngle(90);
            }
        });

        findViewById(R.id.check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animCheckView.check();
            }
        });

        findViewById(R.id.uncheck).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animCheckView.unCheck();
            }
        });
    }

    private ArrayList<PieData> initData() {
        mData = new ArrayList<PieData>();
        for (int i = 0; i < 6; i++) {
            PieData pieData = new PieData("1", 2 + i);
            pieData.setColor(mColors[i % mColors.length]);
            mData.add(pieData);
        }
        return (ArrayList<PieData>) mData;
    }
}
