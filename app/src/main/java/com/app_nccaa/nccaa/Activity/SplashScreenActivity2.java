package com.app_nccaa.nccaa.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.app_nccaa.nccaa.Utils.UserSession;
import com.app_nccaa.nccaa.R;

public class SplashScreenActivity2 extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIME_OUT=2000;
    private UserSession session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //This method is used so that your splash activity
        //can cover the entire screen.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splashscreen2);

        session = new UserSession(SplashScreenActivity2.this);


    }

    @Override
    protected void onResume() {
        super.onResume();

        //this method is used to delay the next visible screen for 2000 ms
        delayNextScreen();
    }

    private void delayNextScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (session.isLoggedIn()){
                    startActivity(new Intent(SplashScreenActivity2.this, HomeScreen.class));
                } else {
                    startActivity(new Intent(SplashScreenActivity2.this, LoginActivity.class));
                }


         //       intent1 = new Intent(SplashScreenActivity2.this, PsiMandatoryActivity.class);

                finish();
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }


}