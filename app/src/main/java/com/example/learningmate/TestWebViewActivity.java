package com.example.learningmate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TestWebViewActivity extends AppCompatActivity {
//    private String fileUrl = "http://windry.dothome.co.kr/se_learning_mate/files";
//    private String fileName = "ec32d6ee67cc518f47dea67c4996b88b.pdf";
    private String url = "http://windry.dothome.co.kr/se_learning_mate/files/db833060a9d1554f01d4d161b21b9fd1.pptx";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_web_view);
        WebView webView =findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().supportZoom();

        webView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url="+url);
//        webView.setWebChromeClient(new WebChromeClient(){
//            @Override
//            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//
//            }
//        });

    }
}