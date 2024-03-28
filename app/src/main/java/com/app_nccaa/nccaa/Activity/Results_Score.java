package com.app_nccaa.nccaa.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.app_nccaa.nccaa.Adapter.Adapter_individualScores;
import com.app_nccaa.nccaa.Model.IndividualScores;
import com.app_nccaa.nccaa.Utils.UserSession;
import com.app_nccaa.nccaa.Utils.VolleyMultipartRequest;
import com.app_nccaa.nccaa.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Results_Score extends AppCompatActivity {


    private TextView mRS_Exam_result,mRS_Your_result,mRS_Min_Pass,mGT_Your_Score,mGT_Max_Score,mGT_Correct,mGT_National_Correct_1,mGT_National_Correct_2,mDate_title;
    private UserSession session;
    private ArrayList<IndividualScores> individualScoresArrayList = new ArrayList<>() ;
    private RecyclerView recBookingCenter;
    private Adapter_individualScores adapter_individualScores;
    private String examID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_score);

        session = new UserSession(Results_Score.this);



        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                examID = null;

            } else {
                examID = extras.getString("examID");

            }
        } else {
            examID = (String) savedInstanceState.getSerializable("examID");

        }

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mDate_title = findViewById(R.id.mDate_title);
        mRS_Exam_result = findViewById(R.id.mRS_Exam_result);
        mRS_Your_result = findViewById(R.id.mRS_Your_result);
        mRS_Min_Pass = findViewById(R.id.mRS_Min_Pass);

        mGT_Your_Score = findViewById(R.id.mGT_Your_Score);
        mGT_Max_Score = findViewById(R.id.mGT_Max_Score);
        mGT_Correct = findViewById(R.id.mGT_Correct);
        mGT_National_Correct_1 = findViewById(R.id.mGT_National_Correct_1);
        mGT_National_Correct_2 = findViewById(R.id.mGT_National_Correct_2);


        // for booking center
        recBookingCenter = findViewById(R.id.recBookingCenter);
        recBookingCenter.setLayoutManager(new LinearLayoutManager(Results_Score.this));
        adapter_individualScores = new Adapter_individualScores(this, individualScoresArrayList, new Adapter_individualScores.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {

            }
        });
        recBookingCenter.setAdapter(adapter_individualScores);


        getResult();
    }

    private void getResult() {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.GET, session.BASEURL + "users/me/exams/"+examID+"/result",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        try {
                            Log.e("users_me", new String(response.data) + "--");


                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            mDate_title.setText(jsonObject.getString("name"));
                            mRS_Exam_result.setText(capitalizeAllFirstLetters(jsonObject.getString("result")));
                            mRS_Your_result.setText(jsonObject.getString("score")+"%");
                            mRS_Min_Pass.setText(jsonObject.getString("minimumPassing")+"%");

                            mGT_Your_Score.setText(jsonObject.getJSONObject("grandTotal").getString("score")+"");
                            mGT_Max_Score.setText(jsonObject.getJSONObject("grandTotal").getString("maximumScore")+"");
                            mGT_Correct.setText(jsonObject.getJSONObject("grandTotal").getString("percentageCorrect")+"%");
                            mGT_National_Correct_1.setText(jsonObject.getJSONObject("grandTotal").getString("nationalCorrect")+"");
                            mGT_National_Correct_2.setText(jsonObject.getJSONObject("grandTotal").getString("nationalPercentageCorrect")+"%");


                            JSONArray jsonArray = jsonObject.getJSONArray("individualScores");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);

                                IndividualScores individualScores = new IndividualScores();
                                individualScores.setScore(object.getString("score"));
                                individualScores.setMaximumScore(object.getString("maximumScore"));
                                individualScores.setNationalCorrect(object.getString("nationalCorrect"));
                                individualScores.setPercentageCorrect(object.getString("percentageCorrect"));
                                individualScores.setNationalPercentageCorrect(object.getString("nationalPercentageCorrect"));
                                individualScores.setName(object.getString("name"));

                                individualScoresArrayList.add(individualScores);
                            }
                            adapter_individualScores.notifyDataSetChanged();


                        } catch (Exception e) {

                            Toast.makeText(Results_Score.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                        json = session.trimMessage(json, "error");
                                        if(json != null) {
                                            Toast.makeText(Results_Score.this, json, Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                        case 404:
                                        String json404 = new String(error.networkResponse.data);
                                            json404 = session.trimMessage(json404, "error");
                                        if(json404 != null) {
                                            Toast.makeText(Results_Score.this, json404  , Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                            }
                        }
                        else if (error instanceof TimeoutError)
                            Toast.makeText(Results_Score.this, "Connection Timed Out", Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(Results_Score.this, "Bad Network Connection", Toast.LENGTH_LONG).show();

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

        Volley.newRequestQueue(Results_Score.this).add(volleyMultipartRequest);
    }

   private String capitalizeAllFirstLetters(String name)
    {
        char[] array = name.toCharArray();
        array[0] = Character.toUpperCase(array[0]);

        for (int i = 1; i < array.length; i++) {
            if (Character.isWhitespace(array[i - 1])) {
                array[i] = Character.toUpperCase(array[i]);
            }
        }

        return new String(array);
    }


}