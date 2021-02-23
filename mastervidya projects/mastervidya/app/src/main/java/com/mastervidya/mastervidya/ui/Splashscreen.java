package com.mastervidya.mastervidya.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.mastervidya.mastervidya.R;
import com.mastervidya.mastervidya.helper.SessionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Splashscreen extends AppCompatActivity
{
    int Splash_Time_Out = 3000;
    SessionHandler sessionHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);


        sessionHandler=new SessionHandler(this);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if(sessionHandler.isLoggedIn())
                {
                    Intent i = new Intent(Splashscreen.this, Homepage.class);
                    startActivity(i);
                }

                else
                {
                    Intent i = new Intent(Splashscreen.this, IntroPage.class);
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

//        HashMap<String, String> news = new HashMap<>();
//        news.put("mobile", mobilenumber);
//        news.put("name", name);
//        list.add(news);
//
//        Gson gson = new Gson();
//        List<HashMap<String, String>> textList = new ArrayList<>();
//        textList.addAll(list);
//        String jsonText = gson.toJson(textList);
//        sessionHandler.createLoginSession(jsonText);

    }
