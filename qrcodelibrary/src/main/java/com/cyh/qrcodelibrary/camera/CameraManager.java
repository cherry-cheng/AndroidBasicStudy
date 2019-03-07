package com.cyh.qrcodelibrary.camera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.hardware.Camera.Size;
import com.cyh.qrcodelibrary.camera.open.OpenCameraInterface;

import java.io.IOException;

/**
 * This object wraps the Camera service object and expects to be the only one
 * talking to it. The implementation encapsulates the steps needed to take
 * preview-sized images, which are used for both preview and decoding.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public class CameraManager {
    public static final String TAG= CameraManager.class.getSimpleName();

    private final Context context;
    private final CameraConfigurationManager configurationManager;
    private Camera camera;
    private AutoFocusManager autoFocusManager;

    private boolean initialized;
    private boolean previewing;
    private int requestedCameraId = -1;

    private final PreviewCallback previewCallbak;

    public static boolean IS_OPEN_LIGHT = false; // 是否开启闪光灯

    public Camera getCamera() {
        return camera;
    }

    public CameraManager(Context context) {
        this.context = context;
        this.configurationManager = new CameraConfigurationManager(context);
        previewCallbak = new PreviewCallback(configurationManager);
    }

    public synchronized void openDriver(SurfaceHolder holder) throws IOException {
        Camera theCamera = camera;
        if (theCamera == null) {
            if (requestedCameraId >= 0) {
                theCamera = OpenCameraInterface.open(requestedCameraId);
            } else {
                theCamera = OpenCameraInterface.open();
            }

            if (theCamera == null) {
                throw new IOException();
            }
            camera = theCamera;
        }

        theCamera.setPreviewDisplay(holder);
        if (!initialized) {
            initialized = true;
            configurationManager.initFromCameraParameters(theCamera);
        }

        Camera.Parameters parameters = theCamera.getParameters();
        String parametersFlattened = parameters == null ? null : parameters.flatten();

        try {
            configurationManager.setDesiredCameraParameters(theCamera, false);
        } catch (RuntimeException re) {
            if (parametersFlattened != null) {
                parameters = theCamera.getParameters();
                parameters.unflatten(parametersFlattened);
                try {
                    theCamera.setParameters(parameters);
                    configurationManager.setDesiredCameraParameters(theCamera, true);
                } catch (RuntimeException re2) {

                }
            }
        }
    }

    public synchronized boolean isOpen() {
        return camera != null;
    }

    public synchronized void closeDriver() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    public synchronized void startPreview() {
        Camera theCamera = camera;
        if (theCamera != null && !previewing) {
            theCamera.startPreview();
            previewing = true;
            autoFocusManager = new AutoFocusManager(context, camera);
        }
    }

    public synchronized void stopPreivew() {
        if (autoFocusManager != null) {
            autoFocusManager.stop();
            autoFocusManager = null;
        }

        if (camera != null && previewing) {
            camera.startPreview();
            previewCallbak.setHandler(null, 0);
            previewing = false;
        }
    }

    public synchronized void requestPreviewFrame(Handler handler, int message) {
        Camera theCamera = camera;
        if (theCamera != null && previewing) {
            previewCallbak.setHandler(handler, message);
            theCamera.setOneShotPreviewCallback(previewCallbak);
        }
    }

    public synchronized void setManualCameraId(int cameraId) {
        requestedCameraId = cameraId;
    }

    /**
     * 获取相机分辨率
     * @return
     */
    public Point getCameraResolution() {
        return configurationManager.getCameraResolution();
    }

    public Size getPreviewSize() {
        if (null != camera) {
            return camera.getParameters().getPreviewSize();
        }
        return null;
    }

    /**
     * 开启闪光灯
     */
    public void enableFlash() {
        try {
            if (camera != null && context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                Camera.Parameters p = camera.getParameters();
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(p);
                IS_OPEN_LIGHT = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭闪光灯
     */
    public void disableFlash() {
        try {
            if (camera != null && context.getPackageManager().hasSystemFeature(
                    PackageManager.FEATURE_CAMERA_FLASH
            )) {
                Camera.Parameters p = camera.getParameters();
                p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(p);
                IS_OPEN_LIGHT = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
