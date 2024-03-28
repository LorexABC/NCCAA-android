package com.app_nccaa.nccaa.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

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
import com.app_nccaa.nccaa.Utils.NameTextWatcher;
import com.app_nccaa.nccaa.Utils.UserSession;
import com.app_nccaa.nccaa.Utils.VolleyMultipartRequest;
import com.app_nccaa.nccaa.Activity.EditProfile;
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
import java.util.Locale;
import java.util.Map;

public class Personal_Info_Frag extends Fragment {

    private String lastChar = " ";

    private ArrayList<CityModel> title_ArrayList = new ArrayList<>();
    private ArrayList<CityModel> suffix_ArrayList = new ArrayList<>();
    private ArrayList<CityModel> gender_ArrayList = new ArrayList<>();
    private ArrayList<CityModel> race_ArrayList = new ArrayList<>();
    private ArrayList<CityModel> ethnicity_ArrayList = new ArrayList<>();
    private ArrayList<CityModel> marital_Status_ArrayList = new ArrayList<>();
    private ArrayList<CityModel> state_ArrayList = new ArrayList<>();
    private ArrayList<CityModel> alma_mater_ArrayList = new ArrayList<>();

    private UserSession session;
    private RequestQueue mRequestqueue;

    private TextView autoIDTV, roleTV, degree_TV, alma_CodeTV;
    private EditText email, firstNameET, middleNameET, lastNameET, otherEthnicityET, streetAddressET, cityET, zipCodeET, classET;
    private EditText cell_phone_ET, other_cell_phone_ET;
    private static TextView DOB_TV, graduation_date_TV;

    private Spinner titleSpinner, suffixSpinner, genderSpinner, raceSpinner, ethnicitySpinner,
            maritalStatusSpinner, stateSpinner, alma_mater_Spinner;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_personal_info_frag, container, false);

        session = new UserSession(getContext());
        mRequestqueue = Volley.newRequestQueue(getContext());


        DOB_TV = view.findViewById(R.id.DOB_TV);
        graduation_date_TV = view.findViewById(R.id.graduation_date_TV);

        cell_phone_ET = view.findViewById(R.id.cell_phone_ET);
        other_cell_phone_ET = view.findViewById(R.id.other_cell_phone_ET);

        titleSpinner = view.findViewById(R.id.titleSpinner);
        suffixSpinner = view.findViewById(R.id.suffixSpinner);
        genderSpinner = view.findViewById(R.id.genderSpinner);
        raceSpinner = view.findViewById(R.id.raceSpinner);
        ethnicitySpinner = view.findViewById(R.id.ethnicitySpinner);
        maritalStatusSpinner = view.findViewById(R.id.maritalStatusSpinner);
        stateSpinner = view.findViewById(R.id.stateSpinner);
        alma_mater_Spinner = view.findViewById(R.id.alma_mater_Spinner);

        autoIDTV = view.findViewById(R.id.autoIDTV);
        roleTV = view.findViewById(R.id.roleTV);
        degree_TV = view.findViewById(R.id.degree_TV);

        email = view.findViewById(R.id.email);
        firstNameET = view.findViewById(R.id.firstNameET);
        middleNameET = view.findViewById(R.id.middleNameET);
        lastNameET = view.findViewById(R.id.lastNameET);
        otherEthnicityET = view.findViewById(R.id.otherEthnicityET);
        streetAddressET = view.findViewById(R.id.streetAddressET);
        cityET = view.findViewById(R.id.cityET);
        zipCodeET = view.findViewById(R.id.zipCodeET);
        alma_CodeTV = view.findViewById(R.id.alma_CodeTV);
        classET = view.findViewById(R.id.classET);

        isChangeData(DOB_TV);
        isChangeData(graduation_date_TV);
        isChangeData(cell_phone_ET);
        isChangeData(other_cell_phone_ET);
        isChangeData(titleSpinner);
        isChangeData(suffixSpinner);
        isChangeData(genderSpinner);
        isChangeData(raceSpinner);
        isChangeData(ethnicitySpinner);
        isChangeData(maritalStatusSpinner);
        isChangeData(stateSpinner);
        isChangeData(alma_mater_Spinner);
        isChangeData(autoIDTV);
        isChangeData(roleTV);
        isChangeData(degree_TV);
        isChangeData(email);
        isChangeData(firstNameET);
        isChangeData(middleNameET);
        isChangeData(lastNameET);
        isChangeData(otherEthnicityET);
        isChangeData(streetAddressET);
        isChangeData(cityET);
        isChangeData(zipCodeET);
        isChangeData(alma_CodeTV);


        view.findViewById(R.id.subMitBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (email.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "enter email", Toast.LENGTH_SHORT).show();
                } else if (firstNameET.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "enter first name", Toast.LENGTH_SHORT).show();
                } else if (lastNameET.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "enter last name", Toast.LENGTH_SHORT).show();
                } else if (genderSpinner.getSelectedItemPosition() == gender_ArrayList.size()-1){
                    Toast.makeText(getContext(), "select gender", Toast.LENGTH_SHORT).show();
                } else if (DOB_TV.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "enter date of birth", Toast.LENGTH_SHORT).show();
                } else if (cell_phone_ET.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "enter cell phone", Toast.LENGTH_SHORT).show();
                } else if (other_cell_phone_ET.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "enter other cell phone", Toast.LENGTH_SHORT).show();
                } else {
                    updateMe();
                }
            }
        });

        firstNameET.addTextChangedListener(new NameTextWatcher(firstNameET));
        lastNameET.addTextChangedListener(new NameTextWatcher(lastNameET));
        middleNameET.addTextChangedListener(new NameTextWatcher(middleNameET));

        DOB_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        graduation_date_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment_Graduation();
                newFragment.show(getActivity().getSupportFragmentManager(), "datePickerGraduation");
            }
        });


  /*      cell_phone_ET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int digits = cell_phone_ET.getText().toString().length();
                if (digits > 1)
                    lastChar = cell_phone_ET.getText().toString().substring(digits-1);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int digits = cell_phone_ET.getText().toString().length();
                Log.d("LENGTH",""+digits);
                if (!lastChar.equals("-")) {
                    if (digits == 3 || digits == 7) {
                        cell_phone_ET.append("-");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/


       /* other_cell_phone_ET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int digits = other_cell_phone_ET.getText().toString().length();
                if (digits > 1)
                    lastChar = other_cell_phone_ET.getText().toString().substring(digits-1);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int digits = other_cell_phone_ET.getText().toString().length();
                Log.d("LENGTH",""+digits);
                if (!lastChar.equals("-")) {
                    if (digits == 3 || digits == 7) {
                        other_cell_phone_ET.append("-");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/



        // TITLE SPINNER
        CityModel ttl1 = new CityModel();
        ttl1.setId("Mr.");
        ttl1.setName("Mr.");
        title_ArrayList.add(ttl1);
        CityModel ttl2 = new CityModel();
        ttl2.setId("Ms.");
        ttl2.setName("Ms.");
        title_ArrayList.add(ttl2);
        CityModel ttl3 = new CityModel();
        ttl3.setId("Miss.");
        ttl3.setName("Miss.");
        title_ArrayList.add(ttl3);
        CityModel ttl4 = new CityModel();
        ttl4.setId("Mrs.");
        ttl4.setName("Mrs.");
        title_ArrayList.add(ttl4);
        CityModel cityModel3 = new CityModel();
        cityModel3.setId("");
        cityModel3.setName("Select Title");
        title_ArrayList.add(cityModel3);

        SelectCitySpinner adapter1 = new SelectCitySpinner(getContext(), android.R.layout.simple_spinner_item, title_ArrayList);
        adapter1.setDropDownViewResource(android.R.layout.simple_list_item_1);
        titleSpinner.setAdapter(adapter1);
        titleSpinner.setSelection(adapter1.getCount());

        titleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == (title_ArrayList.size() - 1)) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_gray_light));
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        // SUFFIX SPINNER
        CityModel suff1 = new CityModel();
        suff1.setId("Sr.");
        suff1.setName("Sr.");
        suffix_ArrayList.add(suff1);
        CityModel suff2 = new CityModel();
        suff2.setId("Jr.");
        suff2.setName("Jr.");
        suffix_ArrayList.add(suff2);
        CityModel suff3 = new CityModel();
        suff3.setId("I");
        suff3.setName("I");
        suffix_ArrayList.add(suff3);
        CityModel suff4 = new CityModel();
        suff4.setId("II");
        suff4.setName("II");
        suffix_ArrayList.add(suff4);
        CityModel suff5 = new CityModel();
        suff5.setId("III");
        suff5.setName("III");
        suffix_ArrayList.add(suff5);
        CityModel cityModel5 = new CityModel();
        cityModel5.setId("");
        cityModel5.setName("Select Suffix");
        suffix_ArrayList.add(cityModel5);

        SelectCitySpinner adapter2 = new SelectCitySpinner(getContext(), android.R.layout.simple_spinner_item, suffix_ArrayList);
        adapter2.setDropDownViewResource(android.R.layout.simple_list_item_1);
        suffixSpinner.setAdapter(adapter2);
        suffixSpinner.setSelection(adapter2.getCount());

        suffixSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == (suffix_ArrayList.size() - 1)) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_gray_light));
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        // GENDER SPINNER
        CityModel gen1 = new CityModel();
        gen1.setId("male");
        gen1.setName("Male");
        gender_ArrayList.add(gen1);
        CityModel gen2 = new CityModel();
        gen2.setId("female");
        gen2.setName("Female");
        gender_ArrayList.add(gen2);
        CityModel cityModel7 = new CityModel();
        cityModel7.setId("");
        cityModel7.setName("Select Gender");
        gender_ArrayList.add(cityModel7);

        SelectCitySpinner adapter3 = new SelectCitySpinner(getContext(), android.R.layout.simple_spinner_item, gender_ArrayList);
        adapter3.setDropDownViewResource(android.R.layout.simple_list_item_1);
        genderSpinner.setAdapter(adapter3);
        genderSpinner.setSelection(adapter3.getCount());

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



        // RACE SPINNER
        CityModel rac1 = new CityModel();
        rac1.setId("alaska-native");
        rac1.setName("Alaska Native");
        race_ArrayList.add(rac1);
        CityModel rac2 = new CityModel();
        rac2.setId("american-indian");
        rac2.setName("American Indian");
        race_ArrayList.add(rac2);
        CityModel rac3 = new CityModel();
        rac3.setId("asian");
        rac3.setName("Asian");
        race_ArrayList.add(rac3);
        CityModel rac4 = new CityModel();
        rac4.setId("black-afrian-american");
        rac4.setName("Black or Afrian American");
        race_ArrayList.add(rac4);
        CityModel rac5 = new CityModel();
        rac5.setId("latino-hispanic");
        rac5.setName("Latino or Hispanic");
        race_ArrayList.add(rac5);
        CityModel rac6 = new CityModel();
        rac6.setId("middle-eastern");
        rac6.setName("Middle Eastern");
        race_ArrayList.add(rac6);
        CityModel rac7 = new CityModel();
        rac7.setId("native-hawaiian");
        rac7.setName("Native Hawaiian");
        race_ArrayList.add(rac7);
        CityModel rac8 = new CityModel();
        rac8.setId("pacific-islander");
        rac8.setName("Pacific Islander");
        race_ArrayList.add(rac8);
        CityModel rac9 = new CityModel();
        rac9.setId("white-caucasian");
        rac9.setName("White or Caucasian");
        race_ArrayList.add(rac9);
        CityModel rac10 = new CityModel();
        rac10.setId("other");
        rac10.setName("Other");
        race_ArrayList.add(rac10);
        CityModel rac11 = new CityModel();
        rac11.setId("no-answer");
        rac11.setName("Prefer Not To Answer");
        race_ArrayList.add(rac11);
        CityModel cityModel9 = new CityModel();
        cityModel9.setId("");
        cityModel9.setName("Select Race");
        race_ArrayList.add(cityModel9);

        SelectCitySpinner adapter4 = new SelectCitySpinner(getContext(), android.R.layout.simple_spinner_item, race_ArrayList);
        adapter4.setDropDownViewResource(android.R.layout.simple_list_item_1);
        raceSpinner.setAdapter(adapter4);
        raceSpinner.setSelection(adapter4.getCount());

        raceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == (race_ArrayList.size() - 1)) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_gray_light));
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        // ETHNICITY SPINNER
        CityModel eth1 = new CityModel();
        eth1.setId("african-american");
        eth1.setName("African-American");
        ethnicity_ArrayList.add(eth1);
        CityModel eth2 = new CityModel();
        eth2.setId("american");
        eth2.setName("American");
        ethnicity_ArrayList.add(eth2);
        CityModel eth3 = new CityModel();
        eth3.setId("dutch");
        eth3.setName("Dutch");
        ethnicity_ArrayList.add(eth3);
        CityModel eth4 = new CityModel();
        eth4.setId("english");
        eth4.setName("English");
        ethnicity_ArrayList.add(eth4);
        CityModel eth5 = new CityModel();
        eth5.setId("french");
        eth5.setName("French");
        ethnicity_ArrayList.add(eth5);
        CityModel eth6 = new CityModel();
        eth6.setId("german");
        eth6.setName("German");
        ethnicity_ArrayList.add(eth6);
        CityModel eth7 = new CityModel();
        eth7.setId("irish");
        eth7.setName("Irish");
        ethnicity_ArrayList.add(eth7);
        CityModel eth8 = new CityModel();
        eth8.setId("italian");
        eth8.setName("Italian");
        ethnicity_ArrayList.add(eth8);
        CityModel eth9 = new CityModel();
        eth9.setId("mexican");
        eth9.setName("Mexican");
        ethnicity_ArrayList.add(eth9);
        CityModel eth10 = new CityModel();
        eth10.setId("norwegian");
        eth10.setName("Norwegian");
        ethnicity_ArrayList.add(eth10);
        CityModel eth11 = new CityModel();
        eth11.setId("polish");
        eth11.setName("Polish");
        ethnicity_ArrayList.add(eth11);
        CityModel eth12 = new CityModel();
        eth12.setId("scottish");
        eth12.setName("Scottish");
        ethnicity_ArrayList.add(eth12);
        CityModel eth13 = new CityModel();
        eth13.setId("swedish");
        eth13.setName("Swedish");
        ethnicity_ArrayList.add(eth13);
        CityModel eth14 = new CityModel();
        eth14.setId("other");
        eth14.setName("Other");
        ethnicity_ArrayList.add(eth14);
        CityModel eth15 = new CityModel();
        eth15.setId("");
        eth15.setName("Select Ethnicity");
        ethnicity_ArrayList.add(eth15);

        SelectCitySpinner adapter5 = new SelectCitySpinner(getContext(), android.R.layout.simple_spinner_item, ethnicity_ArrayList);
        adapter5.setDropDownViewResource(android.R.layout.simple_list_item_1);
        ethnicitySpinner.setAdapter(adapter5);
        ethnicitySpinner.setSelection(adapter5.getCount());

        ethnicitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == (ethnicity_ArrayList.size() - 1)) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_gray_light));
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        // MARITAL STATUS SPINNER
        CityModel mar1 = new CityModel();
        mar1.setId("single");
        mar1.setName("Single");
        marital_Status_ArrayList.add(mar1);
        CityModel mar2 = new CityModel();
        mar2.setId("married");
        mar2.setName("Married");
        marital_Status_ArrayList.add(mar2);
        CityModel mar3 = new CityModel();
        mar3.setId("separated");
        mar3.setName("Separated");
        marital_Status_ArrayList.add(mar3);
        CityModel mar4 = new CityModel();
        mar4.setId("divorced");
        mar4.setName("Divorced");
        marital_Status_ArrayList.add(mar4);
        CityModel mard4 = new CityModel();
        mard4.setId("widowed");
        mard4.setName("Widowed");
        marital_Status_ArrayList.add(mard4);
        CityModel cityModel15 = new CityModel();
        cityModel15.setId("");
        cityModel15.setName("Marital Status");
        marital_Status_ArrayList.add(cityModel15);

        SelectCitySpinner adapter7 = new SelectCitySpinner(getContext(), android.R.layout.simple_spinner_item, marital_Status_ArrayList);
        adapter7.setDropDownViewResource(android.R.layout.simple_list_item_1);
        maritalStatusSpinner.setAdapter(adapter7);
        maritalStatusSpinner.setSelection(adapter7.getCount());

        maritalStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == (marital_Status_ArrayList.size() - 1)) {
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

        SelectCitySpinner adapter8 = new SelectCitySpinner(getContext(), android.R.layout.simple_spinner_item, state_ArrayList);
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        getUniversities();

        return view;
    }

    private void getUniversities() {


        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET, session.BASEURL + "universities",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        getUsersMe();
                        getPersonal();


                        try {
                            Log.e("universityList", new String(response.data) + "--");

                            JSONArray jsonArray = new JSONArray(new String(response.data));

                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                CityModel cityModel = new CityModel();
                                cityModel.setId(jsonObject.getString("id"));
                                cityModel.setName(jsonObject.getString("name"));
                                cityModel.setCode(jsonObject.getString("code"));

                                alma_mater_ArrayList.add(cityModel);
                            }

                            CityModel cityModel = new CityModel();
                            cityModel.setId(session.getUNIVERSITY_ID());
                            cityModel.setName("Select Alma mater");
                            cityModel.setCode("");
                            alma_mater_ArrayList.add(cityModel);


                            SelectCitySpinner adapter9 = new SelectCitySpinner(getContext(), android.R.layout.simple_spinner_item, alma_mater_ArrayList);
                            adapter9.setDropDownViewResource(android.R.layout.simple_list_item_1);
                            alma_mater_Spinner.setAdapter(adapter9);
                            alma_mater_Spinner.setSelection(adapter9.getCount());

                            alma_mater_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    if (position == (alma_mater_ArrayList.size() - 1)) {
                                        ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_gray_light));
                                    } else {
                                        alma_CodeTV.setText(alma_mater_ArrayList.get(position).getCode());
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                        } catch (Exception e) {

                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof ServerError){

                           /* if(error.networkResponse != null && error.networkResponse.data != null){
                                switch(error.networkResponse.statusCode){
                                    case 500:
                                        String json = new String(error.networkResponse.data);
                                        json = session.trimMessage(json, "");
                                        if(json != null) {
                                            Toast.makeText(getContext(), json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                            }*/
                        }
                        else if (error instanceof TimeoutError)
                            Toast.makeText(getContext(), "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(getContext(), "Bad Network Connection", Toast.LENGTH_LONG).show();

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

        Volley.newRequestQueue(getContext()).add(volleyMultipartRequest);
    }

    private void getUsersMe() {
        final KProgressHUD progressDialog = KProgressHUD.create(getContext())
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


                            JSONObject jsonObject = new JSONObject(new String(response.data));

                            autoIDTV.setText("ID " + jsonObject.getString("id"));

                            roleTV.setText(jsonObject.getString("status").toUpperCase(Locale.ROOT));

                          /*  if (jsonObject.getString("status").length() < 4) {
                                roleTV.setText(jsonObject.getString("status").toUpperCase(Locale.ROOT));
                            } else {
                                roleTV.setText(capitalize(jsonObject.getString("status")));
                            }*/
                            email.setText(jsonObject.getString("email"));

                            firstNameET.setText(jsonObject.getString("firstName"));
                            middleNameET.setText(jsonObject.getString("middleName"));
                            lastNameET.setText(jsonObject.getString("lastName"));


                            if (!jsonObject.getString("graduationDate").equals("null")) {
                                graduation_date_TV.setText(chnage_date_formate(jsonObject.getString("graduationDate")));
                            }

                            if (!jsonObject.getString("designation").equals("null")) {
                                degree_TV.setText(jsonObject.getString("designation"));
                            }

                            EditProfile.isChange = false;


                        } catch (Exception e) {

                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(getContext(), json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                            }
                        }
                        else if (error instanceof TimeoutError)
                            Toast.makeText(getContext(), "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(getContext(), "Bad Network Connection", Toast.LENGTH_LONG).show();

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

        Volley.newRequestQueue(getContext()).add(volleyMultipartRequest);
    }

    private void getPersonal() {
        final KProgressHUD progressDialog = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET, session.BASEURL + "users/me/personal",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        progressDialog.dismiss();

                        try {
                            Log.e("personal", new String(response.data) + "--");

                            JSONObject jsonObject = new JSONObject(new String(response.data));


                            DOB_TV.setText(chnage_date_formate(jsonObject.getString("dateOfBirth")));

                            if (!jsonObject.getString("otherEthnicity").equals("null")) {
                                otherEthnicityET.setText(jsonObject.getString("otherEthnicity"));
                            }

                            cell_phone_ET.setText(jsonObject.getString("phone"));
                            other_cell_phone_ET.setText(jsonObject.getString("otherPhone"));
                            streetAddressET.setText(jsonObject.getString("address"));
                            cityET.setText(jsonObject.getString("city"));
                            if (!jsonObject.getString("zipCode").equals("null")) {
                                zipCodeET.setText(jsonObject.getString("zipCode"));
                            }


                            if (!jsonObject.getString("title").equals("null")){
                                for (int i = 0; i < title_ArrayList.size(); i++){
                                    if (jsonObject.getString("title").equals(title_ArrayList.get(i).getId())){
                                        titleSpinner.setSelection(i);
                                    }
                                }
                            }
                            if (!jsonObject.getString("suffix").equals("null")){
                                for (int i = 0; i < suffix_ArrayList.size(); i++){
                                    if (jsonObject.getString("suffix").equals(suffix_ArrayList.get(i).getId())){
                                        suffixSpinner.setSelection(i);
                                    }
                                }
                            }
                            if (!jsonObject.getString("gender").equals("null")){
                                for (int i = 0; i < gender_ArrayList.size(); i++){
                                    if (jsonObject.getString("gender").equals(gender_ArrayList.get(i).getId())){
                                        genderSpinner.setSelection(i);
                                    }
                                }
                            }
                            if (!jsonObject.getString("race").equals("null")){
                                for (int i = 0; i < race_ArrayList.size(); i++){
                                    if (jsonObject.getString("race").equals(race_ArrayList.get(i).getId())){
                                        raceSpinner.setSelection(i);
                                    }
                                }
                            }
                            if (!jsonObject.getString("ethnicity").equals("null")){
                                for (int i = 0; i < ethnicity_ArrayList.size(); i++){
                                    if (jsonObject.getString("ethnicity").equals(ethnicity_ArrayList.get(i).getId())){
                                        ethnicitySpinner.setSelection(i);
                                    }
                                }
                            }
                            if (!jsonObject.getString("maritalStatus").equals("null")){
                                for (int i = 0; i < marital_Status_ArrayList.size(); i++){
                                    if (jsonObject.getString("maritalStatus").equals(marital_Status_ArrayList.get(i).getId())){
                                        maritalStatusSpinner.setSelection(i);
                                    }
                                }
                            }
                            if (!jsonObject.getString("state").equals("null")){
                                for (int i = 0; i < state_ArrayList.size(); i++){
                                    if (jsonObject.getString("state").equals(state_ArrayList.get(i).getId())){
                                        stateSpinner.setSelection(i);
                                    }
                                }
                            }

                            if (!jsonObject.getString("graduationYear").equals("null")) {
                                classET.setText(jsonObject.getString("graduationYear"));
                            }

                            if (session.getUSER_TYPE().equals("student")){
                                classET.setFocusable(false);
                                classET.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
                                classET.setClickable(false);
                            } else {
                                if (!jsonObject.getString("graduationYear").equals("null")) {
                                    classET.setFocusable(false);
                                    classET.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
                                    classET.setClickable(false);
                                }
                            }


                            if (!session.getUSER_TYPE().equals("student")){
                                if (!jsonObject.getString("universityId").equals("null")){
                                    for (int i = 0; i < alma_mater_ArrayList.size(); i++){
                                        if (jsonObject.getString("universityId").equals(alma_mater_ArrayList.get(i).getId())){
                                            alma_mater_Spinner.setSelection(i);
                                        }
                                    }
                                }
                            } else {
                                if (!jsonObject.getString("universityId").equals("null")){
                                    for (int i = 0; i < alma_mater_ArrayList.size(); i++){
                                        if (jsonObject.getString("universityId").equals(alma_mater_ArrayList.get(i).getId())){
                                            alma_mater_Spinner.setSelection(i);
                                        }
                                    }
                                }
                                alma_mater_Spinner.setEnabled(false);
                            }

                            EditProfile.isChange = false;


                        } catch (Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(getContext(), json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                            }
                        }
                        else if (error instanceof TimeoutError)
                            Toast.makeText(getContext(), "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(getContext(), "Bad Network Connection", Toast.LENGTH_LONG).show();

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

        Volley.newRequestQueue(getContext()).add(volleyMultipartRequest);
    }

    private void updateMe() {
        final KProgressHUD progressDialog = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        Map<String, Object> postParam3= new HashMap<String, Object>();
        postParam3.put("firstName", firstNameET.getText().toString());
        postParam3.put("middleName", middleNameET.getText().toString());
        postParam3.put("lastName", lastNameET.getText().toString());
        postParam3.put("email", email.getText().toString());

        JSONObject object = new JSONObject(postParam3);

        Log.e("getJson", object.toString() + "--");


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT, session.BASEURL + "users/me", object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressDialog.dismiss();

                        Log.e("usersUpdate", response.toString());

                        try {

                            if (response.getString("status").equals("success")){
                                updatePersonal();
                            }


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
                Toast.makeText(getContext(), json, Toast.LENGTH_LONG).show();

                if (error instanceof ServerError) {

                }
                else if (error instanceof TimeoutError) {
                    Toast.makeText(getContext(), "Connection Timed Out", Toast.LENGTH_LONG).show();
                }
                else if (error instanceof NetworkError) {
                    Toast.makeText(getContext(), "Bad Network Connection", Toast.LENGTH_LONG).show();
                }

            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //       headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + session.getAPITOKEN());
                return headers;
            }
        };

        jsonObjReq.setTag("TAG");
        // Adding request to request queue
        mRequestqueue.add(jsonObjReq);
    }

    private void updatePersonal() {
        final KProgressHUD progressDialog = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        Map<String, Object> postParam3= new HashMap<String, Object>();
        if (titleSpinner.getSelectedItemPosition() != title_ArrayList.size() -1) {
            postParam3.put("title", title_ArrayList.get(titleSpinner.getSelectedItemPosition()).getId());
        }
        if (suffixSpinner.getSelectedItemPosition() != suffix_ArrayList.size() -1) {
            postParam3.put("suffix", suffix_ArrayList.get(suffixSpinner.getSelectedItemPosition()).getId());
        }
        postParam3.put("gender", gender_ArrayList.get(genderSpinner.getSelectedItemPosition()).getId());
        postParam3.put("dateOfBirth", chnage_date_format_Old(DOB_TV.getText().toString()));

        if (raceSpinner.getSelectedItemPosition() != race_ArrayList.size() -1) {
            postParam3.put("race", race_ArrayList.get(raceSpinner.getSelectedItemPosition()).getId());
        }
        if (ethnicitySpinner.getSelectedItemPosition() != ethnicity_ArrayList.size() -1) {
            postParam3.put("ethnicity", ethnicity_ArrayList.get(ethnicitySpinner.getSelectedItemPosition()).getId());
        }
        postParam3.put("otherEthnicity", otherEthnicityET.getText().toString());
        if (maritalStatusSpinner.getSelectedItemPosition() != marital_Status_ArrayList.size() -1) {
            postParam3.put("maritalStatus", marital_Status_ArrayList.get(maritalStatusSpinner.getSelectedItemPosition()).getId());
        }
        postParam3.put("phone", cell_phone_ET.getText().toString());
        postParam3.put("otherPhone", other_cell_phone_ET.getText().toString());
        postParam3.put("address", streetAddressET.getText().toString());
        postParam3.put("city", cityET.getText().toString());

        if (stateSpinner.getSelectedItemPosition() != state_ArrayList.size() -1) {
            postParam3.put("state", state_ArrayList.get(stateSpinner.getSelectedItemPosition()).getId());
        }

        if (!zipCodeET.getText().toString().isEmpty()) {
            postParam3.put("zipCode", zipCodeET.getText().toString());
        }

        if (alma_mater_Spinner.getSelectedItemPosition() != alma_mater_ArrayList.size() -1) {
            postParam3.put("universityId", Integer.parseInt(alma_mater_ArrayList.get(alma_mater_Spinner.getSelectedItemPosition()).getId()));
        }

        JSONObject object = new JSONObject(postParam3);

        Log.e("getJson", object.toString() + "--");


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT, session.BASEURL + "users/me/personal", object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressDialog.dismiss();

                        Log.e("updatePersonal", response.toString());

                        try {

                            if (response.getString("status").equals("success")){
                                getActivity().finish();
                                Toast.makeText(getActivity(), "update successfully" , Toast.LENGTH_SHORT).show();
                            }

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
                Toast.makeText(getContext(), json, Toast.LENGTH_LONG).show();

                if (error instanceof ServerError) {

                }
                else if (error instanceof TimeoutError) {
                    Toast.makeText(getContext(), "Connection Timed Out", Toast.LENGTH_LONG).show();
                }
                else if (error instanceof NetworkError) {
                    Toast.makeText(getContext(), "Bad Network Connection", Toast.LENGTH_LONG).show();
                }

            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //       headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + session.getAPITOKEN());
                return headers;
            }
        };

        jsonObjReq.setTag("TAG");
        // Adding request to request queue
        mRequestqueue.add(jsonObjReq);
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

    public static class DatePickerFragment_Graduation extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            return  dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String dateString = dateFormat.format(calendar.getTime());

            graduation_date_TV.setText(dateString);

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

    public static String chnage_date_format_Old(@NonNull String mDate) {
        String finalDate = null;
        SimpleDateFormat input = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date oneWayTripDate = input.parse(mDate);  // parse input
            finalDate = output.format(oneWayTripDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return finalDate;
    }


    private void isChangeData(View view){

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                EditProfile.isChange = true;
                return false;
            }
        });
    }

}