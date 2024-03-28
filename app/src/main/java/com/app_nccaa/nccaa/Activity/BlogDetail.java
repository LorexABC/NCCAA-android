package com.app_nccaa.nccaa.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import com.app_nccaa.nccaa.R;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.Locale;

public class BlogDetail extends AppCompatActivity {

    private WebView wv;
    private KProgressHUD progressDialog;

    private TextView dateTV, subjectTV,txtTest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);


        wv = findViewById(R.id.wv);
        dateTV = findViewById(R.id.dateTV);
        subjectTV = findViewById(R.id.subjectTV);
        txtTest = findViewById(R.id.txtTest);


        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        String contentHTML1 = getIntent().getStringExtra("contentHTML").replace("<p>&nbsp;</p>", "");
        String contentHTML = contentHTML1.replace("&nbsp;", "");

        dateTV.setText(getIntent().getStringExtra("date"));

        String mID = getIntent().getStringExtra("id");

        Log.e("mID",mID);
        findViewById(R.id.subjectTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mID.equals("53")){
                    String format = "https://drive.google.com/viewerng/viewer?embedded=true&url=%s";
                    String fullPath = String.format(Locale.ENGLISH, format, "https://www.nccaatest.org/NCCAAHandbooksandPolicies.pdf");
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fullPath));
                    startActivity(browserIntent);
                }
            }
        });

        subjectTV.setText(getIntent().getStringExtra("subject"));


        progressDialog = KProgressHUD.create(BlogDetail.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();


        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wv.getSettings().setDomStorageEnabled(true);
        wv.getSettings().setAllowFileAccessFromFileURLs(true);
        wv.getSettings().setAllowUniversalAccessFromFileURLs(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            wv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            wv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv.loadDataWithBaseURL(null, getHtmlData(contentHTML), "text/html", "utf-8", null);

        wv.setWebViewClient(new WebViewClient());


        String html = "<a href=\"http://www.google.com\">Google</a>";
        Spanned result = HtmlCompat.fromHtml(contentHTML, HtmlCompat.FROM_HTML_MODE_LEGACY);
        txtTest.setText(result);
        txtTest.setMovementMethod(LinkMovementMethod.getInstance());
        Log.e("contentHTML", contentHTML + "--");


    }

    private String getHtmlData(String bodyHTML) {
        String head = "<head><style>img{max-width: 100%; width:auto; height: auto;}</style></head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }


    public class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            progressDialog.dismiss();
        }

    }

    private CharSequence noTrailingwhiteLines(CharSequence text) {

        while (text.charAt(text.length() - 1) == '\n') {
            text = text.subSequence(0, text.length() - 1);
        }
        return text;
    }


}