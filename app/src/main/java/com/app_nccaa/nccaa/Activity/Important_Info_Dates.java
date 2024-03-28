package com.app_nccaa.nccaa.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.app_nccaa.nccaa.Adapter.Adapter_Cdq_Register;
import com.app_nccaa.nccaa.Model.Certificate_Exam_Model;
import com.app_nccaa.nccaa.Utils.UserSession;
import com.app_nccaa.nccaa.Utils.VolleyMultipartRequest;
import com.app_nccaa.nccaa.R;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Important_Info_Dates extends AppCompatActivity {

    private UserSession session;
    private LinearLayout mCertificate;

    private TextView account, status, certificateNo, certifiedThrough, designation, cme_due_date, cdq_due_date, year, graduation_date, certificate_due_date,
            clinicals_completed, science_exam_due_ate, program, tv, mLateDate,mCME_DeadLine,mCME_Title,txt_view;

    private RecyclerView recRegisterExam;
    private Adapter_Cdq_Register adapter_cdqRegister;

    private ArrayList<Certificate_Exam_Model> certificateExamArrayList = new ArrayList<>();

    private LinearLayout impDateCMELayout;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_important_info_dates);

        session = new UserSession(Important_Info_Dates.this);


        mCertificate = (LinearLayout) this.findViewById(R.id.mCertificate);
        tv = (TextView) this.findViewById(R.id.mywidget);
        mLateDate = (TextView) this.findViewById(R.id.mLateDate);
        mCME_DeadLine = (TextView) this.findViewById(R.id.mCME_DeadLine);
        mCME_Title = (TextView) this.findViewById(R.id.mCME_Title);
        impDateCMELayout = findViewById(R.id.impDateCMELayout);
        account = findViewById(R.id.account);
        status = findViewById(R.id.status);
        certificateNo = findViewById(R.id.certificateNo);
        certifiedThrough = findViewById(R.id.certifiedThrough);
        designation = findViewById(R.id.designation);
        cme_due_date = findViewById(R.id.cme_due_date);
        cdq_due_date = findViewById(R.id.cdq_due_date);
        year = findViewById(R.id.year);
        graduation_date = findViewById(R.id.graduation_date);
        certificate_due_date = findViewById(R.id.certificate_due_date);
        clinicals_completed = findViewById(R.id.clinicals_completed);
        science_exam_due_ate = findViewById(R.id.science_exam_due_ate);
        program = findViewById(R.id.program);
        txt_view = findViewById(R.id.txt_view);


        tv.setSelected(true);  // Set focus to the textview


        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        findViewById(R.id.mCME).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (session.getUSER_TYPE().equals("student")){
                    Toast.makeText(Important_Info_Dates.this, "This module is not available for Student.", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(Important_Info_Dates.this, CME_Submissions.class));
                }
            }
        });

        findViewById(R.id.mCDQ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (session.getUSER_TYPE().equals("student")) {
                    Toast.makeText(Important_Info_Dates.this, "This module is not available for Student", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(Important_Info_Dates.this, CDQ_Exam.class));
                }
            }
        });

        findViewById(R.id.mCERT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (session.getUSER_TYPE().equals("student")) {
                    startActivity(new Intent(Important_Info_Dates.this, Certificate_Exam.class));
                } else {
                    Toast.makeText(Important_Info_Dates.this, "This module is not available for Caa", Toast.LENGTH_SHORT).show();
                }
            }
        });






        getInfo();

        if (!session.getUSER_TYPE().equals("student")) {
            getCMEInfo();
        }


        // for register exam
        recRegisterExam = findViewById(R.id.recRegisterExam);
        recRegisterExam.setLayoutManager(new LinearLayoutManager(Important_Info_Dates.this));
        adapter_cdqRegister = new Adapter_Cdq_Register(Important_Info_Dates.this, certificateExamArrayList, new Adapter_Cdq_Register.OnItemClickListener() {
            @Override
            public void onItemRegister(int pos) {
                if (certificateExamArrayList.get(pos).getRegistrationIsAvailable().equals("true")) {
                    if (certificateExamArrayList.get(pos).getPsiFilled().equals("false")) {
                        startActivity(new Intent(Important_Info_Dates.this, PSI_Form.class)
                                .putExtra("examID", certificateExamArrayList.get(pos).getId()));
                    } else {
                        if (certificateExamArrayList.get(pos).getReceiptPaid().equals("false")) {
                            startActivity(new Intent(Important_Info_Dates.this, Add_Credit_Card_Payment.class)
                                    .putExtra("receiptId", certificateExamArrayList.get(pos).getReceiptId()));
                        }
                    }
                } else {
                    Toast.makeText(Important_Info_Dates.this, "Registration isn't available.", Toast.LENGTH_SHORT).show();
                }
            }

        });
        recRegisterExam.setAdapter(adapter_cdqRegister);
        getExams();
        txt_view.setText(noTrailingwhiteLines(Html.fromHtml("<p style=\\\"text-align:left\\\"><strong>1st 4 Years of CAA</strong></p><p style=\\\"text-align:left\\\">During the initial 4-year period, CAAs must submit 50 hours of CME documents and pay $295 every 2 years.</p><p style=\\\"text-align:left\\\">In the 4th year, CAAs must take the first CDQ Exam, paying $1,300, and submit their 2nd CME submission, paying $295.</p><p style=\\\"text-align:left\\\">If the CDQ Exam is passed, the subsequent exam will take place 10 years after the pass date. For instance, if a CAA passes the CDQ exam in 2024, the next exam will be scheduled for 2034.</p><p style=\\\"text-align:left\\\">In addition to the CDQ Exam, CAAs must submit CMEs every two years until retirement.</p>")));


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        // SetOnRefreshListener on SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                getExams();
                getInfo();
                if (!session.getUSER_TYPE().equals("student")) {
                    getCMEInfo();
                }

            }
        });


    }

    private CharSequence noTrailingwhiteLines(CharSequence text) {

        while (text.charAt(text.length() - 1) == '\n') {
            text = text.subSequence(0, text.length() - 1);
        }
        return text;
    }

    private void getInfo() {
        final KProgressHUD progressDialog = KProgressHUD.create(Important_Info_Dates.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET, session.BASEURL + "users/me",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        progressDialog.dismiss();

                        try {
                            Log.e("users_me", new String(response.data) + "--");


                            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat output = new SimpleDateFormat("MM/dd/yyyy");


                            JSONObject jsonObject = new JSONObject(new String(response.data));

                            account.setText(jsonObject.getString("account").toUpperCase(Locale.ROOT));
                            if (jsonObject.getString("status").length() < 4) {
                                status.setText(jsonObject.getString("status").toUpperCase(Locale.ROOT));
                            } else {
                                status.setText(capitalize(jsonObject.getString("status")));
                            }

                            if (!jsonObject.getString("certificateNumber").equals("null")) {
                                certificateNo.setText(jsonObject.getString("certificateNumber"));
                                mCertificate.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(Important_Info_Dates.this, View_CAA_Certificate.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                            if (!jsonObject.getString("certifiedThrough").equals("null")) {
                                Date oneWayTripDate = input.parse(jsonObject.getString("certifiedThrough"));  // parse input
                                certifiedThrough.setText(output.format(oneWayTripDate));
                            } else {
                                certifiedThrough.setText("N/A");
                            }

                            if (!jsonObject.getString("designation").equals("null")) {
                                designation.setText(jsonObject.getString("designation"));
                            } else {
                                designation.setText("N/A");
                            }

                            if (!jsonObject.getString("firstYear").equals("null")) {
                                year.setText(jsonObject.getString("firstYear"));
                            } else {
                                year.setText("N/A");
                            }


                            if (!jsonObject.getString("cmeDueDate").equals("null")) {
                                Date oneWayTripDate = input.parse(jsonObject.getString("cmeDueDate"));  // parse input
                                cme_due_date.setText(output.format(oneWayTripDate));
                            } else {
                                cme_due_date.setText("N/A");
                            }

                            if (!jsonObject.getString("cdqDueDate").equals("null")) {
                                Date oneWayTripDate = input.parse(jsonObject.getString("cdqDueDate"));  // parse input
                                cdq_due_date.setText(output.format(oneWayTripDate));
                            } else {
                                cdq_due_date.setText("N/A");
                            }

                            if (!jsonObject.getString("graduationDate").equals("null")) {
                                Date oneWayTripDate = input.parse(jsonObject.getString("graduationDate"));  // parse input
                                graduation_date.setText(output.format(oneWayTripDate));
                            } else {
                                graduation_date.setText("N/A");
                            }

                            if (!jsonObject.getString("certificationDueDate").equals("null")) {
                                Date oneWayTripDate = input.parse(jsonObject.getString("certificationDueDate"));  // parse input
                                certificate_due_date.setText(output.format(oneWayTripDate));
                            } else {
                                certificate_due_date.setText("N/A");
                            }

                            if (!jsonObject.getString("clinicalsCompleted").equals("null")) {
                                clinicals_completed.setText(jsonObject.getString("clinicalsCompleted"));
                                findViewById(R.id.mCLINICAL).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (session.getUSER_TYPE().equals("student")){
                                            Toast.makeText(Important_Info_Dates.this, "This module will not be available until 2024.", Toast.LENGTH_SHORT).show();
//                                            startActivity(new Intent(Important_Info_Dates.this, ClinicalCompetenciesActivity.class));
                                        } else {
                                            if (!clinicals_completed.getText().toString().equals("Unavailable")) {
                                                Toast.makeText(Important_Info_Dates.this, "This module will not be available until 2024.", Toast.LENGTH_SHORT).show();
//                                                startActivity(new Intent(Important_Info_Dates.this, ClinicalCompetenciesActivity.class));
                                            }
                                        }
                                    }
                                });
                            } else {
                                clinicals_completed.setText("Unavailable");
                                clinicals_completed.setClickable(false);
                            }

                            if (!jsonObject.getString("scienceExamDueDate").equals("null")) {
                                Date oneWayTripDate = input.parse(jsonObject.getString("scienceExamDueDate"));  // parse input
                                science_exam_due_ate.setText(output.format(oneWayTripDate));
                            } else {
                                science_exam_due_ate.setText("N/A");
                            }

                            if (!jsonObject.getString("program").equals("null")) {
                                program.setText(jsonObject.getString("program"));
                            } else {
                                program.setText("N/A");
                            }


                        } catch (Exception e) {

                            Toast.makeText(Important_Info_Dates.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        if (error instanceof ServerError) {

                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                switch (error.networkResponse.statusCode) {
                                    case 500:
                                        String json = new String(error.networkResponse.data);
                                        json = session.trimMessage(json, "message");
                                        if (json != null) {
                                            Toast.makeText(Important_Info_Dates.this, json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                            }
                        } else if (error instanceof TimeoutError)
                            Toast.makeText(Important_Info_Dates.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(Important_Info_Dates.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

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

        Volley.newRequestQueue(Important_Info_Dates.this).add(volleyMultipartRequest);
    }

    private void getCMEInfo() {
        final KProgressHUD progressDialog = KProgressHUD.create(Important_Info_Dates.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET, session.BASEURL + "users/me/cmeCycles",
                new Response.Listener<NetworkResponse>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(NetworkResponse response) {
                        progressDialog.dismiss();

                        try {
                            Log.e("CME_me", new String(response.data) + "--");

                            JSONArray jsonArray = new JSONArray(new String(response.data));

                            for (int i = 0; i < jsonArray.length(); i++){
                                if (jsonArray.getJSONObject(i).getString("isCurrent").equals("true")){
                                    impDateCMELayout.setVisibility(View.VISIBLE);
                                    break;
                                }
                            }


                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);

                                if(object.getBoolean("isCurrent")){

                                    SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
                                    SimpleDateFormat output = new SimpleDateFormat("MM/dd/yyyy");
                                    SimpleDateFormat output2 = new SimpleDateFormat("MMMM d, yyyy");


                                    findViewById(R.id.mUpload_CME).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            try {
                                                startActivity(new Intent(Important_Info_Dates.this, Add_CME.class).putExtra("mYear",object.getString("cycle")));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });


                                    Date oneWayTripDate = input.parse(object.getString("deadline"));  // parse input
                                    mCME_DeadLine.setText(output.format(oneWayTripDate));
                                    Date oneWayTripDate1 = input.parse(object.getString("lateStart"));  // parse input
                                    Date oneWayTripDate2 = input.parse(object.getString("lateEnd"));  // parse input
                                    mLateDate.setText(output.format(oneWayTripDate1) + " - " + output.format(oneWayTripDate2));
                                    mCME_Title.setText(output2.format(oneWayTripDate)+ ", CME Due Date");

                                }

                            }

                        } catch (Exception e) {

                            Toast.makeText(Important_Info_Dates.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        if (error instanceof ServerError) {

                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                switch (error.networkResponse.statusCode) {
                                    case 500:
                                        String json = new String(error.networkResponse.data);
                                        json = session.trimMessage(json, "message");
                                        if (json != null) {
                                            Toast.makeText(Important_Info_Dates.this, json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                            }
                        } else if (error instanceof TimeoutError)
                            Toast.makeText(Important_Info_Dates.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(Important_Info_Dates.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

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

        Volley.newRequestQueue(Important_Info_Dates.this).add(volleyMultipartRequest);
    }

    private void getExams() {
        final KProgressHUD progressDialog = KProgressHUD.create(Important_Info_Dates.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET, session.BASEURL + "users/me/exams",
                new Response.Listener<NetworkResponse>() {
                    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
                    @Override
                    public void onResponse(NetworkResponse response) {

                        progressDialog.dismiss();
                        certificateExamArrayList.clear();


                        try {
                            Log.e("examsList", new String(response.data) + "--");

                            JSONArray jsonArray = new JSONArray(new String(response.data));


                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);

                                Certificate_Exam_Model exam = new Certificate_Exam_Model();
                                exam.setId(object.getString("id"));
                                exam.setType(object.getString("type"));
                                exam.setAttemptNumber(object.getString("attemptNumber"));
                                exam.setDateStart(object.getString("dateStart"));
                                exam.setDateEnd(object.getString("dateEnd"));
                                exam.setRegistrationIsAvailable(object.getString("registrationIsAvailable"));
                                exam.setRegistrationStart(object.getString("registrationStart"));
                                exam.setRegistrationEnd(object.getString("registrationEnd"));
                                exam.setLateStart(object.getString("lateStart"));
                                exam.setLateEnd(object.getString("lateEnd"));
                                exam.setRegularFee(object.getString("regularFee"));
                                exam.setLateFee(object.getString("lateFee"));
                                exam.setRetakeFee(object.getString("retakeFee"));
                                exam.setPsiFilled(object.getString("psiFilled"));
                                exam.setReceiptId(object.getString("receiptId"));
                                exam.setReceiptPaid(object.getString("receiptPaid"));
                                exam.setTestingCenterUrl(object.getString("testingCenterUrl"));
                                exam.setBookingMade(object.getString("bookingMade"));
                                exam.setResultsAvailable(object.getString("resultsAvailable"));

                                certificateExamArrayList.add(exam);
                            }

                            adapter_cdqRegister.notifyDataSetChanged();




                        } catch (Exception e) {
                            Toast.makeText(Important_Info_Dates.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(Important_Info_Dates.this, json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                                //Additional cases
                            }
                        }
                        else if (error instanceof TimeoutError)
                            Toast.makeText(Important_Info_Dates.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(Important_Info_Dates.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

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

        Volley.newRequestQueue(Important_Info_Dates.this).add(volleyMultipartRequest);
    }


    public static String capitalize(@NonNull String input) {

        String[] words = input.toLowerCase().split(" ");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];

            if (i > 0 && word.length() > 0) {
                builder.append(" ");
            }

            String cap = word.substring(0, 1).toUpperCase() + word.substring(1);
            builder.append(cap);
        }
        return builder.toString();
    }


}