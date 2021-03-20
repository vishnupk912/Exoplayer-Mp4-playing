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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.mastervidya.mastervidya.R;
import com.mastervidya.mastervidya.adapter.VideoAdapter;
import com.mastervidya.mastervidya.helper.RequestQueueSingleton;
import com.mastervidya.mastervidya.helper.SessionHandler;
import com.mastervidya.mastervidya.helper.Url;
import com.mastervidya.mastervidya.model.VideoModel1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class VideoListing extends AppCompatActivity {

    SessionHandler sessionHandler;
    String id,name,phone,avatar_image;
    Dialog dialog_progress;
    RequestQueue requestQueue;
    String subid;
    RecyclerView recyclerView;
    TextView subnametv;
    String chapid;
    ImageView back;
    LinearLayout linearLayout_Quiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_listing);

        back=findViewById(R.id.back);
        dialog_progress = new Dialog(this);
        dialog_progress.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_progress.setContentView(R.layout.progressbar);
        dialog_progress.setCancelable(false);
        dialog_progress.setCanceledOnTouchOutside(false);
        dialog_progress.setCancelable(false);
        dialog_progress.setCanceledOnTouchOutside(false);
        dialog_progress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        sessionHandler=new SessionHandler(this);
        requestQueue = RequestQueueSingleton.getInstance(this)
                .getRequestQueue();
        subnametv=findViewById(R.id.subnametv);
        recyclerView=findViewById(R.id.rvid);
        linearLayout_Quiz=findViewById(R.id.layquiz);
        Intent intent=getIntent();
        String chapname=intent.getStringExtra("chapname");
        chapid=intent.getStringExtra("chapter_id");



        subnametv.setText(chapname);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        linearLayout_Quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(VideoListing.this,Quizinfo.class);
                startActivity(intent1);
            }
        });
        getcustomerdetails();
        getdata();

    }

    public void getdata()
    {
        ArrayList<VideoModel1> videoModelArrayList=new ArrayList<>();
        dialog_progress.show();
        JSONObject json = new JSONObject();
        try
        {
            json.put("key",sessionHandler.getuniquekey());
            json.put("id",id);
            json.put("chapter_id",chapid);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, Url.videos, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                dialog_progress.dismiss();
                Log.d("response",jsonObject.toString());
                try {
                    String status=jsonObject.getString("status");
                    if(status.contains("success"))
                    {
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            VideoModel1 videoModel=new VideoModel1();

                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            String video_id=jsonObject1.getString("video_id");
                            String title=jsonObject1.getString("title");
                            String url=jsonObject1.getString("url");
                            String description=jsonObject1.getString("description");
                            String chapter=jsonObject1.getString("chapter");
                            String subject=jsonObject1.getString("subject");
                            String classs=jsonObject1.getString("class");
                            String chapter_id=jsonObject1.getString("chapter_id");
                            String path=jsonObject1.getString("path");

                            videoModel.setChapter(chapter);
                            videoModel.setClasss(classs);
                            videoModel.setSubject(subject);
                            videoModel.setTitle(title);
                            videoModel.setDescirption(description);
                            videoModel.setUrl(url);
                            videoModel.setVideoid(video_id);
                            videoModel.setChapter_id(chapter_id);
                            videoModel.setPath(path);
                            videoModelArrayList.add(videoModel);


                        }
                    }
                    else if(status.contains("invalid api key"))
                    {
                        Dialog dialog;
                        dialog = new Dialog(VideoListing.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.alertdialog);
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.show();
                        LinearLayout linearLayout=dialog.findViewById(R.id.okid);


                        linearLayout.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view) {
                                sessionHandler.logoutUser();
                            }
                        });

                    }


                    if (!videoModelArrayList.isEmpty())
                    {
                        VideoAdapter adapter = new VideoAdapter(videoModelArrayList,VideoListing.this);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(VideoListing.this,RecyclerView.VERTICAL,false));
                        recyclerView.setAdapter(adapter);

                    }
                    else
                    {

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