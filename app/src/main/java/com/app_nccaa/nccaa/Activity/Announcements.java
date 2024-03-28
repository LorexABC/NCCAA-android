package com.app_nccaa.nccaa.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.app_nccaa.nccaa.Adapter.AdapterAnnouncement;
import com.app_nccaa.nccaa.Model.AnnouncementsModel;
import com.app_nccaa.nccaa.Utils.UserSession;
import com.app_nccaa.nccaa.Utils.VolleyMultipartRequest;
import com.app_nccaa.nccaa.R;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Announcements extends AppCompatActivity {

    private RecyclerView recAnnouncement;
    private AdapterAnnouncement adapterAnnouncement;

    private UserSession session;
    private ArrayList<AnnouncementsModel> announcementsModels = new ArrayList<>();

    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);

        session = new UserSession(Announcements.this);


        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        recAnnouncement = findViewById(R.id.recAnnouncement);
        recAnnouncement.setLayoutManager(new LinearLayoutManager(Announcements.this));
        adapterAnnouncement = new AdapterAnnouncement(Announcements.this, announcementsModels, new AdapterAnnouncement.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {

            }
        });
        recAnnouncement.setAdapter(adapterAnnouncement);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        // SetOnRefreshListener on SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                getAnnouncementList();
            }
        });

        getAnnouncementList();

    }


    private void getAnnouncementList() {
        final KProgressHUD progressDialog = KProgressHUD.create(Announcements.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET, session.BASEURL + "announcements",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        announcementsModels.clear();
                        progressDialog.dismiss();

                        try {
                            Log.e("announcementsData", new String(response.data) + "--");


                            JSONArray jsonArray = new JSONArray(new String(response.data));


                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                AnnouncementsModel announcementsModel = new AnnouncementsModel();
                                announcementsModel.setId(jsonObject.getString("id"));
                                announcementsModel.setSubject(jsonObject.getString("subject"));
                                announcementsModel.setText(jsonObject.getString("text"));

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = sdf.parse(jsonObject.getString("date"));
                                sdf = new SimpleDateFormat("MM/dd/yyyy");
                                String yourFormatedDateString = sdf.format(date);

                                announcementsModel.setDate(yourFormatedDateString);


                                announcementsModels.add(announcementsModel);
                            }

                            adapterAnnouncement.notifyDataSetChanged();


                        } catch (Exception e) {

                            Toast.makeText(Announcements.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(Announcements.this, json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                                //Additional cases
                            }
                        }
                        else if (error instanceof TimeoutError)
                            Toast.makeText(Announcements.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(Announcements.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

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

        Volley.newRequestQueue(Announcements.this).add(volleyMultipartRequest);
    }




}