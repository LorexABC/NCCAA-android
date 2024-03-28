package com.app_nccaa.nccaa.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.androidnetworking.interfaces.BitmapRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.app_nccaa.nccaa.Adapter.AdapterCreditHistory;
import com.app_nccaa.nccaa.Adapter.SelectCitySpinner;
import com.app_nccaa.nccaa.Model.CME_HistoryModel;
import com.app_nccaa.nccaa.Model.CityModel;
import com.app_nccaa.nccaa.Utils.UserSession;
import com.app_nccaa.nccaa.Utils.VolleyMultipartRequest;
import com.app_nccaa.nccaa.R;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * CME module Activity
 *
 * Modified by phantom
 * On 03/22/24
 * Date format and fractions feature is updated.
 */
public class CME_Submissions extends AppCompatActivity {


    private TextView examTextView, add_CME_Btn, subADD_CME, receiptPaidBtn;
    private ArrayList<CityModel> recipientArray = new ArrayList<>();

    private ArrayList<CME_HistoryModel> historyArrayList = new ArrayList<>();

    private Spinner recipientspinner;

    private AdapterCreditHistory adapterCreditHistory;

    private RecyclerView recCreditHistory;
    private SelectCitySpinner adapter1;

    private UserSession session;

    private SharedPreferences sharedPreferences;

    private TextView titleCycleMain, histryCyTV, nameCreditTVBlue, nameCreditTVOrng;
    private TextView credNeedBlue, credCompletedBlue, credStillBlue, anethesiaCredBlue;
    private TextView credNeedOrng, credCompletedOrng, credStillOrng, anethesiaCredOrng;

    private LinearLayout layoutCycleBlue, layoutCycleOrng, cycleMainLayout;

    private String strRecipient, strIsCurrent;
    private String mYear = "";
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cme_submissions);

        session = new UserSession(this);


        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sharedPreferences = getSharedPreferences("recipientSpinner", MODE_PRIVATE);

        examTextView = findViewById(R.id.examTextView);
        add_CME_Btn = findViewById(R.id.add_CME_Btn);
        recCreditHistory = findViewById(R.id.recCreditHistory);

        histryCyTV = findViewById(R.id.histryCyTV);
        titleCycleMain = findViewById(R.id.titleCycleMain);
        nameCreditTVBlue = findViewById(R.id.nameCreditTVBlue);
        nameCreditTVOrng = findViewById(R.id.nameCreditTVOrng);

        credNeedBlue = findViewById(R.id.credNeedBlue);
        credCompletedBlue = findViewById(R.id.credCompletedBlue);
        credStillBlue = findViewById(R.id.credStillBlue);
        anethesiaCredBlue = findViewById(R.id.anethesiaCredBlue);

        credNeedOrng = findViewById(R.id.credNeedOrng);
        credCompletedOrng = findViewById(R.id.credCompletedOrng);
        credStillOrng = findViewById(R.id.credStillOrng);
        anethesiaCredOrng = findViewById(R.id.anethesiaCredOrng);

        layoutCycleBlue = findViewById(R.id.layoutCycleBlue);
        layoutCycleOrng = findViewById(R.id.layoutCycleOrng);
        cycleMainLayout = findViewById(R.id.cycleMainLayout);

        subADD_CME = findViewById(R.id.subADD_CME);
        receiptPaidBtn = findViewById(R.id.receiptPaidBtn);

        recipientspinner = findViewById(R.id.voc_recipientSpinner);

        examTextView.setText(noTrailingwhiteLines(Html.fromHtml("<p><span style=\"color: #6c727f;\">CMEs are managed on this page. The default CME cycle will always show the current cycle; going forward, all documents you have uploaded into your account will become part of your permanent record. If you do not see previous cycles, we do not have sufficient documents since moving from paper submissions to digital submissions. We have streamlined CME Submissions for both the desktop and mobile versions. Simply click on Add CME and upload the actual certificate or document. The document must display all the required information, enabling us to verify the practitioner name, dates, titles, accrediting body, and earned CMEs. For \"credits,\" you may enter EITHER anesthesia or other credit hours, not both within the same document. If you enter hours into the anesthesia box, please leave the other box blank (no zeros or N/A needed). Likewise with the other box. If you have a document which includes both types of credits, please upload the document twice. </span></p>\n" +
                "<p><span style=\"color: #6c727f;\">Although CME documents cannot be modified or deleted after submission, please make sure you upload only the CME credits you want counted. If you need a particular document deleted from the system, please email us with at mailto:contact@nccaa.org with specific details of the document you want deleted from your account. Once submitted, we will keep track of your hours earned and documents, wherein you can review 24/7 on both desktop and mobile. The Pay Now button above will activate once you have entered 50 approved hours in the system (minimum 40 Anesthesia, plus 10 Other Medical Related; or 50 total Anesthesia hours) and have uploaded all necessary CME documents or certificates. </span></p>\n" +
                "<p><span style=\"color: #6c727f;\">Any issues during the CME submission process should be reported to <strong>contact@nccaa.org</strong>. Please include very specific details, such as, type of device, browser, operating system, etc. The regular CME submission fee is $295.00 before the June 1st deadline, and $895.00 between June 2 - Aug. 31. </span></p>")));


     /*   examTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CME_Submissions.this, More_Info_Cert_Exam.class)
                        .putExtra("from", "cme"));
            }
        });
*/


        add_CME_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (recipientArray.get(recipientspinner.getSelectedItemPosition()).getIsCurrent().equals("true")) {
                    startActivityForResult(new Intent(CME_Submissions.this, Add_CME.class)
                            .putExtra("mYear", mYear)
                            .putExtra("strRecipient", strRecipient)
                            .putExtra("strIsCurrent", strIsCurrent), 22);
                } else {
                    Toast.makeText(CME_Submissions.this, "Not allow to add", Toast.LENGTH_SHORT).show();
                }

            }
        });

        subADD_CME.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (recipientArray.get(recipientspinner.getSelectedItemPosition()).getIsCurrent().equals("true")) {
                    startActivityForResult(new Intent(CME_Submissions.this, Add_CME.class)
                            .putExtra("mYear", mYear)
                            .putExtra("strRecipient", strRecipient)
                            .putExtra("strIsCurrent", strIsCurrent), 22);
                } else {
                    Toast.makeText(CME_Submissions.this, "Not allow to add", Toast.LENGTH_SHORT).show();
                }

            }
        });

        receiptPaidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("getInfoPaid", recipientArray.get(recipientspinner.getSelectedItemPosition()).getReceiptPaid() + "--");
                if (recipientArray.get(recipientspinner.getSelectedItemPosition()).getReceiptPaid().equals("false")) {
                    startActivity(new Intent(CME_Submissions.this, Add_Credit_Card_Payment.class)
                            .putExtra("receiptId", recipientArray.get(recipientspinner.getSelectedItemPosition()).getReceiptId()));
                }

            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        // SetOnRefreshListener on SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                getCreditCycles();
            }
        });


    }


    /**
     * Get rid of Entries from entry id
     * Calling API
     * @param id
     *
     * Modified by phantom
     * On 03/22/24
     */
    private void deleteEntries(String id) {
        final KProgressHUD progressDialog = KProgressHUD.create(CME_Submissions.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.DELETE,
                session.BASEURL + "users/me/cmeCycles/" + mYear + "/entries/" + id,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        progressDialog.dismiss();

                        try {
                            Log.e("casesList", new String(response.data) + "--");


                            JSONObject object = new JSONObject(new String(response.data));

                   //         getHistory(strRecipient, strIsCurrent);
                            getCreditCycles();

                        } catch (Exception e) {
                            Toast.makeText(CME_Submissions.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(CME_Submissions.this, json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                                //Additional cases
                            }
                        }
                        else if (error instanceof TimeoutError)
                            Toast.makeText(CME_Submissions.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(CME_Submissions.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

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

        Volley.newRequestQueue(CME_Submissions.this).add(volleyMultipartRequest);
    }


    /**
     * Get Cycles
     * Calling API
     *
     * Modified by phantom
     * On 03/22/24
     */
    private void getCreditCycles() {
        final KProgressHUD progressDialog = KProgressHUD.create(CME_Submissions.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET,
                session.BASEURL + "users/me/cmeCycles",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        progressDialog.dismiss();
                        recipientArray.clear();

                        try {
                            Log.e("cmeCycles", new String(response.data) + "--");


                            JSONArray array = new JSONArray(new String(response.data));

                            for (int i = 0; i < array.length(); i++){
                                JSONObject jsonObject = array.getJSONObject(i);

                                CityModel model = new CityModel();
                                if(i == 0){
                                    mYear = jsonObject.getString("cycle");
                                }
                                int full_cycle = (Integer.parseInt(jsonObject.getString("cycle")) - 2);
                                model.setCycle(jsonObject.getString("cycle"));

                                model.setName("CME Credit Cycle (" + full_cycle + "-" + jsonObject.getString("cycle") + ")");
                                model.setCreditsNeeded(jsonObject.getString("creditsNeeded"));
                                model.setCreditsCompleted(jsonObject.getString("creditsCompleted"));
                                model.setCreditsLeft(jsonObject.getString("creditsLeft"));
                                model.setAnesthesiaCreditsLeft(jsonObject.getString("anesthesiaCreditsLeft"));
                                model.setIsCurrent(jsonObject.getString("isCurrent"));
                                model.setReceiptId(jsonObject.getString("receiptId"));
                                model.setReceiptPaid(jsonObject.getString("receiptPaid"));
                                recipientArray.add(model);
                            }

                            CityModel model = new CityModel();
                            model.setName("Select CME Credit Cycle");
                            model.setCycle("Select CME Credit Cycle");
                            model.setCreditsNeeded("");
                            model.setCreditsCompleted("");
                            model.setCreditsLeft("");
                            model.setAnesthesiaCreditsLeft("");
                            model.setIsCurrent("");
                            model.setReceiptId("");
                            model.setReceiptPaid("");
                            recipientArray.add(model);

                            adapter1 = new SelectCitySpinner(CME_Submissions.this, android.R.layout.simple_spinner_item, recipientArray);
                            adapter1.setDropDownViewResource(android.R.layout.simple_list_item_1);
                            recipientspinner.setAdapter(adapter1);

                            for (int i = 0; i < recipientArray.size(); i++){
                                if (recipientArray.get(i).getIsCurrent().equals("true")){
                                    recipientspinner.setSelection(i);
                                }
                            }


                            recipientspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putInt("spinnerSelection", position);
                                    editor.apply();

                                    if (position == (recipientArray.size() - 1)) {
                                        ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_gray_light));
                                    } else {


                                        Log.e("getErrorData", recipientArray.get(position).getCreditsCompleted() + "--"
                                        + recipientArray.get(position).getCreditsNeeded());

                                        if (Float.parseFloat(recipientArray.get(position).getCreditsCompleted())
                                                >= Float.parseFloat(recipientArray.get(position).getCreditsNeeded())){
                                            receiptPaidBtn.setVisibility(View.VISIBLE);
                                        } else {
                                            receiptPaidBtn.setVisibility(View.GONE);
                                        }

                                        if (Integer.parseInt(recipientArray.get(position).getReceiptId()) > 0){
                                            receiptPaidBtn.setText("Pay Now");
                                        } else {
                                            if (recipientArray.get(position).getReceiptPaid().equals("false")){
                                                subADD_CME.setVisibility(View.VISIBLE);
                                                add_CME_Btn.setVisibility(View.VISIBLE);
                                                receiptPaidBtn.setText("Pay Now");
                                            } else {
                                                subADD_CME.setVisibility(View.GONE);
                                                add_CME_Btn.setVisibility(View.GONE);
                                                receiptPaidBtn.setText("Paid");
                                            }
                                        }

                                        int full_cycle = (Integer.parseInt(recipientArray.get(position).getCycle()) - 2);

                                        nameCreditTVBlue.setText("Credit needed for this cycle (" + full_cycle + "-" + recipientArray.get(position).getCycle() + ")");
                                        nameCreditTVOrng.setText("Credit needed for this cycle (" + full_cycle + "-" + recipientArray.get(position).getCycle() + ")");
                                        titleCycleMain.setText("Credits This Cycle (" + full_cycle + "-" + recipientArray.get(position).getCycle() + ")");
                                        histryCyTV.setText("CME History (" + full_cycle + "-" + recipientArray.get(position).getCycle() + ")");

                                        credNeedBlue.setText(recipientArray.get(position).getCreditsNeeded());
                                        credNeedOrng.setText(recipientArray.get(position).getCreditsNeeded());

                                        credCompletedBlue.setText("" + new DecimalFormat("#00.00").format(Float.parseFloat(recipientArray.get(position).getCreditsCompleted())));
                                        credCompletedOrng.setText("" + new DecimalFormat("#00.00").format(Float.parseFloat(recipientArray.get(position).getCreditsCompleted())));

                                        credStillBlue.setText("" + new DecimalFormat("#00.00").format(Float.parseFloat(recipientArray.get(position).getCreditsLeft())));
                                        credStillOrng.setText("" + new DecimalFormat("#00.00").format(Float.parseFloat(recipientArray.get(position).getCreditsLeft())));

                                        anethesiaCredBlue.setText("" + new DecimalFormat("#00.00").format(Float.parseFloat(recipientArray.get(position).getAnesthesiaCreditsLeft())));
                                        anethesiaCredOrng.setText("" + new DecimalFormat("#00.00").format(Float.parseFloat(recipientArray.get(position).getAnesthesiaCreditsLeft())));

                                        cycleMainLayout.setVisibility(View.VISIBLE);

                                        if (recipientArray.get(position).getIsCurrent().equals("true")){
                                            cycleMainLayout.setBackground(getResources().getDrawable(R.drawable.blue_5dp));
                                            layoutCycleBlue.setVisibility(View.VISIBLE);
                                            layoutCycleOrng.setVisibility(View.GONE);
                                            add_CME_Btn.setBackground(getResources().getDrawable(R.drawable.blue_light_6dp));
                                            subADD_CME.setBackground(getResources().getDrawable(R.drawable.blue_light_6dp));
                                        } else {
                                            cycleMainLayout.setBackground(getResources().getDrawable(R.drawable.orange_light_5dp));
                                            layoutCycleBlue.setVisibility(View.GONE);
                                            layoutCycleOrng.setVisibility(View.VISIBLE);
                                            add_CME_Btn.setBackground(getResources().getDrawable(R.drawable.gray_light_6dp));
                                            subADD_CME.setBackground(getResources().getDrawable(R.drawable.gray_light_6dp));
                                        }

                                        mYear = recipientArray.get(position).getCycle();

                                        getHistory(recipientArray.get(position).getCycle(), recipientArray.get(position).getIsCurrent());

                                        strRecipient = recipientArray.get(position).getCycle();
                                        strIsCurrent = recipientArray.get(position).getIsCurrent();
                                    }

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                            int pos = sharedPreferences.getInt("spinnerSelection", 0);
                            recipientspinner.setSelection(pos);

                        } catch (Exception e) {
                            Toast.makeText(CME_Submissions.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(CME_Submissions.this, json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                                //Additional cases
                            }
                        }
                        else if (error instanceof TimeoutError)
                            Toast.makeText(CME_Submissions.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(CME_Submissions.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

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

        Volley.newRequestQueue(CME_Submissions.this).add(volleyMultipartRequest);
    }


    /**
     * Get History based on year
     * Calling API
     * @param year
     * @param isCurrent
     *
     * Modified by phantom
     * On 03/22/24
     */
    private void getHistory(String year, String isCurrent) {
        final KProgressHUD progressDialog = KProgressHUD.create(CME_Submissions.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET, session.BASEURL + "users/me/cmeCycles/" + year + "/entries",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        progressDialog.dismiss();
                        historyArrayList.clear();

                        try {
                            Log.e("entries", new String(response.data) + "--");

                            JSONArray array = new JSONArray(new String(response.data));

                            for (int i = 0; i < array.length(); i++){
                                JSONObject jsonObject = array.getJSONObject(i);

                                CME_HistoryModel model = new CME_HistoryModel();
                                model.setId(jsonObject.getString("id"));
                                model.setName(jsonObject.getString("name"));
                                model.setHours(jsonObject.getString("hours"));
                                model.setCmeProvider(jsonObject.getString("cmeProvider"));
                                String date = jsonObject.getString("dateSubmitted");
                                String _date = date.substring(0,19).replace("T"," ");
                                SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                SimpleDateFormat output = new SimpleDateFormat("dd/mm/yyyy (hh:mm aa)");

                                // Change date mode here.
                                try {
                                    Date oneWayTripDate = input.parse(_date);  // parse input
                                    model.setDateSubmitted(output.format(oneWayTripDate));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
//                                model.setDateSubmitted(_date);
                                model.setType(jsonObject.getString("type"));
                                model.setUploadId(jsonObject.getString("uploadId"));
                                model.setEditable(jsonObject.getString("editable"));
                                historyArrayList.add(model);
                            }

                            recCreditHistory.setLayoutManager(new LinearLayoutManager(CME_Submissions.this));
                            adapterCreditHistory = new AdapterCreditHistory(CME_Submissions.this, historyArrayList, isCurrent, new AdapterCreditHistory.OnItemClickListener() {
                                @Override
                                public void onItemUpdate(int pos) {

                                    if (historyArrayList.get(pos).getEditable().equals("true")){
                                        startActivityForResult(new Intent(CME_Submissions.this, Edit_CME.class)
                                                .putExtra("id", historyArrayList.get(pos).getId())
                                                .putExtra("mYear", mYear)
                                                .putExtra("name", historyArrayList.get(pos).getName())
                                                .putExtra("hours", historyArrayList.get(pos).getHours())
                                                .putExtra("dateSubmitted", historyArrayList.get(pos).getDateSubmitted())
                                                .putExtra("cmeProvider", historyArrayList.get(pos).getCmeProvider())
                                                .putExtra("type", historyArrayList.get(pos).getType())
                                                .putExtra("uploadId", historyArrayList.get(pos).getUploadId())
                                                .putExtra("strRecipient", strRecipient)


                                                .putExtra("strIsCurrent", strIsCurrent), 22
                                        );
                                    } else {
                                        Toast.makeText(CME_Submissions.this, "Not eligible!", Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onItemDelete(int pos) {
                                    if (historyArrayList.get(pos).getEditable().equals("true")){
                                        new AlertDialog.Builder(CME_Submissions.this)
//                                                .setTitle("Delete entry")
                                                .setMessage("Are you sure you want to delete this CME submission?")

                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        deleteEntries(historyArrayList.get(pos).getId());
                                                    }
                                                })

                                                // A null listener allows the button to dismiss the dialog and take no further action.
                                                .setNegativeButton(android.R.string.no, null)
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .show();
                                    } else {
                                        Toast.makeText(CME_Submissions.this, "Not eligible!", Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onItemView(int pos) {

                                    startActivity(new Intent(CME_Submissions.this, ViewDocument.class)
                                            .putExtra("uploadId", historyArrayList.get(pos).getUploadId()));

                                }
                            });
                            recCreditHistory.setAdapter(adapterCreditHistory);

                        } catch (Exception e) {
                            Toast.makeText(CME_Submissions.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
              //          progressDialog.dismiss();
                        if (error instanceof ServerError){

                            if(error.networkResponse != null && error.networkResponse.data != null){
                                switch(error.networkResponse.statusCode){
                                    case 500:
                                        String json = new String(error.networkResponse.data);
                                        json = session.trimMessage(json, "message");
                                        if(json != null) {
                                            Toast.makeText(CME_Submissions.this, json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                            }
                        }
                        else if (error instanceof TimeoutError)
                            Toast.makeText(CME_Submissions.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(CME_Submissions.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
             /*   params.put("username", userNameET.getText().toString());*/
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
        Volley.newRequestQueue(CME_Submissions.this).add(volleyMultipartRequest);
    }


    /**
     * Get rid of Trailing white lines.
     * @param text
     * @return result
     */
    private CharSequence noTrailingwhiteLines(CharSequence text) {

        while (text.charAt(text.length() - 1) == '\n') {
            text = text.subSequence(0, text.length() - 1);
        }
        return text;
    }


    @Override
    protected void onResume() {
        super.onResume();

        getCreditCycles();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == 22) {
                getHistory(data.getStringExtra("strRecipient"), data.getStringExtra("strIsCurrent"));
            }
        } else {

        }
    }


}