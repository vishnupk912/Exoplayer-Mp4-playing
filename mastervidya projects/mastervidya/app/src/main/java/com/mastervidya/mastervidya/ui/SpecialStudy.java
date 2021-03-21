package com.mastervidya.mastervidya.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.mastervidya.mastervidya.R;
import com.mastervidya.mastervidya.Subjects;
import com.mastervidya.mastervidya.helper.RequestQueueSingleton;
import com.mastervidya.mastervidya.helper.SessionHandler;
import com.mastervidya.mastervidya.helper.Url;
import com.mastervidya.mastervidya.model.ChapterModel;
import com.mastervidya.mastervidya.model.SubModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SpecialStudy extends AppCompatActivity {
    RecyclerView recyclerView_sub;
    RequestQueue requestQueue;
    String id,name,phone,avatar_image;
    SessionHandler sessionHandler;
    Dialog dialog_progress;
    String classid;
    LinearLayout lay_main;
    LinearLayout noclasslay,classlay;
    ImageView  back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_study);
        recyclerView_sub=findViewById(R.id.rvsub);

        noclasslay=findViewById(R.id.noclasslay);
        classlay=findViewById(R.id.classlay);
        back=findViewById(R.id.back);

        sessionHandler=new SessionHandler(this);
        requestQueue = RequestQueueSingleton.getInstance(this)
                .getRequestQueue();


        dialog_progress = new Dialog(this);
        dialog_progress.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_progress.setContentView(R.layout.progressbar);
        dialog_progress.setCancelable(false);
        dialog_progress.setCanceledOnTouchOutside(false);
        dialog_progress.setCancelable(false);
        dialog_progress.setCanceledOnTouchOutside(false);
        dialog_progress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        lay_main=findViewById(R.id.lay_main);
        getcustomerdetails();
        subjects();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
        });

        Log.d("key",sessionHandler.getuniquekey());
    }

    private void subjects()
    {
        ArrayList<SubModel> subModelArrayList=new ArrayList<>();
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

        dialog_progress.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Url.special_study, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject)
                    {


                                dialog_progress.dismiss();
                        try {
                            String status=jsonObject.getString("status");
                            if(status.contains("success"))
                            {
                                JSONArray jsonArray=jsonObject.getJSONArray("data");
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    SubModel subModel=new SubModel();

                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    String sub_id=jsonObject1.getString("id");
                                    String subject=jsonObject1.getString("subject");
                                    String image=jsonObject1.getString("image");

                                    subModel.setId(sub_id);
                                    subModel.setName(subject);
                                    subModel.setImagel(image);

                                    subModelArrayList.add(subModel);


                                }
                            }

                            else if(status.contains("invalid api key"))
                            {
                                Dialog dialog;
                                dialog = new Dialog(SpecialStudy.this);
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

                            if(!subModelArrayList.isEmpty())
                            {
                                SubjectAdapter adapter = new SubjectAdapter(SpecialStudy.this,subModelArrayList);
                                recyclerView_sub.setHasFixedSize(true);
                                recyclerView_sub.setLayoutManager(new GridLayoutManager(SpecialStudy.this,3,RecyclerView.VERTICAL,false));
                                recyclerView_sub.setAdapter(adapter);
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
                }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog_progress.dismiss();
                Snackbar snackbar = Snackbar.make(lay_main, "Some server error occurred .", Snackbar.LENGTH_LONG);
                View sbView = snackbar.getView();
                sbView.setBackgroundResource(R.color.colorprimarydark);
                snackbar.show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public class SubjectAdapter  extends RecyclerView.Adapter<SubjectAdapter.ViewHolder>

    {
        Context context;
        ArrayList<SubModel> subModelArrayList=new ArrayList<>();

        public SubjectAdapter(Context context, ArrayList<SubModel> subModelArrayList) {
            this.context = context;
            this.subModelArrayList = subModelArrayList;
        }

        @NonNull
        @Override
        public SubjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
            View listitem=layoutInflater.inflate(R.layout.laysub1,parent,false);
            SubjectAdapter.ViewHolder viewHolder=new SubjectAdapter.ViewHolder(listitem);
            return  viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull SubjectAdapter.ViewHolder holder, int position)
        {
            String subname=subModelArrayList.get(position).getName();
            Glide.with(context)
                    .load(subModelArrayList.get(position).getImagel())
                    .centerCrop()
                    .into(holder.imageView);
            holder.subnametv.setText(position+1 +". "+subname);

            holder.lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(SpecialStudy.this, SpecialStudy_Chapters.class);
                    intent.putExtra("sub_id",subModelArrayList.get(position).getId());
                    intent.putExtra("sub_name",subModelArrayList.get(position).getName());
                    startActivity(intent);

                    Log.d("subid",subModelArrayList.get(position).getId());

                }
            });


        }

        @Override
        public int getItemCount() {
            return subModelArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView subnametv;
            LinearLayout lay;
            ImageView imageView;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                subnametv=itemView.findViewById(R.id.name);
                lay=itemView.findViewById(R.id.lay);
                imageView=itemView.findViewById(R.id.image);
            }
        }
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