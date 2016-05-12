package com.blanke.drawabletest;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/**
 * 圆角imageview
 * Created by blanke on 16-5-5.
 */
public class RoundImageDrawable extends Drawable {
    private Bitmap bitmap;
    private Paint mPaint;
    private RectF mRectF;
    private float radius;
    private final static float DEFAULT_RADIUS = 10.0F;

    public RoundImageDrawable(Bitmap bitmap) {
        this(bitmap, DEFAULT_RADIUS);
    }

    public RoundImageDrawable(Bitmap bitmap, float radius) {
        this.bitmap = bitmap;
        this.radius = radius;
        mPaint = new Paint();
        BitmapShader bs = new BitmapShader(bitmap,
                Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint.setAntiAlias(true);
        mPaint.setShader(bs);
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        mRectF = new RectF(left, top, right, bottom);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRoundRect(mRectF, radius, radius, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    @Override
    public int getIntrinsicHeight() {
        return bitmap.getHeight();
    }

    @Override
    public int getIntrinsicWidth() {
        return bitmap.getWidth();
    }
}
