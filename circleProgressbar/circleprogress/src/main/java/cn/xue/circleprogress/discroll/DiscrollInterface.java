package cn.xue.circleprogress.discroll;

/**
 * <pre>
 *     author       : lixue
 *     e-mail       :  lixue326@163.com
 *     time         : 2020/06/19
 *     desc       :
 *     version  : 1.0
 * </pre>
 */
public interface DiscrollInterface {
    //当滑动得到时候调用这个方法, 用来控制里边控件执行响应的动画
     void onDiscroll(float ratio);
    //重置动画== 让view所有的属性都恢复到原来样子
    void  onResetDiscroll();
}
