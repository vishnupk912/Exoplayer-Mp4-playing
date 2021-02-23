package com.mastervidya.mastervidya.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mastervidya.mastervidya.R;
import com.mastervidya.mastervidya.adapter.ViewPagerAdapter;
import com.mastervidya.mastervidya.model.PagerModel;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

public class IntroPage extends AppCompatActivity {

    ViewPager2 viewPager;
    SpringDotsIndicator dotsIndicator;


    LinearLayout loginlay,signuplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_page);

        dotsIndicator = (SpringDotsIndicator) findViewById(R.id.dots_indicator);
        viewPager=findViewById(R.id.viewPager);
        loginlay=findViewById(R.id.loginlayid);
        signuplay=findViewById(R.id.signuplayid);


        PagerModel[] PagerModel = new PagerModel[] {
                new PagerModel("Welcome to MasterVidya the learning App","We are the team who teach your Children as school does with clear concepts" ,R.drawable.ic_slider1),
                new PagerModel("Syllabus oriented studies","Master Vidya offers you a syllabus oriented studies with VR technology" ,R.drawable.ic_slider1),
                new PagerModel("Real time report","Worried about your childrens education Status? Master vidhya provides an real time report of your child" ,R.drawable.ic_slider1),
                new PagerModel("Access to multiple children","We provide a single login to teach your multiple children ,so now more mulitple devices or phones for your childrens,everything in a single platform ." ,R.drawable.ic_slider1),


        };

//        Display display = getWindowManager().getDefaultDisplay();
//        int width = display.getWidth(); // ((display.getWidth()*20)/100)
//        int height =((display.getHeight()*40)/100);
//
//        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height);
//        layid.setLayoutParams(parms);


        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(this,PagerModel);
        viewPager.setAdapter(viewPagerAdapter);
        dotsIndicator.setViewPager2(viewPager);




        loginlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(IntroPage.this, Login.class);
                startActivity(intent);
            }
        });


        signuplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(IntroPage.this, Login.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        ActivityCompat.finishAffinity(this);
    }
}