package com.app_nccaa.nccaa.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.app_nccaa.nccaa.Utils.UserSession;
import com.app_nccaa.nccaa.R;

public class PsiMandatory2Activity extends AppCompatActivity {

    private UserSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psi_mandatory2);

        session = new UserSession(PsiMandatory2Activity.this);


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PsiMandatory2Activity.this, LoginActivity.class));
                finishAffinity();
            }
        });



        findViewById(R.id.btnContinue2Id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PsiMandatory2Activity.this, HomeScreen.class));
                session.setIsLogin(true);
                session.setAPITOKEN(getIntent().getStringExtra("token"));

                Log.e("token", getIntent().getStringExtra("token") + "--");
                finishAffinity();
            }
        });



    }

    @Override
    public void onBackPressed() {
   //     super.onBackPressed();
        startActivity(new Intent(PsiMandatory2Activity.this, LoginActivity.class));
        finishAffinity();
    }

}