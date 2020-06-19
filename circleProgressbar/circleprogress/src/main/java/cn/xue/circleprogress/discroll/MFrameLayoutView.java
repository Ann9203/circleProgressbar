package cn.xue.circleprogress.discroll;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * <pre>
 *     author       : lixue
 *     e-mail       :  lixue326@163.com
 *     time         : 2020/06/19
 *     desc       :
 *     version  : 1.0
 * </pre>
 */
public class MFrameLayoutView extends FrameLayout  implements DiscrollInterface{
    private static final int TRANSLATION_FROM_TOP = 0X01;
    private static final int TRANSLATION_FROM_BOTTOM = 0X02;
    private static final int TRANSLATION_FROM_LEFT = 0X04;
    private static final int TRANSLATION_FROM_RIGHT = 0X08;

    //颜色估值器
    private static ArgbEvaluator sArgbEvaluator = new ArgbEvaluator();
    //自定义属性的一些接受的变量
    private int mDiscrollveFromBgColor ;//背景颜色变化开始值
    private int mDiscrollveToBgColor;//背景颜色变化结束值
    private boolean  mDiscrollveAlpha;//是否需要透明度动画
    private int mDiscrollveTranslation;//平移值
    private boolean mDiscrollveScaleX;//是否需要x方向缩放
    private boolean mDiscrollveScaleY;//是否需要Y方向缩放
    private int mHeight;//笨view的高度
    private int mWidth;//宽度

    public static void setsArgbEvaluator(ArgbEvaluator sArgbEvaluator) {
        MFrameLayoutView.sArgbEvaluator = sArgbEvaluator;
    }

    public void setmDiscrollveFromBgColor(int mDiscrollveFromBgColor) {
        this.mDiscrollveFromBgColor = mDiscrollveFromBgColor;
    }

    public void setmDiscrollveToBgColor(int mDiscrollveToBgColor) {
        this.mDiscrollveToBgColor = mDiscrollveToBgColor;
    }

    public void setmDiscrollveAlpha(boolean mDiscrollveAlpha) {
        this.mDiscrollveAlpha = mDiscrollveAlpha;
    }

    public void setmDiscrollveTranslation(int mDiscrollveTranslation) {
        this.mDiscrollveTranslation = mDiscrollveTranslation;
    }

    public void setmDiscrollveScaleX(boolean mDiscrollveScaleX) {
        this.mDiscrollveScaleX = mDiscrollveScaleX;
    }

    public void setmDiscrollveScaleY(boolean mDiscrollveScaleY) {
        this.mDiscrollveScaleY = mDiscrollveScaleY;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }


    public MFrameLayoutView(@NonNull Context context) {
        super(context);
    }

    public MFrameLayoutView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MFrameLayoutView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //执行动画
    @Override
    public void onDiscroll(float ratio) {
        //执行动画
        if (mDiscrollveAlpha){
            setAlpha(ratio);
        }
        if (mDiscrollveScaleX){
            setScaleX(ratio);
        }
        if (mDiscrollveScaleY){
            setScaleY(ratio);
        }

        if (isTranslationFrom(TRANSLATION_FROM_BOTTOM)){
            setTranslationY(mHeight*(1-ratio));
        }
        if (isTranslationFrom(TRANSLATION_FROM_TOP)){
            setTranslationY(-mHeight*(1-ratio));
        }
        if(isTranslationFrom(TRANSLATION_FROM_LEFT)){
            setTranslationX(-mWidth*(1-ratio));
        }
        if (isTranslationFrom(TRANSLATION_FROM_RIGHT)){
            setTranslationX(mWidth*(1-ratio));
        }
        //判断颜色变化
        if (mDiscrollveFromBgColor !=-1 && mDiscrollveToBgColor !=-1){
            setBackgroundColor((Integer) sArgbEvaluator.evaluate(ratio, mDiscrollveFromBgColor, mDiscrollveToBgColor));
        }

    }

    //重置
    @Override
    public void onResetDiscroll() {
        if (mDiscrollveAlpha){
            setAlpha(0);
        }
        if (mDiscrollveScaleX){
            setScaleX(0);
        }
        if (mDiscrollveScaleY){
            setScaleX(0);
        }
        //平移动画 int值  left right top bottom
        if (isTranslationFrom(TRANSLATION_FROM_BOTTOM)){
            setTranslationY(mHeight);
        }
        if (isTranslationFrom(TRANSLATION_FROM_TOP)){
            setTranslationY(-mHeight);
        }
        if (isTranslationFrom(TRANSLATION_FROM_LEFT)){
            setTranslationX(-mWidth);
        }
        if (isTranslationFrom(TRANSLATION_FROM_RIGHT)){
            setTranslationX(mWidth);
        }

    }

    private boolean isTranslationFrom(int translationMask){
        if (mDiscrollveTranslation == -1){
            return false;
        }
        return  (mDiscrollveTranslation & translationMask) == translationMask;
    }
}
