package com.app_nccaa.nccaa.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.android.volley.toolbox.Volley;
import com.app_nccaa.nccaa.Adapter.AdapterHome;
import com.app_nccaa.nccaa.Utils.UserSession;
import com.app_nccaa.nccaa.Utils.VolleyMultipartRequest;
import com.app_nccaa.nccaa.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * Home Screen
 *
 * Modified by phantom
 * On 03/22/24
 * Roles and permissions are modified in this section
 */
public class HomeScreen extends AppCompatActivity {

    private AdapterHome adapterHome;
    private RecyclerView recItems;

    private UserSession session;
    private RequestQueue mRequestqueue;

    private TextView nameUser;

    private int[] imagesHome = {R.drawable.ic_announcements, R.drawable.ic_important_infodates, R.drawable.ic_student_clinical, R.drawable.ic_certification_exam,
            R.drawable.ic_state, R.drawable.ic_view_caa_certificate, R.drawable.ic_cdq, R.drawable.ic_cme_submissions,
            R.drawable.ic_blog, R.drawable.ic_history, R.drawable.ic_edit, R.drawable.ic_contact};
    private String[] titleHome = {"Announcements", "Important \n" + "Info/Dates", "Student Clinical \n" + "Competencies", "Certification \n" + "Exam",
            "State \n" + "Licensing Info", "View CAA \n" + "Certificate", "CDQ \n" + "Exam", "CME \n" + "Submissions", "Blog", "History", "Edit \n" + "Profile",
            "<p><span style=\"color: #272F3E ;\">Contact <br/>NC</span><span style=\"color: #DF5D58 ;\">CAA</span></p>"};

    // "<p><span style=\"color: #272F3E ;\">Contact <br/>NC</span><span style=\"color: #DF5D58 ;\">CAA</span></p>"
    // "Contact \nNCCAA"

    private int[] imagesHomeStdnt = {R.drawable.ic_announcements, R.drawable.ic_important_infodates, R.drawable.ic_student_clinical,
            R.drawable.ic_state, R.drawable.ic_view_caa_certificate, R.drawable.ic_cdq, R.drawable.ic_cme_submissions,
            R.drawable.ic_blog, R.drawable.ic_history, R.drawable.ic_edit, R.drawable.ic_contact};
    private String[] titleHomeStdnt = {"Announcements", "Important \n" + "Info/Dates", "Student Clinical \n" + "Competencies",
            "State \n" + "Licensing Info", "View CAA \n" + "Certificate", "CDQ \n" + "Exam", "CME \n" + "Submissions", "Blog", "History", "Edit \n" + "Profile",
            "<p><span style=\"color: #272F3E ;\">Contact <br/>NC</span><span style=\"color: #DF5D58 ;\">CAA</span></p>"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        session = new UserSession(HomeScreen.this);
        mRequestqueue = Volley.newRequestQueue(HomeScreen.this);

        Log.e("token", session.getAPITOKEN());

        /*BiometricManager biometricManager = BiometricManager.from(this);
        if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS) {
            attemptBiometricAuth();
        } else {
            Toast.makeText(this, "No Biometric Sensor available/registered", Toast.LENGTH_SHORT).show();
        }*/

        nameUser = findViewById(R.id.nameUser);


        recItems = findViewById(R.id.recItems);
        recItems.setLayoutManager(new GridLayoutManager(HomeScreen.this, 2));
        adapterHome = new AdapterHome(HomeScreen.this, titleHome, imagesHome, new AdapterHome.OnItemClickListener() {
            /**
             * All permissions are here
             *
             * @param pos
             *
             * Modified by phantom
             * On 03/22/24
             */
            @Override
            public void onItemClick(int pos) {

                if (pos == 0) {
                    startActivity(new Intent(HomeScreen.this, Announcements.class));
                } else if (pos == 1) {
                    startActivity(new Intent(HomeScreen.this, Important_Info_Dates.class));
                } else if (pos == 2) {
                    if (!session.getUSER_TYPE().equals("student")) {
                        Toast.makeText(HomeScreen.this, "This module is not available to CAAs", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(new Intent(HomeScreen.this, ClinicalCompetenciesActivity.class));
//                        Toast.makeText(HomeScreen.this, "The Clinical Competency module is presently not accessible for your class.\n However, it will become available in future administrations.", Toast.LENGTH_SHORT).show();
                    }
                } else if (pos == 3) {
                    if (session.getUSER_TYPE().equals("student")) {
                        startActivity(new Intent(HomeScreen.this, Certificate_Exam.class));
                    } else {
                        Toast.makeText(HomeScreen.this, "This module is not available to CAAs", Toast.LENGTH_SHORT).show();
                    }
                } else if (pos == 4) {
                    if (session.getUSER_TYPE().equals("student")) {
                        Toast.makeText(HomeScreen.this, "This module is not available for students.", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(new Intent(HomeScreen.this, State_Licensing_Form.class));
                    }
                } else if (pos == 5) {
                    if (session.getUSER_TYPE().equals("student")) {
                        Toast.makeText(HomeScreen.this, "This module is not available for students.", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(new Intent(HomeScreen.this, View_CAA_Certificate.class));
                    }
                } else if (pos == 6) {
                    if (session.getUSER_TYPE().equals("student")) {
                        Toast.makeText(HomeScreen.this, "This module is not available for student", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(new Intent(HomeScreen.this, CDQ_Exam.class));
                    }
                } else if (pos == 7) {
                    if (session.getUSER_TYPE().equals("student")) {
                        Toast.makeText(HomeScreen.this, "This module is not available for students.", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(new Intent(HomeScreen.this, CME_Submissions.class));
                    }
                } else if (pos == 8) {
                    startActivity(new Intent(HomeScreen.this, Blog.class));
                } else if (pos == 9) {
                    startActivity(new Intent(HomeScreen.this, History.class));
                } else if (pos == 10) {
                    startActivity(new Intent(HomeScreen.this, EditProfile.class));
                } else if (pos == 11) {
                    startActivity(new Intent(HomeScreen.this, Contact_Us.class));
                }

            }
        });
        recItems.setAdapter(adapterHome);


        findViewById(R.id.notifications).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeScreen.this, Announcements.class));
            }
        });



        findViewById(R.id.logOutIV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:

                                session.logout();
                                startActivity(new Intent(HomeScreen.this, LoginActivity.class));
                                finishAffinity();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreen.this);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        });



        getUsersPersonal();
        getUsersMe();
     //AppLockDialog();
    }

    private void AppLockDialog(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:

                        session.logout();
                        startActivity(new Intent(HomeScreen.this, LoginActivity.class));
                        finishAffinity();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreen.this);
        builder.setCancelable(false);
        builder.setMessage("We are sorry. Both the website and app are under going maintenance. Check back soon. Thanks you!").setPositiveButton("Okay", dialogClickListener).show();
    }


    private void attemptBiometricAuth() {
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt.AuthenticationCallback callback = getAuthenticationCallback();
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, callback);

        BiometricPrompt.PromptInfo promptInfo = getPromptInfo("Biometric Authentication", "Please login to get into the app",
                "This app is using Biometric Authentication to recognize user", true);
        biometricPrompt.authenticate(promptInfo);
    }

    /**
     * These are the detail info for prompt info
     *
     * @param title                     prompt title
     * @param subtitle                  prompt subtitle
     * @param description               prompt description
     * @param isDeviceCredentialAllowed should it use
     * @return
     */
    private BiometricPrompt.PromptInfo getPromptInfo(String title, String subtitle, String description, boolean isDeviceCredentialAllowed) {

        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setSubtitle(subtitle)
                .setDescription(description)
                .setDeviceCredentialAllowed(isDeviceCredentialAllowed)
                .build();
    }

    private BiometricPrompt.AuthenticationCallback getAuthenticationCallback() {
        return new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                Toast.makeText(HomeScreen.this, errString.toString(), Toast.LENGTH_SHORT).show();
                super.onAuthenticationError(errorCode, errString);
                finish();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                recreate();
            }
        };
    }


    private void getUsersPersonal() {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET,
                session.BASEURL + "users/me/personal",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        try {
                            JSONObject jsonObject = new JSONObject(new String(response.data));

                            if (!jsonObject.getString("graduationYear").equals("null")) {
                                session.setGRADUATION_YEAR(jsonObject.getString("graduationYear"));
                            }
                            if (!jsonObject.getString("universityId").equals("null")) {
                                session.setUNIVERSITY_ID(jsonObject.getString("universityId"));
                            }


                        } catch (Exception e) {

                            Toast.makeText(HomeScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(HomeScreen.this, json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                            }
                        }
                        else if (error instanceof TimeoutError)
                            Toast.makeText(HomeScreen.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(HomeScreen.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

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

        Volley.newRequestQueue(HomeScreen.this).add(volleyMultipartRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUsersMe();
    }

    private void getUsersMe() {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET, session.BASEURL + "users/me",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        try {
                            Log.e("users_me", new String(response.data) + "--");
                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            if(jsonObject.getString("isAppLocked").equals("1")){
                                AppLockDialog();
                            }

                            session.setUSER_ID(jsonObject.getString("id"));
                            session.setFIRST_NAME(jsonObject.getString("firstName"));
                            session.setMIDDLE_NAME(jsonObject.getString("middleName"));
                            session.setLAST_NAME(jsonObject.getString("lastName"));

                            if (session.getFIRST_NAME().length() > 0) {
                                nameUser.setText("Hello, " + session.getFIRST_NAME().substring(0, 1).toUpperCase() + session.getFIRST_NAME().substring(1).toLowerCase() + "!");
                            }
                        } catch (Exception e) {

                            Toast.makeText(HomeScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(HomeScreen.this, json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                            }
                        }
                        else if (error instanceof TimeoutError)
                            Toast.makeText(HomeScreen.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(HomeScreen.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

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

        Volley.newRequestQueue(HomeScreen.this).add(volleyMultipartRequest);
    }

    

}