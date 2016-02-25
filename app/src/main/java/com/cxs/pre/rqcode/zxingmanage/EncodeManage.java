package com.cxs.pre.rqcode.zxingmanage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by Xiaoshan on 2016/2/17.
 */
public class EncodeManage {

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;
    private Context context;

    public EncodeManage(Context context) {
        this.context = context;
    }

    public Bitmap encodeAsBitmap(String encodeContents) throws WriterException {
        Map<EncodeHintType, Object> hints = null;
        String encoding = "UTF-8";
        if (encoding != null) {
            hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        BitMatrix result;
        try {
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point displaySize = new Point();
            display.getSize(displaySize);
            int width = displaySize.x;
            int height = displaySize.y;
            int smallerDimension = width < height ? width : height;
            smallerDimension = smallerDimension * 7 / 8;

            result = new MultiFormatWriter().encode(encodeContents, BarcodeFormat.QR_CODE,
                    smallerDimension, smallerDimension, hints);
        } catch (Exception iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
}
