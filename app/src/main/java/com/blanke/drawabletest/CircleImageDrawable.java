package com.blanke.drawabletest;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/**
 * 圆角imageview
 * Created by blanke on 16-5-5.
 */
public class CircleImageDrawable extends Drawable {
    private Bitmap bitmap;
    private Paint mPaint;
    private float radius;//半径

    public CircleImageDrawable(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.radius = Math.min(bitmap.getWidth(), bitmap.getHeight()) / 2;
        mPaint = new Paint();
        BitmapShader bs = new BitmapShader(bitmap,
                Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint.setAntiAlias(true);
        mPaint.setShader(bs);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(radius, radius, radius, mPaint);
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
