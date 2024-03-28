package com.app_nccaa.nccaa.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
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
import com.app_nccaa.nccaa.R;
import com.app_nccaa.nccaa.Utils.UserSession;
import com.app_nccaa.nccaa.Utils.VolleyMultipartRequest;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewPasswordActivity extends AppCompatActivity {

    private Button btnCreatePasswordId;
    private UserSession session;

    private EditText etPasswordId, etreenterpassId;
    private Boolean isPasswordVisible = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        session = new UserSession(NewPasswordActivity.this);


        btnCreatePasswordId = findViewById(R.id.btnCreatePasswordId);
        etPasswordId = findViewById(R.id.etPasswordId);
        etreenterpassId = findViewById(R.id.etreenterpassId);


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String email_phone = getIntent().getStringExtra("email_phone");
        String type = getIntent().getStringExtra("type");
        String otp = getIntent().getStringExtra("otp");

        Log.e("credAll", otp + "--" + email_phone + "--" + type);

        btnCreatePasswordId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etPasswordId.getText().toString().isEmpty()){
                    Toast.makeText(NewPasswordActivity.this, "enter new password", Toast.LENGTH_SHORT).show();
                } else if (etreenterpassId.getText().toString().isEmpty()){
                    Toast.makeText(NewPasswordActivity.this, "enter confirm password", Toast.LENGTH_SHORT).show();
                } else if (!etPasswordId.getText().toString().equals(etreenterpassId.getText().toString())){
                    Toast.makeText(NewPasswordActivity.this, "password doesn't match!", Toast.LENGTH_SHORT).show();
                } else {
                    forgotPasswordPhone(email_phone, type, otp);
                }

            }
        });


        etPasswordId.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean result = false;
                final int RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (etPasswordId.getRight() - etPasswordId.getCompoundDrawables()[RIGHT].getBounds().width())) {
                        int selection = etPasswordId.getSelectionEnd();
                        if (isPasswordVisible) {
                            // set drawable image
                            etPasswordId.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                            // hide Password
                            etPasswordId.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            isPasswordVisible = false;
                        } else {
                            // set drawable image
                            etPasswordId.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_black_24dp, 0);
                            // show Password
                            etPasswordId.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            isPasswordVisible = true;
                        }
                        etPasswordId.setSelection(selection);
                        result = true;
                    }
                }
                return result;
            }
        });
        
        

    }


    private void forgotPasswordPhone(String email_phone, String type, String otp) {
        final KProgressHUD progressDialog = KProgressHUD.create(NewPasswordActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, session.BASEURL + "auth/otp-change-password",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        progressDialog.dismiss();

                        try {
                            Log.e("newPasswordResponce", new String(response.data) + "--");

                            JSONObject jsonObject = new JSONObject(new String(response.data));


                            startActivity(new Intent(NewPasswordActivity.this, PasswordResetActivity.class));
                            Toast.makeText(NewPasswordActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                            finish();


                        } catch (Exception e) {

                            Toast.makeText(NewPasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                      //  startActivity(new Intent(NewPasswordActivity.this, PasswordResetActivity.class));

                        String json = new String(error.networkResponse.data);
                        json = session.trimMessage(json, "message");
                        Toast.makeText(NewPasswordActivity.this, json, Toast.LENGTH_LONG).show();


                        if (error instanceof ServerError){

                            if(error.networkResponse != null && error.networkResponse.data != null){
                                switch(error.networkResponse.statusCode){
                                    case 500:

                                }
                                //Additional cases
                            }
                        }
                        else if (error instanceof TimeoutError)
                            Toast.makeText(NewPasswordActivity.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(NewPasswordActivity.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                if (type.equals("email")){
                    params.put("email", email_phone);
                } else {
                    params.put("mobile", email_phone);
                }
                params.put("password", etPasswordId.getText().toString());
                params.put("token", otp);

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

        Volley.newRequestQueue(NewPasswordActivity.this).add(volleyMultipartRequest);
    }




}