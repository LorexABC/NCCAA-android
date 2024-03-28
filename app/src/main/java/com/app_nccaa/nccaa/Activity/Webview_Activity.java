package com.app_nccaa.nccaa.Activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app_nccaa.nccaa.R;

public class Webview_Activity extends AppCompatActivity {

    private String pdf = "";
    private WebView webView;
    private ImageView backBtn;
    private TextView service_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        webView = findViewById(R.id.webView);
        backBtn = findViewById(R.id.backBtn);
        service_title = findViewById(R.id.service_title);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pdf = getIntent().getStringExtra("pdf");

        service_title.setText(getIntent().getStringExtra("title"));

        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf);
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                if (view.getTitle().equals(""))
//                    view.reload();
//            }
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//
//                try {
//                    Toast.makeText(Webview_Activity.this, url, Toast.LENGTH_SHORT).show();
//                    // do whatever you want to do on a web link click
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return false;
//            }
//        });

    }

}