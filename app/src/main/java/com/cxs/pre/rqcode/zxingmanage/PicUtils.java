package com.cxs.pre.rqcode.zxingmanage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Xiaoshan on 2016/2/19.
 */
public class PicUtils {

    private static final String TAG = "PicUtils";

    /*保存图片*/
    public static void savePic(byte[] data, Context context) {
        try {
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            String fileName = format.format(date) + ".jpg";
            File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "rqcode");
            if (!folder.exists()) {
                folder.mkdir();
            }
            File picFile = new File(folder, fileName);
            FileOutputStream fos = new FileOutputStream(picFile);
            fos.write(data);
            fos.close();
            //通知图库改变
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + picFile)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*保存图片*/
    public static void savePic(byte[] data) {
        try {
            FileOutputStream fos = new FileOutputStream(getFileName());
            fos.write(data);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File getFileName(){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = format.format(date) + ".jpg";
        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "rqcode");
        if (!folder.exists()) {
            folder.mkdir();
        }
        return new File(folder, fileName);
    }

    /*相机预览角度*/
    public static int getPreviewDegree(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        Log.d(TAG, "---degree--->" + rotation);
        int degree = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 90;
                break;
            case Surface.ROTATION_90:
                degree = 0;
                break;
            case Surface.ROTATION_180:
                degree = 270;
                break;
            case Surface.ROTATION_270:
                degree = 180;
                break;
        }
        return degree;
    }

    public static Rect getRect(int rectWidth, int width, int height) {
        int haf = rectWidth / 2;
        int left = width / 2 - haf;
        int top = height / 2 - haf;
        int right = width / 2 + haf;
        int bottom = height / 2 + haf;
        return new Rect(left, top, right, bottom);
    }
}
