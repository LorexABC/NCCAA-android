package com.app_nccaa.nccaa.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.app_nccaa.nccaa.R;
import com.app_nccaa.nccaa.Utils.VolleyMultipartRequest;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordPhoneActivity extends AppCompatActivity {

    private EditText etPhoneId;

    private UserSession session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_phone);

      //  getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        session = new UserSession(ForgotPasswordPhoneActivity.this);



        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        etPhoneId = findViewById(R.id.etPhoneId);



        findViewById(R.id.btnSendId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(etPhoneId.getText().toString().isEmpty()) {
                    Toast.makeText(ForgotPasswordPhoneActivity.this, "Please enter your phone", Toast.LENGTH_SHORT).show();
                } else {
                    forgotPasswordPhone();
                }

            }
        });

    }


    private void forgotPasswordPhone() {
        final KProgressHUD progressDialog = KProgressHUD.create(ForgotPasswordPhoneActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, session.BASEURL + "auth/forgot-password",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        progressDialog.dismiss();

                        try {
                            Log.e("forgotResponse", new String(response.data) + "--");

                            JSONObject jsonObject = new JSONObject(new String(response.data));

                            Toast.makeText(ForgotPasswordPhoneActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(ForgotPasswordPhoneActivity.this, CheckEmailActivity.class)
                                    .putExtra("email_phone", etPhoneId.getText().toString())
                                    .putExtra("type", "phone"));


                        } catch (Exception e) {

                            Toast.makeText(ForgotPasswordPhoneActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        String json = new String(error.networkResponse.data);
                        json = session.trimMessage(json, "message");
                        Toast.makeText(ForgotPasswordPhoneActivity.this, json, Toast.LENGTH_LONG).show();


                        if (error instanceof ServerError){

                            if(error.networkResponse != null && error.networkResponse.data != null){
                                switch(error.networkResponse.statusCode){
                                    case 500:

                                }
                                //Additional cases
                            }
                        }
                        else if (error instanceof TimeoutError)
                            Toast.makeText(ForgotPasswordPhoneActivity.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(ForgotPasswordPhoneActivity.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mobile", etPhoneId.getText().toString().replace("-",""));

                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //     params.put("Accept", "application/json");
      //          params.put("Authorization", "Bearer " + session.getAPITOKEN());
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

                return params;
            }
        };

        volleyMultipartRequest.setShouldRetryServerErrors(true);

        Volley.newRequestQueue(ForgotPasswordPhoneActivity.this).add(volleyMultipartRequest);
    }





}