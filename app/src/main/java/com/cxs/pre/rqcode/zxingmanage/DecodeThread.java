package com.cxs.pre.rqcode.zxingmanage;

import android.os.Handler;
import android.os.Looper;

import com.cxs.pre.rqcode.CameraActivity;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Xiaoshan on 2016/2/23.
 */
public class DecodeThread extends Thread {

    private Handler handler;
    private final CountDownLatch countDownLatch;
    private CameraActivity activity;

    public DecodeThread(CameraActivity activity){
        countDownLatch = new CountDownLatch(1);
        this.activity = activity;
    }

    public Handler getHandler(){
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return handler;
    }

    @Override
    public void run() {
        Looper.prepare();
        handler = new DecodeHandler(activity);
        countDownLatch.countDown();
        Looper.loop();
    }
}
