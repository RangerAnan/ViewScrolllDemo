# ViewScrollDemo
## view滑动实现
### 1.layout方法实现 <br/>
    layout(getLeft() + offsetX, getTop() + offsetY, getRight() + offsetX, getBottom() + offsetY);
### notice：layout方法的参数是相对于parentView来确定的。
### 2.使用view的offsetLeftAndRight方法来实现移动<br/>
     offsetLeftAndRight(offsetX);
     offsetTopAndBottom(offsetY);
### 3.使用LayoutParams来设置margin实现移动。<br/>
    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
    params.leftMargin = getLeft() + offsetX;
    params.topMargin = getTop() + offsetY;
    setLayoutParams(params);
### 4.使用scrollBy/scrollTo来实现。
     ((View) getParent()).scrollBy(-offsetX, -offsetY);
### notice：scrollBy移动的是view的内容，所以想要移动view，实际操作的是parentView，即移动parentView的内容（childView）。

### 5.scroller/overScroller实现持续滑动
     scroller.startScroll(scrollX, scrollY, -scrollX, -scrollY, 800);
     invalidate();
