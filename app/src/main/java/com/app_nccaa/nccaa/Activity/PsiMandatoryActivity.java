package com.app_nccaa.nccaa.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.app_nccaa.nccaa.R;

public class PsiMandatoryActivity extends AppCompatActivity {

    private ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psi_mandatory);

        back = findViewById(R.id.back);

        back.setVisibility(View.GONE);



        findViewById(R.id.btnContinueId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PsiMandatoryActivity.this, PersonalInformationActivity.class)
                        .putExtra("token", getIntent().getStringExtra("token")));
            }
        });


    }


}