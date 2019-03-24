package com.cyh.minecomponent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        if (getIntent() != null) {
            int userid = getIntent().getIntExtra("ID", -1);
            Toast.makeText(this, " " + userid, Toast.LENGTH_SHORT).show();
        }
    }
}
