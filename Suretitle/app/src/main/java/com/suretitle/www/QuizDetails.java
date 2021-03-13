package com.suretitle.www;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class QuizDetails extends AppCompatActivity {

    LinearLayout layid;
    String propertyid,propertyname;
    String mobilenumber,customer_id,customer_name,country_id,state_id,country_name,state_name,email,profession,organisation,gender,place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_quiz_details);
        layid=findViewById(R.id.layid);

        Intent intent=getIntent();
        propertyid=intent.getStringExtra("propertyid");
        propertyname=intent.getStringExtra("propertyname");

        layid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(QuizDetails.this, QuestionCatogery.class);
                intent.putExtra("propertyid",propertyid);
                intent.putExtra("propertyname",propertyname);
                startActivity(intent);
            }
        });
    }


}