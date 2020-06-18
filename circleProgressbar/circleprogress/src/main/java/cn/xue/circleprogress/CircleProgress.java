package cn.xue.circleprogress;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.Nullable;

/**
 * <pre>
 *     author       : lixue
 *     time         :  2020-06-09 16:22
 *    desc       :
 * </pre>
 */


public class CircleProgress extends View {
    private Paint mCirclePaint;
    private Paint mRingPaint;
    private Paint mRingPaintBg;

    private Paint mTextPaint;
    private int mCircleColor;
    private int mRingColor;
    private int mRingBgColor;
    private float mRadius; // 半径
    private float mRingRadius; //圆环半径
    private float mStrokeWidth;
    private int mXCenter;
    private int mYCenter;

    private int mProgress;
    private String str;
    RectF oval;
    private ValueAnimator animator;
    private int currentProgress;

    public CircleProgress(Context context) {
        this(context, null);
    }

    public CircleProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.CircleProgress, 0, 0);
        mRadius = typedArray.getDimension(R.styleable.CircleProgress_radius, 80);
        mStrokeWidth = typedArray.getDimension(R.styleable.CircleProgress_strokeWidth, 20);
        mCircleColor = typedArray.getColor(R.styleable.CircleProgress_circleColor, 0xFFFFFFFF);
        mRingColor = typedArray.getColor(R.styleable.CircleProgress_ringColor, 0xFFFFFFFF);
        mRingBgColor = typedArray.getColor(R.styleable.CircleProgress_ringBgColor, 0xFFFFFFFF);
        mRingRadius = mRadius + mStrokeWidth / 2;
        initPaint();
    }

    private void initPaint() {
        oval = new RectF();
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setStrokeCap(Paint.Cap.ROUND);

        //外圆弧背景
        mRingPaintBg = new Paint();
        mRingPaintBg.setAntiAlias(true);
        mRingPaintBg.setColor(mRingBgColor);
        mRingPaintBg.setStyle(Paint.Style.STROKE);
        mRingPaintBg.setStrokeWidth(mStrokeWidth);

        //外圆弧
        mRingPaint = new Paint();
        mRingPaint.setAntiAlias(true);
        mRingPaint.setColor(mRingColor);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setStrokeWidth(mStrokeWidth);
        mRingPaint.setStrokeCap(Paint.Cap.ROUND);

        //中间字
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(mRingColor);
        mTextPaint.setTextSize(mRadius / 2);

    }

    SweepGradient sweepGradient;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mXCenter = getWidth() / 2;
        mYCenter = getHeight() / 2;
        canvas.drawCircle(mXCenter, mYCenter, mRadius, mCirclePaint);
        RectF rectF = new RectF();
        rectF.left = (mXCenter - mRingRadius);
        rectF.top = (mYCenter - mRingRadius);
        rectF.right = mRingRadius * 2 + (mXCenter - mRingRadius);
        rectF.bottom = mRingRadius * 2 + (mYCenter - mRingRadius);
        canvas.drawArc(rectF, 0, 360, false, mRingPaintBg);

        //外圆弧
        if (mProgress > 0) {
            oval.left = (mXCenter - mRingRadius);
            oval.top = (mYCenter - mRingRadius);
            oval.right = mRingRadius * 2 + (mXCenter - mRingRadius);
            oval.bottom = mRingRadius * 2 + (mYCenter - mRingRadius);
            String text = currentProgress + "%";
            float textWidth = mTextPaint.measureText(text);
            float textHeight = (mTextPaint.descent() + mTextPaint.ascent()) / 2;
            canvas.drawText(text, mXCenter - textWidth / 2, mYCenter - textHeight / 2, mTextPaint);

            canvas.drawArc(oval, 275, 360 * mProgress / 100, false, mRingPaint);
            //

        }
    }

    //设置进度
    public void setProgress(int progress) {
        mProgress = progress;
        postInvalidate();//重绘
    }

    public void setProgress(int progress, long animationTime) {
        if (animationTime <= 0) setProgress(progress);
        else {
            init(progress);
            animator = ValueAnimator.ofInt(mProgress, progress);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(animationTime);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mProgress = (int) animation.getAnimatedValue();
                    updatePaint(mProgress);
                    currentProgress = mProgress;
                    if (mProgress > 100) {
                        mProgress = mProgress - mProgress / 100 * 100;
                    }
                    invalidate();//重绘
                }
            });

            animator.start();
        }
    }

    private void init(int progress) {
        if (progress < 31) {
            mRingPaint.setColor(Color.parseColor("#FFFAC702"));
            mTextPaint.setColor(Color.parseColor("#FFFAC702"));
        } else if (progress < 101) {
            mRingPaint.setColor(Color.parseColor("#FF2EE069"));
            mTextPaint.setColor(Color.parseColor("#FF2EE069"));
        }

    }

    private void updatePaint(int mProgress) {
        //外圆弧背景
        if (mProgress <= 100) {
            mRingPaintBg.setColor(Color.parseColor("#ececec"));
        } else {
            mRingPaintBg.setColor(Color.parseColor("#FF2EE069"));
        }
        if (mProgress > 100) {
            //先创建一个渲染器
            mTextPaint.setColor(Color.parseColor("#FF2EE069"));
            if (sweepGradient == null) {
                int[] arcColors = new int[]{Color.parseColor("#FF2EE069"), Color.parseColor(
                        "#FF00B075")};
                float[] arcPostion = new float[]{0.0f, 0.95f};
                sweepGradient = new SweepGradient(mXCenter, mYCenter, arcColors, arcPostion);
                Matrix matrix = new Matrix();
                matrix.setRotate(-97, mXCenter, mYCenter);
                sweepGradient.setLocalMatrix(matrix);
                mRingPaint.setShader(sweepGradient);
            }

//把渐变设置到笔刷
            mRingPaint.setShader(sweepGradient);
        }
    }

}
