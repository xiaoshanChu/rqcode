package com.cxs.pre.rqcode.zxingmanage;

import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.cxs.pre.rqcode.CameraActivity;

/**
 * Created by Xiaoshan on 2016/2/23.
 */
public class MainHandler extends Handler {

    private DecodeThread decodeThread;
    private PreviewCallback previewCallback;
    private Camera camera;
    private int state;

    public MainHandler(CameraActivity activity, PreviewCallback previewCallback, Camera camera) {
        this.camera = camera;
        decodeThread = new DecodeThread(activity);
        this.previewCallback = previewCallback;
        decodeThread.start();
        camera.startPreview();
        restartDecode();
        state = 0;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                restartDecode();
                break;
            case 1:
                Log.d("MainHandler", "---success--->" + msg.obj.toString());
                state = 0;
                break;
            case 2:
                Log.d("MainHandler", "---failure--->" + msg.obj.toString());
                restartDecode();
                state = 1;
                break;
            default:
                break;
        }
    }

    public void quitSynchronously() {
        state = 2;
        camera.stopPreview();
        Message quit = Message.obtain(decodeThread.getHandler(), 2);
        quit.sendToTarget();
        try {
            decodeThread.join(500L);
        } catch (InterruptedException e) {
        }
        removeMessages(0);
        removeMessages(1);
    }

    private void restartDecode() {
        if (camera != null) {
            previewCallback.setHandler(decodeThread.getHandler(), 1);
            camera.setOneShotPreviewCallback(previewCallback);
        }
    }
}
