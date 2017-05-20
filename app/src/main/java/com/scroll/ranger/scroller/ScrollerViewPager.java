package com.scroll.ranger.scroller;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by fcl on 2017/5/20.
 * description:使用scroller实现viewpager的效果
 * todo:
 * (1)ViewPager会根据用户手指滑动速度的快慢来决定是否要翻页
 * (2)子view只能使用clickable=true的控件
 * (3)viewpager每个childView不全屏显示
 */

public class ScrollerViewPager extends ViewGroup {

    private Scroller overScroller;                                          //滑动辅助类
    private int mTouchSlop;                                                //系统所能识别的最小滑动距离

    private int leftBorder;                                                //界面可滚动的左边界
    private int rightBorder;

    private int xDown;                                                      //按下的位置
    private int xLastPosition;
    private int xMove;

    private String tag = "ScrollerViewPager";

    public ScrollerViewPager(Context context) {
        super(context);
        init(context);
    }


    public ScrollerViewPager(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScrollerViewPager(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        overScroller = new Scroller(context);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        mTouchSlop = viewConfiguration.getScaledPagingTouchSlop();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //measure childView
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            // measure width/height of the childView and padding of the parentView
            measureChild(childAt, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!changed) {
            return;
        }
        //layout childView
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            // layout all childView on the horizontal
            //水平方向子view一路排开，但是当前可见的只有一个，其他子view都在屏幕外
            childAt.layout(i * childAt.getMeasuredWidth(), 0, (i + 1) * childAt.getMeasuredWidth(), childAt.getMeasuredHeight());
        }

        leftBorder = getChildAt(0).getLeft();
        rightBorder = getChildAt(childCount - 1).getRight();
        Log.i(tag, "--leftBorder:" + leftBorder + "----rightBorder:" + rightBorder);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //按下时记录位置
                xDown = (int) ev.getRawX();
                xLastPosition = xDown;
                break;
            case MotionEvent.ACTION_MOVE:
                //计算offset，拦截事件
                xMove = (int) ev.getRawX();
                xLastPosition = xMove;
                if (Math.abs(xMove - xDown) > mTouchSlop) {
                    //进行滚动
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                xMove = (int) event.getRawX();
                int scrollX = xLastPosition - xMove;
                Log.i(tag, "--onTouchEvent--scrollX:" + scrollX + "---xLastPosition:" + xLastPosition + "---xMove:" + xMove);
                if (getScrollX() + scrollX < leftBorder) {
//                    Log.i(tag, "--onTouchEvent--leftBorder:" + leftBorder + "---getScrollX():" + getScrollX() + "---scrollX:" + scrollX);
                    scrollTo(leftBorder, 0);
                    return true;
                } else if (getScrollX() + getWidth() + scrollX > rightBorder) {
//                    Log.i(tag, "--onTouchEvent--rightBorder:" + leftBorder + "---getScrollX():" + getScrollX() + "---scrollX:" + scrollX);
                    scrollTo(rightBorder - getWidth(), 0);
                    return true;
                }
                scrollBy(scrollX, 0);
                xLastPosition = xMove;
                break;

            case MotionEvent.ACTION_UP: //手指抬起
                //抬起手指时，确定当前在那个childView:看是否划出当前view的一半宽度
                int childViewIndex = (int) ((getScrollX() + getWidth() / 2) / getWidth());
                //offset
                int dx = (int) (childViewIndex * getWidth() - getScrollX());
                //开始弹性滑动:
                overScroller.startScroll(getScrollX(), 0, dx, 0);
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (overScroller.computeScrollOffset()) {
            scrollTo(overScroller.getCurrX(), overScroller.getCurrY());
            invalidate();
        }
    }
}