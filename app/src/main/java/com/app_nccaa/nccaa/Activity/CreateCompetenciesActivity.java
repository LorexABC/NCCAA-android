package com.app_nccaa.nccaa.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
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
import com.app_nccaa.nccaa.Adapter.SelectClassificationSpinner;
import com.app_nccaa.nccaa.Model.ClassificationModel;
import com.app_nccaa.nccaa.R;
import com.app_nccaa.nccaa.Utils.UserSession;
import com.app_nccaa.nccaa.Utils.VolleyMultipartRequest;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.tabs.TabLayout;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemScrollListener;
import com.weigan.loopview.OnItemSelectedListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Create Competency Activity
 *
 * Modified by phantom
 * On 03/22/24
 * ASA classification spinner is created.
 * All components are redesigned.
 */
public class CreateCompetenciesActivity extends AppCompatActivity {

    private FrameLayout btnDelete;

    private AutoCompleteTextView c_category_tv, c_title_tv, c_age_tv;

    private Spinner classification_spinner;

    private LoopView st_rv_hour, st_rv_min, et_rv_hour, et_rv_min;
    private static AutoCompleteTextView c_date_tv;

    private TabLayout start_ampm_tabbar, end_ampm_tabbar;
    private SwitchMaterial c_asa;

    private UserSession session;

    private String hr_st, min_st;
    private String hr_et, min_et;

    private String st_for_final, et_for_final;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_competencies);

        session = new UserSession(CreateCompetenciesActivity.this);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        classification_spinner = findViewById(R.id.c_classification_tv);
        c_category_tv = findViewById(R.id.c_category_tv);
        btnDelete = findViewById(R.id.btnDelete);
        c_date_tv = findViewById(R.id.c_date_tv);
        c_title_tv = findViewById(R.id.c_title_tv);
        c_age_tv = findViewById(R.id.c_age_tv);
        c_asa = findViewById(R.id.c_asa);
        st_rv_min = findViewById(R.id.st_rv_min);
        st_rv_hour = findViewById(R.id.st_rv_hour);
        et_rv_hour = findViewById(R.id.et_rv_hour);
        et_rv_min = findViewById(R.id.et_rv_min);
        show_category_name = findViewById(R.id.show_category_name);


        btnDelete.setVisibility(View.GONE);


        String fromClinical = getIntent().getStringExtra("fromClinical");

        c_category_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClassificationModel model = (ClassificationModel)classification_spinner.getSelectedItem();
                startActivityForResult(new Intent(CreateCompetenciesActivity.this, CategoriesActivity.class)
                                .putExtra("fromClinical", fromClinical)
                                .putExtra("patientAge", c_age_tv.getText().toString())
                                .putExtra("classification", model.getName())
                                .putExtra("emergent", c_asa.isChecked() ? "Yes" : "No")
                        , 2);
            }
        });

        start_ampm_tabbar = (TabLayout)findViewById(R.id.start_ampm_tabbar);
        start_ampm_tabbar.setSelectedTabIndicatorColor(Color.parseColor("#3c5bc3"));
        start_ampm_tabbar.setTabTextColors(Color.parseColor("#6d6d6d"), Color.parseColor("#3c5bc3"));

        end_ampm_tabbar = (TabLayout)findViewById(R.id.end_ampm_tabbar);
        end_ampm_tabbar.setSelectedTabIndicatorColor(Color.parseColor("#3c5bc3"));
        end_ampm_tabbar.setTabTextColors(Color.parseColor("#6d6d6d"), Color.parseColor("#3c5bc3"));


        Loop_StHour(st_rv_hour);
        Loop_StMin(st_rv_min);
        Loop_EtHour(et_rv_hour);
        Loop_EtMin(et_rv_min);



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
                    Toast.makeText(CreateCompetenciesActivity.this, "Choose date", Toast.LENGTH_SHORT).show();
                } else if (c_title_tv.getText().toString().isEmpty()){
                    Toast.makeText(CreateCompetenciesActivity.this, "Enter title", Toast.LENGTH_SHORT).show();
                } else if (c_age_tv.getText().toString().isEmpty()){
                    Toast.makeText(CreateCompetenciesActivity.this, "Enter age", Toast.LENGTH_SHORT).show();
                } else if (classification_spinner.getSelectedItem().toString().equals("ASA Classification")) {
                    Toast.makeText(CreateCompetenciesActivity.this, "Choose ASA Classification", Toast.LENGTH_SHORT).show();
                } else if (categoriesIDArray.isEmpty()){
                    Toast.makeText(CreateCompetenciesActivity.this, "Please select category!", Toast.LENGTH_SHORT).show();
                } else {
                    getCases();
                }


                Log.e("getALlData", c_title_tv.getText().toString() + "--" + c_age_tv.getText().toString()
                        + "--" + c_date_tv.getText().toString() + "--" + "start time--" + st_for_final + "  " + "end time--" + et_for_final);

                for (int i = 0; i < categoriesIDArray.size(); i++){
                    Log.e("getALlData", categoriesIDArray.get(i) + "--");
                }

            }
        });

        c_asa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // isChecked will be true if the checkbox is now checked, false if it is now unchecked
                if (isChecked) {
                    // Perform action when the checkbox is checked
                    openPopUp("Emergent (ASA Class E)", "<p><b>Emergent (ASA Class E)</b></p>\n" +
                            "<p>The addition of “E” to the ASAPS (e.g., ASA 2E) denotes an emergency surgical procedure. The ASA defines an emergency as existing “when the delay in treatment of the patient would lead to a significant increase in the threat to life or body part.” You are required to have a minimum of (30) cases in Emergent Class E. Once the minimums have been met, the student may engage in any Emergent Class E Classes to reach the (30) case minimum for this main category. You may only select one sub-skill in this category per case. You may also select the “Other” sub-skill if a case is Emergent Class E but does not fit into any other sub-skill category.</p>\n" +
                            "<p>(source: www.statpearls.com)</p>\n" +
                            "<br>\n" +
                            "\n" +
                            "<p>The ASA defines an emergency as existing “when the delay in treatment of the patient would lead to a significant increase in the threat to life or body part.” For this category, the candidate may select this case if there is an urgency/emergency to provide immediate care and there is limited time for planning, assessment of the patient and set-up for the case.  You are required to have a minimum of (30) cases in the Emergent ASA Class. </p>");
                }
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

        SelectClassificationSpinner selectClassificationSpinner = new SelectClassificationSpinner(
                CreateCompetenciesActivity.this,
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
     * Open popup the category details
     * @param name
     * @param description
     */
    private void openPopUp(String name, String description) {

        Dialog dialog1 = new Dialog(CreateCompetenciesActivity.this);
        dialog1.setContentView(R.layout.categories_info_dailog);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog1.setCancelable(true);

        TextView catName = dialog1.findViewById(R.id.catName);
        TextView desName = dialog1.findViewById(R.id.desName);

        catName.setText(name);

        if (!description.equals("null")) {
            //   desName.setText(description);
            desName.setText(Html.fromHtml(description));
        } else {
            desName.setVisibility(View.GONE);
        }

        dialog1.findViewById(R.id.closeImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });

        dialog1.show();
    }

    /**
     * Get current users cases
     * Calling API
     */
    private void getCases() {
        final KProgressHUD progressDialog = KProgressHUD.create(CreateCompetenciesActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, session.BASEURL + "cases",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        progressDialog.dismiss();

                        try {
                            Log.e("createCase", new String(response.data) + "--");

                            JSONObject object = new JSONObject(new String(response.data));


                            finish();


                        } catch (Exception e) {
                            Toast.makeText(CreateCompetenciesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(CreateCompetenciesActivity.this, json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                                //Additional cases
                            }
                        }
                        else if (error instanceof TimeoutError)
                            Toast.makeText(CreateCompetenciesActivity.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(CreateCompetenciesActivity.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("title", c_title_tv.getText().toString());
                params.put("date", c_date_tv.getText().toString());
                params.put("start_time", st_for_final);
                params.put("end_time", et_for_final);
                params.put("age", c_age_tv.getText().toString());
                if (c_asa.isChecked()){
                    params.put("asa", "yes");
                } else {
                    params.put("asa", "no");
                }

                for (int i = 0; i < categoriesIDArray.size(); i++){
                    params.put("categorie_id[" + i + "]", categoriesIDArray.get(i));
                }

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

        Volley.newRequestQueue(CreateCompetenciesActivity.this).add(volleyMultipartRequest);
    }


    /**
     * Loop St Hours
     * @param loopView
     */
    private void Loop_StHour(LoopView loopView) {

        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i <= 12; i++) {
            if (i < 10) {
                list.add("0" + i);
            } else {
                list.add("" + i);
            }
        }

        hr_st = String.valueOf(list.get(0));

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

        loopView.setInitPosition(0);
    }

    /**
     * Loop St Minutes
     * @param loopView
     */
    private void Loop_StMin(LoopView loopView) {

        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                list.add("0" + i);
            } else {
                list.add("" + i);
            }
        }

        min_st = String.valueOf(list.get(0));

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

        loopView.setItems(list);

        loopView.setInitPosition(0);
    }


    /**
     * Loop Et Hours
     * @param loopView
     */
    private void Loop_EtHour(LoopView loopView) {

        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i <= 12; i++) {
            if (i < 10) {
                list.add("0" + i);
            } else {
                list.add("" + i);
            }
        }

        hr_et = String.valueOf(list.get(0));

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

        loopView.setInitPosition(0);
    }

    /**
     * Loop Et Minutes
     * @param loopView
     */
    private void Loop_EtMin(LoopView loopView) {

        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                list.add("0" + i);
            } else {
                list.add("" + i);
            }
        }

        min_et = String.valueOf(list.get(0));

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

        loopView.setItems(list);

        loopView.setInitPosition(0);
    }


    /**
     * Lifecycle function
     * Description: if you call the activity with request code, the activity will be granted.
     * @param requestCode
     * @param resultCode
     * @param data
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

                for (int i = 0; i < categoriesNameArray.size(); i++) {
                    Log.e("categoriesNameArray", categoriesNameArray.get(i) + "--");
                }

                String academyStr = TextUtils.join(",\n", categoriesNameArray);

                if (!categoriesIDArray.isEmpty()){
                    show_category_name.setVisibility(View.VISIBLE);
                    show_category_name.setText(academyStr);
                } else {
                    show_category_name.setVisibility(View.GONE);
                }

            }
        } else {

        }

    }



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
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String dateString = dateFormat.format(calendar.getTime());

            c_date_tv.setText(dateString);

        }
    }


}