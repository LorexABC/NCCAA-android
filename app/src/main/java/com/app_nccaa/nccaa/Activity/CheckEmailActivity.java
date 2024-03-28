package com.app_nccaa.nccaa.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app_nccaa.nccaa.R;

public class CheckEmailActivity extends AppCompatActivity {

    Button btnConfirmId;
    ImageView back;

    TextView txt2Id, txtForgotId;
    //Intent intent1=new Intent();
  //  Bundle extras = getIntent().getExtras();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_email);


        btnConfirmId = findViewById(R.id.btnConfirmId);
        txtForgotId = findViewById(R.id.txtForgotId);
        txt2Id = findViewById(R.id.txt2Id);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        String email_phone = getIntent().getStringExtra("email_phone");
        String type = getIntent().getStringExtra("type");


        if (type.equals("email")){
            txt2Id.setText("We have sent a password recovery instruction to \nyour email.");
            txtForgotId.setText("Check your email");
        } else {
            txt2Id.setText("We have sent a password recovery instruction to \nyour phone.");
            txtForgotId.setText("Check your inbox");
        }



        btnConfirmId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CheckEmailActivity.this, EmailOtpActivity.class);
                intent.putExtra("email_phone", email_phone);
                intent.putExtra("type", type);
                startActivity(intent);
                finish();
            }
        });


    }



}