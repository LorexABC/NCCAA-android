package com.app_nccaa.nccaa.Activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.BitmapRequestListener;
import com.app_nccaa.nccaa.R;
import com.app_nccaa.nccaa.Utils.UserSession;
import com.app_nccaa.nccaa.Utils.VolleyMultipartRequest;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewDocument extends AppCompatActivity {

    private UserSession session;
    private WebView urlWebView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_document);

        session = new UserSession(ViewDocument.this);

        urlWebView = (WebView)findViewById(R.id.webView);
        urlWebView.setWebViewClient(new AppWebViewClients());
        urlWebView.getSettings().setBuiltInZoomControls(true);
        urlWebView.getSettings().setJavaScriptEnabled(true);
        urlWebView.getSettings().setUseWideViewPort(true);
        urlWebView.getSettings().setLoadWithOverviewMode(true);
        urlWebView.getSettings().setUseWideViewPort(true);

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String uploadId = getIntent().getStringExtra("uploadId");


     /*   AndroidNetworking.get("https://nccaatest1.globaltechkyllc.com/api/users/me/uploads/" + uploadId)
                .addHeaders("Authorization", "Bearer " + session.getAPITOKEN())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type", "application/x-www-form-urlencoded")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsBitmap(new BitmapRequestListener() {
                    @Override
                    public void onResponse(Bitmap response) {
                        docImage.setImageBitmap(response);
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });*/


        getImageUploded(uploadId);

    }


    private void getImageUploded(String uploadID) {
        final KProgressHUD progressDialog = KProgressHUD.create(ViewDocument.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET,
                session.BASEURL + "users/me/uploads/" + uploadID + "/metadata",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        progressDialog.dismiss();

                        try {
                            Log.e("uploadResponse", new String(response.data) + "--");

                            JSONObject object = new JSONObject(new String(response.data));

                            if (object.getString("mime").contains("image")){
                                urlWebView.loadUrl(object.getString("url"));
                            } else {
                                urlWebView.loadUrl("http://docs.google.com/gview?embedded=true&url="
                                        + object.getString("url"));
                            }


                        } catch (Exception e) {
                            Toast.makeText(ViewDocument.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(ViewDocument.this, json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                            }
                        }
                        else if (error instanceof TimeoutError)
                            Toast.makeText(ViewDocument.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(ViewDocument.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

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
                params.put("Accept", "application/json");
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
        Volley.newRequestQueue(ViewDocument.this).add(volleyMultipartRequest);
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