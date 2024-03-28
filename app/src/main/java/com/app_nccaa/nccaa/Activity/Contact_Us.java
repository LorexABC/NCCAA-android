package com.app_nccaa.nccaa.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app_nccaa.nccaa.Model.BlogModel;
import com.app_nccaa.nccaa.R;
import com.app_nccaa.nccaa.Utils.UserSession;
import com.app_nccaa.nccaa.Utils.VolleyMultipartRequest;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Contact_Us extends AppCompatActivity {

    private RelativeLayout emailLT;
    private TextView phone_TV, addressTV, btnSend;
    private EditText questionET;

    private UserSession session;
    private RequestQueue mRequestqueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        session = new UserSession(Contact_Us.this);
        mRequestqueue = Volley.newRequestQueue(Contact_Us.this);


        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        emailLT = findViewById(R.id.emailLT);
        phone_TV = findViewById(R.id.phone_TV);
        addressTV = findViewById(R.id.addressTV);
        questionET = findViewById(R.id.questionET);
        btnSend = findViewById(R.id.btnSend);


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (questionET.getText().toString().isEmpty()){
                    Toast.makeText(Contact_Us.this, "enter your question!", Toast.LENGTH_SHORT).show();
                } else {
                    sendQuestion();
                }
            }
        });


        getContactInfo();

    }



    private void getContactInfo() {
        final KProgressHUD progressDialog = KProgressHUD.create(Contact_Us.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET, session.BASEURL + "contact/data",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        progressDialog.dismiss();

                        try {
                            Log.e("contactInfo", new String(response.data) + "--");

                            JSONObject object = new JSONObject(new String(response.data));

                            phone_TV.setText(object.getString("phone"));

                            addressTV.setText(object.getJSONArray("person").getString(0) + "\n" +
                                    object.getJSONArray("person").getString(1) + "\n" +
                                    object.getJSONArray("person").getString(2) + "\n" +
                                    object.getJSONArray("person").getString(3));


                            emailLT.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                                    try {
                                        emailIntent.setData(Uri.parse("mailto:" + object.getString("email")));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    startActivity(Intent.createChooser(emailIntent, "Send to"));
                                }
                            });


                        } catch (Exception e) {

                            Toast.makeText(Contact_Us.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(Contact_Us.this, json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                                //Additional cases
                            }
                        }
                        else if (error instanceof TimeoutError)
                            Toast.makeText(Contact_Us.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(Contact_Us.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

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

        Volley.newRequestQueue(Contact_Us.this).add(volleyMultipartRequest);
    }

    private void sendQuestion() {
        final KProgressHUD progressDialog = KProgressHUD.create(Contact_Us.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        Map<String, Object> mMainObject = new HashMap<String, Object>();
        mMainObject.put("question", questionET.getText().toString());

        JSONObject object = new JSONObject(mMainObject);

        Log.e("getJson", object.toString() + "--");


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, session.BASEURL + "contact/question", object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressDialog.dismiss();

                        Log.e("questionContact", response.toString());

                        try {

                            if (response.getString("status").equals("success")){
                                Toast.makeText(Contact_Us.this, "Sent successfully.", Toast.LENGTH_SHORT).show();
                                questionET.setText("");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                if (error instanceof ServerError) {
                    Toast.makeText(Contact_Us.this, "Server error", Toast.LENGTH_LONG).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(Contact_Us.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(Contact_Us.this, "Bad Network Connection", Toast.LENGTH_LONG).show();
                }

            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //       headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + session.getAPITOKEN());
                return headers;
            }
        };

        jsonObjReq.setTag("TAG");
        // Adding request to request queue
        mRequestqueue.add(jsonObjReq);
    }


}