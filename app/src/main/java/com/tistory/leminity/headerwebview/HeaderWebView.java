package com.tistory.leminity.headerwebview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by User on 2016-07-27.
 */
public class HeaderWebView extends WebView {
    public HeaderWebView(Context context) {
        super(context);
    }

    public HeaderWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HeaderWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

}
