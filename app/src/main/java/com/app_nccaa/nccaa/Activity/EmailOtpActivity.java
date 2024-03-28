package com.app_nccaa.nccaa.Activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.app_nccaa.nccaa.Utils.GenericTextWatcher;
import com.app_nccaa.nccaa.Utils.UserSession;
import com.app_nccaa.nccaa.Utils.VolleyMultipartRequest;
import com.app_nccaa.nccaa.R;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EmailOtpActivity extends AppCompatActivity implements View.OnClickListener {

    private AppCompatEditText et1,et2,et3,et4,et5,et6;

    private Button otpResendBtn;

    private ImageView back;
    private TextView infoIdTV,counter;

    private EditText[] edit;
    private boolean isOTP;

    private UserSession session;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_email_otp);
     //   setContentView(R.layout.dummy1);
        initiateViews();
   //     startTimer();

        session = new UserSession(EmailOtpActivity.this);


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void startTimer() {
        counter.setVisibility(View.VISIBLE);
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                counter.setText("00:"+ millisUntilFinished / 1000 +" sec");
            }

            public void onFinish() {
                counter.setVisibility(View.GONE);
                otpResendBtn.setClickable(true);
                otpResendBtn.setBackground(getResources().getDrawable(R.drawable.capsul_blue_btn));
              //  mTextField.setText("done!");
            }
        }.start();
    }

    // Below function is used to initiate Views elements used in this activity
    private void initiateViews() {
        et1=findViewById(R.id.et1);
        et2=findViewById(R.id.et2);
        et3=findViewById(R.id.et3);
        et4=findViewById(R.id.et4);
        et5=findViewById(R.id.et5);
        et6=findViewById(R.id.et6);
        counter=findViewById(R.id.counter);
        infoIdTV=findViewById(R.id.infoIdTV);
       // otpIdBtn=findViewById(R.id.btnOtpId);
      //  otpIdBtn.setOnClickListener(this);
        otpResendBtn=findViewById(R.id.btnResendOtpId);
        otpResendBtn.setClickable(false);
        otpResendBtn.setOnClickListener(this);

        otpResendBtn.setBackground(getResources().getDrawable(R.drawable.capsul_grey_btn));

       // appbar_layout=findViewById(R.id.appbar_layout);
        back =findViewById(R.id.back);
        back.setOnClickListener(this);

        edit= new EditText[]{et1, et2, et3, et4, et5, et6};
        et1.addTextChangedListener(new GenericTextWatcher(et1, edit));
        et2.addTextChangedListener(new GenericTextWatcher(et2, edit));
        et3.addTextChangedListener(new GenericTextWatcher(et3, edit));
        et4.addTextChangedListener(new GenericTextWatcher(et4, edit));
        et5.addTextChangedListener(new GenericTextWatcher(et5, edit));
     //   et6.addTextChangedListener(new GenericTextWatcher(et6, edit));
        et6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SetValidation();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0) {
                    edit[4].requestFocus();
//                    mQueue.cancelAll(stringRequest);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

         /*   case R.id.btnOtpId:
               
                break;*/

            case R.id.btnResendOtpId:

                if (otpResendBtn.isClickable()){
                    resendOtp();
                }

                infoIdTV.setVisibility(View.INVISIBLE);
               // infoIdTV.setText("The code sent does not match the code you entered!");
                infoIdTV.setTextColor(getResources().getColor(R.color.inactive_btn));
                et1.getBackground().setColorFilter(getResources().getColor(R.color.inactive_btn),
                        PorterDuff.Mode.SRC_ATOP);
                et2.getBackground().setColorFilter(getResources().getColor(R.color.inactive_btn),
                        PorterDuff.Mode.SRC_ATOP);
                et3.getBackground().setColorFilter(getResources().getColor(R.color.inactive_btn),
                        PorterDuff.Mode.SRC_ATOP);
                et4.getBackground().setColorFilter(getResources().getColor(R.color.inactive_btn),
                        PorterDuff.Mode.SRC_ATOP);
                et5.getBackground().setColorFilter(getResources().getColor(R.color.inactive_btn),
                        PorterDuff.Mode.SRC_ATOP);
                et6.getBackground().setColorFilter(getResources().getColor(R.color.inactive_btn),
                        PorterDuff.Mode.SRC_ATOP);

            /*    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);*/

                startTimer();

                otpResendBtn.setClickable(false);
                otpResendBtn.setBackground(getResources().getDrawable(R.drawable.capsul_grey_btn));
                infoIdTV.setText("");


                break;
        }

    }


    private void SetValidation() {

        if (et1.getText().toString().isEmpty() && et2.getText().toString().isEmpty() && et3.getText().toString().isEmpty() &&
                et4.getText().toString().isEmpty() && et5.getText().toString().isEmpty() && et6.getText().toString().isEmpty()) {
            Toast.makeText(this,"Please enter the OTP",Toast.LENGTH_LONG).show();
            isOTP=false;
        }else {
            isOTP=true;
        }
        if (isOTP){

            sendOTPVerificationRequest(et1.getText().toString() +et2.getText().toString()+et3.getText().toString()+et4.getText().toString()+et5.getText().toString()+
                    et6.getText().toString());
        }
    }

    private void sendOTPVerificationRequest(String otp) {

       /* Intent intent=new Intent(EmailOtpActivity.this, NewPasswordActivity.class);
        intent.putExtra("email_phone", getIntent().getStringExtra("email_phone"));
        intent.putExtra("type", getIntent().getStringExtra("type"));
        intent.putExtra("otp", otp);
        startActivity(intent);
        finish();*/


        forgotPassword(otp);

    }

    private void forgotPassword(String mOTP) {
        final KProgressHUD progressDialog = KProgressHUD.create(EmailOtpActivity.this)
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

                            if(jsonObject.getBoolean("status")){
                                Intent intent=new Intent(EmailOtpActivity.this, NewPasswordActivity.class);
                                intent.putExtra("email_phone", getIntent().getStringExtra("email_phone"));
                                intent.putExtra("type", getIntent().getStringExtra("type"));
                                intent.putExtra("otp", mOTP);
                                startActivity(intent);
                                finish();
                            }


                        } catch (Exception e) {

                            Toast.makeText(EmailOtpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        String json = new String(error.networkResponse.data);
                        json = session.trimMessage(json, "message");
                        Toast.makeText(EmailOtpActivity.this, json, Toast.LENGTH_LONG).show();


                        if (error instanceof ServerError){

                            if(error.networkResponse != null && error.networkResponse.data != null){
                                switch(error.networkResponse.statusCode){
                                    case 500:

                                }
                                //Additional cases
                            }
                        }
                        else if (error instanceof TimeoutError)
                            Toast.makeText(EmailOtpActivity.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(EmailOtpActivity.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", getIntent().getStringExtra("email_phone"));
                params.put("token", mOTP);

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

        Volley.newRequestQueue(EmailOtpActivity.this).add(volleyMultipartRequest);
    }


    private void resendOtp() {
        final KProgressHUD progressDialog = KProgressHUD.create(EmailOtpActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, session.BASEURL + "auth/otp-resend",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        progressDialog.dismiss();

                        try {
                            Log.e("otp-resend", new String(response.data) + "--");

                            JSONObject jsonObject = new JSONObject(new String(response.data));

                            Toast.makeText(EmailOtpActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();


                        } catch (Exception e) {

                            Toast.makeText(EmailOtpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        String json = new String(error.networkResponse.data);
                        json = session.trimMessage(json, "message");
                        Toast.makeText(EmailOtpActivity.this, json, Toast.LENGTH_LONG).show();


                        if (error instanceof ServerError){

                            if(error.networkResponse != null && error.networkResponse.data != null){
                                switch(error.networkResponse.statusCode){
                                    case 500:

                                }
                                //Additional cases
                            }
                        }
                        else if (error instanceof TimeoutError)
                            Toast.makeText(EmailOtpActivity.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(EmailOtpActivity.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                if (getIntent().getStringExtra("type").equals("email")){
                    params.put("email", getIntent().getStringExtra("email_phone"));
                } else {
                    params.put("mobile", getIntent().getStringExtra("email_phone"));
                }
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

        Volley.newRequestQueue(EmailOtpActivity.this).add(volleyMultipartRequest);
    }



}