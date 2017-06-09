package com.cyy.mywidget.scrollview;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.OverScroller;

/**
 * Created by cyy on 17/3/9.
 *
 */

public class MyScrollView extends FrameLayout {

    final static String TAG = MyScrollView.class.getSimpleName();

    private OverScroller mScroller;
    private VelocityTracker mVelocityTracker;
    private int mMaximumVelocity, mMinimumVelocity;
    private int mTouchSlop;
    private float mLastY;
    private boolean mDragging;

    public MyScrollView(@NonNull Context context) {
        this(context , null);
    }

    public MyScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public MyScrollView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        android.widget.ScrollView s = new android.widget.ScrollView(this.getContext());
        setFocusable(true);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        setWillNotDraw(false);

        mScroller = new OverScroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMaximumVelocity = ViewConfiguration.get(context)
                .getScaledMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.get(context)
                .getScaledMinimumFlingVelocity();

        setVerticalScrollBarEnabled(true);
    }

    @Override
    public void addView(View child) {
        if (getChildCount()>0){
            throw new IllegalStateException("只能有一个子View");
        }
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        if (getChildCount()>0){
            throw new IllegalStateException("只能有一个子View");
        }
        super.addView(child, index);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount()>0){
            throw new IllegalStateException("只能有一个子View");
        }
        super.addView(child, index, params);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (getChildCount()>0){
            throw new IllegalStateException("只能有一个子View");
        }
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int width, int height) {
        if (getChildCount()>0){
            throw new IllegalStateException("只能有一个子View");
        }
        super.addView(child, width, height);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount()>0){
            View child = getChildAt(0);
            final FrameLayout.LayoutParams lp = (LayoutParams) child.getLayoutParams();
            final int childMeasureWidth = getChildMeasureSpec(
                    widthMeasureSpec , lp.leftMargin + lp.rightMargin ,lp.width);
            final int childMeasureHeight = MeasureSpec.makeMeasureSpec(
                    heightMeasureSpec , MeasureSpec.UNSPECIFIED);
            child.measure(childMeasureWidth , childMeasureHeight);

            Log.i(TAG,"child height = "+child.getMeasuredHeight());
        }
    }

    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        super.measureChild(child, parentWidthMeasureSpec, parentHeightMeasureSpec);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e("onInterceptTouchEvent" , "fff"+ev);
        int action = ev.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mDragging = false;
                mLastY = ev.getY();

                //计算点击的时候是不是滑动
                mScroller.computeScrollOffset();
                mDragging = !mScroller.isFinished();
                Log.e("tag" , mDragging+"111111");
                break;

            case MotionEvent.ACTION_MOVE:
                int deltaY = (int) Math.abs((int)ev.getY() - mLastY);
                if (deltaY > mTouchSlop){
                    mDragging = true;
                }
                break;

            case MotionEvent.ACTION_UP:

                break;
        }
        return mDragging;
    }

    private int getChildHeight(){
        if (getChildCount()>0){
            return getChildAt(0).getBottom()-getChildAt(0).getTop();
        }else {
            return 0;
        }
    }

    private int getScallRange(){
        if (getChildCount()>0){
            int childHeight = getChildHeight();
            FrameLayout.LayoutParams lp = (LayoutParams) getChildAt(0).getLayoutParams();
            return childHeight+lp.topMargin+lp.bottomMargin-getHeight();
        }
        return 0;
    }

    private void initVelocityTrackerIfNotExists()
    {
        if (mVelocityTracker == null)
        {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker()
    {
        if (mVelocityTracker != null)
        {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }


    private int  mActivePointerId = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        initVelocityTrackerIfNotExists();
        mVelocityTracker.addMovement(event);
        int action = event.getActionMasked();
        float y = event.getY();

        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished())
                    mScroller.abortAnimation();
                mLastY = (int) event.getY();
                mActivePointerId = event.getPointerId(0);
                return true;
            case MotionEvent.ACTION_MOVE:
                int dy = (int) (mLastY - y);
                if (!mDragging && Math.abs(dy) > mTouchSlop) {
                    mDragging = true;
                }
                if (mDragging) {
                    Log.i(TAG , "dy=="+dy);
                    overScrollBy(0 , dy, 0, getScrollY() , 0 , getScallRange() , 0 , 300 , true );
                }

                mLastY = y;
                break;
            case MotionEvent.ACTION_CANCEL:
                mDragging = false;
                recycleVelocityTracker();
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_UP:
                mDragging = false;
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int velocityY = (int) mVelocityTracker.getYVelocity(mActivePointerId);
                Log.i(TAG , "velocityY="+velocityY);
                if (Math.abs(velocityY) > mMinimumVelocity) {
                    fling(-velocityY);
                }else if (mScroller.springBack(0 , getScrollY() , 0 , 0 , 0 , getScallRange())){
                    postInvalidateOnAnimation();
                }
                recycleVelocityTracker();
                mActivePointerId = -1;
                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        Log.e("onOverScrolled" , "onOverScrolled clampedY = >>>>" + clampedY);
        Log.e("scrollY" , ">>>>>>>scrollY = "+scrollY + " mScroller.isFinished() = " + mScroller.isFinished());
        if (!mScroller.isFinished()) {
            if (clampedY){
                mScroller.springBack(0,getScrollY() , 0 , 0 ,0 , getScallRange());
            }
        }

        scrollTo(scrollX , scrollY);
        awakenScrollBars();
    }

    @Override
    protected int computeVerticalScrollExtent() {
        return getHeight();
    }

    private int offset;
    @Override
    protected int computeVerticalScrollOffset() {
        offset = Math.max(0, super.computeVerticalScrollOffset());
        Log.e("offset" , "offset = " + offset);
        return offset;
    }

    @Override
    protected int computeVerticalScrollRange() {
        final int count = getChildCount();
        final int contentHeight = getHeight() - 0 - 0;
        if (count == 0) {
            return contentHeight;
        }

        int scrollRange = getChildAt(0).getBottom();
        final int scrollY = getScrollY();
        final int overscrollBottom = Math.max(0, scrollRange - contentHeight);
        if (scrollY < 0) {
            scrollRange -= scrollY;
        } else if (scrollY > overscrollBottom) {
            scrollRange += scrollY - overscrollBottom;
        }

        return scrollRange;
    }

    public void fling(int velocityY) {
        Log.e("velocityY == " , "getScrollY()  = "+getScrollY() );
        mScroller.fling(0, offset , 0, velocityY, 0, 0, 0, getScallRange() , 0 , 300);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {

            Log.e("computeScroll" , "computeScroll>>>>" + mScroller.getCurrY()
                    + " mScroller.finaY=="+mScroller.getFinalY()
                    +" isOverScrolled = " + mScroller.isOverScrolled()
            );

            //最后会跳动
//            scrollTo(0, mScroller.getCurrY());

            //直接用这个计算滚动
            overScrollBy(0 , mScroller.getCurrY()-getScrollY() , 0 , getScrollY() , 0 , getScallRange() , 0 , 300 , false);

//            if (!awakenScrollBars()) {
//                // Keep on drawing until the animation has finished.
//                postInvalidateOnAnimation();
//            }
        }
    }
}

