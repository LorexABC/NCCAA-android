package com.app_nccaa.nccaa.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.app_nccaa.nccaa.Utils.UserSession;
import com.app_nccaa.nccaa.Utils.VolleyMultipartRequest;
import com.app_nccaa.nccaa.Activity.EditProfile;
import com.app_nccaa.nccaa.Adapter.AdapterAnesthesiology;
import com.app_nccaa.nccaa.Adapter.AdapterRetirementPlan;
import com.app_nccaa.nccaa.Model.CityModel;
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

public class Employer_Info_Frag extends Fragment {

    private static TextView retirement_date_TV, dateFirstEmployeTV;
    private TextView academy_anesthesio_TV, society_anethesio_TV;

    private Spinner current_caa_Spinner, states_Spinner, emp_state_Spinner, emp_type1_practice_Spinner, emp_type2_practice_Spinner, receive_overtime_Spinner,
            overtime_reason_Spinner, work_schedule_Spinner, working_hours_Spinner, working_distribution_spinner, employer_benefits_Spinner,
            employer_ret_plan1_Spinner, communication_lang_Spinner, teaching_Spinner, specialties_Spinner;

    private ArrayList<CityModel> current_caa_ArrayList = new ArrayList<>();
    private ArrayList<CityModel> states_ArrayList = new ArrayList<>();
    private ArrayList<CityModel> employer_states_ArrayList = new ArrayList<>();
    private ArrayList<CityModel> emp_practice1_ArrayList = new ArrayList<>();
    private ArrayList<CityModel> emp_practice2_ArrayList = new ArrayList<>();
    private ArrayList<CityModel> receive_overtime_ArrayList = new ArrayList<>();
    private ArrayList<CityModel> overtime_reason_ArrayList = new ArrayList<>();
    private ArrayList<CityModel> work_schedule_ArrayList = new ArrayList<>();
    private ArrayList<CityModel> working_hours_ArrayList = new ArrayList<>();
    private ArrayList<CityModel> working_distribution_ArrayList = new ArrayList<>();
    private ArrayList<CityModel> employer_benefits_ArrayList = new ArrayList<>();
    private ArrayList<CityModel> employer_ret_plan1_ArrayList = new ArrayList<>();
    private ArrayList<CityModel> communication_lang_ArrayList = new ArrayList<>();
    private ArrayList<CityModel> teaching_ArrayList = new ArrayList<>();
    private ArrayList<CityModel> specialties_ArrayList = new ArrayList<>();
    private ArrayList<CityModel> academy_anesthesio_ArrayList = new ArrayList<>();
    private ArrayList<CityModel> society_anethesio_ArrayList = new ArrayList<>();

    private RelativeLayout layout;
    private boolean isRetirePlan_2_select = false, isAcademy_TV_select = false, isSociety_TV_select = false;

    private UserSession session;

    private EditText emp_name_ET, emp_address_ET, emp_aptsuite_ET, emp_city_ET, emp_zipcode_ET, emp_compensation_ET, language_speak_ET;

    private EditText practice_type_1_ET, practice_type_2_ET, workSchedule_Other_ET, employer_benefits_other_ET, specialties_other_ET;
    private RequestQueue mRequestqueue;

    private TextView retirementPlan2;
    private String mCompensation = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_employeer_info_frag, container, false);

        session = new UserSession(getContext());
        mRequestqueue = Volley.newRequestQueue(getActivity());

        layout = view.findViewById(R.id.activity_hours_layout);

        current_caa_Spinner = view.findViewById(R.id.current_caa_Spinner);
        states_Spinner = view.findViewById(R.id.states_Spinner);
        emp_state_Spinner = view.findViewById(R.id.emp_state_Spinner);
        emp_type1_practice_Spinner = view.findViewById(R.id.emp_type1_practice_Spinner);
        emp_type2_practice_Spinner = view.findViewById(R.id.emp_type2_practice_Spinner);
        receive_overtime_Spinner = view.findViewById(R.id.receive_overtime_Spinner);
        overtime_reason_Spinner = view.findViewById(R.id.overtime_reason_Spinner);
        work_schedule_Spinner = view.findViewById(R.id.work_schedule_Spinner);
        working_hours_Spinner = view.findViewById(R.id.working_hours_Spinner);
        working_distribution_spinner = view.findViewById(R.id.working_distribution_spinner);
        employer_benefits_Spinner = view.findViewById(R.id.employer_benefits_Spinner);
        employer_ret_plan1_Spinner = view.findViewById(R.id.employer_ret_plan1_Spinner);
        communication_lang_Spinner = view.findViewById(R.id.communication_lang_Spinner);
        teaching_Spinner = view.findViewById(R.id.teaching_Spinner);
        specialties_Spinner = view.findViewById(R.id.specialties_Spinner);
        academy_anesthesio_TV = view.findViewById(R.id.academy_anesthesio_TV);
        society_anethesio_TV = view.findViewById(R.id.society_anethesio_TV);

        emp_name_ET = view.findViewById(R.id.emp_name_ET);
        emp_address_ET = view.findViewById(R.id.emp_address_ET);
        emp_aptsuite_ET = view.findViewById(R.id.emp_aptsuite_ET);
        emp_city_ET = view.findViewById(R.id.emp_city_ET);
        emp_zipcode_ET = view.findViewById(R.id.emp_zipcode_ET);
        emp_compensation_ET = view.findViewById(R.id.emp_compensation_ET);
        language_speak_ET = view.findViewById(R.id.language_speak_ET);

        practice_type_1_ET = view.findViewById(R.id.practice_type_1_ET);
        practice_type_2_ET = view.findViewById(R.id.practice_type_2_ET);
        workSchedule_Other_ET = view.findViewById(R.id.workSchedule_Other_ET);
        employer_benefits_other_ET = view.findViewById(R.id.employer_benefits_other_ET);
        specialties_other_ET = view.findViewById(R.id.specialties_other_ET);

        retirementPlan2 = view.findViewById(R.id.retirementPlan2);



        retirement_date_TV = view.findViewById(R.id.retirement_date_TV);
        retirement_date_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        dateFirstEmployeTV = view.findViewById(R.id.dateFirstEmployeTV);
        dateFirstEmployeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragmentFirstEmpl();
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });




        isChangeData(current_caa_Spinner);
        isChangeData(states_Spinner);
        isChangeData(emp_state_Spinner);
        isChangeData(emp_type1_practice_Spinner);
        isChangeData(emp_type2_practice_Spinner);
        isChangeData(receive_overtime_Spinner);
        isChangeData(overtime_reason_Spinner);
        isChangeData(work_schedule_Spinner);
        isChangeData(working_hours_Spinner);
        isChangeData(working_distribution_spinner);
        isChangeData(employer_benefits_Spinner);
        isChangeData(employer_ret_plan1_Spinner);
        isChangeData(communication_lang_Spinner);
        isChangeData(society_anethesio_TV);
        isChangeData(academy_anesthesio_TV);
        isChangeData(specialties_Spinner);
        isChangeData(teaching_Spinner);
        isChangeData(emp_name_ET);
        isChangeData(emp_address_ET);
        isChangeData(emp_aptsuite_ET);
        isChangeData(emp_city_ET);
        isChangeData(emp_zipcode_ET);
        isChangeData(emp_compensation_ET);
        isChangeData(language_speak_ET);
        isChangeData(practice_type_1_ET);
        isChangeData(practice_type_2_ET);
        isChangeData(workSchedule_Other_ET);
        isChangeData(employer_benefits_other_ET);
        isChangeData(dateFirstEmployeTV);
        isChangeData(retirement_date_TV);
        isChangeData(retirementPlan2);
        isChangeData(specialties_other_ET);

        // CURRENT CA EMPLOYER SPINNER
        CityModel suff1 = new CityModel();
        suff1.setId("full-time");
        suff1.setName("Full-time");
        current_caa_ArrayList.add(suff1);
        CityModel suff2 = new CityModel();
        suff2.setId("part-time");
        suff2.setName("Part-time");
        current_caa_ArrayList.add(suff2);
        CityModel suff3 = new CityModel();
        suff3.setId("prn");
        suff3.setName("PRN");
        current_caa_ArrayList.add(suff3);
        CityModel suff4 = new CityModel();
        suff4.setId("locum-tenens");
        suff4.setName("Locum tenens");
        current_caa_ArrayList.add(suff4);
        CityModel suff5 = new CityModel();
        suff5.setId("retired");
        suff5.setName("Retired");
        current_caa_ArrayList.add(suff5);
        CityModel suff6 = new CityModel();
        suff6.setId("not-employed");
        suff6.setName("Not currently employed as a CAA");
        current_caa_ArrayList.add(suff6);
        CityModel cityModel1 = new CityModel();
        cityModel1.setId("");
        cityModel1.setName("Current CAA Employer Status");
        current_caa_ArrayList.add(cityModel1);

        SelectCitySpinner adapter = new SelectCitySpinner(getContext(), android.R.layout.simple_spinner_item, current_caa_ArrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        current_caa_Spinner.setAdapter(adapter);
        current_caa_Spinner.setSelection(adapter.getCount());

        current_caa_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == (current_caa_ArrayList.size() - 1)) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_gray_light));
                } else {

                    if (position == 5){
                        layout.setVisibility(View.GONE);
                        working_distribution_spinner.setSelection(working_distribution_ArrayList.size()-1);
                    } else {
                        layout.setVisibility(View.VISIBLE);
                        working_distribution_spinner.setSelection(working_distribution_ArrayList.size()-1);
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        // STATES SPINNER
        CityModel sta1 = new CityModel();
        sta1.setId("AL");
        sta1.setName("AL");
        states_ArrayList.add(sta1);
        CityModel sta2 = new CityModel();
        sta2.setId("AK");
        sta2.setName("AK");
        states_ArrayList.add(sta2);
        CityModel sta3 = new CityModel();
        sta3.setId("AZ");
        sta3.setName("AZ");
        states_ArrayList.add(sta3);
        CityModel sta4 = new CityModel();
        sta4.setId("AR");
        sta4.setName("AR");
        states_ArrayList.add(sta4);
        CityModel sta5 = new CityModel();
        sta5.setId("CA");
        sta5.setName("CA");
        states_ArrayList.add(sta5);
        CityModel sta6 = new CityModel();
        sta6.setId("CO");
        sta6.setName("CO");
        states_ArrayList.add(sta6);
        CityModel sta7 = new CityModel();
        sta7.setId("CT");
        sta7.setName("CT");
        states_ArrayList.add(sta7);
        CityModel sta8 = new CityModel();
        sta8.setId("DE");
        sta8.setName("DE");
        states_ArrayList.add(sta8);
        CityModel sta9 = new CityModel();
        sta9.setId("DC");
        sta9.setName("DC");
        states_ArrayList.add(sta9);
        CityModel sta10 = new CityModel();
        sta10.setId("FL");
        sta10.setName("FL");
        states_ArrayList.add(sta10);
        CityModel sta11 = new CityModel();
        sta11.setId("GA");
        sta11.setName("GA");
        states_ArrayList.add(sta11);
        CityModel sta12 = new CityModel();
        sta12.setId("HI");
        sta12.setName("HI");
        states_ArrayList.add(sta12);
        CityModel sta13 = new CityModel();
        sta13.setId("ID");
        sta13.setName("ID");
        states_ArrayList.add(sta13);
        CityModel sta14 = new CityModel();
        sta14.setId("IL");
        sta14.setName("IL");
        states_ArrayList.add(sta14);
        CityModel sta15 = new CityModel();
        sta15.setId("IN");
        sta15.setName("IN");
        states_ArrayList.add(sta15);
        CityModel sta16 = new CityModel();
        sta16.setId("IA");
        sta16.setName("IA");
        states_ArrayList.add(sta16);
        CityModel sta17 = new CityModel();
        sta17.setId("KS");
        sta17.setName("KS");
        states_ArrayList.add(sta17);
        CityModel sta18 = new CityModel();
        sta18.setId("KY");
        sta18.setName("KY");
        states_ArrayList.add(sta18);
        CityModel sta19 = new CityModel();
        sta19.setId("LA");
        sta19.setName("LA");
        states_ArrayList.add(sta19);
        CityModel sta20 = new CityModel();
        sta20.setId("ME");
        sta20.setName("ME");
        states_ArrayList.add(sta20);
        CityModel sta21 = new CityModel();
        sta21.setId("MD");
        sta21.setName("MD");
        states_ArrayList.add(sta21);
        CityModel sta22 = new CityModel();
        sta22.setId("MA");
        sta22.setName("MA");
        states_ArrayList.add(sta22);
        CityModel sta23 = new CityModel();
        sta23.setId("MI");
        sta23.setName("MI");
        states_ArrayList.add(sta23);
        CityModel sta24 = new CityModel();
        sta24.setId("MN");
        sta24.setName("MN");
        states_ArrayList.add(sta24);
        CityModel sta25 = new CityModel();
        sta25.setId("MS");
        sta25.setName("MS");
        states_ArrayList.add(sta25);
        CityModel sta26 = new CityModel();
        sta26.setId("MO");
        sta26.setName("MO");
        states_ArrayList.add(sta26);
        CityModel sta27 = new CityModel();
        sta27.setId("MT");
        sta27.setName("MT");
        states_ArrayList.add(sta27);
        CityModel sta28 = new CityModel();
        sta28.setId("NE");
        sta28.setName("NE");
        states_ArrayList.add(sta28);
        CityModel sta29 = new CityModel();
        sta29.setId("NV");
        sta29.setName("NV");
        states_ArrayList.add(sta29);
        CityModel sta30 = new CityModel();
        sta30.setId("NH");
        sta30.setName("NH");
        states_ArrayList.add(sta30);
        CityModel sta31 = new CityModel();
        sta31.setId("NJ");
        sta31.setName("NJ");
        states_ArrayList.add(sta31);
        CityModel sta32 = new CityModel();
        sta32.setId("NM");
        sta32.setName("NM");
        states_ArrayList.add(sta32);
        CityModel sta33 = new CityModel();
        sta33.setId("NY");
        sta33.setName("NY");
        states_ArrayList.add(sta33);
        CityModel sta34 = new CityModel();
        sta34.setId("NC");
        sta34.setName("NC");
        states_ArrayList.add(sta34);
        CityModel sta35 = new CityModel();
        sta35.setId("ND");
        sta35.setName("ND");
        states_ArrayList.add(sta35);
        CityModel sta36 = new CityModel();
        sta36.setId("OH");
        sta36.setName("OH");
        states_ArrayList.add(sta36);
        CityModel sta37 = new CityModel();
        sta37.setId("OK");
        sta37.setName("OK");
        states_ArrayList.add(sta37);
        CityModel sta38 = new CityModel();
        sta38.setId("OR");
        sta38.setName("OR");
        states_ArrayList.add(sta38);
        CityModel sta39 = new CityModel();
        sta39.setId("PA");
        sta39.setName("PA");
        states_ArrayList.add(sta39);
        CityModel sta40 = new CityModel();
        sta40.setId("RI");
        sta40.setName("RI");
        states_ArrayList.add(sta40);
        CityModel sta41 = new CityModel();
        sta41.setId("SC");
        sta41.setName("SC");
        states_ArrayList.add(sta41);
        CityModel sta42 = new CityModel();
        sta42.setId("SD");
        sta42.setName("SD");
        states_ArrayList.add(sta42);
        CityModel sta43 = new CityModel();
        sta43.setId("TN");
        sta43.setName("TN");
        states_ArrayList.add(sta43);
        CityModel sta44 = new CityModel();
        sta44.setId("TX");
        sta44.setName("TX");
        states_ArrayList.add(sta44);
        CityModel sta45 = new CityModel();
        sta45.setId("UT");
        sta45.setName("UT");
        states_ArrayList.add(sta45);
        CityModel sta46 = new CityModel();
        sta46.setId("VT");
        sta46.setName("VT");
        states_ArrayList.add(sta46);
        CityModel sta47 = new CityModel();
        sta47.setId("VA");
        sta47.setName("VA");
        states_ArrayList.add(sta47);
        CityModel sta48 = new CityModel();
        sta48.setId("WA");
        sta48.setName("WA");
        states_ArrayList.add(sta48);
        CityModel sta49 = new CityModel();
        sta49.setId("WV");
        sta49.setName("WV");
        states_ArrayList.add(sta49);
        CityModel sta50 = new CityModel();
        sta50.setId("WI");
        sta50.setName("WI");
        states_ArrayList.add(sta50);
        CityModel sta51 = new CityModel();
        sta51.setId("WY");
        sta51.setName("WY");
        states_ArrayList.add(sta51);
        CityModel ctyM = new CityModel();
        ctyM.setId("");
        ctyM.setName("State");
        states_ArrayList.add(ctyM);

        SelectCitySpinner adapter1 = new SelectCitySpinner(getContext(), android.R.layout.simple_spinner_item, states_ArrayList);
        adapter1.setDropDownViewResource(android.R.layout.simple_list_item_1);
        states_Spinner.setAdapter(adapter1);
        states_Spinner.setSelection(adapter1.getCount());

        states_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == (states_ArrayList.size() - 1)) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_gray_light));
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        for (int i = 0; i < states_ArrayList.size()-1; i++){
            CityModel ct1yM = new CityModel();
            ct1yM.setId(states_ArrayList.get(i).getId());
            ct1yM.setName(states_ArrayList.get(i).getName());
            employer_states_ArrayList.add(ct1yM);
        }
        CityModel ct1yM = new CityModel();
        ct1yM.setId("");
        ct1yM.setName("Employer State");
        employer_states_ArrayList.add(ct1yM);



        // Employer state SPINNER
        SelectCitySpinner adapter2 = new SelectCitySpinner(getContext(), android.R.layout.simple_spinner_item, employer_states_ArrayList);
        adapter2.setDropDownViewResource(android.R.layout.simple_list_item_1);
        emp_state_Spinner.setAdapter(adapter2);
        emp_state_Spinner.setSelection(adapter2.getCount());

        emp_state_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == (employer_states_ArrayList.size() - 1)) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_gray_light));
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        // Receive overtime SPINNER
        CityModel cityModel10 = new CityModel();
        cityModel10.setId("yes");
        cityModel10.setName("Yes");
        receive_overtime_ArrayList.add(cityModel10);
        CityModel citel10 = new CityModel();
        citel10.setId("no");
        citel10.setName("No");
        receive_overtime_ArrayList.add(citel10);
        CityModel cityModel11 = new CityModel();
        cityModel11.setId("");
        cityModel11.setName("Receive Overtime Pay");
        receive_overtime_ArrayList.add(cityModel11);

        SelectCitySpinner adapter5 = new SelectCitySpinner(getContext(), android.R.layout.simple_spinner_item, receive_overtime_ArrayList);
        adapter5.setDropDownViewResource(android.R.layout.simple_list_item_1);
        receive_overtime_Spinner.setAdapter(adapter5);
        receive_overtime_Spinner.setSelection(adapter5.getCount());

        receive_overtime_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == (receive_overtime_ArrayList.size() - 1)) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_gray_light));
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        // Overtime Pay Reasons SPINNER
        CityModel gen1 = new CityModel();
        gen1.setId("any-time");
        gen1.setName("Any time");
        overtime_reason_ArrayList.add(gen1);
        CityModel gen2 = new CityModel();
        gen2.setId("after-cumulative");
        gen2.setName("After cumulative hours");
        overtime_reason_ArrayList.add(gen2);
        CityModel cityMo = new CityModel();
        cityMo.setId("not-received");
        cityMo.setName("Do not receive");
        overtime_reason_ArrayList.add(cityMo);
        CityModel cityMow = new CityModel();
        cityMow.setId("not-eligible");
        cityMow.setName("Not eligible");
        overtime_reason_ArrayList.add(cityMow);
        CityModel cityModel13 = new CityModel();
        cityModel13.setId("");
        cityModel13.setName("Overtime Pay Reasons");
        overtime_reason_ArrayList.add(cityModel13);

        SelectCitySpinner adapter6 = new SelectCitySpinner(getContext(), android.R.layout.simple_spinner_item, overtime_reason_ArrayList);
        adapter6.setDropDownViewResource(android.R.layout.simple_list_item_1);
        overtime_reason_Spinner.setAdapter(adapter6);
        overtime_reason_Spinner.setSelection(adapter6.getCount());

        overtime_reason_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == (overtime_reason_ArrayList.size() - 1)) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_gray_light));
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        // Typical Weekly Work Schedule SPINNER
        CityModel rac1 = new CityModel();
        rac1.setId("consistent-shift");
        rac1.setName("Consistent shift");
        work_schedule_ArrayList.add(rac1);
        CityModel rac2 = new CityModel();
        rac2.setId("variable-shifts");
        rac2.setName("Variable shifts");
        work_schedule_ArrayList.add(rac2);
        CityModel rac3 = new CityModel();
        rac3.setId("fixed-start-variable-end");
        rac3.setName("Set start time each day");
        work_schedule_ArrayList.add(rac3);
        CityModel rac4 = new CityModel();
        rac4.setId("call");
        rac4.setName("Call");
        work_schedule_ArrayList.add(rac4);
        CityModel rac5 = new CityModel();
        rac5.setId("other");
        rac5.setName("Other");
        work_schedule_ArrayList.add(rac5);
        CityModel cityModel15 = new CityModel();
        cityModel15.setId("");
        cityModel15.setName("Typical Weekly Work Schedule");
        work_schedule_ArrayList.add(cityModel15);

        SelectCitySpinner adapter7 = new SelectCitySpinner(getContext(), android.R.layout.simple_spinner_item, work_schedule_ArrayList);
        adapter7.setDropDownViewResource(android.R.layout.simple_list_item_1);
        work_schedule_Spinner.setAdapter(adapter7);
        work_schedule_Spinner.setSelection(adapter7.getCount());

        work_schedule_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == (work_schedule_ArrayList.size() - 1)) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_gray_light));
                } else {
                    if (position == (work_schedule_ArrayList.size() - 2)) {
                        workSchedule_Other_ET.setVisibility(View.VISIBLE);
                    } else {
                        workSchedule_Other_ET.setVisibility(View.GONE);
                        workSchedule_Other_ET.setText("");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Working hours SPINNER
        CityModel rac6 = new CityModel();
        rac6.setId("less-20");
        rac6.setName("Less then 20 hours");
        working_hours_ArrayList.add(rac6);
        CityModel rac7 = new CityModel();
        rac7.setId("20-30");
        rac7.setName("20 to 30 hours");
        working_hours_ArrayList.add(rac7);
        CityModel rac8 = new CityModel();
        rac8.setId("30-40");
        rac8.setName("30 to 40 hours");
        working_hours_ArrayList.add(rac8);
        CityModel rac9 = new CityModel();
        rac9.setId("greater-40");
        rac9.setName("Greater then 40 hours");
        working_hours_ArrayList.add(rac9);
        CityModel cityModel17 = new CityModel();
        cityModel17.setId("");
        cityModel17.setName("Working Hours");
        working_hours_ArrayList.add(cityModel17);

        SelectCitySpinner adapter8 = new SelectCitySpinner(getContext(), android.R.layout.simple_spinner_item, working_hours_ArrayList);
        adapter8.setDropDownViewResource(android.R.layout.simple_list_item_1);
        working_hours_Spinner.setAdapter(adapter8);
        working_hours_Spinner.setSelection(adapter8.getCount());

        working_hours_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == (working_hours_ArrayList.size() - 1)) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_gray_light));
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        // Activity Hours Per Week SPINNER
        for (int i = 1; i < 101; i++){
            CityModel cityModel18 = new CityModel();
            cityModel18.setId(String.valueOf(i));
            cityModel18.setName(String.valueOf(i));
            working_distribution_ArrayList.add(cityModel18);
        }
        CityModel cityModel19 = new CityModel();
        cityModel19.setId("0");
        cityModel19.setName("Activity Hours Per Week");
        working_distribution_ArrayList.add(cityModel19);

        SelectCitySpinner adapter9 = new SelectCitySpinner(getContext(), android.R.layout.simple_spinner_item, working_distribution_ArrayList);
        adapter9.setDropDownViewResource(android.R.layout.simple_list_item_1);
        working_distribution_spinner.setAdapter(adapter9);
        working_distribution_spinner.setSelection(adapter9.getCount());

        working_distribution_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == (working_distribution_ArrayList.size() - 1)) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_gray_light));
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        // Communication Languages to patients SPINNER
        CityModel cityLan1 = new CityModel();
        cityLan1.setId("yes");
        cityLan1.setName("Yes");
        communication_lang_ArrayList.add(cityLan1);
        CityModel citeLan = new CityModel();
        citeLan.setId("no");
        citeLan.setName("No");
        communication_lang_ArrayList.add(citeLan);
        CityModel cityModel29 = new CityModel();
        cityModel29.setId("");
        cityModel29.setName("Communication Languages to patients");
        communication_lang_ArrayList.add(cityModel29);

        SelectCitySpinner adapter14 = new SelectCitySpinner(getContext(), android.R.layout.simple_spinner_item, communication_lang_ArrayList);
        adapter14.setDropDownViewResource(android.R.layout.simple_list_item_1);
        communication_lang_Spinner.setAdapter(adapter14);
        communication_lang_Spinner.setSelection(adapter14.getCount());

        communication_lang_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == (communication_lang_ArrayList.size() - 1)) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_gray_light));
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        // Teaching SPINNER
        CityModel cityModel30 = new CityModel();
        cityModel30.setId("aa-students");
        cityModel30.setName("AA-Students");
        teaching_ArrayList.add(cityModel30);
        CityModel cityModel3f2 = new CityModel();
        cityModel3f2.setId("healthcare-learners");
        cityModel3f2.setName("Healthcare Learners");
        teaching_ArrayList.add(cityModel3f2);
        CityModel cityModel31 = new CityModel();
        cityModel31.setId("");
        cityModel31.setName("Teaching");
        teaching_ArrayList.add(cityModel31);

        SelectCitySpinner adapter15 = new SelectCitySpinner(getContext(), android.R.layout.simple_spinner_item, teaching_ArrayList);
        adapter15.setDropDownViewResource(android.R.layout.simple_list_item_1);
        teaching_Spinner.setAdapter(adapter15);
        teaching_Spinner.setSelection(adapter15.getCount());

        teaching_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == (teaching_ArrayList.size() - 1)) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_gray_light));
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        view.findViewById(R.id.retirementPlan2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogRetirePlan2(positionRetirePlan1);
            }
        });


        academy_anesthesio_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAcademy();
            }
        });


        society_anethesio_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSociety();
            }
        });


        view.findViewById(R.id.submitBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (employer_ret_plan1_ArrayList.get(employer_ret_plan1_Spinner.getSelectedItemPosition()).getRetirementPlan2().isEmpty()){
                    isRetirePlan_2_select = true;
                }

                for (int i = 0; i < employer_ret_plan1_ArrayList.get(employer_ret_plan1_Spinner.getSelectedItemPosition()).getRetirementPlan2().size(); i++){
                    if (employer_ret_plan1_ArrayList.get(employer_ret_plan1_Spinner.getSelectedItemPosition()).getRetirementPlan2().get(i).getValue().isEmpty()){
                        isRetirePlan_2_select = false;
                        break;
                    } else {
                        isRetirePlan_2_select = true;
                    }
                }

                for (int i = 0; i < academy_anesthesio_ArrayList.size(); i++){
                    if (academy_anesthesio_ArrayList.get(i).isChecked()){
                        isAcademy_TV_select = true;
                        break;
                    } else {
                        isAcademy_TV_select = false;
                    }
                }
                for (int i = 0; i < society_anethesio_ArrayList.size(); i++){
                    if (society_anethesio_ArrayList.get(i).isChecked()){
                        isSociety_TV_select = true;
                        break;
                    } else {
                        isSociety_TV_select = false;
                    }
                }


                if (states_Spinner.getSelectedItemPosition() == states_ArrayList.size()-1){
                    Toast.makeText(getContext(), "Select states", Toast.LENGTH_SHORT).show();
                } else if (emp_name_ET.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Enter employer name", Toast.LENGTH_SHORT).show();
                } else if (emp_type1_practice_Spinner.getSelectedItemPosition() == emp_practice1_ArrayList.size()-1){
                    Toast.makeText(getContext(), "Select employer type of practise #1", Toast.LENGTH_SHORT).show();
                }
                /* else if (emp_type1_practice_Spinner.getSelectedItemPosition() == emp_practice1_ArrayList.size()-2 && practice_type_1_ET.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Enter other practice type 1", Toast.LENGTH_SHORT).show();
                } else if (emp_type2_practice_Spinner.getSelectedItemPosition() == emp_practice2_ArrayList.size()-2 && practice_type_2_ET.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Enter other practice type 2", Toast.LENGTH_SHORT).show();
                } else if (emp_compensation_ET.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Enter compensation offered", Toast.LENGTH_SHORT).show();
                }*/
                else if (emp_type2_practice_Spinner.getSelectedItemPosition() == emp_practice2_ArrayList.size()-1) {
                    Toast.makeText(getContext(), "Select employer type of practise #2", Toast.LENGTH_SHORT).show();
                }
                else if (current_caa_Spinner.getSelectedItemPosition() != current_caa_ArrayList.size()-2
                        && working_distribution_spinner.getSelectedItemPosition() == working_distribution_ArrayList.size()-1){
                    Toast.makeText(getContext(), "Enter hours per week", Toast.LENGTH_SHORT).show();
                } else if (receive_overtime_Spinner.getSelectedItemPosition() == receive_overtime_ArrayList.size()-1){
                    Toast.makeText(getContext(), "Select receive overtime pay", Toast.LENGTH_SHORT).show();
                }
                /* else if (overtime_reason_Spinner.getSelectedItemPosition() == overtime_reason_ArrayList.size()-1){
                    Toast.makeText(getContext(), "Select overtime pay reason", Toast.LENGTH_SHORT).show();
                } else if (work_schedule_Spinner.getSelectedItemPosition() == work_schedule_ArrayList.size()-1){
                    Toast.makeText(getContext(), "Select weekly work schedule", Toast.LENGTH_SHORT).show();
                } else if (work_schedule_Spinner.getSelectedItemPosition() == work_schedule_ArrayList.size()-2 && workSchedule_Other_ET.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Enter other work schedule", Toast.LENGTH_SHORT).show();
                }*/
                else if (working_hours_Spinner.getSelectedItemPosition() == working_hours_ArrayList.size()-1){
                    Toast.makeText(getContext(), "Select working hours", Toast.LENGTH_SHORT).show();
                } else if (employer_benefits_Spinner.getSelectedItemPosition() == employer_benefits_ArrayList.size()-1){
                    Toast.makeText(getContext(), "Select employer benefits", Toast.LENGTH_SHORT).show();
                }
                /* else if (employer_benefits_Spinner.getSelectedItemPosition() == employer_benefits_ArrayList.size()-2
                        && employer_benefits_other_ET.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Enter other employer benefits", Toast.LENGTH_SHORT).show();
                } else if (retirement_date_TV.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Enter anticipated retirement date", Toast.LENGTH_SHORT).show();
                }*/
                else if (employer_ret_plan1_Spinner.getSelectedItemPosition() == employer_ret_plan1_ArrayList.size()-1){
                    Toast.makeText(getContext(), "Select retirement plan setup 1", Toast.LENGTH_SHORT).show();
                } else if (!isRetirePlan_2_select){
                    Toast.makeText(getContext(), "Select retirement plan setup 2", Toast.LENGTH_SHORT).show();
                } else if (language_speak_ET.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Enter number of language spoken", Toast.LENGTH_SHORT).show();
                } else if (communication_lang_Spinner.getSelectedItemPosition() == communication_lang_ArrayList.size()-1){
                    Toast.makeText(getContext(), "Select communication languages", Toast.LENGTH_SHORT).show();
                } else if (teaching_Spinner.getSelectedItemPosition() == teaching_ArrayList.size()-1){
                    Toast.makeText(getContext(), "Select teaching", Toast.LENGTH_SHORT).show();
                } else if (specialties_Spinner.getSelectedItemPosition() == specialties_ArrayList.size()-1){
                    Toast.makeText(getContext(), "Select surgical specialities", Toast.LENGTH_SHORT).show();
                }
                /* else if (specialties_Spinner.getSelectedItemPosition() == specialties_ArrayList.size()-2 && specialties_other_ET.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Enter other specialties", Toast.LENGTH_SHORT).show();
                }*/
                else if (!isAcademy_TV_select){
                    Toast.makeText(getContext(), "Select academy of anesthesiologist", Toast.LENGTH_SHORT).show();
                } else if (!isSociety_TV_select){
                    Toast.makeText(getContext(), "Select society of anesthesiologist", Toast.LENGTH_SHORT).show();
                } else {

                    Map<String, Object> mMainObject = new HashMap<String, Object>();

                    if (!dateFirstEmployeTV.getText().toString().isEmpty()) {
                        mMainObject.put("firstEmployment", chnage_date_format_Old(dateFirstEmployeTV.getText().toString()));
                    }
                    if (current_caa_Spinner.getSelectedItemPosition() != current_caa_ArrayList.size() -1) {
                        mMainObject.put("employerStatus", current_caa_ArrayList.get(current_caa_Spinner.getSelectedItemPosition()).getId());
                    }
                    mMainObject.put("name", emp_name_ET.getText().toString());
                    mMainObject.put("address", emp_address_ET.getText().toString());
                    mMainObject.put("aptOrSuite", emp_aptsuite_ET.getText().toString());
                    mMainObject.put("city", emp_city_ET.getText().toString());
                    if (emp_state_Spinner.getSelectedItemPosition() != employer_states_ArrayList.size() -1) {
                        mMainObject.put("state", employer_states_ArrayList.get(emp_state_Spinner.getSelectedItemPosition()).getId());
                    }
                    if (!emp_zipcode_ET.getText().toString().isEmpty()) {
                        mMainObject.put("zipCode", emp_zipcode_ET.getText().toString());
                    }
                    mMainObject.put("group1Other", practice_type_1_ET.getText().toString());
                    mMainObject.put("group2Other", practice_type_2_ET.getText().toString());


                    if (!emp_compensation_ET.getText().toString().isEmpty()) {
                        try {
                            if (Integer.parseInt(emp_compensation_ET.getText().toString()) <= 100000) {
                                mCompensation = "less-100000";
                            } else if (Integer.parseInt(emp_compensation_ET.getText().toString()) >= 101000 && Integer.parseInt(emp_compensation_ET.getText().toString()) <= 125000) {
                                mCompensation = "101000-125000";
                            } else if (Integer.parseInt(emp_compensation_ET.getText().toString()) >= 126000 && Integer.parseInt(emp_compensation_ET.getText().toString()) <= 140000) {
                                mCompensation = "126000-140000";
                            } else if (Integer.parseInt(emp_compensation_ET.getText().toString()) >= 141000 && Integer.parseInt(emp_compensation_ET.getText().toString()) <= 160000) {
                                mCompensation = "141000-160000";
                            } else if (Integer.parseInt(emp_compensation_ET.getText().toString()) >= 161000 && Integer.parseInt(emp_compensation_ET.getText().toString()) <= 179000) {
                                mCompensation = "161000-179000";
                            } else if (Integer.parseInt(emp_compensation_ET.getText().toString()) >= 180000 && Integer.parseInt(emp_compensation_ET.getText().toString()) <= 200000) {
                                mCompensation = "180000-200000";
                            } else if (Integer.parseInt(emp_compensation_ET.getText().toString()) >= 200000 && Integer.parseInt(emp_compensation_ET.getText().toString()) <= 225000) {
                                mCompensation = "200000-225000";
                            } else {
                                mCompensation = "over-225000";
                            }
                        } catch (Exception e) {

                        }
                        mMainObject.put("compensation", mCompensation);
                    }


                    if (receive_overtime_ArrayList.get(receive_overtime_Spinner.getSelectedItemPosition()).getId().equals("yes")){
                        mMainObject.put("overtime", true);
                    } else {
                        mMainObject.put("overtime", false);
                    }

                    if (overtime_reason_Spinner.getSelectedItemPosition() != overtime_reason_ArrayList.size() -1) {
                        mMainObject.put("overtimeReceived", overtime_reason_ArrayList.get(overtime_reason_Spinner.getSelectedItemPosition()).getId());
                    }
                    if (work_schedule_Spinner.getSelectedItemPosition() != work_schedule_ArrayList.size() -1) {
                        mMainObject.put("workSchedule", work_schedule_ArrayList.get(work_schedule_Spinner.getSelectedItemPosition()).getId());
                        mMainObject.put("workScheduleOther", workSchedule_Other_ET.getText().toString());
                    }
                    mMainObject.put("workingHours", working_hours_ArrayList.get(working_hours_Spinner.getSelectedItemPosition()).getId());


                    JSONArray jsonArray4 = new JSONArray();
                    jsonArray4.put(employer_benefits_ArrayList.get(employer_benefits_Spinner.getSelectedItemPosition()).getCode());
                    mMainObject.put("employerBenefits", jsonArray4);
                    mMainObject.put("employerBenefitOther", employer_benefits_other_ET.getText().toString());

                    if (!retirement_date_TV.getText().toString().isEmpty()) {
                        mMainObject.put("retirementDate", chnage_date_format_Old(retirement_date_TV.getText().toString()));
                    }
                    mMainObject.put("languagesSpoken", Integer.parseInt(language_speak_ET.getText().toString()));

                    if (communication_lang_ArrayList.get(communication_lang_Spinner.getSelectedItemPosition()).getId().equals("yes")){
                        mMainObject.put("useOtherLanguages", true);
                    } else {
                        mMainObject.put("useOtherLanguages", false);
                    }

                    mMainObject.put("specialtyOther", specialties_other_ET.getText().toString());
                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put(states_ArrayList.get(states_Spinner.getSelectedItemPosition()).getId());
                    mMainObject.put("statesEligible", jsonArray);
                    JSONArray jsonArray2 = new JSONArray();
                    jsonArray2.put(emp_practice1_ArrayList.get(emp_type1_practice_Spinner.getSelectedItemPosition()).getCode());
                    mMainObject.put("practiceTypeCodesGroup1", jsonArray2);
                    JSONArray jsonArray3 = new JSONArray();
                    jsonArray3.put(emp_practice2_ArrayList.get(emp_type2_practice_Spinner.getSelectedItemPosition()).getCode());
                    mMainObject.put("practiceTypeCodesGroup2", jsonArray3);
                    JSONArray jsonArray5 = new JSONArray();
                    jsonArray5.put(teaching_ArrayList.get(teaching_Spinner.getSelectedItemPosition()).getId());
                    mMainObject.put("teaches", jsonArray5);
                    JSONArray jsonArray6 = new JSONArray();
                    jsonArray6.put(specialties_ArrayList.get(specialties_Spinner.getSelectedItemPosition()).getCode());
                    mMainObject.put("specialties", jsonArray6);

                    JSONArray jsonArray7 = new JSONArray();
                    for (int i = 0; i < academy_anesthesio_ArrayList.size(); i++){
                        if (academy_anesthesio_ArrayList.get(i).isChecked()){
                            jsonArray7.put(academy_anesthesio_ArrayList.get(i).getCode());
                        }
                    }
                    mMainObject.put("belongsToAaaaGroups", jsonArray7);


                    JSONArray jsonArray8 = new JSONArray();
                    for (int i = 0; i < society_anethesio_ArrayList.size(); i++){
                        if (society_anethesio_ArrayList.get(i).isChecked()){
                            jsonArray8.put(society_anethesio_ArrayList.get(i).getCode());
                        }
                    }
                    mMainObject.put("belongsToAsaGroups", jsonArray8);


                    Map<String, Object> object = new HashMap<String, Object>();
                    object.put("code", "direct");
                    object.put("hours", Integer.parseInt(working_distribution_ArrayList.get(working_distribution_spinner.getSelectedItemPosition()).getId()));
                    JSONObject jsonObject = new JSONObject(object);
                    JSONArray jsonArray9 = new JSONArray();
                    jsonArray9.put(jsonObject);
                    mMainObject.put("workingHoursDistribution", jsonArray9);

                    Map<String, Object> object2 = new HashMap<String, Object>();
                    object2.put("code", employer_ret_plan1_ArrayList.get(employer_ret_plan1_Spinner.getSelectedItemPosition()).getCode());

                    JSONArray jsonArray101 = new JSONArray();

                    for (int i = 0; i < employer_ret_plan1_ArrayList.get(employer_ret_plan1_Spinner.getSelectedItemPosition()).getRetirementPlan2().size(); i++){
                        Map<String, Object> object22 = new HashMap<String, Object>();
                        object22.put("fieldCode", employer_ret_plan1_ArrayList.get(employer_ret_plan1_Spinner.getSelectedItemPosition()).getRetirementPlan2().get(i).getCode());
                        object22.put("value", employer_ret_plan1_ArrayList.get(employer_ret_plan1_Spinner.getSelectedItemPosition()).getRetirementPlan2().get(i).getValue());
                        JSONObject jsonObject431 = new JSONObject(object22);
                        jsonArray101.put(jsonObject431);
                    }

                    object2.put("fieldValues", jsonArray101);
                    JSONObject jsonObject43 = new JSONObject(object2);
                    JSONArray jsonArray10 = new JSONArray();
                    jsonArray10.put(jsonObject43);
                    mMainObject.put("retirementPlan", jsonArray10);

                    JSONObject mFinalObject = new JSONObject(mMainObject);


                    updateEmpProfile(mFinalObject);
                    Log.e("sfasadfasfd", mFinalObject.toString());
                }


            }
        });



        getPracticeType();

        return view;
    }


    private void updateEmpProfile(JSONObject jsonObject) {
        final KProgressHUD progressDialog = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT, session.BASEURL + "users/me/employer", jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressDialog.dismiss();

                        Log.e("responceSignUp", response.toString());
                        try {

                            if (response.getString("status").equals("success")){
                                Toast.makeText(getActivity(), "update successfully" , Toast.LENGTH_SHORT).show();
                                getActivity().finish();
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
                Toast.makeText(getActivity(), json, Toast.LENGTH_LONG).show();

                if (error instanceof ServerError) {

                } else if (error instanceof TimeoutError) {
                    Toast.makeText(getActivity(), "Connection Timed Out", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getActivity(), "Bad Network Connection", Toast.LENGTH_LONG).show();
                }

            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + session.getAPITOKEN());
                return headers;
            }
        };

        jsonObjReq.setTag("TAG");
        // Adding request to request queue
        mRequestqueue.add(jsonObjReq);
    }



    private void getEmpInfo() {
        final KProgressHUD progressDialog = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET, session.BASEURL + "users/me/employer",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        progressDialog.dismiss();

                        try {
                            Log.e("employerInfo", new String(response.data) + "--");

                            JSONObject jsonObject = new JSONObject(new String(response.data));

                            if (!jsonObject.getString("firstEmployment").equals("null")) {
                                dateFirstEmployeTV.setText(chnage_date_formate(jsonObject.getString("firstEmployment")));
                            }
                            if (!jsonObject.getString("zipCode").equals("null")) {
                                emp_zipcode_ET.setText(jsonObject.getString("zipCode"));
                            }
                            if (!jsonObject.getString("compensation").equals("null")) {
                                emp_compensation_ET.setText(jsonObject.getString("compensation"));
                                mCompensation = jsonObject.getString("compensation");
                            }
                            if (!jsonObject.getString("retirementDate").equals("null")) {
                                retirement_date_TV.setText(chnage_date_formate(jsonObject.getString("retirementDate")));
                            }
                            emp_name_ET.setText(jsonObject.getString("name"));
                            if (!jsonObject.getString("address").equals("null")) {
                                emp_address_ET.setText(jsonObject.getString("address"));
                            }
                            if (!jsonObject.getString("aptOrSuite").equals("null")) {
                                emp_aptsuite_ET.setText(jsonObject.getString("aptOrSuite"));
                            }
                            if (!jsonObject.getString("city").equals("null")) {
                                emp_city_ET.setText(jsonObject.getString("city"));
                            }
                            if (!jsonObject.getString("languagesSpoken").equals("null")) {
                                language_speak_ET.setText(jsonObject.getString("languagesSpoken"));
                            }



                            if (!jsonObject.getString("employerStatus").equals("null")){
                                for (int i = 0; i < current_caa_ArrayList.size(); i++){
                                    if (jsonObject.getString("employerStatus").equals(current_caa_ArrayList.get(i).getId())){
                                        current_caa_Spinner.setSelection(i);
                                    }
                                }
                            }
                            if (!jsonObject.getString("state").equals("null")){
                                for (int i = 0; i < employer_states_ArrayList.size(); i++){
                                    if (jsonObject.getString("state").equals(employer_states_ArrayList.get(i).getId())){
                                        emp_state_Spinner.setSelection(i);
                                    }
                                }
                            }




                            if (jsonObject.getJSONArray("statesEligible").length() != 0){
                                for (int i = 0; i < states_ArrayList.size(); i++){
                                    if (jsonObject.getJSONArray("statesEligible").getString(0).equals(states_ArrayList.get(i).getId())){
                                        states_Spinner.setSelection(i);
                                    }
                                }
                            }

                            if (jsonObject.getJSONArray("practiceTypeCodesGroup1").length() != 0) {
                                for (int i = 0; i < emp_practice1_ArrayList.size(); i++) {
                                    if (jsonObject.getJSONArray("practiceTypeCodesGroup1").getString(0).equals(emp_practice1_ArrayList.get(i).getCode())) {
                                        emp_type1_practice_Spinner.setSelection(i);
                                    }
                                }
                            }


                            if (!jsonObject.getString("group1Other").equals("null")) {
                                practice_type_1_ET.setText(jsonObject.getString("group1Other"));
                            }

                            if (jsonObject.getJSONArray("practiceTypeCodesGroup2").length() != 0){
                                for (int i = 0; i < emp_practice2_ArrayList.size(); i++){
                                    if (jsonObject.getJSONArray("practiceTypeCodesGroup2").getString(0).equals(emp_practice2_ArrayList.get(i).getCode())){
                                        emp_type2_practice_Spinner.setSelection(i);
                                    }
                                }
                            }
                            if (!jsonObject.getString("group2Other").equals("null")) {
                                practice_type_2_ET.setText(jsonObject.getString("group2Other"));
                            }

                            if (!jsonObject.getString("overtimeReceived").equals("null")){
                                for (int i = 0; i < overtime_reason_ArrayList.size(); i++){
                                    if (jsonObject.getString("overtimeReceived").equals(overtime_reason_ArrayList.get(i).getId())){
                                        overtime_reason_Spinner.setSelection(i);
                                    }
                                }
                            }

                            if (!jsonObject.getString("workSchedule").equals("null")){
                                for (int i = 0; i < work_schedule_ArrayList.size(); i++){
                                    if (jsonObject.getString("workSchedule").equals(work_schedule_ArrayList.get(i).getId())){
                                        work_schedule_Spinner.setSelection(i);
                                    }
                                }
                            }
                            if (!jsonObject.getString("workScheduleOther").equals("null")) {
                                workSchedule_Other_ET.setText(jsonObject.getString("workScheduleOther"));
                            }

                            if (!jsonObject.getString("workingHours").equals("null")){
                                for (int i = 0; i < working_hours_ArrayList.size(); i++){
                                    if (jsonObject.getString("workingHours").equals(working_hours_ArrayList.get(i).getId())){
                                        working_hours_Spinner.setSelection(i);
                                    }
                                }
                            }


                            if (jsonObject.getJSONArray("workingHoursDistribution").length() != 0){
                                for (int i = 0; i < working_distribution_ArrayList.size(); i++){
                                    if (jsonObject.getJSONArray("workingHoursDistribution").getJSONObject(0).getString("hours")
                                            .equals(working_distribution_ArrayList.get(i).getId())){
                                        working_distribution_spinner.setSelection(i);
                                    }
                                }
                            }

                            if (jsonObject.getJSONArray("employerBenefits").length() != 0){
                                for (int i = 0; i < employer_benefits_ArrayList.size(); i++){
                                    if (jsonObject.getJSONArray("employerBenefits").getString(0).equals(employer_benefits_ArrayList.get(i).getCode())){
                                        employer_benefits_Spinner.setSelection(i);
                                    }
                                }
                            }
                            if (!jsonObject.getString("employerBenefitOther").equals("null")) {
                                employer_benefits_other_ET.setText(jsonObject.getString("employerBenefitOther"));
                            }


                            if (jsonObject.getJSONArray("retirementPlan").length() != 0) {
                                for (int i = 0; i < employer_ret_plan1_ArrayList.size(); i++) {
                                    if (jsonObject.getJSONArray("retirementPlan").getJSONObject(0).getString("code")
                                            .equals(employer_ret_plan1_ArrayList.get(i).getCode())) {

                                        employer_ret_plan1_Spinner.setSelection(i);
                                        positionRetirePlan1 = i;


                                        for (int j = 0; j < employer_ret_plan1_ArrayList.get(i).getRetirementPlan2().size(); j++) {
                                            for (int k = 0; k < jsonObject.getJSONArray("retirementPlan").getJSONObject(0)
                                                    .getJSONArray("fieldValues").length(); k++){
                                                if (employer_ret_plan1_ArrayList.get(i).getRetirementPlan2().get(j).getCode()
                                                        .equals(jsonObject.getJSONArray("retirementPlan").getJSONObject(0).getJSONArray("fieldValues")
                                                                .getJSONObject(k).getString("fieldCode"))){
                                                    employer_ret_plan1_ArrayList.get(i).getRetirementPlan2().get(j).setValue(jsonObject.getJSONArray("retirementPlan").getJSONObject(0).getJSONArray("fieldValues")
                                                            .getJSONObject(k).getString("value"));
                                                    isRetirePlan_2_select = true;
                                                }
                                            }
                                        }

                                    }
                                }
                            }


                            if (jsonObject.getJSONArray("teaches").length() != 0){
                                for (int i = 0; i < teaching_ArrayList.size(); i++){
                                    if (jsonObject.getJSONArray("teaches").getString(0).equals(teaching_ArrayList.get(i).getId())){
                                        teaching_Spinner.setSelection(i);
                                    }
                                }
                            }

                            if (jsonObject.getJSONArray("specialties").length() != 0) {
                                for (int i = 0; i < specialties_ArrayList.size(); i++) {
                                    if (jsonObject.getJSONArray("specialties").getString(0).equals(specialties_ArrayList.get(i).getCode())) {
                                        specialties_Spinner.setSelection(i);
                                    }
                                }
                            }

                            if (!jsonObject.getString("specialtyOther").equals("null")) {
                                specialties_other_ET.setText(jsonObject.getString("specialtyOther"));
                            }



                            if (jsonObject.getString("overtime").equals("false")){
                                receive_overtime_Spinner.setSelection(1);
                            } else if (jsonObject.getString("overtime").equals("true")){
                                receive_overtime_Spinner.setSelection(0);
                            }

                            if (jsonObject.getString("useOtherLanguages").equals("false")){
                                communication_lang_Spinner.setSelection(1);
                            } else if (jsonObject.getString("overtime").equals("true")){
                                communication_lang_Spinner.setSelection(0);
                            }



                            // last two fields in page --> checkbox vadi
                            for (int i = 0; i < jsonObject.getJSONArray("belongsToAaaaGroups").length(); i++){
                                for (int j = 0; j < academy_anesthesio_ArrayList.size(); j++){
                                    if (jsonObject.getJSONArray("belongsToAaaaGroups").getString(i)
                                            .equals(academy_anesthesio_ArrayList.get(j).getCode())){
                                        academy_anesthesio_ArrayList.get(j).setChecked(true);
                                        isAcademy_TV_select = true;
                                    }
                                }
                            }

                            for (int i = 0; i < jsonObject.getJSONArray("belongsToAsaGroups").length(); i++){
                                for (int j = 0; j < society_anethesio_ArrayList.size(); j++){
                                    if (jsonObject.getJSONArray("belongsToAsaGroups").getString(i)
                                            .equals(society_anethesio_ArrayList.get(j).getCode())){
                                        society_anethesio_ArrayList.get(j).setChecked(true);
                                        isSociety_TV_select = true;
                                    }
                                }
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


    private int positionRetirePlan1 = 0;
    private void getRetirementPlans() {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET, session.BASEURL + "retirementPlans",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        getSpecialties();


                        try {
                            Log.e("retirementPlans", new String(response.data) + "--");


                            JSONArray jsonArray = new JSONArray(new String(response.data));

                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);

                                CityModel model = new CityModel();
                                model.setCode(object.getString("code"));
                                model.setName(object.getString("name"));

                                ArrayList<CityModel> retirementPlanArray2 = new ArrayList<>();
                                JSONArray array = object.getJSONArray("fields");

                                for (int j = 0; j < array.length(); j++){
                                    JSONObject object1 = array.getJSONObject(j);

                                    CityModel model1 = new CityModel();
                                    model1.setCode(object1.getString("code"));
                                    model1.setName(object1.getString("name"));
                                    model1.setType(object1.getString("type"));
                                    model1.setValue("");
                                    model1.setChecked(false);
                                    retirementPlanArray2.add(model1);
                                }
                                model.setRetirementPlan2(retirementPlanArray2);
                                employer_ret_plan1_ArrayList.add(model);
                            }

                            CityModel cityModel23 = new CityModel();
                            cityModel23.setCode("");
                            cityModel23.setName("Employer Retirement Plan Setup #1");
                            ArrayList<CityModel> retirementPlanArray2 = new ArrayList<>();
                            CityModel model1 = new CityModel();
                            model1.setCode("");
                            model1.setName("");
                            model1.setType("");
                            model1.setValue("");
                            model1.setChecked(false);
                            retirementPlanArray2.add(model1);
                            model1.setRetirementPlan2(retirementPlanArray2);
                            employer_ret_plan1_ArrayList.add(model1);


                            // Employer Retirement Plan Setup #1 SPINNER
                            SelectCitySpinner adapter11 = new SelectCitySpinner(getContext(), android.R.layout.simple_spinner_item, employer_ret_plan1_ArrayList);
                            adapter11.setDropDownViewResource(android.R.layout.simple_list_item_1);
                            employer_ret_plan1_Spinner.setAdapter(adapter11);
                            employer_ret_plan1_Spinner.setSelection(adapter11.getCount());

                            employer_ret_plan1_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    if (position == (employer_ret_plan1_ArrayList.size() - 1)) {
                                        ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_gray_light));
                                    } else {
                                        positionRetirePlan1 = position;
                                        retirementPlan2.setText("");
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

    private void getEmployerBenefits() {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET, session.BASEURL + "employerBenefits",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        getEmpInfo();

                        try {
                            Log.e("employerBenefits", new String(response.data) + "--");

                            JSONArray jsonArray = new JSONArray(new String(response.data));

                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);

                                CityModel model = new CityModel();
                                model.setCode(object.getString("code"));
                                model.setName(object.getString("name"));
                                employer_benefits_ArrayList.add(model);
                            }

                            // Employer Benefits SPINNER
                            CityModel cityModel21 = new CityModel();
                            cityModel21.setId("");
                            cityModel21.setName("Employer Benefits");
                            employer_benefits_ArrayList.add(cityModel21);

                            SelectCitySpinner adapter10 = new SelectCitySpinner(getContext(), android.R.layout.simple_spinner_item, employer_benefits_ArrayList);
                            adapter10.setDropDownViewResource(android.R.layout.simple_list_item_1);
                            employer_benefits_Spinner.setAdapter(adapter10);
                            employer_benefits_Spinner.setSelection(adapter10.getCount());

                            employer_benefits_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    if (position == (employer_benefits_ArrayList.size() - 1)) {
                                        ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_gray_light));
                                    } else {
                                        if (position == (employer_benefits_ArrayList.size() - 2)){
                                            employer_benefits_other_ET.setVisibility(View.VISIBLE);
                                        } else {
                                            employer_benefits_other_ET.setVisibility(View.GONE);
                                            employer_benefits_other_ET.setText("");
                                        }
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

    private void getPracticeType() {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET, session.BASEURL + "practiceTypes",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        getRetirementPlans();


                        try {
                            Log.e("practiceTypes", new String(response.data) + "--");


                            JSONArray jsonArray = new JSONArray(new String(response.data));

                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);

                                if (object.getString("group").equals("1")) {
                                    CityModel model = new CityModel();
                                    model.setCode(object.getString("code"));
                                    model.setName(object.getString("name"));
                                    model.setGroup(object.getString("group"));
                                    emp_practice1_ArrayList.add(model);
                                } else {
                                    CityModel model = new CityModel();
                                    model.setCode(object.getString("code"));
                                    model.setName(object.getString("name"));
                                    model.setGroup(object.getString("group"));
                                    emp_practice2_ArrayList.add(model);
                                }

                            }
                            CityModel cityModel7 = new CityModel();
                            cityModel7.setCode("");
                            cityModel7.setName("Employer Type of Practise Setting #1");
                            cityModel7.setGroup("");
                            emp_practice1_ArrayList.add(cityModel7);

                            CityModel aw1 = new CityModel();
                            aw1.setCode("");
                            aw1.setName("Employer Type of Practise Setting #2");
                            aw1.setGroup("");
                            emp_practice2_ArrayList.add(aw1);


                            // Employer type setting 1 SPINNER
                            SelectCitySpinner adapter3 = new SelectCitySpinner(getContext(), android.R.layout.simple_spinner_item, emp_practice1_ArrayList);
                            adapter3.setDropDownViewResource(android.R.layout.simple_list_item_1);
                            emp_type1_practice_Spinner.setAdapter(adapter3);
                            emp_type1_practice_Spinner.setSelection(adapter3.getCount());

                            emp_type1_practice_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    if (position == (emp_practice1_ArrayList.size() - 1)) {
                                        ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_gray_light));
                                    } else {
                                        if (position == (emp_practice1_ArrayList.size() - 2)) {
                                            practice_type_1_ET.setVisibility(View.VISIBLE);
                                        } else {
                                            practice_type_1_ET.setVisibility(View.GONE);
                                            practice_type_1_ET.setText("");
                                        }

                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });


                            // Employer type setting 2 SPINNER
                            SelectCitySpinner adapter4 = new SelectCitySpinner(getContext(), android.R.layout.simple_spinner_item, emp_practice2_ArrayList);
                            adapter4.setDropDownViewResource(android.R.layout.simple_list_item_1);
                            emp_type2_practice_Spinner.setAdapter(adapter4);
                            emp_type2_practice_Spinner.setSelection(adapter4.getCount());

                            emp_type2_practice_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    if (position == (emp_practice2_ArrayList.size() - 1)) {
                                        ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_gray_light));
                                    } else {
                                        if (position == (emp_practice2_ArrayList.size() - 2)) {
                                            practice_type_2_ET.setVisibility(View.VISIBLE);
                                        } else {
                                            practice_type_2_ET.setVisibility(View.GONE);
                                            practice_type_2_ET.setText("");
                                        }
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

    private void getSpecialties() {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET, session.BASEURL + "specialties",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        getAnesthesiology();

                        try {
                            Log.e("specialties", new String(response.data) + "--");


                            JSONArray jsonArray = new JSONArray(new String(response.data));

                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);

                                CityModel model = new CityModel();
                                model.setCode(object.getString("code"));
                                model.setName(object.getString("name"));
                                specialties_ArrayList.add(model);
                            }

                            // Surgical specialities/anesthestic SPINNER
                            CityModel cityModel33 = new CityModel();
                            cityModel33.setCode("");
                            cityModel33.setName("Surgical specialities/anesthestic");
                            specialties_ArrayList.add(cityModel33);

                            SelectCitySpinner adapter16 = new SelectCitySpinner(getContext(), android.R.layout.simple_spinner_item, specialties_ArrayList);
                            adapter16.setDropDownViewResource(android.R.layout.simple_list_item_1);
                            specialties_Spinner.setAdapter(adapter16);
                            specialties_Spinner.setSelection(adapter16.getCount());

                            specialties_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    if (position == (specialties_ArrayList.size() - 1)) {
                                        ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_gray_light));
                                    } else {
                                        if (position == (specialties_ArrayList.size() - 2)) {
                                            specialties_other_ET.setVisibility(View.VISIBLE);
                                        } else {
                                            specialties_other_ET.setVisibility(View.GONE);
                                            specialties_other_ET.setText("");
                                        }
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

    private void getAnesthesiology() {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET, session.BASEURL + "anesthesiologyGroups",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        getEmployerBenefits();

                        try {
                            Log.e("anesthesiology", new String(response.data) + "--");

                            JSONArray jsonArray = new JSONArray(new String(response.data));

                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);

                                CityModel model = new CityModel();
                                model.setCode(object.getString("code"));
                                model.setName(object.getString("name"));
                                model.setType(object.getString("type"));
                                model.setChecked(false);
                                academy_anesthesio_ArrayList.add(model);
                                CityModel w2 = new CityModel();
                                w2.setCode(object.getString("code"));
                                w2.setName(object.getString("name"));
                                w2.setType(object.getString("type"));
                                w2.setChecked(false);
                                society_anethesio_ArrayList.add(w2);
                            }

                        } catch (Exception e) {

                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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




    private void dialogRetirePlan2(int position) {

        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_dialog_academy);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        AppCompatButton btnSave = dialog.findViewById(R.id.btnSave);
        ImageView dialogClose = dialog.findViewById(R.id.dialogClose);
        RecyclerView recCatList = dialog.findViewById(R.id.recCatList);
        recCatList.setLayoutManager(new LinearLayoutManager(dialog.getContext()));

        AdapterRetirementPlan adapterRetirementPlan = new AdapterRetirementPlan(dialog.getContext(), employer_ret_plan1_ArrayList.get(position).getRetirementPlan2(), new AdapterRetirementPlan.OnItemClickListener() {
            @Override
            public void onItemValue(int item, String value) {

                employer_ret_plan1_ArrayList.get(position).getRetirementPlan2().get(item).setValue(value);
            }
        });
        recCatList.setAdapter(adapterRetirementPlan);


        dialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StringBuilder builder = new StringBuilder();

                for (int i = 0; i < employer_ret_plan1_ArrayList.get(position).getRetirementPlan2().size(); i++){
                    if (i != employer_ret_plan1_ArrayList.get(position).getRetirementPlan2().size() - 1) {
                        builder.append(employer_ret_plan1_ArrayList.get(position).getRetirementPlan2().get(i).getName() + " - "
                                + employer_ret_plan1_ArrayList.get(position).getRetirementPlan2().get(i).getValue() + "\n");
                    }
                    else {
                        builder.append(employer_ret_plan1_ArrayList.get(position).getRetirementPlan2().get(i).getName() + " - "
                                + employer_ret_plan1_ArrayList.get(position).getRetirementPlan2().get(i).getValue());
                    }
                }

                if (!employer_ret_plan1_ArrayList.get(position).getRetirementPlan2().isEmpty()){
                    retirementPlan2.setText(builder);
                } else {
                    retirementPlan2.setText("");
                }
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StringBuilder builder = new StringBuilder();

                for (int i = 0; i < employer_ret_plan1_ArrayList.get(position).getRetirementPlan2().size(); i++){
                    if (i != employer_ret_plan1_ArrayList.get(position).getRetirementPlan2().size() - 1) {
                        builder.append(employer_ret_plan1_ArrayList.get(position).getRetirementPlan2().get(i).getName() + " - "
                                + employer_ret_plan1_ArrayList.get(position).getRetirementPlan2().get(i).getValue() + "\n");
                    }
                    else {
                        builder.append(employer_ret_plan1_ArrayList.get(position).getRetirementPlan2().get(i).getName() + " - "
                                + employer_ret_plan1_ArrayList.get(position).getRetirementPlan2().get(i).getValue());
                    }
                }

                if (!employer_ret_plan1_ArrayList.get(position).getRetirementPlan2().isEmpty()){
                    retirementPlan2.setText(builder);
                } else {
                    retirementPlan2.setText("");
                }
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void dialogAcademy() {

        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_dialog_academy);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        AppCompatButton btnSave = dialog.findViewById(R.id.btnSave);
        ImageView dialogClose = dialog.findViewById(R.id.dialogClose);
        RecyclerView recCatList = dialog.findViewById(R.id.recCatList);
        recCatList.setLayoutManager(new LinearLayoutManager(dialog.getContext()));

        AdapterAnesthesiology adapterSubCatService = new AdapterAnesthesiology(dialog.getContext(), academy_anesthesio_ArrayList, new AdapterAnesthesiology.OnItemClickListener() {
            @Override
            public void onItemClick(int item) {

                if (academy_anesthesio_ArrayList.get(item).isChecked()) {
                    academy_anesthesio_ArrayList.get(item).setChecked(false);

                } else {
                    academy_anesthesio_ArrayList.get(item).setChecked(true);
                }

            }
        });
        recCatList.setAdapter(adapterSubCatService);


        dialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> nameArrayAcademy = new ArrayList<>();

                for (int i = 0; i < academy_anesthesio_ArrayList.size(); i++){
                    if (academy_anesthesio_ArrayList.get(i).isChecked()) {
                        nameArrayAcademy.add(academy_anesthesio_ArrayList.get(i).getName());
                    }
                }

                String academyStr = TextUtils.join(", ", nameArrayAcademy);

                if (!academy_anesthesio_ArrayList.isEmpty()){
                    academy_anesthesio_TV.setText(academyStr);
                }

                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<String> nameArrayAcademy = new ArrayList<>();

                for (int i = 0; i < academy_anesthesio_ArrayList.size(); i++){
                    if (academy_anesthesio_ArrayList.get(i).isChecked()) {
                        nameArrayAcademy.add(academy_anesthesio_ArrayList.get(i).getName());
                    }
                }

                String academyStr = TextUtils.join(", ", nameArrayAcademy);

                if (!academy_anesthesio_ArrayList.isEmpty()){
                    academy_anesthesio_TV.setText(academyStr);
                }

                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void dialogSociety() {

        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_dialog_academy);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        AppCompatButton btnSave = dialog.findViewById(R.id.btnSave);
        ImageView dialogClose = dialog.findViewById(R.id.dialogClose);
        RecyclerView recCatList = dialog.findViewById(R.id.recCatList);
        recCatList.setLayoutManager(new LinearLayoutManager(dialog.getContext()));

        AdapterAnesthesiology adapterSubCatService = new AdapterAnesthesiology(dialog.getContext(), society_anethesio_ArrayList, new AdapterAnesthesiology.OnItemClickListener() {
            @Override
            public void onItemClick(int item) {

                if (society_anethesio_ArrayList.get(item).isChecked()) {
                    society_anethesio_ArrayList.get(item).setChecked(false);

                } else {
                    society_anethesio_ArrayList.get(item).setChecked(true);
                }

            }
        });
        recCatList.setAdapter(adapterSubCatService);


        dialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<String> nameArraySociety = new ArrayList<>();

                for (int i = 0; i < society_anethesio_ArrayList.size(); i++){
                    if (society_anethesio_ArrayList.get(i).isChecked()) {
                        nameArraySociety.add(society_anethesio_ArrayList.get(i).getName());
                    }
                }

                String societyStr = TextUtils.join(", ", nameArraySociety);

                if (!society_anethesio_ArrayList.isEmpty()){
                    society_anethesio_TV.setText(societyStr);
                }

                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<String> nameArraySociety = new ArrayList<>();

                for (int i = 0; i < society_anethesio_ArrayList.size(); i++){
                    if (society_anethesio_ArrayList.get(i).isChecked()) {
                        nameArraySociety.add(society_anethesio_ArrayList.get(i).getName());
                    }
                }

                String societyStr = TextUtils.join(", ", nameArraySociety);

                if (!society_anethesio_ArrayList.isEmpty()){
                    society_anethesio_TV.setText(societyStr);
                }

                dialog.dismiss();
            }
        });

        dialog.show();

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

            retirement_date_TV.setText(dateString);

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

    public static class DatePickerFragmentFirstEmpl extends DialogFragment
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

            dateFirstEmployeTV.setText(dateString);

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