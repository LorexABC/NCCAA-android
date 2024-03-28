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

import androidx.annotation.NonNull;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Certificate_Exam extends AppCompatActivity {

    private TextView examTextView, book_testing_center_Btn, resultBtn,noHistoryTV;

    private UserSession session;


    private TextView mRetakeValue;
    private TextView mRetakeDate;
    private TextView mRetakeFee;
    private LinearLayout mRetakeLayout;

    private TextView titleExam, registrationExam, lateExam, registerExamButton, programTV, certificate_due_date, attemptTV, testingCenterTV, resultTV;

    private boolean registrationIsAvailable = false;
    private boolean psiFilled = false;
    private boolean receiptPaid = false;
    private boolean bookingMade = false;
    private boolean resultsAvailable = false;


    private JSONObject object;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate_exam);

        session = new UserSession(Certificate_Exam.this);

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        examTextView = findViewById(R.id.examTextView);
        book_testing_center_Btn = findViewById(R.id.book_testing_center_Btn);
        resultBtn = findViewById(R.id.resultBtn);
        titleExam = findViewById(R.id.titleExam);
        registrationExam = findViewById(R.id.registrationExam);
        lateExam = findViewById(R.id.lateExam);
        registerExamButton = findViewById(R.id.registerExamButton);
        programTV = findViewById(R.id.programTV);
        certificate_due_date = findViewById(R.id.certificate_due_date);
        attemptTV = findViewById(R.id.attemptTV);
        testingCenterTV = findViewById(R.id.testingCenterTV);
        resultTV = findViewById(R.id.resultTV);
        noHistoryTV = findViewById(R.id.noHistoryTV);


        //Retake
        mRetakeLayout = findViewById(R.id.mRetakeLayout);
        mRetakeValue = findViewById(R.id.mRetakeValue);
        mRetakeDate = findViewById(R.id.mDate);
        mRetakeFee = findViewById(R.id.mRetakeFee);



        examTextView.setText(noTrailingwhiteLines(Html.fromHtml("<p><span style=\"color: #6c727f;\">To maintain certification, CAAs must take the CDQ Exam every ten years after passing their next exam from 1/1/2020. Students becoming CAAs from 2020 will take their first CDQ Exam in 4 years, followed by subsequent exams every 10 years. Please select an option below.</span> <span style=\"text-decoration: underline; color: #fff;\"></span></p>")));

        /*examTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Certificate_Exam.this, More_Info_Cert_Exam.class)
                        .putExtra("from", "cert"));
            }
        });*/

        findViewById(R.id.certInfoPage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(Certificate_Exam.this, More_Info_Cert_Exam.class)
//                        .putExtra("from", "cert"));
            }
        });


        book_testing_center_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (receiptPaid){
                        if (bookingMade){
                            startActivity(new Intent(Certificate_Exam.this, Testing_Center.class)
                                    .putExtra("examID", object.getString("id"))
                                    .putExtra("testingCenterUrl", object.getString("testingCenterUrl")));
                        } else {
                            Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(object.getString("testingCenterUrl")));
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(Certificate_Exam.this, "Booking is not available until registration and payment has been made during the registration periods", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


        resultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (resultsAvailable) {
                    try {
                        startActivity(new Intent(Certificate_Exam.this, Results_Score.class)
                                .putExtra("examID", object.getString("id")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        registerExamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    if (registrationIsAvailable) {
                        if (!psiFilled) {
                            startActivity(new Intent(Certificate_Exam.this, PSI_Form.class)
                                    .putExtra("examID", object.getString("id")));
                        } else {
                            if (!receiptPaid) {
                                startActivity(new Intent(Certificate_Exam.this, Add_Credit_Card_Payment.class)
                                        .putExtra("receiptId", object.getString("receiptId")));
                            } else {
                                startActivity(new Intent(Certificate_Exam.this, Receipt.class)
                                        .putExtra("receiptId", object.getString("receiptId"))
                                        .putExtra("from", "certificate_exam"));
                            }
                        }
                    } else {
                        Toast.makeText(Certificate_Exam.this, "Registration isn't available.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        });


    }

    private static final DecimalFormat df = new DecimalFormat("0.00");
    private void getExams() {


        Log.e("token", session.getAPITOKEN()+ "--");
        final KProgressHUD progressDialog = KProgressHUD.create(Certificate_Exam.this)
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


                        try {
                            Log.e("examsList", new String(response.data) + "--");

                            JSONArray jsonArray = new JSONArray(new String(response.data));
                            object = jsonArray.getJSONObject(0);


                            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                            DateFormat outputDate = new SimpleDateFormat("dd");
                            DateFormat outputMonth = new SimpleDateFormat("MMMM");
                            DateFormat outputYear = new SimpleDateFormat("yyyy");

                            String inputDateStr = object.getString("dateStart");
                            String inputDateEnd = object.getString("dateEnd");

                            Date dateStart = inputFormat.parse(inputDateStr);
                            Date dateEnd = inputFormat.parse(inputDateEnd);
                            Date monthD = inputFormat.parse(inputDateStr);
                            Date yearD = inputFormat.parse(inputDateStr);

                            String strDateStart = outputDate.format(dateStart);
                            String strDateEnd = outputDate.format(dateEnd);
                            String month = outputMonth.format(monthD);
                            String year = outputYear.format(yearD);


                            String mFinalStartDate = null;
                            if (!strDateStart.equals("null")) {
                                if (Integer.parseInt(strDateStart) < 10) {
                                    mFinalStartDate = strDateStart.replace("0", "");
                                } else {
                                    mFinalStartDate = strDateStart;
                                }
                            }

                            String mFinalEndDate = null;
                            if (!strDateEnd.equals("null")) {
                                if (Integer.parseInt(strDateEnd) < 10) {
                                    mFinalEndDate = strDateEnd.replace("0", "");
                                } else {
                                    mFinalEndDate = strDateEnd;
                                }
                            }


                            titleExam.setText(month + " " + mFinalStartDate + "-" + mFinalEndDate + ", " + year + ", " + object.getString("type").toUpperCase(Locale.ROOT) + " Exam");

                            registrationExam.setText(chnage_date_formate(object.getString("registrationStart") )+ " - "
                                    + chnage_date_formate(object.getString("registrationEnd")));

                            lateExam.setText(chnage_date_formate(object.getString("lateStart")) + " - "
                                    + chnage_date_formate(object.getString("lateEnd")));


                            registrationIsAvailable = object.getBoolean("registrationIsAvailable");
                            psiFilled = object.getBoolean("psiFilled");
                            receiptPaid = object.getBoolean("receiptPaid");
                            bookingMade = object.getBoolean("bookingMade");
                            resultsAvailable = object.getBoolean("resultsAvailable");

                            if (!registrationIsAvailable){
                                registerExamButton.setBackground(getDrawable(R.drawable.capsul_grey_btn));
                            } else {
                                registerExamButton.setBackground(getDrawable(R.drawable.capsul_blue_btn));
                            }
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

                            noHistoryTV.setText(object.getString("attemptNumber")+getDayOfMonthSuffix(Integer.parseInt(object.getString("attemptNumber")))+" Attempt");

                            if(Integer.parseInt(object.getString("attemptNumber")) <= 1){
                                mRetakeLayout.setVisibility(View.GONE);
                            }else {
                                mRetakeLayout.setVisibility(View.VISIBLE);
                                mRetakeValue.setText("Retake #"+object.getString("attemptNumber"));
                                String first = "You are required to take the next Certification exam on ";
                                String next = "<font color='#EE0000'>"+parseDateToddMMyyyy(object.getString("dateStart"))+"</font>";
                                mRetakeDate.setText(Html.fromHtml(first + next));
                                //mRetakeFee.setText("$" + df.format(Float.parseFloat(object.getString("retakeFee"))));
                                mRetakeFee.setText("$" + currencyFormat(object.getString("retakeFee")));
                            }



                            if (!receiptPaid){
                                book_testing_center_Btn.setBackground(getDrawable(R.drawable.capsul_grey_btn));
                            } else {
                                if (bookingMade){

                                    if(object.getString("bookingStatus").equals("EXAM_COMPLETED")||object.getString("bookingStatus").equals("ABSENT") && !resultsAvailable){
                                        book_testing_center_Btn.setText("VIEW DETAILS");
                                        book_testing_center_Btn.setBackground(getDrawable(R.drawable.capsul_grey_btn));
                                    }else {
                                        book_testing_center_Btn.setText("VIEW DETAILS");
                                        book_testing_center_Btn.setBackground(getDrawable(R.drawable.capsul_blue_btn));
                                    }


                                }else {
                                    book_testing_center_Btn.setText("Book Testing Center Now");
                                    book_testing_center_Btn.setBackground(getDrawable(R.drawable.capsul_grey_btn));
                                }

                            }

                            if (resultsAvailable){
                                resultBtn.setBackground(getDrawable(R.drawable.capsul_blue_btn));
                                resultBtn.setText("Results (available)");
                            } else {
                                resultBtn.setBackground(getDrawable(R.drawable.capsul_grey_btn));
                                resultBtn.setText("Results (not available)");
                            }


                        } catch (Exception e) {
                            Toast.makeText(Certificate_Exam.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(Certificate_Exam.this, json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                                //Additional cases
                            }
                        }
                        else if (error instanceof TimeoutError)
                            Toast.makeText(Certificate_Exam.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(Certificate_Exam.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

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

        Volley.newRequestQueue(Certificate_Exam.this).add(volleyMultipartRequest);
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
            case 1:  return "st";
            case 2:  return "nd";
            case 3:  return "rd";
            default: return "th";
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
                            programTV.setText(jsonObject.getString("program"));
                            if (!jsonObject.getString("certificationDueDate").equals("null")) {
                                certificate_due_date.setText(chnage_date_formate(jsonObject.getString("certificationDueDate")));
                            } else {
                                certificate_due_date.setText("N/A");
                            }
                        } catch (Exception e) {
                            Toast.makeText(Certificate_Exam.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof ServerError){
                            if(error.networkResponse != null && error.networkResponse.data != null){
                                switch(error.networkResponse.statusCode){
                                    case 500:
                                        String json = new String(error.networkResponse.data);
                                        json = session.trimMessage(json, "message");
                                        if(json != null) {
                                            Toast.makeText(Certificate_Exam.this, json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                            }
                        }
                        else if (error instanceof TimeoutError)
                            Toast.makeText(Certificate_Exam.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(Certificate_Exam.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

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

        Volley.newRequestQueue(Certificate_Exam.this).add(volleyMultipartRequest);
    }



    private CharSequence noTrailingwhiteLines(CharSequence text) {

        while (text.charAt(text.length() - 1) == '\n') {
            text = text.subSequence(0, text.length() - 1);
        }
        return text;
    }

    public static String chnage_date_formate(@NonNull String mDate) {
        String finalDate = null;
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat output = new SimpleDateFormat("MM/dd/yyyy");

        try {
            Date oneWayTripDate = input.parse(mDate);  // parse input
            finalDate = output.format(oneWayTripDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return finalDate;
    }


    @Override
    protected void onResume() {
        super.onResume();
        getExams();
        getInfo();
    }

}