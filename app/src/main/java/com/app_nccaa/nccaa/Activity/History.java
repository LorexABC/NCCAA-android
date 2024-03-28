package com.app_nccaa.nccaa.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import com.app_nccaa.nccaa.Model.CDQModel;
import com.app_nccaa.nccaa.Model.CERTModel;
import com.app_nccaa.nccaa.Model.CMEModel;
import com.app_nccaa.nccaa.Model.RESULTModel;
import com.app_nccaa.nccaa.Utils.UserSession;
import com.app_nccaa.nccaa.Utils.VolleyMultipartRequest;
import com.app_nccaa.nccaa.Adapter.HistoryAdapter;
import com.app_nccaa.nccaa.R;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class History extends AppCompatActivity {


    private RecyclerView recListNotification;
    private HistoryAdapter adapterNotification;
    private ArrayList<Object> notificatioModelArrayList = new ArrayList<>();
    private UserSession session;

    private TextView noHistoryTV;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        session = new UserSession(History.this);

        noHistoryTV = findViewById(R.id.noHistoryTV);


        recListNotification = findViewById(R.id.recListNotification);
        recListNotification.setLayoutManager(new LinearLayoutManager(this));
        adapterNotification = new HistoryAdapter(this, notificatioModelArrayList, new HistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int item, String type, String id) {


                if (type.equals("result")){
                    RESULTModel model = (RESULTModel) notificatioModelArrayList.get(item);

                    if (!model.getResults().equals("WAITING ON RESULTS")) {
                        startActivity(new Intent(History.this, Results_Score.class)
                                .putExtra("examID", id));
                    } else {
                        Toast.makeText(History.this, "Result not available", Toast.LENGTH_SHORT).show();
                    }

                    Log.e("resultsID", model.getId() + "--" + model.getResults());

                } else if (type.equals("cme")){
                    startActivity(new Intent(History.this, Receipt.class)
                            .putExtra("receiptId", id)
                            .putExtra("from", "history_cme"));
                } else {
                    startActivity(new Intent(History.this, Receipt.class)
                            .putExtra("receiptId", id)
                            .putExtra("from", "history_rest"));
                }

            }
        });
        recListNotification.setAdapter(adapterNotification);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        // SetOnRefreshListener on SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                getHistory();
            }
        });

        getHistory();

    }


    private void getHistory() {
        final KProgressHUD progressDialog = KProgressHUD.create(History.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET, session.BASEURL + "users/me/history",
                new Response.Listener<NetworkResponse>() {
                    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
                    @Override
                    public void onResponse(NetworkResponse response) {

                        progressDialog.dismiss();
                        notificatioModelArrayList.clear();

                        try {
                            Log.e("examsList", new String(response.data) + "--");

                            JSONArray jsonArray = new JSONArray(new String(response.data));

                            for (int i = 0 ; i<jsonArray.length() ; i++){
                                JSONObject object = jsonArray.getJSONObject(i);

                                if(object.getString("type").equals("cdq")){
                                    if (!session.getUSER_TYPE().equals("student")) {
                                        CDQModel model = new CDQModel();
                                        model.setId(object.getString("id"));
                                        model.setType(object.getString("type"));
                                        model.setExamDateStart(object.getString("examDateStart"));
                                        model.setExamDateEnd(object.getString("examDateEnd"));
                                        model.setPaidDate(object.getString("paidDate"));
                                        model.setPaidAmount(object.getString("paidAmount"));
                                        model.setPaymentPeriod(object.getString("paymentPeriod"));
                                        model.setAttemptNumber(object.getString("attemptNumber"));
                                        model.setReceiptId(object.getString("receiptId"));
                                        notificatioModelArrayList.add(model);
                                    }

                                }else if(object.getString("type").equals("cme")){
                                    if (!session.getUSER_TYPE().equals("student")) {
                                        CMEModel model = new CMEModel();
                                        model.setId(object.getString("id"));
                                        model.setType(object.getString("type"));
                                        model.setCycle(object.getString("cycle"));
                                        model.setDueDate(object.getString("dueDate"));
                                        model.setPaidDate(object.getString("paidDate"));
                                        model.setPaidAmount(object.getString("paidAmount"));
                                        model.setPaymentPeriod(object.getString("paymentPeriod"));
                                        model.setReceiptId(object.getString("receiptId"));
                                        notificatioModelArrayList.add(model);
                                    }

                                }else if(object.getString("type").equals("result")){

                                    if (!session.getUSER_TYPE().equals("student") && object.getString("examType").equals("cdq")) {
                                        RESULTModel model = new RESULTModel();
                                        model.setId(object.getString("id"));
                                        model.setType(object.getString("type"));
                                        model.setExamType(object.getString("examType"));
                                        model.setDate(object.getString("date"));
                                        model.setResults(object.getString("results"));
                                        model.setExamId(object.getString("examId"));
                                        notificatioModelArrayList.add(model);
                                    } else if (session.getUSER_TYPE().equals("student") && object.getString("examType").equals("cert")){
                                        RESULTModel model = new RESULTModel();
                                        model.setId(object.getString("id"));
                                        model.setType(object.getString("type"));
                                        model.setExamType(object.getString("examType"));
                                        model.setDate(object.getString("date"));
                                        model.setResults(object.getString("results"));
                                        model.setExamId(object.getString("examId"));
                                        notificatioModelArrayList.add(model);
                                    }

                                }else if(object.getString("type").equals("cert")){
                                    if (session.getUSER_TYPE().equals("student")) {
                                        CERTModel model = new CERTModel();
                                        model.setId(object.getString("id"));
                                        model.setType(object.getString("type"));
                                        model.setExamDateStart(object.getString("examDateStart"));
                                        model.setExamDateEnd(object.getString("examDateEnd"));
                                        model.setPaidDate(object.getString("paidDate"));
                                        model.setPaidAmount(object.getString("paidAmount"));
                                        model.setPaymentPeriod(object.getString("paymentPeriod"));
                                        model.setAttemptNumber(object.getString("attemptNumber"));
                                        model.setUniversityName(object.getString("universityName"));
                                        model.setUniversityCode(object.getString("universityCode"));
                                        model.setReceiptId(object.getString("receiptId"));
                                        notificatioModelArrayList.add(model);
                                    }

                                }

                            }

                            if (notificatioModelArrayList.isEmpty()){
                                noHistoryTV.setVisibility(View.VISIBLE);
                                recListNotification.setVisibility(View.GONE);
                            }

                            adapterNotification.notifyDataSetChanged();

                        } catch (Exception e) {
                            Toast.makeText(History.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(History.this, json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                                //Additional cases
                            }
                        }
                        else if (error instanceof TimeoutError)
                            Toast.makeText(History.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(History.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

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

        Volley.newRequestQueue(History.this).add(volleyMultipartRequest);
    }



}