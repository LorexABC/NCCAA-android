package com.app_nccaa.nccaa.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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
import com.app_nccaa.nccaa.Utils.UserSession;
import com.app_nccaa.nccaa.Utils.VolleyMultipartRequest;
import com.app_nccaa.nccaa.R;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Testing_Center extends AppCompatActivity {

    private TextView address, location, time, date, centerType;
    private UserSession session;

    
    private String testingCenterUrl = "";
    private String examID = "";
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_center);

        session = new UserSession(Testing_Center.this);


        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        address = findViewById(R.id.address);
        location = findViewById(R.id.location);
        time = findViewById(R.id.time);
        date = findViewById(R.id.date);
        centerType = findViewById(R.id.centerType);


        testingCenterUrl = getIntent().getStringExtra("testingCenterUrl");
        examID = getIntent().getStringExtra("examID");
        
        

        findViewById(R.id.rescheduleBoooking).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(testingCenterUrl));
                startActivity(intent);
            }
        });

        getBookingInfo(examID);
        

    }


    private void getBookingInfo(String examID) {
        final KProgressHUD progressDialog = KProgressHUD.create(Testing_Center.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET, session.BASEURL + "users/me/exams/" + examID + "/booking",
                new Response.Listener<NetworkResponse>() {
                    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
                    @Override
                    public void onResponse(NetworkResponse response) {

                        progressDialog.dismiss();


                        try {
                            Log.e("getBookingCenter", new String(response.data) + "--");

                            JSONObject jsonObject = new JSONObject(new String(response.data));

                            centerType.setText(jsonObject.getString("type").toUpperCase(Locale.ROOT) + " Exam");

                            if (!jsonObject.getString("locationName").equals("null")) {
                                location.setText(jsonObject.getString("locationName"));
                            }
                            if (!jsonObject.getString("address").equals("null")) {
                                address.setText(jsonObject.getString("address"));
                            }

                            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date oneWayTripDate = input.parse(jsonObject.getString("dateTime").substring(0,19).replace("T"," "));  // parse input

                            SimpleDateFormat output = new SimpleDateFormat("MMMM dd, yyyy");
                            date.setText(output.format(oneWayTripDate));

                            SimpleDateFormat outputTime = new SimpleDateFormat("hh:mm aaa");
                            time.setText(outputTime.format(oneWayTripDate));


                        } catch (Exception e) {
                            Toast.makeText(Testing_Center.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(Testing_Center.this, json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                                //Additional cases
                            }
                        }
                        else if (error instanceof TimeoutError)
                            Toast.makeText(Testing_Center.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(Testing_Center.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

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

        Volley.newRequestQueue(Testing_Center.this).add(volleyMultipartRequest);
    }

    

}