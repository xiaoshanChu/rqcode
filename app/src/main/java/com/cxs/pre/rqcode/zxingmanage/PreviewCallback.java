package com.cxs.pre.rqcode.zxingmanage;

import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;

/**
 * Created by Xiaoshan on 2016/2/22.
 */
public class PreviewCallback implements Camera.PreviewCallback {

    private Handler handler;
    private int what;

    public void setHandler(Handler previewHandler, int what) {
        this.handler = previewHandler;
        this.what = what;
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        if (handler != null) {
            Message msg = handler.obtainMessage(what,0,0,bytes);
            msg.sendToTarget();
            handler = null;
        }
    }
}
