package com.scroll.ranger.scrollview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.scroll.ranger.R;

/**
 * Created by fcl on 2017/5/20.
 * description:通过设置view的scrollTo来实现移动
 * tip:(1)scrollTo移动到某一位置，scrollBy移动多少偏移量。他们都不直接移动view位置，而是移动view的内容来实现。
 * (2)所以，我们想要移动view,可以在他的parentView中去使用它。因为childView本身就是parentView的内容。
 * (3)scrollBy的参数 offset是起始位置在水平、竖直方向的偏移量。offsetX=originX-scrollX.这与我们计算的偏移量值正好相反了。
 */

public class ScrollViewByScrollTo extends View {

    private float mScrollX;
    private float mScrollY;
    public String tag = "ScrollViewByScrollTo";

    public ScrollViewByScrollTo(Context context) {
        super(context);
        init(context);
    }

    public ScrollViewByScrollTo(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScrollViewByScrollTo(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化
     */
    private void init(Context context) {

        setBackgroundColor(ContextCompat.getColor(context, R.color.colorNo1));
        Log.i(tag, "--init----");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mScrollX = x;
                mScrollY = y;
                Log.i(tag, "--ACTION_DOWN----mScrollX:" + mScrollX + "----mScrollY:" + mScrollY);
                break;

            case MotionEvent.ACTION_MOVE:
                //计算偏移量，移动view
                //根据坐标系，向左移动，offSetX为负值，向下移动，offSetY为正值
                int offsetX = (int) (x - mScrollX);
                int offsetY = (int) (y - mScrollY);
                Log.i(tag, "--ACTION_MOVE----offsetX:" + offsetX + "----offsetY:" + offsetY);
                //scrollTo与scrollBy
                //负值是因为scrollBy的offset计算方式时初始位置-移动位置，与我们的计算方式相反。
                ((View) getParent()).scrollBy(-offsetX, -offsetY);
                break;
        }
        return true;
    }
}
