package com.tistory.leminity.headerwebview;

import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

//    final String SAMPLE_URL = "http://app.directreader.co.kr/mem";

    private HeaderWebView mMailSaferWebView;
    private TextView      mTextViewChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mMailSaferWebView = (HeaderWebView) findViewById(R.id.mailsaferWebView);

        addHeaderView(mMailSaferWebView, createHeaderView());
        WebSettings webSettings = mMailSaferWebView.getSettings();
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true);
        loadAssetsHtml(mMailSaferWebView);
    }

    private View createHeaderView() {
        View header = LayoutInflater.from(getBaseContext()).inflate(R.layout.view_header, mMailSaferWebView, false);

        mTextViewChild = (TextView)header.findViewById(R.id.textViewChild);
        Button button = (Button)header.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String LABEL_EXPAND = "Expand";
                final String LABEL_HIDE   = "Hide";

                Button clickedBtn = (Button) v;
                int previousVisibility = mTextViewChild.getVisibility();

                switch (previousVisibility) {
                    case View.VISIBLE :
                        mTextViewChild.setVisibility(View.GONE);
                        clickedBtn.setText(LABEL_EXPAND);
                        break;

                    case View.GONE :
                        mTextViewChild.setVisibility(View.VISIBLE);
                        clickedBtn.setText(LABEL_HIDE);
                        break;
                }
            }
        });

        return header;
    }

    private void addHeaderView(HeaderWebView dstWebView, View headerView) {
        dstWebView.setHeaderView(headerView);
    }

    private void loadAssetsHtml(final WebView webView) {
        Runnable loadHtmlRunnable = new Runnable() {
            @Override
            public void run() {
                AssetManager assetManager = getBaseContext().getAssets();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                try {
                    InputStream in = assetManager.open("sample.html");
                    readStream(in, bos);
                    String htmlContents = new String(bos.toByteArray());

                    loadHtml(webView, htmlContents);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

            }
        };

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(loadHtmlRunnable);
    }

    private void loadHtml(final WebView webView, final String htmlContents) {
        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {
            @Override
            public void run() {
                String mimeType = (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN ? "text/html; charset=UTF-8" : "text/html");
                String charSet =  (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN ? null : "UTF-8");
                webView.loadDataWithBaseURL(null, htmlContents, mimeType, charSet, null);
            }
        });
    }

    private void readStream(InputStream in, OutputStream out) throws IOException{
        int readCnt = 0;
        byte[] buf = new byte[1024*4];

        while((readCnt = in.read(buf)) != -1) {
            out.write(buf, 0, readCnt);
        }
    }
}
