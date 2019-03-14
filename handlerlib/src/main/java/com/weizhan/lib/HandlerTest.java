package com.weizhan.lib;

import java.util.UUID;

/**
 * Created by Administrator on 2019/3/14.
 */

public class HandlerTest {
    public static void main(String[] args) {
        Looper.prepare();

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                System.out.println("main = " + Thread.currentThread().getName() + ",msg = " + msg.toString());
            }
        };

        for (int i = 0; i < 10; i++) {
            new Thread() {
                @Override
                public void run() {
                    while (true) {
                        Message msg = new Message();
                        synchronized (UUID.class) {
                            msg.obj = UUID.randomUUID().toString();
                        }
                        System.out.println("send = " + Thread.currentThread().getName() + ",msg = " + msg.toString());
                        handler.sendMessage(msg);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }

        Looper.loop();
    }
}
