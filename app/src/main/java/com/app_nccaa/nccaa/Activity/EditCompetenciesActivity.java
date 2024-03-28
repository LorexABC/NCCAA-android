package com.app_nccaa.nccaa.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.app_nccaa.nccaa.Adapter.SelectClassificationSpinner;
import com.app_nccaa.nccaa.Model.ClassificationModel;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app_nccaa.nccaa.Utils.UserSession;
import com.app_nccaa.nccaa.R;
import com.app_nccaa.nccaa.Utils.UserSession;
import com.app_nccaa.nccaa.Utils.VolleyMultipartRequest;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.tabs.TabLayout;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemScrollListener;
import com.weigan.loopview.OnItemSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Edit competency Activity
 *
 * Modified by phantom
 * On 03/22/24
 * Same Create competency activity
 */
public class EditCompetenciesActivity extends AppCompatActivity {

    private AutoCompleteTextView c_category_tv, c_title_tv, c_age_tv;

    private Spinner classification_spinner;
    private TextView txtName;
    private LoopView st_rv_hour, st_rv_min, et_rv_hour, et_rv_min;
    private static AutoCompleteTextView c_date_tv;

    private TabLayout start_ampm_tabbar, end_ampm_tabbar;
    private SwitchMaterial c_asa;

    private UserSession session;

    private String hr_st, min_st;
    private String hr_et, min_et;

    private String id, asa, st_for_final, et_for_final;

    private ArrayList<String> categoriesIDArray = new ArrayList<>();
    private ArrayList<String> categoriesNameArray = new ArrayList<>();
    private final ArrayList<ClassificationModel> classificationModels = new ArrayList<>();
    private TextView show_category_name;


    /**
     * Creator
     * @param savedInstanceState
     *
     * Modified by phantom
     * On 03/22/24
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_competencies);

        session = new UserSession(EditCompetenciesActivity.this);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        classification_spinner = findViewById(R.id.c_classification_tv);
        c_category_tv = findViewById(R.id.c_category_tv);
        txtName = findViewById(R.id.txtName);
        c_date_tv = findViewById(R.id.c_date_tv);
        c_title_tv = findViewById(R.id.c_title_tv);
        c_age_tv = findViewById(R.id.c_age_tv);
        c_asa = findViewById(R.id.c_asa);
        st_rv_min = findViewById(R.id.st_rv_min);
        st_rv_hour = findViewById(R.id.st_rv_hour);
        et_rv_hour = findViewById(R.id.et_rv_hour);
        et_rv_min = findViewById(R.id.et_rv_min);
        show_category_name = findViewById(R.id.show_category_name);

        txtName.setText("Update Case/Patient");

        String fromClinical = getIntent().getStringExtra("fromClinical");
        id = getIntent().getStringExtra("id");
        String start_time = getIntent().getStringExtra("start_time");
        String end_time = getIntent().getStringExtra("end_time");
        asa = getIntent().getStringExtra("asa");


        c_date_tv.setText(getIntent().getStringExtra("date"));
        c_title_tv.setText(getIntent().getStringExtra("title"));
        c_age_tv.setText(getIntent().getStringExtra("age"));

        c_asa.setChecked(asa.equals("yes"));

        try {
            JSONArray array = new JSONArray(getIntent().getStringExtra("category"));

            for (int i = 0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);

                categoriesIDArray.add(object.getString("id"));
                categoriesNameArray.add(object.getString("name"));
            }

            String academyStr = TextUtils.join(",\n", categoriesNameArray);

            if (!categoriesIDArray.isEmpty()){
                show_category_name.setText(academyStr);
                show_category_name.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        start_ampm_tabbar = (TabLayout)findViewById(R.id.start_ampm_tabbar);
        start_ampm_tabbar.setSelectedTabIndicatorColor(Color.parseColor("#3c5bc3"));
        start_ampm_tabbar.setTabTextColors(Color.parseColor("#6d6d6d"), Color.parseColor("#3c5bc3"));

        end_ampm_tabbar = (TabLayout)findViewById(R.id.end_ampm_tabbar);
        end_ampm_tabbar.setSelectedTabIndicatorColor(Color.parseColor("#3c5bc3"));
        end_ampm_tabbar.setTabTextColors(Color.parseColor("#6d6d6d"), Color.parseColor("#3c5bc3"));


        if (Integer.parseInt(start_time.substring(0,2)) < 12){
            Loop_StHour(st_rv_hour, Integer.parseInt(start_time.substring(0,2)));
        } else {
            Loop_StHour(st_rv_hour, Integer.parseInt(start_time.substring(0,2)) - 12 );
            start_ampm_tabbar.getTabAt(1).select();
        }

        Loop_StMin(st_rv_min, Integer.parseInt(start_time.substring(3,5)));


        if (Integer.parseInt(end_time.substring(0,2)) < 12){
            Loop_EtHour(et_rv_hour, Integer.parseInt(end_time.substring(0,2)));
        } else {
            Loop_EtHour(et_rv_hour, Integer.parseInt(end_time.substring(0,2)) - 12 );
            end_ampm_tabbar.getTabAt(1).select();
        }

        Loop_EtMin(et_rv_min, Integer.parseInt(end_time.substring(3,5)));


        c_category_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(EditCompetenciesActivity.this, CategoriesActivity.class)
                                .putExtra("fromClinical", fromClinical)
                                .putExtra("category", getIntent().getStringExtra("category")),
                        2);

                for (int i = 0; i < categoriesIDArray.size(); i++){
                    Log.e("categoriesIDArray", categoriesIDArray.get(i) + "--");
                }

            }
        });


        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (start_ampm_tabbar.getSelectedTabPosition() == 1){
                    if (!hr_st.equals("12")) {
                        String ab = String.valueOf(Integer.parseInt(hr_st) + 12);

                        st_for_final = ab + ":" + min_st + ":00";
                    } else {
                        st_for_final = "12" + ":" + min_st + ":00";
                    }
                } else if (start_ampm_tabbar.getSelectedTabPosition() == 0){
                    if (!hr_st.equals("12")) {
                        st_for_final = hr_st + ":" + min_st + ":00";
                    } else {
                        st_for_final = "00" + ":" + min_st + ":00";
                    }
                }
                if (end_ampm_tabbar.getSelectedTabPosition() == 1){
                    if (!hr_et.equals("12")) {
                        String ab = String.valueOf(Integer.parseInt(hr_et) + 12);

                        et_for_final = ab + ":" + min_et + ":00";
                    } else {
                        et_for_final = "12" + ":" + min_et + ":00";
                    }
                } else {
                    if (!hr_et.equals("12")) {
                        et_for_final = hr_et + ":" + min_et + ":00";
                    } else {
                        et_for_final = "00" + ":" + min_et + ":00";
                    }
                }

                if (c_date_tv.getText().toString().isEmpty()){
                    Toast.makeText(EditCompetenciesActivity.this, "Choose date", Toast.LENGTH_SHORT).show();
                } else if (c_title_tv.getText().toString().isEmpty()){
                    Toast.makeText(EditCompetenciesActivity.this, "Enter title", Toast.LENGTH_SHORT).show();
                } else if (c_age_tv.getText().toString().isEmpty()){
                    Toast.makeText(EditCompetenciesActivity.this, "Enter age", Toast.LENGTH_SHORT).show();
                } else if (categoriesIDArray.isEmpty()){
                    Toast.makeText(EditCompetenciesActivity.this, "Please select category!", Toast.LENGTH_SHORT).show();
                } else {
                    //   updateCase(id);
                    final KProgressHUD progressDialog = KProgressHUD.create(EditCompetenciesActivity.this)
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setLabel("Please wait")
                            .setCancellable(false)
                            .setAnimationSpeed(2)
                            .setDimAmount(0.5f)
                            .show();
                    AndroidNetworking.initialize(getApplicationContext());
                    String m_casa = "";
                    if (c_asa.isChecked()){
                        m_casa = "yes";
                    } else {
                        m_casa = "no";
                    }

                    Map<String, String> map = new HashMap<>();

                    String categoriesID = "";
                    for (int i = 0; i < categoriesIDArray.size(); i++){
                        categoriesID = categoriesIDArray.get(i);
                        map.put("categorie_id[" + i + "]", categoriesID);
                    }

                    AndroidNetworking.patch("https://nccaatest1.globaltechkyllc.com/api/cases/" + id)
                            .addBodyParameter("title", c_title_tv.getText().toString())
                            .addBodyParameter("date",  c_date_tv.getText().toString())
                            .addBodyParameter("age", c_age_tv.getText().toString())
                            .addBodyParameter("asa",m_casa)
                            .addBodyParameter("start_time",st_for_final)
                            .addBodyParameter("end_time", et_for_final)
                            .addBodyParameter(map)
                            .addHeaders("Authorization", "Bearer "+session.getAPITOKEN())
                            .addHeaders("Accept","application/json")
                            .addHeaders("Content-Type", "application/x-www-form-urlencoded")
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    // do anything with response
                                    Log.e("responEdit", response.toString() + "--");
                                    progressDialog.dismiss();
                                    finish();
                                    Toast.makeText(EditCompetenciesActivity.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                                }
                                @Override
                                public void onError(ANError error) {
                                    // handle error
                                    progressDialog.dismiss();
                                    Log.e("responEdit", error.getErrorBody() + "--");

                                }
                            });

                }

                Log.e("getALlData", id + "--" + c_title_tv.getText().toString() + "--" + c_age_tv.getText().toString()
                        + "--" + asa + "--" + c_date_tv.getText().toString() + "--" + "start time--" + st_for_final + "  " + "end time--" + et_for_final);

                for (int i = 0; i < categoriesIDArray.size(); i++){
                    Log.e("getALlData", categoriesIDArray.get(i) + "--");
                }

            }
        });


        findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                deleteCase(id);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(EditCompetenciesActivity.this);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });


        c_date_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        // Classification Spinner
        ArrayList<String> classifications = new ArrayList<>(Arrays.asList(
                "ASA1",
                "ASA2",
                "ASA3",
                "ASA4",
                "ASA5",
                "ASA6",
                "ASA Classification"
        ));

        long index = 0;
        for (String name: classifications) {
            ClassificationModel klass = new ClassificationModel();
            klass.setId(index);
            klass.setName(name);
            classificationModels.add(klass);
            index++;
        }

        // Classification
        SelectClassificationSpinner selectClassificationSpinner = new SelectClassificationSpinner(
                EditCompetenciesActivity.this,
                android.R.layout.simple_spinner_item,
                classificationModels
        );
        selectClassificationSpinner.setDropDownViewResource(android.R.layout.simple_list_item_1);
        classification_spinner.setAdapter(selectClassificationSpinner);
        classification_spinner.setSelection(classifications.size() - 1);

        classification_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == classifications.size() - 1) {
                    ((TextView) parent.getChildAt(0))
                            .setTextColor(getResources().getColor(R.color.text_color));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });
    }

    /**
     * Update case
     * Calling API
     * @param id
     */
    private void updateCase(String id) {
        final KProgressHUD progressDialog = KProgressHUD.create(EditCompetenciesActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.PATCH, session.BASEURL + "cases/" + id,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        progressDialog.dismiss();

                        try {
                            Log.e("updateCase", new String(response.data) + "--");

                            finish();
                            //                JSONObject object = new JSONObject(new String(response.data));

                            finish();


                        } catch (Exception e) {
                            Toast.makeText(EditCompetenciesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String json1 = new String(error.networkResponse.data);

                        Toast.makeText(EditCompetenciesActivity.this,""+ json1, Toast.LENGTH_LONG).show();

                        progressDialog.dismiss();
                        if (error instanceof ServerError){

                            if(error.networkResponse != null && error.networkResponse.data != null){
                                switch(error.networkResponse.statusCode){
                                    case 500:
                                        String json = new String(error.networkResponse.data);
                                        json = session.trimMessage(json, "message");
                                        if(json != null) {
                                            Toast.makeText(EditCompetenciesActivity.this, json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                                //Additional cases
                            }
                        }
                        else if (error instanceof TimeoutError)
                            Toast.makeText(EditCompetenciesActivity.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(EditCompetenciesActivity.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
             /*   params.put("title", c_title_tv.getText().toString());
                params.put("date", c_date_tv.getText().toString());
                params.put("start_time", st_for_final);
                params.put("end_time", et_for_final);
                params.put("age", c_age_tv.getText().toString());*/

                params.put("title", "asa");
                params.put("date", "2022-01-18");
                params.put("start_time", "06:02:00");
                params.put("end_time", "06:02:00");
                params.put("age", "18");
                params.put("categorie_id[]", "8");
                if (c_asa.isChecked()){
                    params.put("asa", "yes");
                } else {
                    params.put("asa", "no");
                }

              /*  for (int i = 0; i < categoriesIDArray.size(); i++){
                    params.put("categorie_id[]", categoriesIDArray.get(i));
                }*/

                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //   params.put("Accept", "application/x-www-form-urlencoded");
                params.put("Content-Type", "application/x-www-form-urlencoded");
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

        Volley.newRequestQueue(EditCompetenciesActivity.this).add(volleyMultipartRequest);
    }

    /**
     * Remove the case by id
     * Calling API
     * @param id
     */
    private void deleteCase(String id) {
        final KProgressHUD progressDialog = KProgressHUD.create(EditCompetenciesActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.DELETE, session.BASEURL + "cases/" + id,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        progressDialog.dismiss();

                        try {
                            Log.e("deleteCase", new String(response.data) + "--");

                            //               JSONObject object = new JSONObject(new String(response.data));

                            finish();


                        } catch (Exception e) {
                            Toast.makeText(EditCompetenciesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(EditCompetenciesActivity.this, json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                                //Additional cases
                            }
                        }
                        else if (error instanceof TimeoutError)
                            Toast.makeText(EditCompetenciesActivity.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(EditCompetenciesActivity.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //      params.put("title", c_title_tv.getText().toString());

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

        Volley.newRequestQueue(EditCompetenciesActivity.this).add(volleyMultipartRequest);
    }


    /**
     * Date picker handler
     */
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            //        dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            return  dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = dateFormat.format(calendar.getTime());

            c_date_tv.setText(dateString);

        }
    }



    private void Loop_StHour(LoopView loopView, int hour) {

        int hr_api = 0;

        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i <= 12; i++) {
            if (i < 10) {
                list.add("0" + i);
            } else {
                list.add("" + i);
            }
        }

        for (int i = 0; i < list.size(); i++){
            if (hour == i){
                hr_api = Integer.parseInt(list.get(i));
            }
        }
        if (hr_api < 10) {
            hr_st = "0" + hr_api;
        } else {
            hr_st = String.valueOf(hr_api);
        }

        loopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                hr_st = String.valueOf(list.get(index));
            }
        });
        loopView.setOnItemScrollListener(new OnItemScrollListener() {
            @Override
            public void onItemScrollStateChanged(LoopView loopView, int currentPassItem, int oldScrollState, int scrollState, int totalScrollY) {
                Log.i("gy", String.format("onItemScrollStateChanged currentPassItem %d  oldScrollState %d  scrollState %d  totalScrollY %d", currentPassItem, oldScrollState, scrollState, totalScrollY));
            }

            @Override
            public void onItemScrolling(LoopView loopView, int currentPassItem, int scrollState, int totalScrollY) {
                Log.i("gy", String.format("onItemScrolling currentPassItem %d  scrollState %d  totalScrollY %d", currentPassItem, scrollState, totalScrollY));
            }
        });

        loopView.setItems(list);

        loopView.setInitPosition(hr_api);
    }

    private void Loop_StMin(LoopView loopView, int min) {

        String min_api = "00";

        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                list.add("0" + i);
            } else {
                list.add("" + i);
            }
        }
        for (int i = 0; i < list.size(); i++){
            if (min == i){
                min_api = String.valueOf(list.get(i));
                Log.e("asa", min_api + "--" + list.get(i));
            }
        }
        min_st = String.valueOf(min_api);

        //滚动监听
        loopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                min_st = String.valueOf(list.get(index));
            }
        });
        loopView.setOnItemScrollListener(new OnItemScrollListener() {
            @Override
            public void onItemScrollStateChanged(LoopView loopView, int currentPassItem, int oldScrollState, int scrollState, int totalScrollY) {
                Log.i("gy", String.format("onItemScrollStateChanged currentPassItem %d  oldScrollState %d  scrollState %d  totalScrollY %d", currentPassItem, oldScrollState, scrollState, totalScrollY));
            }

            @Override
            public void onItemScrolling(LoopView loopView, int currentPassItem, int scrollState, int totalScrollY) {
                Log.i("gy", String.format("onItemScrolling currentPassItem %d  scrollState %d  totalScrollY %d", currentPassItem, scrollState, totalScrollY));
            }
        });
        //设置原始数据
        loopView.setItems(list);
        //设置初始位置
        loopView.setInitPosition(Integer.parseInt(min_api));
    }


    private void Loop_EtHour(LoopView loopView, int hour) {

        int hr_api = 0;

        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i <= 12; i++) {
            if (i < 10) {
                list.add("0" + i);
            } else {
                list.add("" + i);
            }
        }

        for (int i = 0; i < list.size(); i++){
            if (hour == i){
                hr_api = Integer.parseInt(list.get(i));
            }
        }
        if (hr_api < 10) {
            hr_et = "0" + hr_api;
        } else {
            hr_et = String.valueOf(hr_api);
        }

        loopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                hr_et = String.valueOf(list.get(index));
            }
        });
        loopView.setOnItemScrollListener(new OnItemScrollListener() {
            @Override
            public void onItemScrollStateChanged(LoopView loopView, int currentPassItem, int oldScrollState, int scrollState, int totalScrollY) {
                Log.i("gy", String.format("onItemScrollStateChanged currentPassItem %d  oldScrollState %d  scrollState %d  totalScrollY %d", currentPassItem, oldScrollState, scrollState, totalScrollY));
            }

            @Override
            public void onItemScrolling(LoopView loopView, int currentPassItem, int scrollState, int totalScrollY) {
                Log.i("gy", String.format("onItemScrolling currentPassItem %d  scrollState %d  totalScrollY %d", currentPassItem, scrollState, totalScrollY));
            }
        });

        loopView.setItems(list);

        loopView.setInitPosition(hr_api);
    }

    private void Loop_EtMin(LoopView loopView, int min) {

        String min_api = "00";

        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                list.add("0" + i);
            } else {
                list.add("" + i);
            }
        }
        for (int i = 0; i < list.size(); i++){
            if (min == i){
                min_api = String.valueOf(list.get(i));
                Log.e("asa", min_api + "--" + list.get(i));
            }
        }
        min_et = String.valueOf(min_api);

        //滚动监听
        loopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                min_et = String.valueOf(list.get(index));
            }
        });
        loopView.setOnItemScrollListener(new OnItemScrollListener() {
            @Override
            public void onItemScrollStateChanged(LoopView loopView, int currentPassItem, int oldScrollState, int scrollState, int totalScrollY) {
                Log.i("gy", String.format("onItemScrollStateChanged currentPassItem %d  oldScrollState %d  scrollState %d  totalScrollY %d", currentPassItem, oldScrollState, scrollState, totalScrollY));
            }

            @Override
            public void onItemScrolling(LoopView loopView, int currentPassItem, int scrollState, int totalScrollY) {
                Log.i("gy", String.format("onItemScrolling currentPassItem %d  scrollState %d  totalScrollY %d", currentPassItem, scrollState, totalScrollY));
            }
        });
        //设置原始数据
        loopView.setItems(list);
        //设置初始位置
        loopView.setInitPosition(Integer.parseInt(min_api));
    }


    /**
     * Lifecycle function
     * Description: if you call the startActivityForResult function, this function will be run.
     * @param requestCode
     * @param resultCode
     * @param data
     *
     * Modified by phantom
     * On 03/22/24
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == 2) {

                categoriesIDArray.clear();
                categoriesNameArray.clear();
                categoriesIDArray = data.getStringArrayListExtra("categoriesIDArray");
                categoriesNameArray = data.getStringArrayListExtra("categoriesNameArray");

                for (int i = 0; i < categoriesNameArray.size(); i++){
                    Log.e("categoriesNameArray", categoriesNameArray.get(i) +"--");
                }

                String academyStr = TextUtils.join(",\n", categoriesNameArray);

                if (!categoriesIDArray.isEmpty()){
                    show_category_name.setText(academyStr);
                    show_category_name.setVisibility(View.VISIBLE);
                } else {
                    show_category_name.setVisibility(View.GONE);
                }

            }
        } else {

        }

    }



}