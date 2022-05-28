package com.example.learningmate;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TestWebViewActivity extends AppCompatActivity {
    private final String baseViewer = "https://drive.google.com/viewerng/viewer?embedded=true&url=";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_web_view);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("불러오는 중입니다...");
        String fileUrl = "";
        Intent intent = getIntent();
        if (intent != null) {
            fileUrl = intent.getStringExtra("url");
        }
        Log.d("url", baseViewer + fileUrl);
        WebView webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressDialog.dismiss();
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().supportZoom();
        webView.loadUrl(baseViewer + fileUrl);
    }
}