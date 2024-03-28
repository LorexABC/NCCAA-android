package com.app_nccaa.nccaa.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app_nccaa.nccaa.R;

public class PasswordResetActivity extends AppCompatActivity implements View.OnClickListener {

    Button passwordResetIdBTN;
    ImageView back;
    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        initiateViews();

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }


    // Below function is used to initiate Views elements used in this activity
    private void initiateViews() {
        passwordResetIdBTN=findViewById(R.id.btnCreatePasswordId);
        passwordResetIdBTN.setOnClickListener(this);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        back.setVisibility(View.GONE);
      //  back.setVisibility(View.GONE);
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCreatePasswordId:
                 startActivity(new Intent(PasswordResetActivity.this, LoginActivity.class));
                 finishAffinity();
                break;
            case R.id.back:
              //  onBackPressed();
                break;
        }
    }


}