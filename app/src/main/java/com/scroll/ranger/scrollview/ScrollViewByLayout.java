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
 * description:滑动方法1,通过view的layout方法实现位置变换
 * tip:(1)明确view坐标结构，x轴以右为正，Y轴以下为正
 * (2)在使用layout时注意参数是相对父控件的位置
 * (3)刚开始我是这样写的：在down中获取初始scrollX与scrollY的值，在move中通过getX获取第二次scrollX的值，结果发现此时scrollX并没有变话，无法移动.为什么呢？
 * 因为该控件默认就是无法滑动的，那么第一次offSet的值为0，该view无法滑动。此时若一定要让他滑动，可以设置一个初始的offset值即可，当然这样就与自己的需求不符了（view随手指滑动）
 */

public class ScrollViewByLayout extends View {

    private float mScrollX;
    private float mScrollY;
    public String tag = "ScrollViewByLayout";

    public ScrollViewByLayout(Context context) {
        super(context);
        init(context);
    }

    public ScrollViewByLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScrollViewByLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
//                mScrollX = getX();
//                mScrollY = getY();
                Log.i(tag, "--ACTION_DOWN----mScrollX:" + mScrollX + "----mScrollY:" + mScrollY);
                break;

            case MotionEvent.ACTION_MOVE:
                //计算偏移量，移动view
                //根据坐标系，向左移动，offSetX为负值，向下移动，offSetY为正值
                int offsetX = (int) (x - mScrollX);
                int offsetY = (int) (y - mScrollY);
//                int offsetX = (int) (getX() - mScrollX);
//                int offsetY = (int) (getY() - mScrollY);
//                if (offsetX == 0) {
//                    //test:赋值让他第一次能滑动起来
//                    offsetX = 10;
//                }
//                if (offsetY == 0) {
//                    offsetY = 10;
//                }
                Log.i(tag, "--ACTION_MOVE----offsetX:" + offsetX + "----offsetY:" + offsetY);
                //view.getLeft获取的是该view左边到父view的左边距离
                //这里为什么要添加getLeft方法呢？因为layout的参数都是基于父view的相对位置，可以看源码介绍。
                layout(getLeft() + offsetX, getTop() + offsetY, getRight() + offsetX, getBottom() + offsetY);
                break;
        }
        return true;
    }
}
