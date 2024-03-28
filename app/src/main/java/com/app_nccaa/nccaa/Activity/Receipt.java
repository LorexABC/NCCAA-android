package com.app_nccaa.nccaa.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Receipt Activity
 *
 * Modified by phantom
 * On 03/22/24
 *
 * Success modal is created for payment feature.
 */
public class Receipt extends AppCompatActivity {

    private TextView contact_TV, secureTestingCenter, name, amountPaid, paidOnDate, amountDue, last4Digit, titleExam;

    private String receiptId;

    private UserSession session;
    private boolean testingUrlAvail = false;
    private String testingCenterUrl = "", from = "";

    private static final DecimalFormat df = new DecimalFormat("0.00");

    private boolean isTitleAvailable = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        session = new UserSession(Receipt.this);


        contact_TV = findViewById(R.id.contact_TV);
        secureTestingCenter = findViewById(R.id.secureTestingCenter);
        name = findViewById(R.id.name);
        amountPaid = findViewById(R.id.amountPaid);
        paidOnDate = findViewById(R.id.paidOnDate);
        amountDue = findViewById(R.id.amountDue);
        last4Digit = findViewById(R.id.last4Digit);
        titleExam = findViewById(R.id.titleExam);


        receiptId = getIntent().getStringExtra("receiptId");
        from = getIntent().getStringExtra("from");

        if (from.equals("history_cme")){
            secureTestingCenter.setVisibility(View.INVISIBLE);
        }

        Log.e("receiptId", receiptId + "--");


        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        findViewById(R.id.doneBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // startActivity(new Intent(Receipt.this, HomeScreen.class));
                finish();

            }
        });


        secureTestingCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (testingUrlAvail) {
                    Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(testingCenterUrl));
                    startActivity(intent);
                }

            }
        });

        contact_TV.setPaintFlags(contact_TV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        getReceiptInfo();

    }
    public static String currencyFormat(String amount) {
        DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
        return formatter.format(Double.parseDouble(amount));
    }

    /**
     * Get receipt information
     * Calling API
     *
     * Modified by phantom
     * On 03/22/24
     */
    private void getReceiptInfo() {
        final KProgressHUD progressDialog = KProgressHUD.create(Receipt.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET, session.BASEURL + "receipts/" + receiptId,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        progressDialog.dismiss();

                        try {
                            Log.e("receiptInfoDetail", new String(response.data) + "--");

                            JSONObject jsonObject = new JSONObject(new String(response.data));

                            name.setText(jsonObject.getString("name"));
                            amountPaid.setText("Pay $" +currencyFormat(jsonObject.getString("paidAmount")));

                            if (!jsonObject.getString("paidDate").equals("null")) {
                                paidOnDate.setText(chnage_date_formate(jsonObject.getString("paidDate")));
                            }
                            amountDue.setText("Pay $" +currencyFormat(jsonObject.getString("dueAmount")));

                            if (!jsonObject.getString("lastFourDigits").equals("null")) {
                                last4Digit.setText(jsonObject.getString("lastFourDigits"));
                            }

                            getExams(jsonObject.getString("id"));

                            if(from.equals("stateLicensing")) {
                                Toast.makeText(Receipt.this, "Your message has been sent to NCCAA. Thank You", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(Receipt.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(Receipt.this, json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                            }
                        }
                        else if (error instanceof TimeoutError)
                            Toast.makeText(Receipt.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(Receipt.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

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
                //        params.put("Accept", "application/json");
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

        Volley.newRequestQueue(Receipt.this).add(volleyMultipartRequest);
    }


    private void getExams(String receipt_id) {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET, session.BASEURL + "users/me/exams",
                new Response.Listener<NetworkResponse>() {
                    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
                    @Override
                    public void onResponse(NetworkResponse response) {

                        try {
                            Log.e("examsList", new String(response.data) + "--");

                            JSONArray jsonArray = new JSONArray(new String(response.data));

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                if (receipt_id.equals(object.getString("receiptId"))) {

                                    DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    DateFormat outputMonth = new SimpleDateFormat("MMMM");
                                    DateFormat outputYear = new SimpleDateFormat("yyyy");

                                    String inputDateStr = object.getString("dateStart");

                                    Date monthD = inputFormat.parse(inputDateStr);
                                    Date yearD = inputFormat.parse(inputDateStr);

                                    String month = outputMonth.format(monthD);
                                    String year = outputYear.format(yearD);

                                    if (from.equals("history_cme")){
                                        titleExam.setText("CME Payment");
                                    }else {
                                        titleExam.setText(month + " " + year + " " + object.getString("type").toUpperCase(Locale.ROOT) + " Exam");
                                    }

                                    isTitleAvailable = true;

                                    // for secure testng center now
                                    if (!object.getString("testingCenterUrl").equals("null")){
                                        secureTestingCenter.setBackground(getDrawable(R.drawable.blue_light_6dp));
                                        testingUrlAvail = true;
                                        testingCenterUrl = object.getString("testingCenterUrl");
                                    } else {
                                        secureTestingCenter.setBackground(getDrawable(R.drawable.gray_light_6dp));
                                        testingUrlAvail = false;
                                    }

                                }
                            }

                            if (!isTitleAvailable){
                                titleExam.setVisibility(View.GONE);
                            }

                        } catch (Exception e) {
                            Toast.makeText(Receipt.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(Receipt.this, json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                                //Additional cases
                            }
                        }
                        else if (error instanceof TimeoutError)
                            Toast.makeText(Receipt.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(Receipt.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

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

        Volley.newRequestQueue(Receipt.this).add(volleyMultipartRequest);
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


}