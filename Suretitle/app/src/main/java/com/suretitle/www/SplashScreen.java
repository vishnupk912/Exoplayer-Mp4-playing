package com.suretitle.www;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import helper.SessionHandler;

public class SplashScreen extends AppCompatActivity {

    int Splash_Time_Out = 3000;
    SessionHandler sessionHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash_screen3);
        sessionHandler=new SessionHandler(this);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if(sessionHandler.isLoggedIn())
                {
                    Intent i = new Intent(SplashScreen.this, DashBoard.class);
                    startActivity(i);
                }

                else
                {
                    Intent i = new Intent(SplashScreen.this, Login.class);
                    startActivity(i);
                }

            }
        }, Splash_Time_Out);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}