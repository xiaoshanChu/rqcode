package com.cxs.pre.rqcode.zxingmanage;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.cxs.pre.rqcode.CameraActivity;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.common.HybridBinarizer;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.Hashtable;

/**
 * Created by Xiaoshan on 2016/2/23.
 */
public class DecodeHandler extends Handler {

    private final MultiFormatReader multiFormatReader;
    private static final String TAG = DecodeHandler.class.getSimpleName();
    private CameraActivity activity;
    private boolean running = true;

    public DecodeHandler(CameraActivity activity) {
        multiFormatReader = new MultiFormatReader();
        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF8");
        multiFormatReader.setHints(hints);
        this.activity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        if (!running) {
            return;
        }
        switch (msg.what) {
            case 1:
                decode((byte[]) msg.obj);
                break;
            case 2:
                running = false;
                Looper.myLooper().quit();
                break;
            default:
                break;
        }
    }

    int i = 0;

    private void decode(byte[] data) {
        Result result = null;
        Rect rect = FindView.rect;
        int width = FindView.width;
        int height = FindView.height;

        PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(data, height, width,
                rect.top, rect.left, rect.width(), rect.height(), false);

        if (i % 20 == 0) {
            bundleThumbnail(source);
        }
        i++;

        if (source != null) {
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            try {
                result = multiFormatReader.decodeWithState(bitmap);
            } catch (ReaderException e) {
            } finally {
                multiFormatReader.reset();
            }
        }
        Handler handler = ((CameraActivity) activity).getHandler();
        if (handler != null) {
            Message msg = handler.obtainMessage();
            if (result != null) {
                ParsedResult parsedResult = ResultParser.parseResult(result);
                String resultStr = parsedResult.getDisplayResult();
                Log.d(TAG + "success", "---contents--->" + resultStr);
                msg.what = 1;
                msg.obj = resultStr;
            } else {
                msg.what = 2;
                msg.obj = "失败";
            }
            handler.sendMessage(msg);
        }
    }


    private  void bundleThumbnail(PlanarYUVLuminanceSource source) {
        try {
            int[] pixels = source.renderThumbnail();
            int width = source.getThumbnailWidth();
            int height = source.getThumbnailHeight();
            Bitmap bitmap = Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.ARGB_8888);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
            FileOutputStream fos = new FileOutputStream(PicUtils.getFileName());
            out.writeTo(fos);
        } catch (Exception e) {
            Log.d(TAG,"-------------------------------------------------------------------");
        }

    }
}
