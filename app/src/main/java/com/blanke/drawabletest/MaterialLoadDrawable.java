package com.blanke.drawabletest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.view.animation.FastOutSlowInInterpolator;

/**
 * MaterialLoad  动画
 * 动画分为两个阶段，
 * 第一阶段，startAngle不变，endAngle增加到一定角度，比如0.5，
 * 第二阶段，endAngle不变，startAngle减小到最小角度
 * Created by blanke on 16-5-6.
 */
public class MaterialLoadDrawable extends Drawable {

    private final static int DEFAULT_COLOR = Color.BLUE;//默认颜色
    private final static int DEFAULT_SIZE = 200;//默认大小
    private final static long DEFAULT_DURATION = 3000;//默认显示时间
    private final static float DEFAULT_INSET = 10;//内边距
    private final static int DEFAULT_STROKE_WIDTH = 17;//线条宽度
    private final static float DEFAULT_MINDFRACTION = 0.5F;//两个阶段的中间值
    private final static float DEFAULT_MAX_ANGLE = 360 * 0.85F;//最大角度
    private final static float DEFAULT_MIN_ANGLE = 5.0F;//最小角度
    private long mDuration;
    private int mColor;
    private int mStrokeWidth;
    private int mWidth, mHeight;
    private Paint mPaint;
    private float mCanvasRotate;
    private RectF rectf;
    private float mEndAngle;
    private float mStartAngle;
    private ValueAnimator mAnimator;//动画实现依靠属性动画
    private float mMindFraction;
    private float mInsetValue;
    private float mCurrentStartAngle;
    private int count;

    public MaterialLoadDrawable() {
        setup();
    }

    private void setup() {
        mColor = DEFAULT_COLOR;
        mWidth = mHeight = DEFAULT_SIZE;
        mInsetValue = DEFAULT_INSET;
        mMindFraction = DEFAULT_MINDFRACTION;
        mStrokeWidth = DEFAULT_STROKE_WIDTH;
        rectf = new RectF();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mDuration = DEFAULT_DURATION;
        mAnimator = ValueAnimator.ofFloat(0F, 1F);//属性动画初始化
        mAnimator.setDuration(mDuration);
        mAnimator.setRepeatMode(ValueAnimator.RESTART);//设置反复动画
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setInterpolator(new FastOutSlowInInterpolator());//插值器
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animUpdate(animation.getAnimatedFraction());
                invalidateSelf();
            }
        });
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                count++;
                mCurrentStartAngle = mStartAngle;//保存开始角度
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                count = 0;
            }
        });
        mCurrentStartAngle = 0;
    }

    public void start() {
        mAnimator.start();
    }

    public void stop() {
        mAnimator.cancel();
    }

    private void animUpdate(float fraction) {
        mCanvasRotate += 2;//画布旋转每次增加量，可自己调节
        if (fraction <= mMindFraction) {//第一阶段
            //最小角度是值，fraction为0时，还有一小段弧。思路是每次绘制多绘制minAngle的角度
            mEndAngle = (DEFAULT_MAX_ANGLE + DEFAULT_MIN_ANGLE)
                    * (fraction / mMindFraction);//end只是相对start的位移
            // 绘制总长度：（maxAngle+minAngle）/2，当前进度fraction/当前阶段百分比
            if (mEndAngle < DEFAULT_MIN_ANGLE && count > 0) {//当绘制角度小于最小角度时，绘制最小角度
                mEndAngle = DEFAULT_MIN_ANGLE;//第一圈不执行
            }
        } else {//第二阶段
            //绘制总角度是maxAngle，后面是所占百分比
            mStartAngle = (DEFAULT_MAX_ANGLE) *
                    ((fraction - mMindFraction) / (1 - mMindFraction));
            //endAngle是总角度减startAngle
            mEndAngle = DEFAULT_MAX_ANGLE + DEFAULT_MIN_ANGLE - mStartAngle;
            mStartAngle = (mStartAngle + mCurrentStartAngle) % 360;
        }
//        System.out.println(fraction + "\t\t" + mStartAngle + "\t\t" + (mEndAngle + mStartAngle));
    }

    @Override
    public void draw(Canvas canvas) {
        int canvasCopy = canvas.save();//保存画布
        rectf.set(getBounds());
        canvas.rotate(mCanvasRotate, getCenterX(), getCenterY());//旋转画布
        rectf.inset(mInsetValue, mInsetValue);
        canvas.drawArc(rectf, mStartAngle, mEndAngle, false, mPaint);
        canvas.restoreToCount(canvasCopy);//恢复画布
    }

    private float getCenterX() {
        return mWidth / 2;
    }

    private float getCenterY() {
        return mHeight / 2;
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
    public int getIntrinsicWidth() {
        return mWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return mHeight;
    }
}
