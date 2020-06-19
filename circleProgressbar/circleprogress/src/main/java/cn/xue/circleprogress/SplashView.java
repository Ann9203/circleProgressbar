package cn.xue.circleprogress;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.Nullable;

/**
 * <pre>
 *     author       : lixue
 *     e-mail       :  lixue326@163.com
 *     time         : 2020/06/19
 *     desc       : 圆圈转动 再回收
 *     version  : 1.0
 * </pre>
 */
public class SplashView extends View {
    private ValueAnimator mAnimator;
    //大圆
    private float mRotationRadius = 90;
    //每个小圆半径
    private float mCircleRadius = 18;
    //小圆圈的颜色列表, 在initiallize方法里边初始化
    private int[] mCircleColors;
    //大圆和小圆旋转时间
    private long mRotaionDuration = 1200;
    //第二部分动画执行的总时间 (包括两个动画时间各站1/2)
    private long mSplashDuration = 1200;
    //整体背景颜色
    private int  mSplashBgColor = Color.WHITE;

    //空心圆的半径
    private float mHoleRadius = 0F;
    //当前大圆旋转角度  (弧度)
    private float mCurrentRotaionAngle = 0F;
    //当前大圆的半径
    private float mCurrentRotaionRadius = mRotationRadius;

    //绘制圆的画笔
    private Paint mPaint = new Paint();
    //绘制背景的画笔
    private Paint mPaintBackground = new Paint();
    //屏幕正中心坐标
    private float mCenterX;
    private float mCenterY;
    //屏幕对角线一半
    private float mDiagonalDist;

    private SplashState mState = null;


    public SplashView(Context context) {
        super(context);
        init(context);
    }

    public SplashView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SplashView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mCircleColors = context.getResources().getIntArray(R.array.splash_circle_colors);
        //画笔初始化
        //x消除锯齿
        mPaint.setAntiAlias(true);
        mPaintBackground.setAntiAlias(true);
        //设置样式--边框样式--描边
        mPaintBackground.setStyle(Paint.Style.STROKE);
        mPaintBackground.setColor(mSplashBgColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w/2f;
        mCenterY=h/2f;
        mDiagonalDist = (float) (Math.sqrt((w*w+h*h))/2f); //勾股定理
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mState == null){
            mState = new RotateState();
        }
        mState.drawState(canvas);
    }

    //绘制背景
    private void drawBackground(Canvas canvas){
        if(mHoleRadius >0f){
            mPaintBackground.setColor(Color.BLACK);
            float strokeWidth = mDiagonalDist-mHoleRadius;
            mPaintBackground.setStrokeWidth(strokeWidth);
            float radius = mHoleRadius +strokeWidth/2;
            canvas.drawCircle(mCenterX, mCenterY, radius, mPaintBackground);
        } else {
            canvas.drawColor(mSplashBgColor);
        }
    }

    //绘制圆圈
    private void drawCircles(Canvas canvas){
        //每个小圆之间的间隔角度=2π/小圆的个数
        float rotetionAngle = (float) (2*Math.PI/mCircleColors.length);
        for (int i =0; i < mCircleColors.length ;i++){
            double angle = i* rotetionAngle+mCurrentRotaionAngle;
            float cx = (float) (mCurrentRotaionRadius * Math.cos(angle)+mCenterX);
            float cy = (float) (mCurrentRotaionRadius*Math.sin(angle)+mCenterY);
            mPaint.setColor(mCircleColors[i]);
            canvas.drawCircle(cx, cy, mCircleRadius, mPaint);
        }
    }

    /**
     * 开始动画
     */
    public void splashDisappear(){
        if (mState != null && mState instanceof RotateState){
            RotateState rotateState = (RotateState) mState;
            rotateState.cancel();
            post(new Runnable() {
                @Override
                public void run() {
                    mState = new MergingState();
                }
            });
        }
    }

    //策略模式
    private abstract class SplashState {
        public abstract void drawState(Canvas canvas);

        public void cancel() {
            mAnimator.cancel();
        }
    }

    //1旋转动画
    private class RotateState extends SplashState{
        public RotateState(){
            //动画初始工作 2.开启动画
            mAnimator = ValueAnimator.ofFloat(0f, (float)Math.PI*2);
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentRotaionAngle = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            mAnimator.setDuration(mRotaionDuration);
            //无线循环
            mAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mAnimator.start();
        }

        @Override
        public void drawState(Canvas canvas) {
            //绘制背景
            drawBackground(canvas);
            //绘制小圆
            drawCircles(canvas);
        }
    }


    /**
     * 扩散动画
     */
    private class MergingState extends SplashState{

        public MergingState(){
            mAnimator = ValueAnimator .ofFloat(0f, mRotationRadius);
            mAnimator.setDuration(mRotaionDuration);
            mAnimator.setInterpolator(new OvershootInterpolator(10f));
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentRotaionRadius = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mState = new ExpanState();
                }
            });
            //翻转
            mAnimator.reverse();
        }
      @Override
      public void drawState(Canvas canvas) {
            //绘制背景
          drawBackground(canvas);
          //绘制小圆
          drawCircles(canvas);
      }
  }

    /**
     * 水波纹扩散
     */
  private class ExpanState extends SplashState{

      public ExpanState(){
          mAnimator = ValueAnimator.ofFloat(mCircleRadius, mDiagonalDist);
          mAnimator.setDuration(mRotaionDuration);
          mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
              @Override
              public void onAnimationUpdate(ValueAnimator animation) {
                  mHoleRadius = (float) animation.getAnimatedValue();
                  postInvalidate();
              }
          });
          mAnimator.start();
      }
      @Override
      public void drawState(Canvas canvas) {
          drawBackground(canvas);
      }
  }

}
