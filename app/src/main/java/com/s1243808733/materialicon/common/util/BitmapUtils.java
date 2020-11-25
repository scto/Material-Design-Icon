package com.s1243808733.materialicon.common.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class BitmapUtils {
    
    public static Bitmap drawColorExtractAlpha(Bitmap src, int color, boolean recycle) {
        Bitmap dest = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(dest);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);

        Bitmap alpha = src.extractAlpha();
        canvas.drawBitmap(alpha, 0, 0, paint);

        if(recycle && !src.isRecycled()) src.recycle();
        return dest;
    }
    
}
