package com.mastervidya.mastervidya.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mastervidya.mastervidya.R;
import com.mdgiitr.suyash.graphkit.DataPoint;
import com.mdgiitr.suyash.graphkit.PieChart;

import java.util.ArrayList;

public class QuizResult extends AppCompatActivity {
    PieChart pieChart;
    String token,correct,incorrect,not_answered,pass_percentage,result;
    TextView text_result;
    ImageView closeid;
    LinearLayout viewanswer;
    String chap_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        viewanswer=findViewById(R.id.viewanswer);
        pieChart = findViewById(R.id.grid_pie);
        text_result=findViewById(R.id.text_result);
        closeid=findViewById(R.id.closeid);
        Intent intent=getIntent();
        token=intent.getStringExtra("token");
        correct= intent.getStringExtra("correct");
        incorrect= intent.getStringExtra("incorrect");
        not_answered= intent.getStringExtra("not_answered");
        pass_percentage= intent.getStringExtra("pass_percentage");
        result= intent.getStringExtra("result");
        chap_id=intent.getStringExtra("chap_id");


        if(result.contains("pass"))
        {
            text_result.setTextColor(getResources().getColor(R.color.green));
        }
        else
        {
            text_result.setTextColor(getResources().getColor(R.color.red));
        }
        text_result.setText(result);

        ArrayList<DataPoint> points = new ArrayList<>();
        points.add(new DataPoint("Correct",Integer.parseInt(correct), Color.parseColor("#2ECC71")));
        points.add(new DataPoint("Incorrect",Integer.parseInt(incorrect), Color.parseColor("#EC7063")));
        points.add(new DataPoint("Not Answered", Integer.parseInt(not_answered),Color.parseColor("#34495E")));
        pieChart.setPoints(points);

        closeid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(QuizResult.this,Homepage.class);
                startActivity(intent1);
            }
        });

        viewanswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(QuizResult.this,ViewAnswer.class);
                intent1.putExtra("token",token);
                intent1.putExtra("chap_id",chap_id);


                startActivity(intent1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent1=new Intent(QuizResult.this,Homepage.class);
        startActivity(intent1);
    }
}