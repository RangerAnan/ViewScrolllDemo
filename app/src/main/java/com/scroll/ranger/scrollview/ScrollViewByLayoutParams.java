package com.scroll.ranger.scrollview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.scroll.ranger.R;

/**
 * Created by fcl on 2017/5/20.
 * description:滑动方法2,通过设置view的layoutParams间距来实现移动
 * tip:
 */

public class ScrollViewByLayoutParams extends View {

    private float mScrollX;
    private float mScrollY;
    public String tag = "ScrollViewByLayoutParam";

    public ScrollViewByLayoutParams(Context context) {
        super(context);
        init(context);
    }

    public ScrollViewByLayoutParams(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScrollViewByLayoutParams(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
                //设置layoutParams
                //LayoutParams依据父布局而定，其leftMargin也是相对于父布局而言。
                //view.getLeft获取的是该view左边到父view的左边距离
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
                params.leftMargin = getLeft() + offsetX;
                params.topMargin = getTop() + offsetY;
                setLayoutParams(params);
                break;
        }
        return true;
    }
}
