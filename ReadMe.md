# 自定义Drawable Demo(圆角/圆形图片，Material动画)
## RoundImageDrawable（圆角图片）
参考：https://github.com/dinuscxj/LoadingDrawable

1. bitmap图片的设置
 ``` mPaint = new Paint();
          BitmapShader bs = new BitmapShader(bitmap,
                  Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
          mPaint.setAntiAlias(true);
          mPaint.setShader(bs);
 ```
2. 绘制圆角
 `        canvas.drawRoundRect(mRectF, radius, radius, mPaint);
 `
## CircleImageDrawable (圆形图片)
绘制圆形
 `        canvas.drawCircle(radius, radius, radius, mPaint);
 `
## MaterialLoadDrawable
模仿material design progerss动画.
1. 将动画分为两个阶段。

s代表当前弧度开始绘制角度，e代表当前弧度结束绘制角度，
max表示角度的最大值，比如360*0.8,min则相反，比如5度,
cs表示每圈开始绘制的角度
- 第一个阶段：s=cs，e从cs->max+min+cs
- 第二个阶段：e=max+min+cs，s从cs->max+cs

上面是第0圈的主要流程，需要记录的是每次开始的起始角度cs,
每圈结束后的s值即为下一圈的cs值。
- 第0圈，cs=0;
- 第1圈，cs=max;
- 第2圈，cs=(2*max)%360;
- ...

2. 主要代码：
```  private void setup() {
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
```