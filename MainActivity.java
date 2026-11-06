package com.adultmult.tv;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.view.Gravity;

public class MainActivity extends Activity {
    private static final String URL = "https://mult.adultmult.club/meepmeep.html";
    private WebView browser;
    private TextView errorView;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout root = new FrameLayout(this);
        root.setBackgroundColor(Color.rgb(17, 24, 39));
        browser = new WebView(this);
        browser.setBackgroundColor(Color.rgb(17, 24, 39));
        browser.getSettings().setJavaScriptEnabled(true);
        browser.getSettings().setDomStorageEnabled(true);
        browser.getSettings().setMediaPlaybackRequiresUserGesture(false);
        browser.getSettings().setSupportZoom(false);
        browser.getSettings().setLoadWithOverviewMode(true);
        browser.getSettings().setUseWideViewPort(true);
        browser.setWebChromeClient(new WebChromeClient());
        browser.setWebViewClient(new WebViewClient() {
            @Override public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                errorView.setVisibility(View.GONE);
            }
            @Override public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (request.isForMainFrame()) {
                    errorView.setText("Не удалось открыть AdultMult.\nПроверь интернет и доступность сайта.\n\nНажми OK, чтобы повторить.");
                    errorView.setVisibility(View.VISIBLE);
                    errorView.requestFocus();
                }
            }
        });
        root.addView(browser, new FrameLayout.LayoutParams(-1, -1));
        errorView = new TextView(this);
        errorView.setTextColor(Color.WHITE);
        errorView.setTextSize(20);
        errorView.setGravity(Gravity.CENTER);
        errorView.setBackgroundColor(Color.rgb(17, 24, 39));
        errorView.setFocusable(true);
        errorView.setVisibility(View.GONE);
        errorView.setOnClickListener(v -> browser.loadUrl(URL));
        root.addView(errorView, new FrameLayout.LayoutParams(-1, -1));
        setContentView(root);
        browser.requestFocus();
        browser.loadUrl(URL);
    }

    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (errorView.getVisibility() == View.VISIBLE && (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER)) {
            errorView.setVisibility(View.GONE);
            browser.loadUrl(URL);
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && browser.canGoBack()) {
            browser.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override protected void onDestroy() {
        browser.destroy();
        super.onDestroy();
    }
}
