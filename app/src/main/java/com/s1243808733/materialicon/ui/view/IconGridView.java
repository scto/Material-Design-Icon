package com.s1243808733.materialicon.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import com.s1243808733.materialicon.R;

public class IconGridView extends View {
    private int a;
    private boolean b;
    private boolean c;
    private int d;

    public IconGridView(Context context) {
        this(context, null);
    }

    public IconGridView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public IconGridView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.a = -2139062144;
        this.b = false;
        this.c = false;
        this.d = 2;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.IconGridView);
        this.a = obtainStyledAttributes.getColor(R.styleable.IconGridView_igColor, -2139062144);
        this.b = obtainStyledAttributes.getBoolean(R.styleable.IconGridView_igDash, false);
        this.c = obtainStyledAttributes.getBoolean(R.styleable.IconGridView_igWithGrids, false);
        this.d = obtainStyledAttributes.getInteger(R.styleable.IconGridView_igStyle, 2);
        obtainStyledAttributes.recycle();
    }

    private float a(int i) {
        return TypedValue.applyDimension(1, (float) i, getResources().getDisplayMetrics());
    }

    private Path a() {
        switch (this.d) {
            case 0:
                return b();
            case 1:
                return c();
            case 2:
                return d();
            case 3:
                return e();
            case 4:
                return f();
            default:
                return d();
        }
    }

    private void a(Canvas canvas) {
        canvas.drawPath(a(), h());
        if (this.c) {
            Path g = g();
            Paint h = h();
            h.setAlpha((h.getAlpha() * 50) / 100);
            canvas.drawPath(g, h);
        }
    }

    private Path b() {
        float width = (float) getWidth();
        float height = (float) getHeight();
        Path path = new Path();
        path.addRect(new RectF(0.0f, 0.0f, width, height), Direction.CW);
        return path;
    }

    private Path c() {
        float width = (float) getWidth();
        float height = (float) getHeight();
        Path path = new Path();
        path.addRect(new RectF(0.0f, 0.0f, width, height), Direction.CW);
        path.moveTo(width / 2.0f, 0.0f);
        path.lineTo(width / 2.0f, height);
        path.moveTo(0.0f, height / 2.0f);
        path.lineTo(width, height / 2.0f);
        return path;
    }

    private Path d() {
        float width = (float) getWidth();
        float height = (float) getHeight();
        Path path = new Path();
        path.addRect(new RectF(0.0f, 0.0f, width, height), Direction.CW);
        path.moveTo(width / 2.0f, 0.0f);
        path.lineTo(width / 2.0f, height);
        path.moveTo(0.0f, height / 2.0f);
        path.lineTo(width, height / 2.0f);
        path.moveTo(0.0f, 0.0f);
        path.lineTo(width, height);
        path.moveTo(width, 0.0f);
        path.lineTo(0.0f, height);
        return path;
    }

    private Path e() {
        float width = (float) getWidth();
        float height = (float) getHeight();
        float f = width / 24.0f;
        f = height / 24.0f;
        Path path = new Path();
        path.addRect(new RectF(0.0f, 0.0f, width, height), Direction.CW);
        path.moveTo(width / 2.0f, 0.0f);
        path.lineTo(width / 2.0f, height);
        path.moveTo(0.0f, height / 2.0f);
        path.lineTo(width, height / 2.0f);
        path.moveTo(0.0f, 0.0f);
        path.lineTo(width, height);
        path.moveTo(width, 0.0f);
        path.lineTo(0.0f, height);
        path.moveTo(0.0f, f * 2.0f);
        path.lineTo(width, f * 2.0f);
        path.moveTo(0.0f, f * 22.0f);
        path.lineTo(width, f * 22.0f);
        path.moveTo(f * 2.0f, 0.0f);
        path.lineTo(f * 2.0f, height);
        path.moveTo(f * 22.0f, 0.0f);
        path.lineTo(f * 22.0f, height);
        return path;
    }

    private Path f() {
        float width = (float) getWidth();
        float height = (float) getHeight();
        float f = width / 48.0f;
        float f2 = height / 48.0f;
        Path path = new Path();
        path.addRect(new RectF(0.0f, 0.0f, width, height), Direction.CW);
        path.moveTo(width / 2.0f, 0.0f);
        path.lineTo(width / 2.0f, height);
        path.moveTo(0.0f, height / 2.0f);
        path.lineTo(width, height / 2.0f);
        path.moveTo(0.0f, 0.0f);
        path.lineTo(width, height);
        path.moveTo(width, 0.0f);
        path.lineTo(0.0f, height);
        path.moveTo(0.0f, f2 * 17.0f);
        path.lineTo(width, f2 * 17.0f);
        path.moveTo(0.0f, f2 * 31.0f);
        path.lineTo(width, f2 * 31.0f);
        path.moveTo(f * 17.0f, 0.0f);
        path.lineTo(f * 17.0f, height);
        path.moveTo(f * 31.0f, 0.0f);
        path.lineTo(f * 31.0f, height);
        path.addRoundRect(new RectF(5.0f * f, 5.0f * f2, 43.0f * f, 43.0f * f2), f * 3.0f, f2 * 3.0f, Direction.CW);
        path.addRoundRect(new RectF(f * 2.0f, 8.0f * f2, 46.0f * f, 40.0f * f2), f * 3.0f, f2 * 3.0f, Direction.CW);
        path.addRoundRect(new RectF(8.0f * f, f2 * 2.0f, 40.0f * f, 46.0f * f2), f * 3.0f, f2 * 3.0f, Direction.CW);
        path.addCircle(width / 2.0f, height / 2.0f, 22.0f * f, Direction.CW);
        path.addCircle(width / 2.0f, height / 2.0f, f * 10.0f, Direction.CW);
        return path;
    }

    private Path g() {
        float width = (float) getWidth();
        float height = (float) getHeight();
        float f = width / 48.0f;
        float f2 = height / 48.0f;
        Path path = new Path();
        for (int i = 0; i < 48; i++) {
            path.moveTo(0.0f, ((float) i) * f2);
            path.lineTo(width, ((float) i) * f2);
            path.moveTo(((float) i) * f, 0.0f);
            path.lineTo(((float) i) * f, height);
        }
        return path;
    }

    private Paint h() {
        Paint paint = new Paint();
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(1.0f);
        paint.setColor(this.a);
        paint.setAntiAlias(true);
        if (this.b) {
            float a = a(4);
            paint.setPathEffect(new DashPathEffect(new float[]{a, a}, 1.0f));
        }
        return paint;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        a(canvas);
    }
}
