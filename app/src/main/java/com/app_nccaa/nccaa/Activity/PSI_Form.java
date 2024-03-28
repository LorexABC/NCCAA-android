package com.app_nccaa.nccaa.Activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.app_nccaa.nccaa.Adapter.SelectCitySpinner;
import com.app_nccaa.nccaa.Model.CityModel;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PSI_Form extends AppCompatActivity {

    private Spinner genderSpinner;
    private EditText etFirstNameId, etMiddleNameId, etLastNameId, etCellPhoneId, etHomeAddressId, etCityId, etZipId, emailET;

    private ArrayList<CityModel> gender_ArrayList = new ArrayList<>();

    private UserSession session;

    private static TextView DOB_TV;

    private String lastChar = " ";

    private TextView submitBtn, autoIDTV;

    private ArrayList<CityModel> state_ArrayList = new ArrayList<>();
    private Spinner stateSpinner;
    private String mState = "";

    private Spinner yearSpinner;
    private ArrayList<CityModel> year_ArrayList = new ArrayList<>();
    private ArrayList<CityModel> alma_mater_ArrayList = new ArrayList<>();

    private RequestQueue mRequestqueue;
    private Spinner alma_mater_Spinner;

    private SelectCitySpinner adapter3, adapter9;

    private String examID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psi_form);

        session = new UserSession(PSI_Form.this);
        mRequestqueue = Volley.newRequestQueue(PSI_Form.this);


        genderSpinner = findViewById(R.id.genderSpinner);
        DOB_TV = findViewById(R.id.DOB_TV);
        etCellPhoneId = findViewById(R.id.etCellPhoneId);
        etFirstNameId = findViewById(R.id.etFirstNameId);
        etMiddleNameId = findViewById(R.id.etMiddleNameId);
        etLastNameId = findViewById(R.id.etLastNameId);
        etHomeAddressId = findViewById(R.id.etHomeAddressId);
        emailET = findViewById(R.id.emailET);
        etCityId = findViewById(R.id.etCityId);
        etZipId = findViewById(R.id.etZipId);
        stateSpinner = findViewById(R.id.stateSpinner);
        yearSpinner = findViewById(R.id.yearSpinner);
        alma_mater_Spinner = findViewById(R.id.alma_mater_Spinner);
        submitBtn = findViewById(R.id.submitBtn);
        autoIDTV = findViewById(R.id.autoIDTV);


        examID = getIntent().getStringExtra("examID");


        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        autoIDTV.setText("ID " + session.getUSER_ID());


        findViewById(R.id.submitBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PSI_Form.this, Add_Credit_Card_Payment.class));
                finish();
            }
        });



      /*  etCellPhoneId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int digits = etCellPhoneId.getText().toString().length();
                if (digits > 1)
                    lastChar = etCellPhoneId.getText().toString().substring(digits-1);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int digits = etCellPhoneId.getText().toString().length();
                Log.d("LENGTH",""+digits);
                if (!lastChar.equals("-")) {
                    if (digits == 3 || digits == 7) {
                        etCellPhoneId.append("-");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/


        DOB_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
                //      DatePicker();
            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (etFirstNameId.getText().toString().isEmpty()) {
                    Toast.makeText(PSI_Form.this, "Please enter first name", Toast.LENGTH_SHORT).show();
                } else if (etLastNameId.getText().toString().isEmpty()) {
                    Toast.makeText(PSI_Form.this, "Please enter last name", Toast.LENGTH_SHORT).show();
                } else if (etCellPhoneId.getText().toString().isEmpty() || etCellPhoneId.getText().toString().length() <= 9) {
                    Toast.makeText(PSI_Form.this, "Please enter cell phone", Toast.LENGTH_SHORT).show();
                } else if (DOB_TV.getText().toString().isEmpty()) {
                    Toast.makeText(PSI_Form.this, "Please enter DOB", Toast.LENGTH_SHORT).show();
                } else if (genderSpinner.getSelectedItemPosition() == gender_ArrayList.size() - 1) {
                    Toast.makeText(PSI_Form.this, "Please select gender", Toast.LENGTH_SHORT).show();
                } else if (emailET.getText().toString().isEmpty()) {
                    Toast.makeText(PSI_Form.this, "Please enter email", Toast.LENGTH_SHORT).show();
                } else if (etHomeAddressId.getText().toString().isEmpty()) {
                    Toast.makeText(PSI_Form.this, "Please enter home street address", Toast.LENGTH_SHORT).show();
                } else if (etCityId.getText().toString().isEmpty()) {
                    Toast.makeText(PSI_Form.this, "Please enter city", Toast.LENGTH_SHORT).show();
                } else if (etZipId.getText().toString().isEmpty() || etZipId.getText().toString().length() <= 4) {
                    Toast.makeText(PSI_Form.this, "Please enter zip code", Toast.LENGTH_SHORT).show();
                } else if (mState.isEmpty()) {
                    Toast.makeText(PSI_Form.this, "Please select state", Toast.LENGTH_SHORT).show();
                } else if (yearSpinner.getSelectedItemPosition() == year_ArrayList.size() - 1 && !session.getUSER_TYPE().equals("student")) {
                    Toast.makeText(PSI_Form.this, "select year", Toast.LENGTH_SHORT).show();
                } else if (alma_mater_Spinner.getSelectedItemPosition() == alma_mater_ArrayList.size() - 1 && !session.getUSER_TYPE().equals("student")) {
                    Toast.makeText(PSI_Form.this, "select university", Toast.LENGTH_SHORT).show();
                } else {
                    setPSIFrom(etFirstNameId.getText().toString(), etMiddleNameId.getText().toString(), etLastNameId.getText().toString(),
                            emailET.getText().toString().trim(), gender_ArrayList.get(genderSpinner.getSelectedItemPosition()).getId(),
                            DOB_TV.getText().toString(), etCellPhoneId.getText().toString(),
                            etHomeAddressId.getText().toString(), etCityId.getText().toString(),
                            state_ArrayList.get(stateSpinner.getSelectedItemPosition()).getId(), etZipId.getText().toString());
                }

            }
        });


        // GENDER SPINNER
        CityModel cityModel6 = new CityModel();
        cityModel6.setId("male");
        cityModel6.setName("Male");
        gender_ArrayList.add(cityModel6);
        CityModel cityModel8 = new CityModel();
        cityModel8.setId("female");
        cityModel8.setName("Female");
        gender_ArrayList.add(cityModel8);
        CityModel cityModel7 = new CityModel();
        cityModel7.setId("");
        cityModel7.setName("Select Gender");
        gender_ArrayList.add(cityModel7);

        SelectCitySpinner adapterGen = new SelectCitySpinner(this, android.R.layout.simple_spinner_item, gender_ArrayList);
        adapterGen.setDropDownViewResource(android.R.layout.simple_list_item_1);
        genderSpinner.setAdapter(adapterGen);
        genderSpinner.setSelection(adapterGen.getCount());

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == (gender_ArrayList.size() - 1)) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_gray_light));
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // STATE STATUS SPINNER
        CityModel sta1 = new CityModel();
        sta1.setId("AL");
        sta1.setName("AL");
        state_ArrayList.add(sta1);
        CityModel sta2 = new CityModel();
        sta2.setId("AK");
        sta2.setName("AK");
        state_ArrayList.add(sta2);
        CityModel sta3 = new CityModel();
        sta3.setId("AZ");
        sta3.setName("AZ");
        state_ArrayList.add(sta3);
        CityModel sta4 = new CityModel();
        sta4.setId("AR");
        sta4.setName("AR");
        state_ArrayList.add(sta4);
        CityModel sta5 = new CityModel();
        sta5.setId("CA");
        sta5.setName("CA");
        state_ArrayList.add(sta5);
        CityModel sta6 = new CityModel();
        sta6.setId("CO");
        sta6.setName("CO");
        state_ArrayList.add(sta6);
        CityModel sta7 = new CityModel();
        sta7.setId("CT");
        sta7.setName("CT");
        state_ArrayList.add(sta7);
        CityModel sta8 = new CityModel();
        sta8.setId("DE");
        sta8.setName("DE");
        state_ArrayList.add(sta8);
        CityModel sta9 = new CityModel();
        sta9.setId("DC");
        sta9.setName("DC");
        state_ArrayList.add(sta9);
        CityModel sta10 = new CityModel();
        sta10.setId("FL");
        sta10.setName("FL");
        state_ArrayList.add(sta10);
        CityModel sta11 = new CityModel();
        sta11.setId("GA");
        sta11.setName("GA");
        state_ArrayList.add(sta11);
        CityModel sta12 = new CityModel();
        sta12.setId("HI");
        sta12.setName("HI");
        state_ArrayList.add(sta12);
        CityModel sta13 = new CityModel();
        sta13.setId("ID");
        sta13.setName("ID");
        state_ArrayList.add(sta13);
        CityModel sta14 = new CityModel();
        sta14.setId("IL");
        sta14.setName("IL");
        state_ArrayList.add(sta14);
        CityModel sta15 = new CityModel();
        sta15.setId("IN");
        sta15.setName("IN");
        state_ArrayList.add(sta15);
        CityModel sta16 = new CityModel();
        sta16.setId("IA");
        sta16.setName("IA");
        state_ArrayList.add(sta16);
        CityModel sta17 = new CityModel();
        sta17.setId("KS");
        sta17.setName("KS");
        state_ArrayList.add(sta17);
        CityModel sta18 = new CityModel();
        sta18.setId("KY");
        sta18.setName("KY");
        state_ArrayList.add(sta18);
        CityModel sta19 = new CityModel();
        sta19.setId("LA");
        sta19.setName("LA");
        state_ArrayList.add(sta19);
        CityModel sta20 = new CityModel();
        sta20.setId("ME");
        sta20.setName("ME");
        state_ArrayList.add(sta20);
        CityModel sta21 = new CityModel();
        sta21.setId("MD");
        sta21.setName("MD");
        state_ArrayList.add(sta21);
        CityModel sta22 = new CityModel();
        sta22.setId("MA");
        sta22.setName("MA");
        state_ArrayList.add(sta22);
        CityModel sta23 = new CityModel();
        sta23.setId("MI");
        sta23.setName("MI");
        state_ArrayList.add(sta23);
        CityModel sta24 = new CityModel();
        sta24.setId("MN");
        sta24.setName("MN");
        state_ArrayList.add(sta24);
        CityModel sta25 = new CityModel();
        sta25.setId("MS");
        sta25.setName("MS");
        state_ArrayList.add(sta25);
        CityModel sta26 = new CityModel();
        sta26.setId("MO");
        sta26.setName("MO");
        state_ArrayList.add(sta26);
        CityModel sta27 = new CityModel();
        sta27.setId("MT");
        sta27.setName("MT");
        state_ArrayList.add(sta27);
        CityModel sta28 = new CityModel();
        sta28.setId("NE");
        sta28.setName("NE");
        state_ArrayList.add(sta28);
        CityModel sta29 = new CityModel();
        sta29.setId("NV");
        sta29.setName("NV");
        state_ArrayList.add(sta29);
        CityModel sta30 = new CityModel();
        sta30.setId("NH");
        sta30.setName("NH");
        state_ArrayList.add(sta30);
        CityModel sta31 = new CityModel();
        sta31.setId("NJ");
        sta31.setName("NJ");
        state_ArrayList.add(sta31);
        CityModel sta32 = new CityModel();
        sta32.setId("NM");
        sta32.setName("NM");
        state_ArrayList.add(sta32);
        CityModel sta33 = new CityModel();
        sta33.setId("NY");
        sta33.setName("NY");
        state_ArrayList.add(sta33);
        CityModel sta34 = new CityModel();
        sta34.setId("NC");
        sta34.setName("NC");
        state_ArrayList.add(sta34);
        CityModel sta35 = new CityModel();
        sta35.setId("ND");
        sta35.setName("ND");
        state_ArrayList.add(sta35);
        CityModel sta36 = new CityModel();
        sta36.setId("OH");
        sta36.setName("OH");
        state_ArrayList.add(sta36);
        CityModel sta37 = new CityModel();
        sta37.setId("OK");
        sta37.setName("OK");
        state_ArrayList.add(sta37);
        CityModel sta38 = new CityModel();
        sta38.setId("OR");
        sta38.setName("OR");
        state_ArrayList.add(sta38);
        CityModel sta39 = new CityModel();
        sta39.setId("PA");
        sta39.setName("PA");
        state_ArrayList.add(sta39);
        CityModel sta40 = new CityModel();
        sta40.setId("RI");
        sta40.setName("RI");
        state_ArrayList.add(sta40);
        CityModel sta41 = new CityModel();
        sta41.setId("SC");
        sta41.setName("SC");
        state_ArrayList.add(sta41);
        CityModel sta42 = new CityModel();
        sta42.setId("SD");
        sta42.setName("SD");
        state_ArrayList.add(sta42);
        CityModel sta43 = new CityModel();
        sta43.setId("TN");
        sta43.setName("TN");
        state_ArrayList.add(sta43);
        CityModel sta44 = new CityModel();
        sta44.setId("TX");
        sta44.setName("TX");
        state_ArrayList.add(sta44);
        CityModel sta45 = new CityModel();
        sta45.setId("UT");
        sta45.setName("UT");
        state_ArrayList.add(sta45);
        CityModel sta46 = new CityModel();
        sta46.setId("VT");
        sta46.setName("VT");
        state_ArrayList.add(sta46);
        CityModel sta47 = new CityModel();
        sta47.setId("VA");
        sta47.setName("VA");
        state_ArrayList.add(sta47);
        CityModel sta48 = new CityModel();
        sta48.setId("WA");
        sta48.setName("WA");
        state_ArrayList.add(sta48);
        CityModel sta49 = new CityModel();
        sta49.setId("WV");
        sta49.setName("WV");
        state_ArrayList.add(sta49);
        CityModel sta50 = new CityModel();
        sta50.setId("WI");
        sta50.setName("WI");
        state_ArrayList.add(sta50);
        CityModel sta51 = new CityModel();
        sta51.setId("WY");
        sta51.setName("WY");
        state_ArrayList.add(sta51);
        CityModel cityModel17 = new CityModel();
        cityModel17.setId("");
        cityModel17.setName("State");
        state_ArrayList.add(cityModel17);

        SelectCitySpinner adapter8 = new SelectCitySpinner(PSI_Form.this, android.R.layout.simple_spinner_item, state_ArrayList);
        adapter8.setDropDownViewResource(android.R.layout.simple_list_item_1);
        stateSpinner.setAdapter(adapter8);
        stateSpinner.setSelection(adapter8.getCount());

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == (state_ArrayList.size() - 1)) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_gray_light));
                } else {

                }

                mState = state_ArrayList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getUniversities();

    }


    private void setPSIFrom(String firstName,
                            String middleName,
                            String lastName,
                            String email,
                            String gender,
                            String dateOfBirth,
                            String phone,
                            String address,
                            String city,
                            String state,
                            String zipCode) {
        final KProgressHUD progressDialog = KProgressHUD.create(PSI_Form.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();


        Map<String, Object> postParam3 = new HashMap<String, Object>();
        postParam3.put("firstName", firstName);
        postParam3.put("middleName", middleName);
        postParam3.put("lastName", lastName);
        postParam3.put("email", email);
        postParam3.put("gender", gender);
        postParam3.put("dateOfBirth", dateOfBirth);
        postParam3.put("phone", phone);
        postParam3.put("address", address);
        postParam3.put("city", city);
        postParam3.put("country", "string");
        postParam3.put("state", state);
        postParam3.put("zipCode", zipCode);
        postParam3.put("graduationYear", Integer.parseInt(year_ArrayList.get(yearSpinner.getSelectedItemPosition()).getId()));
        postParam3.put("universityId", Integer.parseInt(alma_mater_ArrayList.get(alma_mater_Spinner.getSelectedItemPosition()).getId()));

        JSONObject object = new JSONObject(postParam3);

        Log.e("getJson", object.toString() + "--");


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, session.BASEURL + "users/me/exams/" + examID + "/psi", object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressDialog.dismiss();

                        Log.e("psiData", response.toString() + "--");

                        try {

                            startActivity(new Intent(PSI_Form.this, Add_Credit_Card_Payment.class)
                                    .putExtra("receiptId", response.getString("receiptId")));

                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                String json = new String(error.networkResponse.data);
                json = session.trimMessage(json, "error");
                Toast.makeText(PSI_Form.this, json, Toast.LENGTH_LONG).show();

                if (error instanceof ServerError) {

                } else if (error instanceof TimeoutError) {
                    Toast.makeText(PSI_Form.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(PSI_Form.this, "Bad Network Connection", Toast.LENGTH_LONG).show();
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


    private void getUniversities() {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET, session.BASEURL + "universities",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {


                        try {
                            Log.e("universityList", new String(response.data) + "--");

                            JSONArray jsonArray = new JSONArray(new String(response.data));

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                CityModel cityModel = new CityModel();
                                cityModel.setId(jsonObject.getString("id"));
                                cityModel.setName(jsonObject.getString("name"));
                                cityModel.setCode(jsonObject.getString("code"));
                                alma_mater_ArrayList.add(cityModel);
                            }

                            CityModel cityModel = new CityModel();
                            cityModel.setId(session.getUNIVERSITY_ID());
                            cityModel.setName("Select CAA School");
                            cityModel.setCode("");
                            alma_mater_ArrayList.add(cityModel);

                            adapter9 = new SelectCitySpinner(PSI_Form.this, android.R.layout.simple_spinner_item, alma_mater_ArrayList);
                            adapter9.setDropDownViewResource(android.R.layout.simple_list_item_1);
                            alma_mater_Spinner.setAdapter(adapter9);
                            alma_mater_Spinner.setSelection(adapter9.getCount());

                            alma_mater_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    if (position == (alma_mater_ArrayList.size() - 1)) {
                                        ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_gray_light));
                                    } else {

                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                            getPersonal();

                        } catch (Exception e) {

                            Toast.makeText(PSI_Form.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(PSI_Form.this, json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                            }
                        } else if (error instanceof TimeoutError)
                            Toast.makeText(PSI_Form.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(PSI_Form.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

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

        Volley.newRequestQueue(PSI_Form.this).add(volleyMultipartRequest);
    }


    private boolean isYearSelected = false;

    private void getPersonal() {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET
                , session.BASEURL + "users/me/personal",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        try {
                            Log.e("personal", new String(response.data) + "--");

                            JSONObject jsonObject = new JSONObject(new String(response.data));

                            int year = Calendar.getInstance().get(Calendar.YEAR);
                            for (int i = 1970; i <= year; i++) {
                                CityModel ciqt2yModel6 = new CityModel();
                                ciqt2yModel6.setId(String.valueOf(i));
                                ciqt2yModel6.setName(String.valueOf(i));
                                year_ArrayList.add(ciqt2yModel6);
                            }
                            CityModel cityModel71 = new CityModel();
                            cityModel71.setId("2024");
                            cityModel71.setName(jsonObject.getString("graduationYear"));
                            year_ArrayList.add(cityModel71);

                            adapter3 = new SelectCitySpinner(PSI_Form.this, android.R.layout.simple_spinner_item, year_ArrayList);
                            adapter3.setDropDownViewResource(android.R.layout.simple_list_item_1);
                            yearSpinner.setAdapter(adapter3);


                            if (!session.getUSER_TYPE().equals("student")) {
                                if (!jsonObject.getString("universityId").equals("null")) {
                                    for (int i = 0; i < alma_mater_ArrayList.size(); i++) {
                                        if (jsonObject.getString("universityId").equals(alma_mater_ArrayList.get(i).getId())) {
                                            alma_mater_Spinner.setSelection(i);
                                        }
                                    }
                                }
                                if (!jsonObject.getString("graduationYear").equals("null")) {
                                    for (int i = 0; i < year_ArrayList.size(); i++) {
                                        if (jsonObject.getString("graduationYear").equals(year_ArrayList.get(i).getId())) {
                                            yearSpinner.setSelection(i);
                                        }
                                    }
                                }
                            } else {
                                if (!jsonObject.getString("universityId").equals("null")) {
                                    for (int i = 0; i < alma_mater_ArrayList.size(); i++) {
                                        if (jsonObject.getString("universityId").equals(alma_mater_ArrayList.get(i).getId())) {
                                            alma_mater_Spinner.setSelection(i);
                                        }
                                    }
                                }
                                if (!jsonObject.getString("graduationYear").equals("null")) {
                                    for (int i = 0; i < year_ArrayList.size(); i++) {
                                        if (jsonObject.getString("graduationYear").equals(year_ArrayList.get(i).getId())) {
                                            yearSpinner.setSelection(i);
                                            isYearSelected = true;
                                        }
                                    }
                                }
                                alma_mater_Spinner.setEnabled(false);
                                yearSpinner.setEnabled(false);
                            }


                            if (!isYearSelected) {
                                yearSpinner.setSelection(adapter3.getCount());
                            }

                            yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    if (position == (year_ArrayList.size() - 1)) {
                                        ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_gray_light));
                                    } else {

                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });


                        } catch (Exception e) {
                            Toast.makeText(PSI_Form.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(PSI_Form.this, json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                            }
                        } else if (error instanceof TimeoutError)
                            Toast.makeText(PSI_Form.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(PSI_Form.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

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
        Volley.newRequestQueue(PSI_Form.this).add(volleyMultipartRequest);
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int mYear, mMonth, mDay, mHour, mMinute;

            c.add(Calendar.YEAR, -18);
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, mYear, mMonth, mDay);
            dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            return dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
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


}