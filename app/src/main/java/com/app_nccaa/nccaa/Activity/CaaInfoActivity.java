package com.app_nccaa.nccaa.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CaaInfoActivity extends AppCompatActivity {


    private Spinner yearSpinner;
    private ArrayList<CityModel> year_ArrayList = new ArrayList<>();
    private ArrayList<CityModel> alma_mater_ArrayList = new ArrayList<>();
    private UserSession session;
    private RequestQueue mRequestqueue;
    private Spinner alma_mater_Spinner;
    private String univercityID = "";

    private SelectCitySpinner adapter3, adapter9;
    private KProgressHUD progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caa_info);

        session = new UserSession(CaaInfoActivity.this);
        mRequestqueue = Volley.newRequestQueue(CaaInfoActivity.this);


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        yearSpinner = findViewById(R.id.yearSpinner);
        alma_mater_Spinner = findViewById(R.id.alma_mater_Spinner);

        final String first_name;
        final String middle_name;
        final String last_name;
        final String cell_phone;
        final String dob;
        final String gender;
        final String address;
        final String city;
        final String zip;
        final String state;


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                first_name = null;
                middle_name = null;
                last_name = null;
                cell_phone = null;
                dob = null;
                gender = null;
                address = null;
                city = null;
                zip = null;
                state = null;
            } else {
                first_name = extras.getString("first_name");
                middle_name = extras.getString("middle_name");
                last_name = extras.getString("last_name");
                cell_phone = extras.getString("cell_phone");
                dob = extras.getString("dob");
                gender = extras.getString("gender");
                address = extras.getString("address");
                city = extras.getString("city");
                zip = extras.getString("zip");
                state = extras.getString("state");
            }
        } else {
            first_name = (String) savedInstanceState.getSerializable("first_name");
            middle_name = (String) savedInstanceState.getSerializable("middle_name");
            last_name = (String) savedInstanceState.getSerializable("last_name");
            cell_phone = (String) savedInstanceState.getSerializable("cell_phone");
            dob = (String) savedInstanceState.getSerializable("dob");
            gender = (String) savedInstanceState.getSerializable("gender");
            address = (String) savedInstanceState.getSerializable("address");
            city = (String) savedInstanceState.getSerializable("city");
            zip = (String) savedInstanceState.getSerializable("zip");
            state = (String) savedInstanceState.getSerializable("state");
        }


        findViewById(R.id.btnContinueId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (yearSpinner.getSelectedItemPosition() == year_ArrayList.size() - 1
                        && !session.getUSER_TYPE().equals("student")) {
                    Toast.makeText(CaaInfoActivity.this, "select year", Toast.LENGTH_SHORT).show();
                } else if (alma_mater_Spinner.getSelectedItemPosition() == alma_mater_ArrayList.size() - 1
                        && !session.getUSER_TYPE().equals("student")) {
                    Toast.makeText(CaaInfoActivity.this, "select university", Toast.LENGTH_SHORT).show();
                } else {
                    setPSIFrom(first_name, middle_name, last_name, session.getEmail(), gender, dob, cell_phone,
                            address, city, state, zip);
                }

            }
        });

        getUniversities();

    }

    private void getUniversities() {
        progressDialog = KProgressHUD.create(CaaInfoActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

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
                            cityModel.setId("00");
                            cityModel.setName("Select CAA School");
                            cityModel.setCode("");
                            alma_mater_ArrayList.add(cityModel);


                            adapter9 = new SelectCitySpinner(CaaInfoActivity.this, android.R.layout.simple_spinner_item, alma_mater_ArrayList);
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

                            Toast.makeText(CaaInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(CaaInfoActivity.this, json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                            }
                        } else if (error instanceof TimeoutError)
                            Toast.makeText(CaaInfoActivity.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(CaaInfoActivity.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

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
                params.put("Authorization", "Bearer " + getIntent().getStringExtra("token"));
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

                return params;
            }
        };

        volleyMultipartRequest.setShouldRetryServerErrors(true);

        Volley.newRequestQueue(CaaInfoActivity.this).add(volleyMultipartRequest);
    }


    private boolean isYearSelected = false;

    private void getPersonal() {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET, session.BASEURL + "users/me/personal",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        progressDialog.dismiss();

                        try {
                            Log.e("personal", new String(response.data) + "--");

                            JSONObject jsonObject = new JSONObject(new String(response.data));

                            univercityID = jsonObject.getString("universityId");

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

                            adapter3 = new SelectCitySpinner(CaaInfoActivity.this, android.R.layout.simple_spinner_item, year_ArrayList);
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
                            Toast.makeText(CaaInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(CaaInfoActivity.this, json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                            }
                        } else if (error instanceof TimeoutError)
                            Toast.makeText(CaaInfoActivity.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(CaaInfoActivity.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

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
                params.put("Authorization", "Bearer " + getIntent().getStringExtra("token"));
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

                return params;
            }
        };

        volleyMultipartRequest.setShouldRetryServerErrors(true);

        Volley.newRequestQueue(CaaInfoActivity.this).add(volleyMultipartRequest);
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
        final KProgressHUD progressDialog = KProgressHUD.create(CaaInfoActivity.this)
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
        if (session.getUSER_TYPE().equals("student")){
            postParam3.put("universityId", Integer.parseInt(univercityID));
        } else {
            postParam3.put("universityId", Integer.parseInt(alma_mater_ArrayList.get(alma_mater_Spinner.getSelectedItemPosition()).getId()));
        }

        JSONObject object = new JSONObject(postParam3);

        Log.e("getJson", object.toString() + "--");


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, session.BASEURL + "users/me/psi", object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressDialog.dismiss();

                        Log.e("psiData", response.toString() + "--");
                        session.setPSI_FILLED("true");

                        startActivity(new Intent(CaaInfoActivity.this, PsiMandatory2Activity.class)
                                .putExtra("token", getIntent().getStringExtra("token")));

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                String json = new String(error.networkResponse.data);
                json = session.trimMessage(json, "error");
                Toast.makeText(CaaInfoActivity.this, json, Toast.LENGTH_LONG).show();

                if (error instanceof ServerError) {

                } else if (error instanceof TimeoutError) {
                    Toast.makeText(CaaInfoActivity.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(CaaInfoActivity.this, "Bad Network Connection", Toast.LENGTH_LONG).show();
                }

            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Authorization", "Bearer " + getIntent().getStringExtra("token"));
                return headers;
            }
        };

        jsonObjReq.setTag("TAG");
        // Adding request to request queue
        mRequestqueue.add(jsonObjReq);
    }


}