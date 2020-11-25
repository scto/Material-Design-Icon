package com.s1243808733.materialicon.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import androidx.core.graphics.ColorUtils;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import com.s1243808733.materialicon.R;
import java.util.Locale;
import android.graphics.PixelFormat;

public class ColorBackgroundTextView extends AppCompatTextView {

    private int rectangleSize = 16;

    private int color;

    public ColorBackgroundTextView(Context context) {
        super(context);
        init();
    }

    public ColorBackgroundTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorBackgroundTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(0,
                                         0,
                                         view.getWidth(),
                                         view.getHeight(),
                                         getResources().getDimension(R.dimen.card_bg_radius));
                }
            });
        setClipToOutline(true);
    }

    public void setRectangleSize(int rectangleSize) {
        this.rectangleSize = rectangleSize;
    }

    public void setColor(int color) {
        this.color = color;

        double luminance = ColorUtils.calculateLuminance(color);
        if (luminance > 0.91f) {
            LayerDrawable ld = new LayerDrawable(new Drawable[]{
                                                     new AlphaPatternDrawable(rectangleSize, color),
                                                     getResources().getDrawable(R.drawable.select_color_card_bg)
                                                 });
            setBackgroundDrawable(ld);
        } else {
            setBackgroundDrawable(new AlphaPatternDrawable(rectangleSize, color));
        }

        setText(convertToARGB(color));
        String colorStr = convertToARGB(color);
        // #00 FFFFFF 去掉透明元素
        setTextColor(Color.parseColor("#" + colorStr.substring(3)) ^ 0x00FFFFFF);
    }

    public int getColor() {
        return color;
    }

    private String convertToARGB(int color) {
        String alpha = Integer.toHexString(Color.alpha(color));
        String red = Integer.toHexString(Color.red(color));
        String green = Integer.toHexString(Color.green(color));
        String blue = Integer.toHexString(Color.blue(color));

        if (alpha.length() == 1) {
            alpha = "0" + alpha;
        }

        if (red.length() == 1) {
            red = "0" + red;
        }

        if (green.length() == 1) {
            green = "0" + green;
        }

        if (blue.length() == 1) {
            blue = "0" + blue;
        }

        return ("#" + alpha + red + green + blue).toUpperCase(Locale.ENGLISH);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            setClickable(true);
            setAlpha(1f);
        } else {
            setClickable(false);
            setAlpha(0.7f);
        }
    }


    public class AlphaPatternDrawable extends Drawable {

        @Override
        public void setAlpha(int p1) {
        }

        @Override
        public void setColorFilter(ColorFilter p1) {
        }

        private int mRectangleSize;

        private Paint mPaint = new Paint();
        private Paint mPaintWhite = new Paint();
        private Paint mPaintGray = new Paint();

        private int numRectanglesHorizontal;
        private int numRectanglesVertical;

        /**
         * Bitmap in which the pattern will be cahched.
         */
        private Bitmap mBitmap;

        private int backgroundColor=Color.TRANSPARENT;

        {
            mPaintWhite.setColor(0xffffffff);
            mPaintGray.setColor(0xffcbcbcb);
        }

        public AlphaPatternDrawable(int rectangleSize, int backgroundColor) {
            mRectangleSize = rectangleSize;
            this.backgroundColor = backgroundColor;
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawBitmap(mBitmap, null, getBounds(), mPaint);
            canvas.drawColor(backgroundColor);
        }

        @Override
        public int getOpacity() {
            return PixelFormat.UNKNOWN;
        }

        @Override
        protected void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);

            int height = bounds.height();
            int width = bounds.width();

            numRectanglesHorizontal = (int) Math.ceil((width / mRectangleSize));
            numRectanglesVertical = (int) Math.ceil(height / mRectangleSize);

            generatePatternBitmap();

        }

        /**
         * This will generate a bitmap with the pattern
         * as big as the rectangle we were allow to draw on.
         * We do this to chache the bitmap so we don't need to
         * recreate it each time draw() is called since it
         * takes a few milliseconds.
         */
        private void generatePatternBitmap() {

            if (getBounds().width() <= 0 || getBounds().height() <= 0) {
                return;
            }

            mBitmap = Bitmap.createBitmap(getBounds().width(), getBounds().height(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(mBitmap);

            Rect r = new Rect();
            boolean verticalStartWhite = true;
            for (int i = 0; i <= numRectanglesVertical; i++) {

                boolean isWhite = verticalStartWhite;
                for (int j = 0; j <= numRectanglesHorizontal; j++) {

                    r.top = i * mRectangleSize;
                    r.left = j * mRectangleSize;
                    r.bottom = r.top + mRectangleSize;
                    r.right = r.left + mRectangleSize;

                    canvas.drawRect(r, isWhite ? mPaintWhite : mPaintGray);

                    isWhite = !isWhite;
                }

                verticalStartWhite = !verticalStartWhite;

            }

        }

    }


}


