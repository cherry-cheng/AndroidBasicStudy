package com.cyh.qrcodelibrary.open;

import android.hardware.Camera;

public class OpenCameraInterface {
    private static final String TAG = OpenCameraInterface.class.getSimpleName();

    public static Camera open(int cameraId) {
        int numCameras = Camera.getNumberOfCameras();
        if (numCameras == 0) {
            return null;
        }

        boolean explicitRequest = cameraId >= 0;
        if (!explicitRequest) {
            int index = 0;
            while (index < numCameras) {
                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                Camera.getCameraInfo(index, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    break;
                }
                index++;
            }
            cameraId = index;
        }

        Camera camera;
        if (cameraId < numCameras) {
            camera = Camera.open(cameraId);
        } else {
            if (explicitRequest) {
                camera = null;
            } else {
                camera = Camera.open(0);
            }
        }
        return camera;
    }

    public static Camera open() {
        return open(-1);
    }
}
