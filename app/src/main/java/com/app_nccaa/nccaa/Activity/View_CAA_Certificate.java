package com.app_nccaa.nccaa.Activity;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.app_nccaa.nccaa.Adapter.AdapterAnnouncement;
import com.app_nccaa.nccaa.R;
import com.app_nccaa.nccaa.Utils.FileDownloader;
import com.app_nccaa.nccaa.Utils.UserSession;
import com.app_nccaa.nccaa.Utils.VolleyMultipartRequest;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class View_CAA_Certificate extends AppCompatActivity {


    private UserSession session;
    private RequestQueue mRequestqueue;
    private WebView urlWebView;
    private String mURL = "";
    private KProgressHUD DowbloadprogressDialog;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_caa_certificate);

        session = new UserSession(View_CAA_Certificate.this);
        mRequestqueue = Volley.newRequestQueue(View_CAA_Certificate.this);
        urlWebView = (WebView)findViewById(R.id.webView);
        urlWebView.setWebViewClient(new AppWebViewClients());
        urlWebView.getSettings().setBuiltInZoomControls(true);
        urlWebView.getSettings().setJavaScriptEnabled(true);
        urlWebView.getSettings().setUseWideViewPort(true);
        urlWebView.getSettings().setLoadWithOverviewMode(true);
        urlWebView.getSettings().setUseWideViewPort(true);

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.mDownload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DowbloadprogressDialog = KProgressHUD.create(View_CAA_Certificate.this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel("Please wait")
                        .setCancellable(false)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f)
                        .show();
                Long tsLong = System.currentTimeMillis()/1000;
                String ts = tsLong.toString();

                new DownloadFileSecond().execute(mURL, ts+".pdf");

            }
        });

        getContactInfo();

    }

    private class DownloadFileSecond extends AsyncTask<String, Void, Void> {

        File dir = null;

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            //    File folder = new File(extStorageDirectory, "Snagpay");

            if (Build.VERSION_CODES.R > Build.VERSION.SDK_INT) {
                dir = new File(Environment.getExternalStorageDirectory().getPath()
                        + "//"+getResources().getString(R.string.app_name));
            } else {
                dir = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).getPath()
                        + "//"+getResources().getString(R.string.app_name));
            }

            if (!dir.exists())
                dir.mkdir();

            File pdfFile = new File(dir, fileName);

            try{
                pdfFile.createNewFile();

            }catch (IOException e){
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            DowbloadprogressDialog.dismiss();
            Toast.makeText(View_CAA_Certificate.this,"File Path : " + dir.getAbsolutePath().toString(),Toast.LENGTH_LONG).show();

        }
    }


    private void getContactInfo() {
        final KProgressHUD progressDialog = KProgressHUD.create(View_CAA_Certificate.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET, session.BASEURL + "users/me/certificate/pdf",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        progressDialog.dismiss();

                        try {


                            JSONObject object = new JSONObject(new String(response.data));

                            if(object.getString("ResponseCode").equals("200")){


                                mURL = object.getJSONObject("data").getString("url");
                                urlWebView.loadUrl("http://docs.google.com/gview?embedded=true&url=" +  mURL);

                            }else {
                                Toast.makeText(View_CAA_Certificate.this,object.getString("ResponseMsg"), Toast.LENGTH_SHORT).show();
                            }



                        } catch (Exception e) {

                            Toast.makeText(View_CAA_Certificate.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        if (error instanceof ServerError){

                            if(error.networkResponse != null && error.networkResponse.data != null){
                                switch(error.networkResponse.statusCode){
                                    case 500:
                                        String json = new String(error.networkResponse.data);
                                        json = session.trimMessage(json, "message");
                                        if(json != null) {
                                            Toast.makeText(View_CAA_Certificate.this, json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                                //Additional cases
                            }
                        }
                        else if (error instanceof TimeoutError)
                            Toast.makeText(View_CAA_Certificate.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(View_CAA_Certificate.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
             /*   params.put("username", userNameET.getText().toString());
                params.put("password", passwordET.getText().toString());
                params.put("fullname", fullNameET.getText().toString());
                params.put("device_type", "android");
                params.put("device_token", android_id);*/

                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //     params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + session.getAPITOKEN());
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

                return params;
            }
        };

        volleyMultipartRequest.setShouldRetryServerErrors(true);

        Volley.newRequestQueue(View_CAA_Certificate.this).add(volleyMultipartRequest);
    }

    public class AppWebViewClients extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

        }
    }

}