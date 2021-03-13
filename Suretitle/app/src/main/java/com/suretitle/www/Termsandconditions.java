package com.suretitle.www;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class Termsandconditions extends AppCompatActivity {

    String propertyname;
    LinearLayout declinlay,acceptlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_termsandconditions);

        Intent intent=getIntent();
        propertyname=intent.getStringExtra("propertyname");
        declinlay=findViewById(R.id.declinelay);
        acceptlay=findViewById(R.id.acceptlay);


        declinlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(Termsandconditions.this,DashBoard.class);
                startActivity(intent1);
            }
        });

        acceptlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(Termsandconditions.this,ResultPage.class);
                intent1.putExtra("propertyname",propertyname);
                startActivity(intent1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent1=new Intent(Termsandconditions.this,DashBoard.class);
        startActivity(intent1);
    }
}