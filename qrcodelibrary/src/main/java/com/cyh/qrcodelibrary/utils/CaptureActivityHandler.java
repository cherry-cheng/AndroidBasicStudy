package com.cyh.qrcodelibrary.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.cyh.qrcodelibrary.CaptureCallback;
import com.cyh.qrcodelibrary.camera.CameraManager;
import com.cyh.qrcodelibrary.decode.DecodeThread;
import com.google.zxing.Result;

/**
 * Created by Administrator on 2019/3/7.
 */

public class CaptureActivityHandler extends Handler {
    private final CaptureCallback activity;
    private final DecodeThread decodeThread;
    private final CameraManager cameraManager;
    private State state;

    private enum State {
        PREVIEW, SUCCESS, DONE
    }

    public CaptureActivityHandler(CaptureCallback activity, CameraManager cameraManager, int decodeMode) {
        this.activity = activity;
        decodeThread = new DecodeThread(activity, decodeMode);
        decodeThread.start();
        state = State.SUCCESS;

        this.cameraManager = cameraManager;
        cameraManager.startPreview();
        restartPreviewAndDecode();
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.what == Constants.RESTART_PREVIEW) {
            restartPreviewAndDecode();
        } else if (msg.what == Constants.DECODE_SUCCEEDED) {
            state = State.SUCCESS;
            Bundle bundle = msg.getData();
            activity.handleDecode((Result) msg.obj, bundle);
        } else if (msg.what == Constants.DECODE_FAILED) {
            state = State.PREVIEW;
            cameraManager.requestPreviewFrame(decodeThread.getHandler(), Constants.DECODE);
        } else if (msg.what == Constants.RETURN_SCAN_RESULT) {
            activity.setResult(Activity.RESULT_OK, (Intent) msg.obj);
            activity.finish();
        }
    }

    public void quitSynchronously() {
        state = State.DONE;
        cameraManager.startPreview();
        Message quit = Message.obtain(decodeThread.getHandler(), Constants.QUIT);
        quit.sendToTarget();
        try {
            decodeThread.join(500L);
        } catch (InterruptedException ie) {

        }

        removeMessages(Constants.DECODE_SUCCEEDED);
        removeMessages(Constants.DECODE_FAILED);
    }

    private void restartPreviewAndDecode() {
        if (state == State.SUCCESS) {
            state = State.PREVIEW;
            cameraManager.requestPreviewFrame(decodeThread.getHandler(), Constants.DECODE);
        }
    }
}
