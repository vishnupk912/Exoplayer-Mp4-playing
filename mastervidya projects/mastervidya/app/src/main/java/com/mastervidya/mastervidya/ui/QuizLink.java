package com.mastervidya.mastervidya.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.mastervidya.mastervidya.R;
import com.mastervidya.mastervidya.helper.RequestQueueSingleton;
import com.mastervidya.mastervidya.helper.SessionHandler;
import com.mastervidya.mastervidya.helper.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class QuizLink extends AppCompatActivity {

    String id, name, phone, avatar_image;
    SessionHandler sessionHandler;
    RequestQueue requestQueue;
    Dialog dialog_progress;
    LinearLayout startlay;
    TextView chapternametv, descriptiontv, timetv, questiontv, passstv;
    String duration_in_minute, path;

    String[] chapid1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_link);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        sessionHandler = new SessionHandler(this);



        Uri uri = getIntent().getData();
        if (uri != null) {
            path = uri.toString();
        }


        chapid1 = path.split("=");

        chapid1[1] = chapid1[1].trim();


        chapternametv = findViewById(R.id.chapternametv);
        descriptiontv = findViewById(R.id.descriptiontv);
        timetv = findViewById(R.id.timetv);
        questiontv = findViewById(R.id.questiontv);
        passstv = findViewById(R.id.passstv);


        dialog_progress = new Dialog(this);
        dialog_progress.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_progress.setContentView(R.layout.progressbar);
        dialog_progress.setCancelable(false);
        dialog_progress.setCanceledOnTouchOutside(false);
        dialog_progress.setCancelable(false);
        dialog_progress.setCanceledOnTouchOutside(false);
        dialog_progress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        startlay = findViewById(R.id.startlay);

        requestQueue = RequestQueueSingleton.getInstance(this)
                .getRequestQueue();

        getcustomerdetails();
        quizinfo();


        startlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(QuizLink.this, QuizaAcitivity.class);
                intent1.putExtra("time", duration_in_minute);
                intent1.putExtra("chap_id", chapid1[1]);

                startActivity(intent1);
            }
        });

    }

    public void quizinfo() {


        JSONObject json = new JSONObject();
        try {
            json.put("key", sessionHandler.getuniquekey());
            json.put("id", id);
            json.put("chapter_id", chapid1[1]);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Url.quiz_info, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                dialog_progress.dismiss();
                Log.d("response", jsonObject.toString());
                try {
                    String status = jsonObject.getString("status");
                    if (status.contains("success")) {


                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String chapter_id = jsonObject1.getString("chapter_id");
                            String name = jsonObject1.getString("name");
                            duration_in_minute = jsonObject1.getString("duration_in_minute");
                            String number_of_questions = jsonObject1.getString("No_of_questions");
                            String pass_percentage = jsonObject1.getString("pass_percentage");
                            String description = jsonObject1.getString("description");


                            chapternametv.setText(name);
                            timetv.setText(duration_in_minute);
                            questiontv.setText(number_of_questions);
                            passstv.setText(pass_percentage + " %");
                            descriptiontv.setText(description);

                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog_progress.dismiss();
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

    public void getcustomerdetails() {
        Gson gson = new Gson();
        HashMap<String, String> user = sessionHandler.getLoginSession();
        String json = user.get(sessionHandler.KEY_LOGIN);
        ArrayList alist = gson.fromJson(json, ArrayList.class);
        JSONArray jsonArrA = new JSONArray(alist);
        try {

            JSONObject userdata = jsonArrA.getJSONObject(0);
            id = userdata.getString("id");
            name = userdata.getString("name");
            phone = userdata.getString("phone");
            avatar_image = userdata.getString("avatar_image");

        } catch (Exception e) {

        }

    }
}

