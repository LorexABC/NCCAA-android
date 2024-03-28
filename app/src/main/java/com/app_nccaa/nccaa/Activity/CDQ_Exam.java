package com.app_nccaa.nccaa.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.app_nccaa.nccaa.Adapter.Adapter_Cdq_Booking;
import com.app_nccaa.nccaa.Adapter.Adapter_Cdq_Register;
import com.app_nccaa.nccaa.Adapter.Adapter_Cdq_Results;
import com.app_nccaa.nccaa.Model.Certificate_Exam_Model;
import com.app_nccaa.nccaa.Utils.UserSession;
import com.app_nccaa.nccaa.Utils.VolleyMultipartRequest;
import com.app_nccaa.nccaa.R;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CDQ_Exam extends AppCompatActivity {

    private RecyclerView recRegisterExam, recBookingCenter, recResults;
    private Adapter_Cdq_Register adapter_cdqRegister;
    private Adapter_Cdq_Booking adapter_cdq_booking;
    private Adapter_Cdq_Results adapter_cdq_results;


    private TextView examTextView;
    private TextView mRetakeValue;
    private TextView mRetakeDate;
    private TextView mRetakeFee;
    private LinearLayout mRetakeLayout;

    private TextView programTV, cme_due_date, attemptTV, testingCenterTV, resultTV, noHistoryTV;

    private UserSession session;

    private ArrayList<Certificate_Exam_Model> certificateExamArrayList = new ArrayList<>();
    private ArrayList<Certificate_Exam_Model> certificateExamArrayListCDQ = new ArrayList<>();


    private boolean bookingMade = false;
    private boolean resultsAvailable = false;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int mFinalObjectIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cdq_exam);

        session = new UserSession(CDQ_Exam.this);


        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        examTextView = findViewById(R.id.examTextView);
        mRetakeLayout = findViewById(R.id.mRetakeLayout);

        programTV = findViewById(R.id.programTV);
        cme_due_date = findViewById(R.id.certificate_due_date);
        attemptTV = findViewById(R.id.attemptTV);
        testingCenterTV = findViewById(R.id.testingCenterTV);
        noHistoryTV = findViewById(R.id.noHistoryTV);
        resultTV = findViewById(R.id.resultTV);


        //Retake
        mRetakeValue = findViewById(R.id.mRetakeValue);
        mRetakeDate = findViewById(R.id.mDate);
        mRetakeFee = findViewById(R.id.mRetakeFee);

        findViewById(R.id.cdqInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(
//                        "https://drive.google.com/viewerng/viewer?embedded=true&url=" + "https://www.nccaatest.org/NCCAAHandbooksandPolicies.pdf"));
//                startActivity(browserIntent);
                startActivity(new Intent(CDQ_Exam.this, Webview_Activity.class)
                        .putExtra("title", "CDQ Info")
                        .putExtra("pdf", "https://www.nccaatest.org/NCCAAHandbooksandPolicies.pdf"));
            }
        });


        examTextView.setText(noTrailingwhiteLines(Html.fromHtml("<p><span style=\"color: #6c727f;\">To maintain certification, CAAs must take the CDQ Exam every ten years after passing their next exam from 1/1/2020. Students becoming CAAs from 2020 will take their first CDQ Exam in 4 years, followed by subsequent exams every 10 years. Please select an option below.</span> <span style=\"text-decoration: underline; color: #fff;\"></span></p>")));

       /* examTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CDQ_Exam.this, More_Info_Cert_Exam.class)
                        .putExtra("from", "cdq"));
            }
        });*/


        // for register exam
        recRegisterExam = findViewById(R.id.recRegisterExam);
        recRegisterExam.setLayoutManager(new LinearLayoutManager(CDQ_Exam.this));
        adapter_cdqRegister = new Adapter_Cdq_Register(CDQ_Exam.this, certificateExamArrayListCDQ, new Adapter_Cdq_Register.OnItemClickListener() {
            @Override
            public void onItemRegister(int pos) {

                if (certificateExamArrayListCDQ.get(pos).getRegistrationIsAvailable().equals("true")) {
                    if (certificateExamArrayListCDQ.get(pos).getPsiFilled().equals("false")) {
                        startActivity(new Intent(CDQ_Exam.this, PSI_Form.class)
                                .putExtra("examID", certificateExamArrayListCDQ.get(pos).getId()));
                    } else {
                        if (certificateExamArrayListCDQ.get(pos).getReceiptPaid().equals("false")) {
                            startActivity(new Intent(CDQ_Exam.this, Add_Credit_Card_Payment.class)
                                    .putExtra("receiptId", certificateExamArrayListCDQ.get(pos).getReceiptId()));
                        } else {
                            startActivity(new Intent(CDQ_Exam.this, Receipt.class)
                                    .putExtra("receiptId", certificateExamArrayListCDQ.get(pos).getReceiptId())
                                    .putExtra("from", "CDQ_exam"));
                        }
                    }

                } else {
                    Toast.makeText(CDQ_Exam.this, "Registration isn't available.", Toast.LENGTH_SHORT).show();
                }
            }

        });
        recRegisterExam.setAdapter(adapter_cdqRegister);


        // for booking center
        recBookingCenter = findViewById(R.id.recBookingCenter);
        recBookingCenter.setLayoutManager(new LinearLayoutManager(CDQ_Exam.this));
        adapter_cdq_booking = new Adapter_Cdq_Booking(this, certificateExamArrayList, new Adapter_Cdq_Booking.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {

                if (certificateExamArrayList.get(pos).getReceiptPaid().equals("true")) {
                    if (certificateExamArrayList.get(pos).getBookingMade().equals("true")) {
                        startActivity(new Intent(CDQ_Exam.this, Testing_Center.class)
                                .putExtra("examID", certificateExamArrayList.get(pos).getId())
                                .putExtra("testingCenterUrl", certificateExamArrayList.get(pos).getTestingCenterUrl()));
                    } else {
                        Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(certificateExamArrayList.get(pos).getTestingCenterUrl()));
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(CDQ_Exam.this, "Booking is not available until registration and payment has been made during the registration periods", Toast.LENGTH_SHORT).show();
                }

            }
        });
        recBookingCenter.setAdapter(adapter_cdq_booking);


        // for booking center
        recResults = findViewById(R.id.recResults);
        recResults.setLayoutManager(new LinearLayoutManager(CDQ_Exam.this));
        adapter_cdq_results = new Adapter_Cdq_Results(this, certificateExamArrayList, new Adapter_Cdq_Results.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {

                if (certificateExamArrayList.get(pos).getResultsAvailable().equals("true")) {
                    startActivity(new Intent(CDQ_Exam.this, Results_Score.class)
                            .putExtra("examID", certificateExamArrayList.get(pos).getId()));
                } else {
                    Toast.makeText(CDQ_Exam.this, "Scores and results are not yet available", Toast.LENGTH_SHORT).show();

                }

            }
        });
        recResults.setAdapter(adapter_cdq_results);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        // SetOnRefreshListener on SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                getInfo();
                getExams();
            }
        });


    }


    private static final DecimalFormat df = new DecimalFormat("0.00");

    private void getExams() {
        final KProgressHUD progressDialog = KProgressHUD.create(CDQ_Exam.this)
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
                        certificateExamArrayListCDQ.clear();


                        try {
                            Log.e("examsList", new String(response.data) + "--");

                            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat output = new SimpleDateFormat("MM/dd/yyyy");


                            JSONArray jsonArray = new JSONArray(new String(response.data));

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                if (object.getString("receiptPaid").equals("true")) {
                                    mFinalObjectIndex = i;
                                }

                            }

                            JSONObject object = jsonArray.getJSONObject(mFinalObjectIndex);
                            bookingMade = object.getBoolean("bookingMade");
                            resultsAvailable = object.getBoolean("resultsAvailable");
                            if (!bookingMade) {
                                testingCenterTV.setText("Not Booked");
                            } else {
                                testingCenterTV.setText("Booked");
                            }
                            if (!resultsAvailable) {
                                resultTV.setText("Not Available");
                            } else {
                                resultTV.setText("Available");
                            }
                            attemptTV.setText(object.getString("attemptNumber"));
                            noHistoryTV.setText(object.getString("attemptNumber") + getDayOfMonthSuffix(Integer.parseInt(object.getString("attemptNumber"))) + " Attempt");


                            //Retake


                            if (Integer.parseInt(object.getString("attemptNumber")) <= 1) {
                                mRetakeLayout.setVisibility(View.GONE);
                            } else {
                                mRetakeLayout.setVisibility(View.VISIBLE);
                                mRetakeValue.setText("Retake #" + object.getString("attemptNumber") + " (" + object.getString("examsRemaining") + " Exams Remaining)");
                                String first = "You are required to take the next CDQ exam on ";
                                String next = "<font color='#EE0000'>" + parseDateToddMMyyyy(object.getString("dateStart")) + "</font>";
                                mRetakeDate.setText(Html.fromHtml(first + next));
                                // mRetakeFee.setText("$" + df.format(Float.parseFloat(object.getString("retakeFee"))));

                                mRetakeFee.setText("$" + currencyFormat(object.getString("retakeFee")));
                            }


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
                            exam.setBookingStatus(object.getString("bookingStatus"));
                            exam.setReceiptPaid(object.getString("receiptPaid"));
                            exam.setTestingCenterUrl(object.getString("testingCenterUrl"));
                            exam.setBookingMade(object.getString("bookingMade"));
                            exam.setResultsAvailable(object.getString("resultsAvailable"));

                            certificateExamArrayList.add(exam);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object1 = jsonArray.getJSONObject(i);
                                Certificate_Exam_Model examModel = new Certificate_Exam_Model();
                                examModel.setId(object1.getString("id"));
                                examModel.setType(object1.getString("type"));
                                examModel.setAttemptNumber(object1.getString("attemptNumber"));
                                examModel.setDateStart(object1.getString("dateStart"));
                                examModel.setDateEnd(object1.getString("dateEnd"));
                                examModel.setRegistrationIsAvailable(object1.getString("registrationIsAvailable"));
                                examModel.setRegistrationStart(object1.getString("registrationStart"));
                                examModel.setRegistrationEnd(object1.getString("registrationEnd"));
                                examModel.setLateStart(object1.getString("lateStart"));
                                examModel.setLateEnd(object1.getString("lateEnd"));
                                examModel.setRegularFee(object1.getString("regularFee"));
                                examModel.setLateFee(object1.getString("lateFee"));
                                examModel.setRetakeFee(object1.getString("retakeFee"));
                                examModel.setPsiFilled(object1.getString("psiFilled"));
                                examModel.setReceiptId(object1.getString("receiptId"));
                                examModel.setReceiptPaid(object1.getString("receiptPaid"));
                                examModel.setTestingCenterUrl(object1.getString("testingCenterUrl"));
                                examModel.setBookingMade(object1.getString("bookingMade"));
                                examModel.setResultsAvailable(object1.getString("resultsAvailable"));

                                certificateExamArrayListCDQ.add(examModel);

                            }

                            adapter_cdqRegister.notifyDataSetChanged();
                            adapter_cdq_booking.notifyDataSetChanged();
                            adapter_cdq_results.notifyDataSetChanged();


                        } catch (Exception e) {
                            Toast.makeText(CDQ_Exam.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(CDQ_Exam.this, json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                                //Additional cases
                            }
                        } else if (error instanceof TimeoutError)
                            Toast.makeText(CDQ_Exam.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(CDQ_Exam.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
             /* params.put("username", userNameET.getText().toString());
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

        Volley.newRequestQueue(CDQ_Exam.this).add(volleyMultipartRequest);
    }

    public static String currencyFormat(String amount) {
        DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
        return formatter.format(Double.parseDouble(amount));
    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "MMM dd, yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    private String getDayOfMonthSuffix(final int n) {

        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    private void getInfo() {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET, session.BASEURL + "users/me",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        try {
                            Log.e("users_me", new String(response.data) + "--");


                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat output = new SimpleDateFormat("MM/dd/yyyy");

                            programTV.setText(jsonObject.getString("program"));

                            if (!jsonObject.getString("cdqDueDate").equals("null")) {
                                Date oneWayTripDate = input.parse(jsonObject.getString("cdqDueDate"));  // parse input
                                cme_due_date.setText(output.format(oneWayTripDate));
                            } else {
                                cme_due_date.setText("N/A");
                            }

                        } catch (Exception e) {

                            Toast.makeText(CDQ_Exam.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(CDQ_Exam.this, json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                            }
                        } else if (error instanceof TimeoutError)
                            Toast.makeText(CDQ_Exam.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(CDQ_Exam.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

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

        Volley.newRequestQueue(CDQ_Exam.this).add(volleyMultipartRequest);
    }


    private CharSequence noTrailingwhiteLines(CharSequence text) {

        while (text.charAt(text.length() - 1) == '\n') {
            text = text.subSequence(0, text.length() - 1);
        }
        return text;
    }


    @Override
    protected void onResume() {
        super.onResume();

        getInfo();
        getExams();
    }

}