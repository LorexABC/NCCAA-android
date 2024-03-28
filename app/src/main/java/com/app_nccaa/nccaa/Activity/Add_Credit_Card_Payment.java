package com.app_nccaa.nccaa.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app_nccaa.nccaa.Utils.UserSession;
import com.app_nccaa.nccaa.R;
import com.app_nccaa.nccaa.Utils.FourDigitCardFormatWatcher;
import com.app_nccaa.nccaa.Utils.VolleyMultipartRequest;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Add_Credit_Card_Payment extends AppCompatActivity {


    private EditText card_number_ET, nameCard, security_code, zipCodeET;
    private TextView payAmountTV, payment_amount_ET;

    private static TextView DOB_TV;

    public int pos = 0;

    private String receiptId, from;

    private UserSession session;
    private RequestQueue mRequestqueue;
    private static final DecimalFormat df = new DecimalFormat("0.00");


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_credit_card_payment);

        session = new UserSession(Add_Credit_Card_Payment.this);
        mRequestqueue = Volley.newRequestQueue(Add_Credit_Card_Payment.this);


        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        card_number_ET = findViewById(R.id.card_number_ET);
        payAmountTV = findViewById(R.id.payAmountTV);
        payment_amount_ET = findViewById(R.id.payment_amount_ET);
        nameCard = findViewById(R.id.nameCard);
        security_code = findViewById(R.id.security_code);
        zipCodeET = findViewById(R.id.zipCodeET);
        DOB_TV = findViewById(R.id.DOB_TV);

        payAmountTV.setText("Pay $" +currencyFormat("25"));
        payment_amount_ET.setText(currencyFormat("25"));

        receiptId = getIntent().getStringExtra("receiptId");
        from = getIntent().getStringExtra("from");
        //receiptId = "0";


        DOB_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    DialogFragment newFragment = new DatePickerFragment();
           //     newFragment.show(getSupportFragmentManager(), "datePicker");

                final Calendar c = Calendar.getInstance();
                int mYear, mMonth, mDay;

                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog monthDatePickerDialog = new DatePickerDialog(Add_Credit_Card_Payment.this,
                        AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String theNumber = String.valueOf(year);
                        String lastTwoDigits = theNumber.substring(theNumber.length() -2, theNumber.length());
                        DOB_TV.setText( (month + 1)+ "/" +lastTwoDigits);
                    }
                }, mYear, mMonth, mDay){
                    @Override
                    protected void onCreate(Bundle savedInstanceState) {
                        super.onCreate(savedInstanceState);
                        getDatePicker().findViewById(getResources().getIdentifier("day","id","android")).setVisibility(View.GONE);
                    }
                };
                monthDatePickerDialog.setTitle("Select Month & Year");
                monthDatePickerDialog.show();
            }
        });


        findViewById(R.id.payBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if (nameCard.getText().toString().isEmpty()){
                    Toast.makeText(Add_Credit_Card_Payment.this, "enter name", Toast.LENGTH_SHORT).show();
                } else if (card_number_ET.getText().toString().isEmpty() || card_number_ET.getText().toString().length() <= 18 ){
                    Toast.makeText(Add_Credit_Card_Payment.this, "Please enter a valid credit card number", Toast.LENGTH_SHORT).show();
                } else if (DOB_TV.getText().toString().isEmpty()){
                    Toast.makeText(Add_Credit_Card_Payment.this, "enter expiry date", Toast.LENGTH_SHORT).show();
                } else if (security_code.getText().toString().isEmpty() || security_code.getText().toString().length() <= 2 ){
                    Toast.makeText(Add_Credit_Card_Payment.this, "enter valid security code", Toast.LENGTH_SHORT).show();
                } else if (zipCodeET.getText().toString().isEmpty() || zipCodeET.getText().toString().length() <= 4 ){
                    Toast.makeText(Add_Credit_Card_Payment.this, "enter valid zip code", Toast.LENGTH_SHORT).show();
                } else {
                    processPayment();
                }
                
            }
        });


        card_number_ET.addTextChangedListener(new FourDigitCardFormatWatcher());


        getReceiptInfo();
      //  Log.e("mtoken",session.getAPITOKEN());

    }

    private void processPayment() {
        final KProgressHUD progressDialog = KProgressHUD.create(Add_Credit_Card_Payment.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();


        Map<String, Object> postParam3= new HashMap<String, Object>();
        postParam3.put("name", nameCard.getText().toString());
        postParam3.put("cardNumber", card_number_ET.getText().toString().replace(" ",""));
        postParam3.put("expireDate", "20"+chnage_date_format_Old(DOB_TV.getText().toString()));
        postParam3.put("securityCode", security_code.getText().toString());
        postParam3.put("zipCode", zipCodeET.getText().toString());

        JSONObject object = new JSONObject(postParam3);

        Log.e("getJson", session.BASEURL + "receipts/" + receiptId + "/payment");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, session.BASEURL + "receipts/" + receiptId + "/payment", object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressDialog.dismiss();

                        Log.e("psiData", response.toString() + "--");

                        String param = "credit_card";
                        if (!from.isEmpty()) {
                            param = from;
                        }

                        startActivity(new Intent(Add_Credit_Card_Payment.this, Receipt.class)
                                .putExtra("receiptId", receiptId)
                                .putExtra("from", param));
                        finish();


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();


                try {
                    String json = new String(error.networkResponse.data);
                    json = session.trimMessage(json, "error");
                    Toast.makeText(Add_Credit_Card_Payment.this, json, Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    Toast.makeText(Add_Credit_Card_Payment.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }


                if (error instanceof ServerError) {

                }
                else if (error instanceof TimeoutError) {
                    Toast.makeText(Add_Credit_Card_Payment.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                }
                else if (error instanceof NetworkError) {
                    Toast.makeText(Add_Credit_Card_Payment.this, "Bad Network Connection", Toast.LENGTH_LONG).show();
                }

            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Authorization", "Bearer " + session.getAPITOKEN());
                return headers;
            }
        };

        jsonObjReq.setTag("TAG");
        // Adding request to request queue
        mRequestqueue.add(jsonObjReq);
    }

    public static String currencyFormat(String amount) {
        DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
        return formatter.format(Double.parseDouble(amount));
    }

    private void getReceiptInfo() {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET, session.BASEURL + "receipts/" + receiptId,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        try {
                            Log.e("receiptInfo", new String(response.data) + "--");

                            JSONObject jsonObject = new JSONObject(new String(response.data));

                            payment_amount_ET.setText(currencyFormat(jsonObject.getString("dueAmount")));

                            payAmountTV.setText("Pay $" +currencyFormat(jsonObject.getString("dueAmount")));


                        } catch (Exception e) {
                            Toast.makeText(Add_Credit_Card_Payment.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(Add_Credit_Card_Payment.this, json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                            }
                        }
                        else if (error instanceof TimeoutError)
                            Toast.makeText(Add_Credit_Card_Payment.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(Add_Credit_Card_Payment.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

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

        Volley.newRequestQueue(Add_Credit_Card_Payment.this).add(volleyMultipartRequest);
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int mYear, mMonth, mDay, mHour, mMinute;

            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, mYear, mMonth, mDay);
            dialog.getDatePicker().setMinDate(c.getTimeInMillis());
            c.add(Calendar.YEAR, 8);
            dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            return  dialog;
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String dateString = dateFormat.format(calendar.getTime());

            DOB_TV.setText(dateString);

           /* if(!time.getText().toString().equals("   Select  Date:")){

                try {
                    Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(dateString);
                    Date date11=new SimpleDateFormat("dd-MM-yyyy").parse(time.getText().toString());
                    printDifference(date2,date11);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }*/
        }

    }

    public static String chnage_date_format_Old(@NonNull String mDate) {
        String finalDate = null;
        SimpleDateFormat input = new SimpleDateFormat("MM/yy");
        SimpleDateFormat output = new SimpleDateFormat("yy-MM");

        try {
            Date oneWayTripDate = input.parse(mDate);  // parse input
            finalDate = output.format(oneWayTripDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return finalDate;
    }



}