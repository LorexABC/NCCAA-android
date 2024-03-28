package com.app_nccaa.nccaa.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
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
import com.app_nccaa.nccaa.R;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * State Licensing Form
 *
 * Modified by phantom
 * On 03/22/24
 *
 * Payment feature is created in this section
 */
public class State_Licensing_Form extends AppCompatActivity {


    private TextView seeInstrucTV;
    private static TextView DOB_TV;
    private Spinner voc_recipientSpinner, stateSpinner;

    private ArrayList<CityModel> state_ArrayList = new ArrayList<>();
    private ArrayList<CityModel> recipientArrayList = new ArrayList<>();

    private LinearLayout state_medical_board_layout, employer_layout, personal_usage_layout;

    private EditText firstNameET, lastNameET, emailET;
    private TextView userID;

    private UserSession session;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private EditText credential_spec, recipient_email, confirm_email, emp_name, empl_contact, emp_email, caa_email;
    private RequestQueue mRequestqueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_licensing_form);

        session = new UserSession(State_Licensing_Form.this);
        mRequestqueue = Volley.newRequestQueue(State_Licensing_Form.this);


        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        seeInstrucTV = findViewById(R.id.seeInstrucTV);
        DOB_TV = findViewById(R.id.DOB_TV);
        state_medical_board_layout = findViewById(R.id.state_medical_board_layout);
        employer_layout = findViewById(R.id.employer_layout);
        personal_usage_layout = findViewById(R.id.personal_usage_layout);
        voc_recipientSpinner = findViewById(R.id.voc_recipientSpinner);
        stateSpinner = findViewById(R.id.stateSpinner);

        userID = findViewById(R.id.userID);
        firstNameET = findViewById(R.id.firstNameET);
        lastNameET = findViewById(R.id.lastNameET);
        emailET = findViewById(R.id.emailET);

        credential_spec = findViewById(R.id.credential_spec);
        recipient_email = findViewById(R.id.recipient_email);
        confirm_email = findViewById(R.id.confirm_email);
        emp_name = findViewById(R.id.emp_name);
        empl_contact = findViewById(R.id.empl_contact);
        emp_email = findViewById(R.id.emp_email);
        caa_email = findViewById(R.id.caa_email);


        userID.setText("ID " + session.getUSER_ID());
        seeInstrucTV.setPaintFlags(seeInstrucTV.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);


        seeInstrucTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(State_Licensing_Form.this, More_Info_Cert_Exam.class)
                        .putExtra("from", "state_licencing"));
            }
        });


        DOB_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });




        CityModel ttl1 = new CityModel();
        ttl1.setId("medical-board");
        ttl1.setName("State Medical Board");
        recipientArrayList.add(ttl1);
        CityModel ttl2 = new CityModel();
        ttl2.setId("employer");
        ttl2.setName("Employer");
        recipientArrayList.add(ttl2);
        CityModel ttl3 = new CityModel();
        ttl3.setId("personal");
        ttl3.setName("Personal Usage");
        recipientArrayList.add(ttl3);
        CityModel cityModel3 = new CityModel();
        cityModel3.setId("");
        cityModel3.setName("VOC Recipient");
        recipientArrayList.add(cityModel3);

        SelectCitySpinner adapter1 = new SelectCitySpinner(this, android.R.layout.simple_spinner_item, recipientArrayList);
        adapter1.setDropDownViewResource(android.R.layout.simple_list_item_1);
        voc_recipientSpinner.setAdapter(adapter1);
        voc_recipientSpinner.setSelection(adapter1.getCount());

        voc_recipientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == (recipientArrayList.size() - 1)) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_gray_light));
                } else {

                    if (position == 0){
                        state_medical_board_layout.setVisibility(View.VISIBLE);
                        employer_layout.setVisibility(View.GONE);
                        personal_usage_layout.setVisibility(View.GONE);
                    }
                    if (position == 1){
                        state_medical_board_layout.setVisibility(View.GONE);
                        employer_layout.setVisibility(View.VISIBLE);
                        personal_usage_layout.setVisibility(View.GONE);
                    }
                    if (position == 2){
                        state_medical_board_layout.setVisibility(View.GONE);
                        employer_layout.setVisibility(View.GONE);
                        personal_usage_layout.setVisibility(View.VISIBLE);
                    }
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

        SelectCitySpinner adapter8 = new SelectCitySpinner(this, android.R.layout.simple_spinner_item, state_ArrayList);
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

        findViewById(R.id.subMitBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (firstNameET.getText().toString().isEmpty()){
                    Toast.makeText(State_Licensing_Form.this, "enter first name", Toast.LENGTH_SHORT).show();
                } else if (lastNameET.getText().toString().isEmpty()){
                    Toast.makeText(State_Licensing_Form.this, "enter last name", Toast.LENGTH_SHORT).show();
                } else if (emailET.getText().toString().isEmpty()){
                    Toast.makeText(State_Licensing_Form.this, "enter email", Toast.LENGTH_SHORT).show();
                } else if (!emailET.getText().toString().matches(emailPattern)){
                    Toast.makeText(State_Licensing_Form.this, "invalid email", Toast.LENGTH_SHORT).show();
                } else if (DOB_TV.getText().toString().isEmpty()){
                    Toast.makeText(State_Licensing_Form.this, "Choose date", Toast.LENGTH_SHORT).show();
                } else if (voc_recipientSpinner.getSelectedItemPosition() == recipientArrayList.size() - 1){
                    Toast.makeText(State_Licensing_Form.this, "select VOC recipient", Toast.LENGTH_SHORT).show();
                } else {

                    if (state_medical_board_layout.getVisibility() == View.VISIBLE) {
                        if (stateSpinner.getSelectedItemPosition() == state_ArrayList.size() - 1) {
                            Toast.makeText(State_Licensing_Form.this, "select state", Toast.LENGTH_SHORT).show();
                        } else if (credential_spec.getText().toString().isEmpty()) {
                            Toast.makeText(State_Licensing_Form.this, "enter credentialing specialist", Toast.LENGTH_SHORT).show();
                        } else if (recipient_email.getText().toString().isEmpty()) {
                            Toast.makeText(State_Licensing_Form.this, "enter recipient email", Toast.LENGTH_SHORT).show();
                        } else if (!recipient_email.getText().toString().matches(emailPattern)) {
                            Toast.makeText(State_Licensing_Form.this, "invalid recipient email", Toast.LENGTH_SHORT).show();
                        } else if (confirm_email.getText().toString().isEmpty()) {
                            Toast.makeText(State_Licensing_Form.this, "enter confirm email", Toast.LENGTH_SHORT).show();
                        } else if (!confirm_email.getText().toString().matches(emailPattern)) {
                            Toast.makeText(State_Licensing_Form.this, "invalid confirm email", Toast.LENGTH_SHORT).show();
                        } else if (!confirm_email.getText().toString().equals(recipient_email.getText().toString())) {
                            Toast.makeText(State_Licensing_Form.this, "email doesn't match", Toast.LENGTH_SHORT).show();
                        } else {
                            setStateLicense();
                        }
                    }

                    if (employer_layout.getVisibility() == View.VISIBLE) {
                        if (emp_name.getText().toString().isEmpty()) {
                            Toast.makeText(State_Licensing_Form.this, "enter name", Toast.LENGTH_SHORT).show();
                        } else if (empl_contact.getText().toString().isEmpty()) {
                            Toast.makeText(State_Licensing_Form.this, "enter employer contact", Toast.LENGTH_SHORT).show();
                        } else if (emp_email.getText().toString().isEmpty()) {
                            Toast.makeText(State_Licensing_Form.this, "enter employer email", Toast.LENGTH_SHORT).show();
                        } else if (!emp_email.getText().toString().matches(emailPattern)) {
                            Toast.makeText(State_Licensing_Form.this, "invalid email", Toast.LENGTH_SHORT).show();
                        } else {
                            setStateLicense();
                        }
                    }

                    if (personal_usage_layout.getVisibility() == View.VISIBLE) {
                        if (caa_email.getText().toString().isEmpty()){
                            Toast.makeText(State_Licensing_Form.this, "enter caa email", Toast.LENGTH_SHORT).show();
                        } else if (!caa_email.getText().toString().matches(emailPattern)) {
                            Toast.makeText(State_Licensing_Form.this, "invalid email", Toast.LENGTH_SHORT).show();
                        } else {
                            setStateLicense();
                        }
                    }

                }

            }
        });


    }

    /**
     * Set State License
     * Calling API
     * Modified by phantom
     * On 03/22/24
     */
    private void setStateLicense() {
        final KProgressHUD progressDialog = KProgressHUD.create(State_Licensing_Form.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        Map<String, Object> postParam3= new HashMap<String, Object>();
        postParam3.put("firstName", firstNameET.getText().toString());
        postParam3.put("lastName", lastNameET.getText().toString());
        postParam3.put("email", emailET.getText().toString());
        postParam3.put("dateOfBirth", chnage_date_format_Old(DOB_TV.getText().toString()));
        postParam3.put("vocRecipient", recipientArrayList.get(voc_recipientSpinner.getSelectedItemPosition()).getId());

        if (state_medical_board_layout.getVisibility() == View.VISIBLE) {
            postParam3.put("state", state_ArrayList.get(stateSpinner.getSelectedItemPosition()).getId());
            postParam3.put("credentialingSpecialist", credential_spec.getText().toString());
            postParam3.put("recipientEmail", recipient_email.getText().toString());
        }
        if (employer_layout.getVisibility() == View.VISIBLE) {
            postParam3.put("employerName", emp_name.getText().toString());
            postParam3.put("employerContact", empl_contact.getText().toString());
            postParam3.put("recipientEmail", emp_email.getText().toString());
        }
        if (personal_usage_layout.getVisibility() == View.VISIBLE) {
            postParam3.put("recipientEmail", caa_email.getText().toString());
        }

        JSONObject object = new JSONObject(postParam3);

        Log.e("getJson", object.toString() + "--");


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, session.BASEURL + "users/me/licensing", object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressDialog.dismiss();

                        Log.e("licenceForm", response.toString());

                        try {

                            String receiptId = response.getString("receiptId");
                            // Toast.makeText(State_Licensing_Form.this, "Your message has been sent to NCCAA. Thank You", Toast.LENGTH_LONG).show();

                            // Create the payment screen if setting of state licensing form is successful
                            startActivity(new Intent(State_Licensing_Form.this, Add_Credit_Card_Payment.class)
                                    .putExtra("receiptId", receiptId)
                                    .putExtra("from", "stateLicensing"));
                            finish();

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

             /*   String json = new String(error.networkResponse.data);
                json = session.trimMessage(json, "error");*/

                Toast.makeText(State_Licensing_Form.this, "This message could not be sent\n" +
                        "Either try again or send NCCAA an email shown below", Toast.LENGTH_LONG).show();

                if (error instanceof ServerError) {

                }
                else if (error instanceof TimeoutError) {
                    Toast.makeText(State_Licensing_Form.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                }
                else if (error instanceof NetworkError) {
                    Toast.makeText(State_Licensing_Form.this, "Bad Network Connection", Toast.LENGTH_LONG).show();
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


}