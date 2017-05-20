package com.scroll.ranger.scrollview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.scroll.ranger.R;

/**
 * Created by fcl on 2017/5/20.
 * description:通过scroller来实现移动
 * tips:
 * (1)scroller内部使用scrollTo来实现的，但是它会根据移动的距离与时间，计算每次移动的距离，不断移动，以达到缓慢移动的效果。
 * (2)scrollBy与scrollTo都是跟踪距离一次移动，瞬间完成移动。
 * (3)scroller滑动机制：startScroll-->updateView-->onDraw()-->回调computeScroll()-->computeScroll()方法中计算判断是否滑到目的地-->未完成会调用scrollTo继续滑动
 * updateView-->进行循环直至滑动目的地
 */

public class ScrollViewByScroller extends View {

    private float mScrollX;
    private float mScrollY;
    public String tag = "ScrollViewByScroller";
    private Scroller scroller;                      //滑动辅助计算类

    public ScrollViewByScroller(Context context) {
        super(context);
        init(context);
    }

    public ScrollViewByScroller(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScrollViewByScroller(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化
     */
    private void init(Context context) {
        Log.i(tag, "--init----");

        setBackgroundColor(ContextCompat.getColor(context, R.color.colorNo1));
        scroller = new Scroller(context);
    }

    /**
     * 松开手指，滑动原来位置
     *
     * @param event
     * @return
     */
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

            case MotionEvent.ACTION_UP:
                View parentView = (View) getParent();
                //ScrollY 和 ScrollX 记录了使用 scrollBy 进行偏移的量
                int scrollX = parentView.getScrollX();
                int scrollY = parentView.getScrollY();
                Log.i(tag, "--ACTION_UP----scrollX:" + scrollX + "----scrollY:" + scrollY);
                //这里把ScrollX作为起始位置，目的地为他们的负数，即偏移量为0的位置，也是view在没有移动之前的位置。
                //注意这里的参数：正数左上，负数右下。
                scroller.startScroll(scrollX, scrollY, -scrollX, -scrollY, 800);
                //刷新view.
                invalidate();
                break;
        }
        return true;
    }

    /**
     * computeScroll方法由onDraw方法调用
     */
    @Override
    public void computeScroll() {
        super.computeScroll();
        int currX = scroller.getCurrX();
        Log.i(tag, "----computeScroll---" + scroller.computeScrollOffset() + "----currX:" + currX);
        //判断是否移动结束,如果到达目的坐标 false
        if (scroller.computeScrollOffset()) {
            //使用 scrollTo 方法进行移动，参数是从 scroller 的 getCurrX 以及 getCurrY 方法得到的，
            // 这两个参数每次在执行 computeScrollOffset 之后都会改变，会越来越接近目的坐标。
            ((View) getParent()).scrollTo(scroller.getCurrX(), scroller.getCurrY());

            //再次刷新，绘制界面，循环滑动
            invalidate();
        }

    }
}
