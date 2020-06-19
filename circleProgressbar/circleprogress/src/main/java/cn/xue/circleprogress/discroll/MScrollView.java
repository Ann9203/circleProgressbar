package cn.xue.circleprogress.discroll;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

/**
 * <pre>
 *     author       : lixue
 *     e-mail       :  lixue326@163.com
 *     time         : 2020/06/19
 *     desc       :
 *     version  : 1.0
 * </pre>
 */
public class MScrollView extends NestedScrollView {
    private MLinearLayoutView mContent;

    public MScrollView(@NonNull Context context) {
        super(context);
    }

    public MScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContent = (MLinearLayoutView) getChildAt(0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        View first =  mContent.getChildAt(0);
        first.getLayoutParams().height = getHeight();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        int scrollViewHeight = getHeight();//scrollView窗口的高度
        //监听滑动的程度 childView下面冒出来多少距离 /childView.getHeight 0-1 动画执行的百分比
        //动画执行的百分比ratio控制动画执行
        for (int i =0; i < mContent.getChildCount(); i++){
            View child = mContent.getChildAt(i);
            int childHeight = child.getHeight();
            if (!(child instanceof DiscrollInterface)){
                continue;
            }
            //接口回掉 传递执行的百分比给MyFragmeLayout
            //低耦合高内聚
            DiscrollInterface discrollInterface = (DiscrollInterface) child;
            int childTop = child.getTop();
            //花出去的之一接高度
            int absoluteTop = childTop-t;
            if (absoluteTop <= scrollViewHeight){
                //child浮现的高度= ScrollView的高度-child离屏幕顶部的高度
                int visibleGap = scrollViewHeight - absoluteTop;
                float ratio = visibleGap/(float)childHeight;
                discrollInterface.onDiscroll(clamp(ratio, 1f, 0f));
            } else {
                discrollInterface.onResetDiscroll();
            }
        }

    }

    public static float clamp(float valut, float max, float min){
        return Math.max(Math.min(valut, max), min);
    }
}
