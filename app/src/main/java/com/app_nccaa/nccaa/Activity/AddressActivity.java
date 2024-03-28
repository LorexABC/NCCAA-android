package com.app_nccaa.nccaa.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app_nccaa.nccaa.Adapter.SelectCitySpinner;
import com.app_nccaa.nccaa.Model.CityModel;
import com.app_nccaa.nccaa.R;

import java.util.ArrayList;

public class AddressActivity extends AppCompatActivity {


    private EditText etHomeAddressId;
    private EditText etCityId;
    private EditText etZipId;
    private ArrayList<CityModel> state_ArrayList = new ArrayList<>();
    private Spinner stateSpinner;
    private String mState = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);



        final String first_name;
        final String middle_name;
        final String last_name;
        final String cell_phone;
        final String dob;
        final String gender;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                first_name = null;
                middle_name = null;
                last_name = null;
                cell_phone = null;
                dob = null;
                gender = null;
            } else {
                first_name = extras.getString("first_name");
                middle_name = extras.getString("middle_name");
                last_name = extras.getString("last_name");
                cell_phone = extras.getString("cell_phone");
                dob = extras.getString("dob");
                gender = extras.getString("gender");
            }
        } else {
            first_name = (String) savedInstanceState.getSerializable("first_name");
            middle_name = (String) savedInstanceState.getSerializable("middle_name");
            last_name = (String) savedInstanceState.getSerializable("last_name");
            cell_phone = (String) savedInstanceState.getSerializable("cell_phone");
            dob = (String) savedInstanceState.getSerializable("dob");
            gender = (String) savedInstanceState.getSerializable("gender");
        }

        etHomeAddressId = findViewById(R.id.etHomeAddressId);
        etCityId = findViewById(R.id.etCityId);
        etZipId = findViewById(R.id.etZipId);
        stateSpinner = findViewById(R.id.stateSpinner);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

        SelectCitySpinner adapter8 = new SelectCitySpinner(AddressActivity.this, android.R.layout.simple_spinner_item, state_ArrayList);
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


        findViewById(R.id.btnContinueId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(etHomeAddressId.getText().toString().isEmpty()){
                    Toast.makeText(AddressActivity.this, "Please Enter Home Street Address", Toast.LENGTH_SHORT).show();
                }else if(etCityId.getText().toString().isEmpty()){
                    Toast.makeText(AddressActivity.this, "Please Enter City", Toast.LENGTH_SHORT).show();
                }else if(etZipId.getText().toString().isEmpty() || etZipId.getText().toString().length() <= 4){
                    Toast.makeText(AddressActivity.this, "Please Enter ZIP", Toast.LENGTH_SHORT).show();
                }else if(mState.isEmpty()){
                    Toast.makeText(AddressActivity.this, "Please Select State", Toast.LENGTH_SHORT).show();

                }else {
                    startActivity(new Intent(AddressActivity.this, CaaInfoActivity.class)
                            .putExtra("first_name",first_name)
                            .putExtra("middle_name",middle_name)
                            .putExtra("last_name",last_name)
                            .putExtra("cell_phone",cell_phone)
                            .putExtra("dob",dob)
                            .putExtra("gender",gender)
                            .putExtra("address",etHomeAddressId.getText().toString())
                            .putExtra("city",etCityId.getText().toString())
                            .putExtra("zip",etZipId.getText().toString())
                            .putExtra("state",mState)
                            .putExtra("token", getIntent().getStringExtra("token"))
                    );
                }

            }
        });


    }


}