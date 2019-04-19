package com.cyh.eventdemo.listener;

import com.cyh.eventdemo.MotionEvent;
import com.cyh.eventdemo.View;

public interface OnTouchListener {
    boolean onTouch(View v, MotionEvent event);
}
