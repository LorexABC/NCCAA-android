package com.app_nccaa.nccaa.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;


public class ForgotPasswordEmailActivity extends AppCompatActivity {

    private Button btnSendId;
    private TextView txtRecId;
    private EditText etMailId;


    private UserSession session;

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_email);
        //  setContentView(R.layout.dummy_layout);
        // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        session = new UserSession(ForgotPasswordEmailActivity.this);



        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        btnSendId = findViewById(R.id.btnSendId);
        txtRecId = findViewById(R.id.txtRecId);
        etMailId = findViewById(R.id.etMailId);

        btnSendId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etMailId.getText().toString().isEmpty()){
                    Toast.makeText(ForgotPasswordEmailActivity.this,"Please enter your email",Toast.LENGTH_SHORT).show();
                } else if (!etMailId.getText().toString().trim().matches(emailPattern)) {
                    Toast.makeText(ForgotPasswordEmailActivity.this,"Invalid email!",Toast.LENGTH_SHORT).show();
                } else {
                    forgotPassword();
                }
            }
        });

        txtRecId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(ForgotPasswordEmailActivity.this, ForgotPasswordPhoneActivity.class));
              //  finish();
            }
        });


    }


    private void forgotPassword() {
        final KProgressHUD progressDialog = KProgressHUD.create(ForgotPasswordEmailActivity.this)
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

                            Toast.makeText(ForgotPasswordEmailActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(ForgotPasswordEmailActivity.this, CheckEmailActivity.class)
                                    .putExtra("email_phone", etMailId.getText().toString())
                                    .putExtra("type", "email"));


                        } catch (Exception e) {

                            Toast.makeText(ForgotPasswordEmailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        String json = new String(error.networkResponse.data);
                        json = session.trimMessage(json, "message");
                        Toast.makeText(ForgotPasswordEmailActivity.this, json, Toast.LENGTH_LONG).show();


                        if (error instanceof ServerError){

                            if(error.networkResponse != null && error.networkResponse.data != null){
                                switch(error.networkResponse.statusCode){
                                    case 500:

                                }
                                //Additional cases
                            }
                        }
                        else if (error instanceof TimeoutError)
                            Toast.makeText(ForgotPasswordEmailActivity.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(ForgotPasswordEmailActivity.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", etMailId.getText().toString().trim());

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

        Volley.newRequestQueue(ForgotPasswordEmailActivity.this).add(volleyMultipartRequest);
    }




}
