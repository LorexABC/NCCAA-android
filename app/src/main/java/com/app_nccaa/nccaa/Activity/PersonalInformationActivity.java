package com.app_nccaa.nccaa.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
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

import com.app_nccaa.nccaa.Adapter.SelectCitySpinner;
import com.app_nccaa.nccaa.Model.CityModel;
import com.app_nccaa.nccaa.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Personal Information Activity
 *
 * Modified by phantom
 * On 03/22/24
 * Capitalize feature is integrated in this field.
 */
public class PersonalInformationActivity extends AppCompatActivity {

    EditText etFirstNameId, etMiddleNameId, etLastNameId, etCellPhoneId;
    private static TextView dobet;

    private Spinner genderSpinner;
    private ArrayList<CityModel> gender_ArrayList = new ArrayList<>();
    private String mGender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });





        etFirstNameId = findViewById(R.id.etFirstNameId);
        etMiddleNameId = findViewById(R.id.etMiddleNameId);
        etLastNameId = findViewById(R.id.etLastNameId);
        etCellPhoneId = findViewById(R.id.etCellPhoneId);
        dobet = findViewById(R.id.dobet);
        genderSpinner = findViewById(R.id.genderSpinner);


//        findViewById(R.id.dobet).setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
////                view.setOnTouchListener(null);
//                DialogFragment newFragment = new DatePickerFragment();
//                newFragment.show(getSupportFragmentManager(), "datePicker");
//                return false;
//            }
//        });

        findViewById(R.id.dobet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
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

        SelectCitySpinner adapter3 = new SelectCitySpinner(this, android.R.layout.simple_spinner_item, gender_ArrayList);
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
                mGender = gender_ArrayList.get(position).getId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findViewById(R.id.btnContinueId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(etFirstNameId.getText().toString().isEmpty()){
                    Toast.makeText(PersonalInformationActivity.this, "Please Enter First Name", Toast.LENGTH_SHORT).show();
                } else if(etLastNameId.getText().toString().isEmpty()){
                    Toast.makeText(PersonalInformationActivity.this, "Please Enter Last Name", Toast.LENGTH_SHORT).show();
                } else if(etCellPhoneId.getText().toString().isEmpty() || etCellPhoneId.getText().toString().length() <= 9){
                    Toast.makeText(PersonalInformationActivity.this, "Please Enter Cell Phone", Toast.LENGTH_SHORT).show();
                } else if(dobet.getText().toString().isEmpty()){
                    Toast.makeText(PersonalInformationActivity.this, "Please Enter DOB", Toast.LENGTH_SHORT).show();
                } else if(mGender.isEmpty()){
                    Toast.makeText(PersonalInformationActivity.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(PersonalInformationActivity.this, AddressActivity.class)
                    .putExtra("first_name", etFirstNameId.getText().toString())
                    .putExtra("middle_name", etMiddleNameId.getText().toString())
                    .putExtra("last_name", etLastNameId.getText().toString())
                    .putExtra("cell_phone", etCellPhoneId.getText().toString())
                    .putExtra("dob", chnage_date_format_Old(dobet.getText().toString()))
                    .putExtra("gender", mGender)
                                    .putExtra("token", getIntent().getStringExtra("token"))
                    );
                }

            }
        });

    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int mYear, mMonth, mDay, mHour, mMinute;

            c.add(Calendar.YEAR,-18);
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, mYear, mMonth, mDay);
            dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            return  dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String dateString = dateFormat.format(calendar.getTime());

            dobet.setText(dateString);

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