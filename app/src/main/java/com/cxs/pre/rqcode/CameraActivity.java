package com.cxs.pre.rqcode;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.cxs.pre.rqcode.zxingmanage.MainHandler;
import com.cxs.pre.rqcode.zxingmanage.PicUtils;
import com.cxs.pre.rqcode.zxingmanage.PreviewCallback;

import java.io.IOException;

/**
 * A login screen that offers login via email/password.
 */
public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private SurfaceView sfvCamera;
    private Camera camera;
    private static final String TAG = CameraActivity.class.getSimpleName();
    private PreviewCallback previewCallback;
    private MainHandler handler;

    public MainHandler getHandler() {
        return handler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
    }

    private void init() {
        previewCallback = new PreviewCallback();

        sfvCamera = (SurfaceView) findViewById(R.id.sfv_camera);
        sfvCamera.getHolder()
                .setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        sfvCamera.getHolder().setKeepScreenOn(true);
        sfvCamera.getHolder().addCallback(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sfvCamera.destroyDrawingCache();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            camera = Camera.open();
            Camera.Parameters parameters = camera.getParameters();
            camera.setPreviewDisplay(surfaceHolder);
            int degree = PicUtils.getPreviewDegree(CameraActivity.this);
            camera.setDisplayOrientation(degree);
            parameters.set("rotation", degree);

            camera.setParameters(parameters);
            handler = new MainHandler(this, previewCallback, camera);
            new AutoFocusManager(CameraActivity.this, camera);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
}

