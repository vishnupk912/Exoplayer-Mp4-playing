package com.mastervidya.mastervidya.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.mastervidya.mastervidya.R;
import com.mastervidya.mastervidya.adapter.VideoAdapter1;
import com.mastervidya.mastervidya.helper.RequestQueueSingleton;
import com.mastervidya.mastervidya.helper.SessionHandler;
import com.mastervidya.mastervidya.helper.Url;
import com.mastervidya.mastervidya.model.VideoModel1;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;

public class Chat extends AppCompatActivity {


    String id,name,phone,avatar_image;


    SessionHandler sessionHandler;

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
     RequestQueue requestQueue;
     ImageView userlogo,back;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

         mp = MediaPlayer.create(this, R.raw.chat);
        back=findViewById(R.id.back);
        userlogo=findViewById(R.id.userlogo);
        sessionHandler=new SessionHandler(this);
        requestQueue = RequestQueueSingleton.getInstance(this)
                .getRequestQueue();

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




        getcustomerdetails();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        send.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                chat2.setText(etmsg.getText().toString());
                hideKeyboard(Chat.this);
                mp.start();


                if(etmsg.getText().toString().isEmpty())
                {
                    Toast.makeText(Chat.this, "Please type something", Toast.LENGTH_SHORT).show();
                }
                else
                    {
                        sendmessage(etmsg.getText().toString());

                    }




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



    public void sendmessage(String msg)
    {

        JSONObject json = new JSONObject();
        try
        {
            json.put("key",sessionHandler.getuniquekey());
            json.put("id",id);
            json.put("message",msg);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, Url.support, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {
                    String status=jsonObject.getString("status");
                    if(status.contains("success"))
                    {
                        mp.start();
                        etmsg.getText().clear();
                        chatlay2.setVisibility(View.VISIBLE);
                        final int intervalTime = 2000; // 10 sec
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable()  {
                            @Override
                            public void run()
                            {
                                Intent intent=new Intent(Chat.this,Chat.class);
                                startActivity(intent);
                            }
                        }, intervalTime);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError
            {

            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public void getcustomerdetails()
    {
        Gson gson = new Gson();
        HashMap<String, String> user = sessionHandler.getLoginSession();
        String json = user.get(sessionHandler.KEY_LOGIN);
        ArrayList alist = gson.fromJson(json, ArrayList.class);
        JSONArray jsonArrA = new JSONArray(alist);
        try
        {

            JSONObject userdata = jsonArrA.getJSONObject(0);
            id = userdata.getString("id");
            name = userdata.getString("name");
            phone = userdata.getString("phone");
            avatar_image = userdata.getString("avatar_image");

            Glide.with(Chat.this)
                    .load(avatar_image)
                    .centerCrop()
                    .into(userlogo);
        }

        catch (Exception e)
        {

        }

    }

}