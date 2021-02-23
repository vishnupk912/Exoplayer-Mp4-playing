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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.mastervidya.mastervidya.Addclass.SelectFragment;
import com.mastervidya.mastervidya.R;
import com.mastervidya.mastervidya.adapter.SubClassAdapter;
import com.mastervidya.mastervidya.helper.RequestQueueSingleton;
import com.mastervidya.mastervidya.helper.SessionHandler;
import com.mastervidya.mastervidya.helper.Url;
import com.mastervidya.mastervidya.model.ClassModel;
import com.mastervidya.mastervidya.model.SubscribedclassModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MyClass extends AppCompatActivity {
    SessionHandler sessionHandler;
    String id,name,phone,avatar_image;
    RequestQueue requestQueue;
    Dialog dialog_progress;
    LinearLayout addlay;
    RecyclerView recyclerView;
    ImageView  back;
    LinearLayout noclasslay,classlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_class);
        recyclerView=findViewById(R.id.rvid);
        addlay=findViewById(R.id.add);
        sessionHandler=new SessionHandler(this);
        requestQueue = RequestQueueSingleton.getInstance(this)
                .getRequestQueue();

        noclasslay=findViewById(R.id.noclasslay);
        classlay=findViewById(R.id.classlay);

        back=findViewById(R.id.back);
        dialog_progress = new Dialog(this);
        dialog_progress.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_progress.setContentView(R.layout.progressbar);
        dialog_progress.setCancelable(false);
        dialog_progress.setCanceledOnTouchOutside(false);
        dialog_progress.setCancelable(false);
        dialog_progress.setCanceledOnTouchOutside(false);
        dialog_progress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyClass.this,Homepage.class);
                startActivity(intent);
            }
        });
        addlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyClass.this,AddClass.class);
                startActivity(intent);
            }
        });

        getcustomerdetails();
        getdata();
    }

    public void getdata()
    {
        ArrayList<SubscribedclassModel> subscribedclassModelArrayList=new ArrayList<>();
        dialog_progress.show();
        JSONObject json = new JSONObject();
        try
        {
            json.put("key",sessionHandler.getuniquekey());
            json.put("id",id);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, Url.subclass, json, new Response.Listener<JSONObject>() {
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
                            SubscribedclassModel classModel=new SubscribedclassModel();

                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            String class_id=jsonObject1.getString("class_id");
                            String board=jsonObject1.getString("board");
                            String classname=jsonObject1.getString("class");
                            String student_name=jsonObject1.getString("student_name");
                            classModel.setClass_id(class_id);
                            classModel.setBoard(board);
                            classModel.setClasss(classname);
                            classModel.setStudent_name(student_name);

                            subscribedclassModelArrayList.add(classModel);

                        }
                    }
                    else if(status.contains("invalid api key"))
                    {
                        Dialog dialog;
                        dialog = new Dialog(MyClass.this);
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


                    if (!subscribedclassModelArrayList.isEmpty())
                    {
                        SubClassAdapter adapter = new SubClassAdapter(MyClass.this,subscribedclassModelArrayList);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MyClass.this,RecyclerView.VERTICAL,false));
                        recyclerView.setAdapter(adapter);
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