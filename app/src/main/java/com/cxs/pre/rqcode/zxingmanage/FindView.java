package com.cxs.pre.rqcode.zxingmanage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.cxs.pre.rqcode.R;

/**
 * Created by Xiaoshan on 2016/2/22.
 */
public class FindView extends View {

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Rect rect;
    public static int width;
    public static int height;



    public FindView(Context context) {
        super(context);
    }

    public FindView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(getResources().getColor(R.color.result_view));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        this.width = width;
        this.height = height;
        rect = PicUtils.getRect(width/3*2, width, height);

        //相机预览中心绘制一个正方形，其他地方半透明Rect
        canvas.drawRect(0, 0, width, rect.top, paint);
        canvas.drawRect(0, rect.top, rect.left, rect.bottom, paint);
        canvas.drawRect(rect.right, rect.top, width, rect.bottom, paint);
        canvas.drawRect(0, rect.bottom, width, height, paint);
        postInvalidate();
    }
}
