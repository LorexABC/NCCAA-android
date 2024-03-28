package com.app_nccaa.nccaa.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.app_nccaa.nccaa.R;

public class PrivacyActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView imgId,backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        backBtn=findViewById(R.id.back);
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
        case R.id.back:
        onBackPressed();
        break;
        }
    }
}