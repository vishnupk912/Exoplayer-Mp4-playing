package com.mastervidya.mastervidya.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.mastervidya.mastervidya.R;
import com.mastervidya.mastervidya.adapter.ReportAdapter1;
import com.mastervidya.mastervidya.adapter.SubClassAdapter1;
import com.mastervidya.mastervidya.helper.RequestQueueSingleton;
import com.mastervidya.mastervidya.helper.SessionHandler;
import com.mastervidya.mastervidya.helper.Url;
import com.mastervidya.mastervidya.model.ReportModel;
import com.mastervidya.mastervidya.model.SubModel;
import com.mastervidya.mastervidya.model.SubscribedclassModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ChapterWiseReport extends AppCompatActivity {
    String id,name,phone,avatar_image;
    SessionHandler sessionHandler;
    RequestQueue requestQueue;
    Dialog dialog_progress;
    RecyclerView  rvchap;
    ImageView back;
    String subject_id,sub_name;
    TextView tvhead;
    LinearLayout noclasslay,classlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_wise_report);

        sessionHandler=new SessionHandler(this);
        requestQueue = RequestQueueSingleton.getInstance(this)
                .getRequestQueue();

        rvchap=findViewById(R.id.rvid);
        tvhead=findViewById(R.id.tvhead);
        back=findViewById(R.id.back);
        dialog_progress = new Dialog(this);
        dialog_progress.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_progress.setContentView(R.layout.progressbar);
        dialog_progress.setCancelable(false);
        dialog_progress.setCanceledOnTouchOutside(false);
        dialog_progress.setCancelable(false);
        dialog_progress.setCanceledOnTouchOutside(false);
        dialog_progress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        noclasslay=findViewById(R.id.noclasslay);
        classlay=findViewById(R.id.classlay);


        Intent intent=getIntent();
        subject_id=intent.getStringExtra("sub_id");
        sub_name=intent.getStringExtra("sub_name");

        tvhead.setText(sub_name+" Report ");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             finish();
            }
        });


        getcustomerdetails();
        reports();
    }

    public void reports()
    {
        ArrayList<ReportModel> reportModelArrayList=new ArrayList<>();
        dialog_progress.show();

        JSONObject json = new JSONObject();
        try
        {
            json.put("key",sessionHandler.getuniquekey());
            json.put("id",id);
            json.put("subject_id",subject_id);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, Url.reports, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                dialog_progress.dismiss();
                Log.d("response",jsonObject.toString());
                try {
                    String status=jsonObject.getString("status");
                    if(status.contains("success")) {

                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            ReportModel reportModel=new ReportModel();

                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            String chapter_id=jsonObject1.getString("chapter_id");
                            String chapter=jsonObject1.getString("chapter");
                            String correct=jsonObject1.getString("correct");
                            String incorrect=jsonObject1.getString("incorrect");
                            String not_answered=jsonObject1.getString("not_answered");
                            String scored_percentage=jsonObject1.getString("scored_percentage");
                            String status1=jsonObject1.getString("status");
                            String time=jsonObject1.getString("time");

                            reportModel.setChapter(chapter);
                            reportModel.setChapter_id(chapter_id);
                            reportModel.setCorrect(correct);
                            reportModel.setIncorrect(incorrect);
                            reportModel.setNot_answered(not_answered);
                            reportModel.setScored_percentage(scored_percentage);
                            reportModel.setStatus1(status1);
                            reportModel.setTime(time);
                            reportModelArrayList.add(reportModel);

                        }
                    }
                    else if(status.contains("invalid api key"))
                    {
                        Dialog dialog;
                        dialog = new Dialog(ChapterWiseReport.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.alertdialog);
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.show();
                        LinearLayout linearLayout=dialog.findViewById(R.id.okid);


                        linearLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                sessionHandler.logoutUser();
                            }
                        });

                    }

                    if (!reportModelArrayList.isEmpty()) {
                        ReportAdapter1 adapter = new ReportAdapter1(ChapterWiseReport.this, reportModelArrayList);
                        rvchap.setHasFixedSize(true);
                        rvchap.setLayoutManager(new LinearLayoutManager(ChapterWiseReport.this, RecyclerView.VERTICAL, false));
                        rvchap.setAdapter(adapter);
                        noclasslay.setVisibility(View.GONE);
                        classlay.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        noclasslay.setVisibility(View.VISIBLE);
                        classlay.setVisibility(View.GONE);
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

        }

        catch (Exception e)
        {

        }

    }

}