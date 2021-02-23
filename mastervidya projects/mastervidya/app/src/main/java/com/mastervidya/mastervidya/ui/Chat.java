package com.mastervidya.mastervidya.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mastervidya.mastervidya.R;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.ArrayList;
import java.util.Timer;

public class Chat extends AppCompatActivity {


    LinearLayoutManager linearLayoutManager;

    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;
    final long PERIOD_MS = 1500;
    ShimmerTextView typing_tv;
    Shimmer shimmer;
    ImageView send;
    EditText etmsg;

    TextView chat1,chat2,chat3;
    LinearLayout chatlay2,chatlay1;
     MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

         mp = MediaPlayer.create(this, R.raw.chat);

        typing_tv=findViewById(R.id.tvid);
        send=findViewById(R.id.send);
        shimmer = new Shimmer();
        shimmer.start(typing_tv);
        etmsg=findViewById(R.id.et);

        chat1=findViewById(R.id.chat1id);
        chat2=findViewById(R.id.chat2id);
        chat3=findViewById(R.id.chat3id);
        chatlay2=findViewById(R.id.chatlay2);
        chatlay1=findViewById(R.id.laychat1);

        final int intervalTime = 2000; // 10 sec
        Handler handler = new Handler();
        handler.postDelayed(new Runnable()  {
            @Override
            public void run()
            {
                typing_tv.setVisibility(View.GONE);
                chatlay1.setVisibility(View.VISIBLE);
                mp.start();
            }
        }, intervalTime);





        send.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                chatlay2.setVisibility(View.VISIBLE);
                chat2.setText(etmsg.getText().toString());
                hideKeyboard(Chat.this);
                mp.start();

            }

        });


    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}