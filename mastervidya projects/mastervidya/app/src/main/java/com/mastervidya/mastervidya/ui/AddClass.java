package com.mastervidya.mastervidya.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.mastervidya.mastervidya.Addclass.SelectFragment;
import com.mastervidya.mastervidya.R;

public class AddClass extends AppCompatActivity {

    RadioButton radioButton1, radioButton2;
    View view_line;
    Dialog dialog_progress;
    LinearLayout lay_main;
    ImageView back;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);


        back=findViewById(R.id.back);
        lay_main = findViewById(R.id.lay_main);
        radioButton1 = findViewById(R.id.rb1);
        radioButton2 = findViewById(R.id.rb2);
        view_line = findViewById(R.id.lineid);

        loadfragment1(new SelectFragment());
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }



    private void loadfragment1(Fragment fragment) {
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.frameDB,fragment);
        ft.commit();
    }


}
