package com.app_nccaa.nccaa.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.app_nccaa.nccaa.Adapter.AdapterCategories;
import com.app_nccaa.nccaa.Adapter.DialogAdapterCategories;
import com.app_nccaa.nccaa.Model.CategoriesModel;
import com.app_nccaa.nccaa.Model.SubcategoriesModel;
import com.app_nccaa.nccaa.Utils.UserSession;
import com.app_nccaa.nccaa.Utils.VolleyMultipartRequest;
import com.app_nccaa.nccaa.R;
import com.google.android.material.tabs.TabLayout;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Category Activity
 *
 * Modified by phantom
 * On 03/22/24
 *
 * Auto selection feature is created.
 */
public class CategoriesActivity extends AppCompatActivity {

    private RecyclerView categories_rv;
    private AdapterCategories adapterCategories;
    private ArrayList<CategoriesModel> categoriesAllArrayList = new ArrayList<>();
    private ArrayList<CategoriesModel> categoriesComplete = new ArrayList<>();
    private ArrayList<CategoriesModel> categoriesRemain = new ArrayList<>();
    private TextView edt_search;
    private UserSession session;
    private String fromClinical, classification, emergent, patientAge;

    private ArrayList<String> categoryIDArray = new ArrayList<>();
    private ArrayList<String> categoryNameArray = new ArrayList<>();
    private ArrayList<String> existingIDArray = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        session = new UserSession(CategoriesActivity.this);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        edt_search = findViewById(R.id.edt_search);

        fromClinical = getIntent().getStringExtra("fromClinical");  // jyathi aave te identify karva mate like create k update case

        categories_rv = findViewById(R.id.categories_rv);
        categories_rv.setLayoutManager(new LinearLayoutManager(this));
        adapterCategories = new AdapterCategories(CategoriesActivity.this, categoriesAllArrayList, new AdapterCategories.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {

                if (categoriesAllArrayList.get(pos).isChecked()){
                    categoriesAllArrayList.get(pos).setChecked(false);
                } else {
                    categoriesAllArrayList.get(pos).setChecked(true);
                }

            }

            @Override
            public void onItemPopup(int pos) {
                openPopUp(categoriesAllArrayList.get(pos).getName(), categoriesAllArrayList.get(pos).getDescription());
            }
        });
        categories_rv.setAdapter(adapterCategories);

        if (fromClinical.equals("EditCase")){
            try {
                JSONArray array = new JSONArray(getIntent().getStringExtra("category"));

                for (int i = 0; i < array.length(); i++){
                    JSONObject object = array.getJSONObject(i);
                    existingIDArray.add(object.getString("id"));
                }
                for (int i = 0; i < existingIDArray.size(); i++){
                    Log.e("existingIDArray", existingIDArray.get(i) + "--");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            classification = getIntent().getStringExtra("classification");
            emergent = getIntent().getStringExtra("emergent");
            patientAge = getIntent().getStringExtra("patientAge");
        }

        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i = 0; i < categoriesAllArrayList.size(); i++) {
                    if (categoriesAllArrayList.get(i).isChecked()) {
                        Log.e("getData", categoriesAllArrayList.get(i).getName());
                        categoryIDArray.add(categoriesAllArrayList.get(i).getId());
                        categoryNameArray.add(categoriesAllArrayList.get(i).getName());
                    }
                    for (int j = 0; j < categoriesAllArrayList.get(i).getSubcatArrayList().size(); j++){
                        if (categoriesAllArrayList.get(i).getSubcatArrayList().get(j).isChecked()){
                            Log.e("getData", categoriesAllArrayList.get(i).getSubcatArrayList().get(j).getName());
                            categoryIDArray.add(categoriesAllArrayList.get(i).getSubcatArrayList().get(j).getId());
                            categoryNameArray.add(categoriesAllArrayList.get(i).getSubcatArrayList().get(j).getName());
                        }
                    }
                }


                Intent intent = new Intent();
                intent.putStringArrayListExtra("categoriesIDArray", categoryIDArray);
                intent.putStringArrayListExtra("categoriesNameArray", categoryNameArray);
                setResult(2, intent);
                finish();

            }
        });

        findViewById(R.id.btn_completed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(0);
            }
        });

        findViewById(R.id.btn_remaining).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(1);
            }
        });

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString());
            }
        });

        categories();
        categoriesForDialog();
    }


    /**
     * Category and sub category details
     * are shown if this function is called.
     * @param name
     * @param description
     */
    private void openPopUp(String name, String description) {

        Dialog dialog1 = new Dialog(CategoriesActivity.this);
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
     * Get Categories
     * Calling API
     *
     * Modified by phantom
     * On 03/22/24
     */
    private void categories() {
        final KProgressHUD progressDialog = KProgressHUD.create(CategoriesActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET, session.BASEURL + "categories",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        progressDialog.dismiss();

                        try {
                            Log.e("categoriesList", new String(response.data) + "--");


                            JSONObject object = new JSONObject(new String(response.data));

                            JSONArray jsonArray = object.getJSONArray("data");


                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                CategoriesModel model = new CategoriesModel();
                                model.setId(jsonObject.getString("id"));
                                model.setName(jsonObject.getString("name"));
                                model.setDescription(jsonObject.getString("description"));
                                model.setIs_selectable(jsonObject.getString("is_selectable"));
                                model.setSelect_multiple(jsonObject.getString("select_multiple"));
                                model.setDisplay_target(jsonObject.getString("display_target"));
                                model.setDisplay_completed(jsonObject.getString("display_completed"));
                                model.setFill(jsonObject.getString("fill"));
                                model.setTarget(jsonObject.getString("target"));


                                if (fromClinical.equals("EditCase")){ // Edit mode
                                    for (int k = 0; k < existingIDArray.size(); k++){
                                        if (existingIDArray.get(k).equals(jsonObject.getString("id"))){
                                            model.setChecked(true);
                                        }
                                    }
                                } else { // Create mode, auto select the categories from candidate
                                    if(model.getName().trim().equals("Patient ASA Class 3 -  6")) {
                                        model.setChecked(classification.equals("ASA3") ||
                                                classification.equals("ASA4") ||
                                                classification.equals("ASA5") ||
                                                classification.equals("ASA6"));
                                    } else if(model.getName().trim().equals("Emergent (ASA Class E)")) {
                                        model.setChecked(emergent.equals("Yes"));
                                    } else if(model.getName().trim().equals("Geriatric (65+)")) {
                                        if(!patientAge.isEmpty())
                                            model.setChecked(Integer.parseInt(patientAge) >= 65);
                                        else
                                            model.setChecked(false);
                                    } else if(model.getName().trim().equals("Pediatric (0-18)")) {
                                        if(!patientAge.isEmpty())
                                            model.setChecked(Integer.parseInt(patientAge) < 18);
                                        else
                                            model.setChecked(false);
                                    } else {
                                        model.setChecked(false);
                                    }
                                }

                                ArrayList<SubcategoriesModel> subcCategoriesArrayList = new ArrayList<>();

                                JSONArray array = jsonObject.getJSONArray("sub_category");
                                for (int j = 0; j < array.length(); j++){
                                    JSONObject object1 = array.getJSONObject(j);

                                    SubcategoriesModel model1 = new SubcategoriesModel();
                                    model1.setId(object1.getString("id"));
                                    model1.setName(object1.getString("name"));
                                    model1.setDescription(object1.getString("description"));
                                    model1.setIs_selectable(object1.getString("is_selectable"));
                                    model1.setDisplay_target(object1.getString("display_target"));
                                    model1.setDisplay_completed(object1.getString("display_completed"));
                                    model1.setTarget(object1.getString("target"));
                                    model1.setFill(object1.getString("fill"));

                                    if (fromClinical.equals("EditCase")){ // Edit mode for sub category
                                        for (int k = 0; k < existingIDArray.size(); k++){
                                            if (existingIDArray.get(k).equals(object1.getString("id"))){
                                                model1.setChecked(true);
                                            }
                                        }
                                    } else { // Auto select the categories based on candidates
                                        if(model.getName().trim().equals("Patient ASA Class 3 -  6")) {
                                            model1.setChecked(classification.equals("ASA3") ||
                                                    classification.equals("ASA4") ||
                                                    classification.equals("ASA5") ||
                                                    classification.equals("ASA6"));
                                        } else if(model.getName().trim().equals("Pediatric (0-18)")) {
                                            if(model1.getName().trim().equals("Pediatric 2 - 12 years")) {
                                                if(patientAge.isEmpty())
                                                    model1.setChecked(false);
                                                else
                                                    model1.setChecked(Integer.parseInt(patientAge) < 13 &&
                                                            Integer.parseInt(patientAge) > 1);
                                            }
                                            else {
                                                if(patientAge.isEmpty())
                                                    model1.setChecked(false);
                                                else
                                                    model1.setChecked(Integer.parseInt(patientAge) < 2);
                                            }
                                        } else {
                                            model.setChecked(false);
                                        }
                                    }

                                    subcCategoriesArrayList.add(model1);
                                }

                                model.setSubcatArrayList(subcCategoriesArrayList);

                                categoriesAllArrayList.add(model);
                            }

                            adapterCategories.notifyDataSetChanged();


                        } catch (Exception e) {
                            Toast.makeText(CategoriesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(CategoriesActivity.this, json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                                //Additional cases
                            }
                        }
                        else if (error instanceof TimeoutError)
                            Toast.makeText(CategoriesActivity.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(CategoriesActivity.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

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

        Volley.newRequestQueue(CategoriesActivity.this).add(volleyMultipartRequest);
    }

    /**
     * Category Dialog for Completed and Remained button
     * Calling API same above method
     *
     * Modified by phantom
     * On 03/22/24
     */
    private void categoriesForDialog() {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET, session.BASEURL + "categories",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        try {
                            Log.e("categoriesList", new String(response.data) + "--");

                            JSONObject object = new JSONObject(new String(response.data));

                            JSONArray jsonArray = object.getJSONArray("data");


                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                CategoriesModel model = new CategoriesModel();
                                model.setId(jsonObject.getString("id"));
                                model.setName(jsonObject.getString("name"));
                                model.setFill(jsonObject.getString("fill"));
                                model.setTarget(jsonObject.getString("target"));

                                // Auto select feature.
                                if(model.getName().trim().equals("Patient ASA Class 3 -  6")) {
                                    model.setChecked(classification.equals("ASA3") ||
                                            classification.equals("ASA4") ||
                                            classification.equals("ASA5") ||
                                            classification.equals("ASA6"));
                                } else if(model.getName().trim().equals("Emergent (ASA Class E)")) {
                                    model.setChecked(emergent.equals("Yes"));
                                } else if(model.getName().trim().equals("Geriatric (65+)")) {
                                    if(!patientAge.isEmpty())
                                        model.setChecked(Integer.parseInt(patientAge) >= 65);
                                    else
                                        model.setChecked(false);
                                } else if(model.getName().trim().equals("Pediatric (0-18)")) {
                                    if(!patientAge.isEmpty())
                                        model.setChecked(Integer.parseInt(patientAge) < 18);
                                    else
                                        model.setChecked(false);
                                } else {
                                    model.setChecked(false);
                                }

                                ArrayList<SubcategoriesModel> subcCategoriesArrayList = new ArrayList<>();

                                // Auto select feature for sub category
                                JSONArray array = jsonObject.getJSONArray("sub_category");
                                for (int j = 0; j < array.length(); j++) {
                                    JSONObject object1 = array.getJSONObject(j);
                                    SubcategoriesModel model1 = new SubcategoriesModel();
                                    model1.setId(object1.getString("id"));
                                    model1.setName(object1.getString("name"));
                                    model1.setTarget(object1.getString("target"));
                                    model1.setFill(object1.getString("fill"));

                                    if(model.getName().trim().equals("Patient ASA Class 3 -  6")) {
                                        model1.setChecked(classification.equals("ASA3") ||
                                                classification.equals("ASA4") ||
                                                classification.equals("ASA5") ||
                                                classification.equals("ASA6"));
                                    } else if(model.getName().trim().equals("Pediatric (0-18)")) {
                                        if(model1.getName().trim().equals("Pediatric 2 - 12 years")) {
                                            if(patientAge.isEmpty())
                                                model1.setChecked(false);
                                            else
                                                model1.setChecked(Integer.parseInt(patientAge) < 13 &&
                                                        Integer.parseInt(patientAge) > 1);
                                        }
                                        else {
                                            if(patientAge.isEmpty())
                                                model1.setChecked(false);
                                            else
                                                model1.setChecked(Integer.parseInt(patientAge) < 2);
                                        }
                                    } else {
                                        model.setChecked(false);
                                    }

                                    subcCategoriesArrayList.add(model1);
                                }

                                model.setSubcatArrayList(subcCategoriesArrayList);

                                categoriesComplete.add(model);

                            }


                        } catch (Exception e) {
                            Toast.makeText(CategoriesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(CategoriesActivity.this, json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                            }
                        }
                        else if (error instanceof TimeoutError)
                            Toast.makeText(CategoriesActivity.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(CategoriesActivity.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

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

        Volley.newRequestQueue(CategoriesActivity.this).add(volleyMultipartRequest);
    }

    /**
     * Open Completed or Remained dialog
     * @param posTab
     */
    private void openDialog(int posTab) {

        Dialog dialog = new Dialog(CategoriesActivity.this);
        dialog.setContentView(R.layout.dialog_category_info);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        ImageButton closeBtn = dialog.findViewById(R.id.closeBtn);


        RecyclerView cat_rv = dialog.findViewById(R.id.cat_rv);
        cat_rv.setLayoutManager(new LinearLayoutManager(dialog.getContext()));
        if (posTab == 0) {
            DialogAdapterCategories dialogAdapterCategories = new DialogAdapterCategories(dialog.getContext(), categoriesComplete, "0", new DialogAdapterCategories.OnItemClickListener() {
                @Override
                public void onItemClick(int pos) {

                }
            });
            cat_rv.setAdapter(dialogAdapterCategories);
        } else if (posTab == 1){
            DialogAdapterCategories dialogAdapterCategories = new DialogAdapterCategories(dialog.getContext(), categoriesComplete, "1", new DialogAdapterCategories.OnItemClickListener() {
                @Override
                public void onItemClick(int pos) {

                }
            });
            cat_rv.setAdapter(dialogAdapterCategories);
        }


        TabLayout cat_info_tab = dialog.findViewById(R.id.cat_info_tab);

        TabLayout.Tab tab = cat_info_tab.getTabAt(posTab);
        tab.select();


        cat_info_tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == 0){
                    DialogAdapterCategories dialogAdapterCategories = new DialogAdapterCategories(dialog.getContext(), categoriesComplete, "0", new DialogAdapterCategories.OnItemClickListener() {
                        @Override
                        public void onItemClick(int pos) {

                        }
                    });
                    cat_rv.setAdapter(dialogAdapterCategories);
                } else if (tab.getPosition() == 1){
                    DialogAdapterCategories dialogAdapterCategories = new DialogAdapterCategories(dialog.getContext(), categoriesComplete, "1", new DialogAdapterCategories.OnItemClickListener() {
                        @Override
                        public void onItemClick(int pos) {

                        }
                    });
                    cat_rv.setAdapter(dialogAdapterCategories);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();


    }

    public void filter(String text){
        ArrayList<CategoriesModel> temp = new ArrayList();
        for(CategoriesModel d: categoriesAllArrayList){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getName().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }
        }
        //update recyclerview
        adapterCategories.updateList(temp);
    }
}