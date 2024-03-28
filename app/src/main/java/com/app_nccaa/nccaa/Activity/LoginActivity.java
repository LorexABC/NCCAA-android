package com.app_nccaa.nccaa.Activity;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app_nccaa.nccaa.Utils.UserSession;
import com.app_nccaa.nccaa.Utils.VolleyMultipartRequest;
import com.app_nccaa.nccaa.R;
import com.google.android.material.textfield.TextInputEditText;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    private TextView forgetPassIdTV, condition, privacy, notice;
    private TextInputEditText passwordIdET, etMailId;
    private Boolean isPasswordVisible = false;

    private Button btnLoginId;

    private UserSession session;
    private RequestQueue mRequestqueue;

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);



     /*   if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));

        }*/

        session = new UserSession(LoginActivity.this);
        mRequestqueue = Volley.newRequestQueue(LoginActivity.this);


        forgetPassIdTV = findViewById(R.id.txtForgetPassId);
        condition = findViewById(R.id.condition);
        privacy = findViewById(R.id.privacy);
        notice = findViewById(R.id.notice);
        etMailId = (TextInputEditText) findViewById(R.id.etMailId);
        passwordIdET = (TextInputEditText) findViewById(R.id.etPasswordId);


        forgetPassIdTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordEmailActivity.class));
            }
        });

        findViewById(R.id.btnLoginId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etMailId.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                }else if(passwordIdET.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this,"Please enter your password",Toast.LENGTH_SHORT).show();
                }else {
                    LogIn();
                }
            }
        });


        condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ConditionActivity.class));
            }
        });

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, PrivacyActivity.class));
            }
        });

        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, NoticeActivity.class));
            }
        });


        passwordIdET.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean result = false;
                final int RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (passwordIdET.getRight() - passwordIdET.getCompoundDrawables()[RIGHT].getBounds().width())) {
                        int selection = passwordIdET.getSelectionEnd();
                        if (isPasswordVisible) {
                            //set drawable image
                            passwordIdET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                            //hide Password
                            passwordIdET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            isPasswordVisible = false;
                        } else {
                            //set drawable image
                            passwordIdET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_black_24dp, 0);
                            //show Password
                            passwordIdET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            isPasswordVisible = true;
                        }
                        passwordIdET.setSelection(selection);
                        result = true;
                    }
                }
                return result;
            }
        });


//        session.setAPITOKEN("4660819670b7585a14f1c296999aad5d6794f674bd7efe7ecf6e84092487b8a33c5ab111ad2f028294ff0d971beeb2ef227f");
//        session.setIsLogin(true);
//        session.setUSER_TYPE("student");


    }


    private void LogIn() {
        final KProgressHUD progressDialog = KProgressHUD.create(LoginActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        Map<String, Object> mMainObject = new HashMap<String, Object>();
        mMainObject.put("username", etMailId.getText().toString());
        mMainObject.put("password", passwordIdET.getText().toString());
        mMainObject.put("client_id", "nccaa");
        mMainObject.put("grant_type", "password");

        JSONObject object = new JSONObject(mMainObject);

        Log.e("getJson", object.toString() + "--");


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, session.BASEURL + "auth/login", object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressDialog.dismiss();

                        Log.e("responceSignUp", response.toString());

                        try {

                            getUsersMe(response.getString("access_token"), response.getString("token_type"), etMailId.getText().toString());

                            Log.e("apiToken", response.getString("access_token") + "--");
                            startActivity(new Intent(LoginActivity.this, HomeScreen.class));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

//                startActivity(new Intent(LoginActivity.this, PsiMandatoryActivity.class)
//                        .putExtra("token", "4660819670b7585a14f1c296999aad5d6794f674bd7efe7ecf6e84092487b8a33c5ab111ad2f028294ff0d971beeb2ef227f"));
//                finish();

            //    String json = new String(error.networkResponse.data);
             //   json = session.trimMessage(json, "error");

               /* if (json.equals("unauthorized")) {
                    etMailId.setError("Please enter valid email and password");
                }*/

                Log.e("sdadad",error.toString() + "--" );
                Toast.makeText(LoginActivity.this, "Test"+error.getMessage(), Toast.LENGTH_LONG).show();

                if (error instanceof ServerError) {
                    Toast.makeText(LoginActivity.this, "Server error", Toast.LENGTH_LONG).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(LoginActivity.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(LoginActivity.this, "Bad Network Connection", Toast.LENGTH_LONG).show();
                }

            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //       headers.put("Content-Type", "application/json");
                //     headers.put("Authorization", "Bearer " + session.getAPITOKEN());
                return headers;
            }
        };

        jsonObjReq.setTag("TAG");
        // Adding request to request queue

        mRequestqueue.add(jsonObjReq);
    }

    private void AppLockDialog(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:

                        session.logout();
                        startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                        finishAffinity();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setCancelable(false);
        builder.setMessage("We are sorry. Both the website and app are under going maintenance. Check back soon. Thanks you!").setPositiveButton("Okay", dialogClickListener).show();
    }

    private void getUsersMe(String access_token, String token_type, String email) {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET, session.BASEURL + "users/me",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {


                        try {
                            Log.e("users_me", new String(response.data) + "--");


                            JSONObject jsonObject = new JSONObject(new String(response.data));

                            if(jsonObject.getString("isAppLocked").equals("1")){
                                AppLockDialog();
                                return;
                            }
                            if (jsonObject.getString("psiFilled").equals("true")) {
                                session.setAPITOKEN(access_token);
                                session.setPSI_FILLED(jsonObject.getString("psiFilled"));
                                session.setTOKEN_TYPE(token_type);
                                session.setEmail(email);
                                session.setIsLogin(true);
                                session.setUSER_TYPE(jsonObject.getString("status"));
                                startActivity(new Intent(LoginActivity.this, HomeScreen.class));
                                finish();

                            } else {
                                session.setEmail(email);
                                session.setTOKEN_TYPE(token_type);
                                session.setUSER_TYPE(jsonObject.getString("status"));

                                startActivity(new Intent(LoginActivity.this, PsiMandatoryActivity.class)
                                        .putExtra("token", access_token));
                                finish();
                            }


                            getUsersInfo(access_token);


                        } catch (Exception e) {

                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof ServerError) {

                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                switch (error.networkResponse.statusCode) {
                                    case 500:
                                        String json = new String(error.networkResponse.data);
                                        json = session.trimMessage(json, "message");
                                        if (json != null) {
                                            Toast.makeText(LoginActivity.this, json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                            }
                        } else if (error instanceof TimeoutError)
                            Toast.makeText(LoginActivity.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(LoginActivity.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + access_token);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

                return params;
            }
        };

        volleyMultipartRequest.setShouldRetryServerErrors(true);

        Volley.newRequestQueue(LoginActivity.this).add(volleyMultipartRequest);
    }


    private void getUsersInfo(String access_token) {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET, session.BASEURL + "users/me/personal",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        try {
                            Log.e("users_me_personal", new String(response.data) + "--");


                            JSONObject jsonObject = new JSONObject(new String(response.data));

                            if (!jsonObject.getString("graduationYear").equals("null")) {
                                session.setGRADUATION_YEAR(jsonObject.getString("graduationYear"));
                            }
                            if (!jsonObject.getString("universityId").equals("null")) {
                                session.setUNIVERSITY_ID(jsonObject.getString("universityId"));
                            }


                        } catch (Exception e) {

                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof ServerError) {

                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                switch (error.networkResponse.statusCode) {
                                    case 500:
                                        String json = new String(error.networkResponse.data);
                                        json = session.trimMessage(json, "message");
                                        if (json != null) {
                                            Toast.makeText(LoginActivity.this, json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                            }
                        } else if (error instanceof TimeoutError)
                            Toast.makeText(LoginActivity.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(LoginActivity.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + access_token);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

                return params;
            }
        };

        volleyMultipartRequest.setShouldRetryServerErrors(true);

        Volley.newRequestQueue(LoginActivity.this).add(volleyMultipartRequest);
    }


}