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
 * description:通过view的offsetLeftAndRight方法来移动
 * tip:
 */

public class ScrollViewByOffSet extends View {

    private float mScrollX;
    private float mScrollY;
    public String tag = "ScrollViewByOffSet";

    public ScrollViewByOffSet(Context context) {
        super(context);
        init(context);
    }

    public ScrollViewByOffSet(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScrollViewByOffSet(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
                offsetLeftAndRight(offsetX);
                offsetTopAndBottom(offsetY);
                break;
        }
        return true;
    }
}
