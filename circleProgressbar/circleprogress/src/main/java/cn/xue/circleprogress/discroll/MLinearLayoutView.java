package cn.xue.circleprogress.discroll;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import cn.xue.circleprogress.R;

/**
 * <pre>
 *     author       : lixue
 *     e-mail       :  lixue326@163.com
 *     time         : 2020/06/19
 *     desc       :
 *     version  : 1.0
 * </pre>
 */
public class MLinearLayoutView extends LinearLayout {


    public MLinearLayoutView(Context context) {
        super(context);
        setOrientation(VERTICAL);
    }

    public MLinearLayoutView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    public MLinearLayoutView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MyLayoutParms(getContext(), attrs);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        MyLayoutParms p = (MyLayoutParms) params;
        if (!isDiscrollable(p)){
            super.addView(child, params);
        } else {
            MFrameLayoutView fm = new MFrameLayoutView(getContext());
            fm.setmDiscrollveAlpha(p.mDiscrollAlpha);
            fm.setmDiscrollveFromBgColor(p.mDiscrollFromBgColor);
            fm.setmDiscrollveToBgColor(p.mDiscrollToBgColor);
            fm.setmDiscrollveScaleX(p.mDiscrollScaleX);
            fm.setmDiscrollveScaleY(p.mDiscrollScaleY);
            fm.setmDiscrollveTranslation(p.mDiscrollTranslation);
            fm.addView(child);
            super.addView(fm, params);
        }
    }

    /**
     * 判断是否有自定义属性
     *
     * @param parms
     * @return
     */
    public boolean isDiscrollable(MyLayoutParms parms) {
        return parms.mDiscrollAlpha ||
                parms.mDiscrollScaleX ||
                parms.mDiscrollScaleY ||
                parms.mDiscrollTranslation != -1 ||
                (parms.mDiscrollFromBgColor != -1 && parms.mDiscrollToBgColor != -1);
    }

    //自定义params为了获取到自定义的属性
    public class MyLayoutParms extends LayoutParams {
        private int mDiscrollFromBgColor;//颜色变化开始值
        private int mDiscrollToBgColor;//颜色变化结束值
        private boolean mDiscrollAlpha;//是否需要透明度画面
        private int mDiscrollTranslation;//平移值
        private boolean mDiscrollScaleX;//是否需要x轴平移
        private boolean mDiscrollScaleY;//是否需要y轴平移

        public MyLayoutParms(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DiscrollView_LayoutParams);
            mDiscrollFromBgColor = a.getColor(R.styleable.DiscrollView_LayoutParams_discrollve_fromBgColor, -1);
            mDiscrollToBgColor = a.getColor(R.styleable.DiscrollView_LayoutParams_discrollve_toBgColor, -1);
            mDiscrollAlpha = a.getBoolean(R.styleable.DiscrollView_LayoutParams_discrollve_alpha, false);
            mDiscrollScaleX = a.getBoolean(R.styleable.DiscrollView_LayoutParams_discrollve_scaleX, false);
            mDiscrollScaleY = a.getBoolean(R.styleable.DiscrollView_LayoutParams_discrollve_scaleY, false);
            mDiscrollTranslation = a.getInt(R.styleable.DiscrollView_LayoutParams_discrollve_translation, -1);
            a.recycle();
        }
    }
}
