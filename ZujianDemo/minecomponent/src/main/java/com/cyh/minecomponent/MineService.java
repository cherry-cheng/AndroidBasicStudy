package com.cyh.minecomponent;

import android.content.Context;
import android.content.Intent;

import com.cyh.componentlib.service.IMineService;

public class MineService implements IMineService {
    @Override
    public void launch(Context context, int userId) {
        Intent intent = new Intent(context, MineActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("ID", userId);
        context.startActivity(intent);
    }
}
