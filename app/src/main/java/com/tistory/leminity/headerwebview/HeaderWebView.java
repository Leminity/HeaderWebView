package com.tistory.leminity.headerwebview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * Created by User on 2016-07-27.
 */
public class HeaderWebView extends WebView {

    private static String   mJsFunctionChangeTop = "javascript:saferChangePostionTop(\"#0\")";

    private View            mViewHeader;
    private LayoutParams    mLayoutParamsHeader;
    private float           mDensity;

    public HeaderWebView(Context context) {
        super(context);
        init();
    }

    public HeaderWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HeaderWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HeaderWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mDensity        = getResources().getDisplayMetrics().density;
    }

    public void setHeaderView(View v) {
        if(mViewHeader == v) return;
        if(mViewHeader != null) {
            removeView(mViewHeader);
        }

        if(null != v) {
            mLayoutParamsHeader = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, 0, 0);
            addView(v, mLayoutParamsHeader);
        }
        mViewHeader = v;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        if(mViewHeader != null) {
            mViewHeader.setTranslationX(getScrollX());
        }
        super.onDraw(canvas);
        canvas.restore();
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        if(getHeaderVisibleHeight() > 0)
            changeDomTopPosition(getHeaderHeight());
        return super.drawChild(canvas, child, drawingTime);
    }

    private int getHeaderHeight() {
        if(mViewHeader != null)
            return (int)(mViewHeader.getHeight() / mDensity);
        return 0;
    }

    private int getHeaderVisibleHeight() {
        return Math.max(getHeaderHeight() - getScrollY(), 0);
    }

    private void changeDomTopPosition(int height) {
        loadUrl(mJsFunctionChangeTop.replace("#0", String.valueOf(height)));
    }

    /******************************************************************************************************************
     * 터치 이벤트 핸들링
     *
     * 헤더뷰 내 스크롤 이벤트를 가진 뷰가 존재 시(ex:EditText) 웹뷰가 스크롤되지 않는다.
     * 이는 OS 터치 핸들링 구조 상 정상 동작이지만, 해당 뷰에서는 헤더 영역의 height가 높을 경우 웹뷰를 스크롤하는데
     * 어려움을 유발한다.
     *
     * 위 사유로 웹뷰 스크롤 처리와 별개로 헤더뷰에 터치 이벤트를 전달하도록 한다.
     ******************************************************************************************************************/

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(mViewHeader != null)
            return true;
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = super.onTouchEvent(event);
        if(mViewHeader != null) {
            dispatchTouchWhenMatchedPosition(mViewHeader, event);
        }
        return result;
    }

    private void dispatchTouchWhenMatchedPosition(View headerView, MotionEvent ev) {
        int visibleHeaderHeight = getHeaderVisibleHeight();
        int eventYDp     = (int)(ev.getY() / mDensity);
        if(eventYDp <= visibleHeaderHeight) {
            headerView.dispatchTouchEvent(ev);
        }
    }

    /******************************************************************************************************************
     * 키 이벤트 핸들링
     *
     * 웹뷰에서 키보드 이벤트를 처리하면서 EditText까지 이벤트가 전달되지 않는 문제가 있음.
     * 키 이벤트 발생 시, 헤더뷰로 수동 전달한다.
     ******************************************************************************************************************/

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(mViewHeader != null) {
            mViewHeader.dispatchKeyEvent(event);
            return false;
        }
        return super.dispatchKeyEvent(event);
    }

}
