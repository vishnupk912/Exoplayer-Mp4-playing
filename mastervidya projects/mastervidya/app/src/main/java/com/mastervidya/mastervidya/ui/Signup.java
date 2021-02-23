package com.mastervidya.mastervidya.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.mastervidya.mastervidya.R;
import com.mastervidya.mastervidya.helper.RequestQueueSingleton;
import com.mastervidya.mastervidya.helper.SessionHandler;
import com.mastervidya.mastervidya.helper.Url;
import com.mastervidya.mastervidya.model.AvatarModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Signup extends AppCompatActivity {

    RecyclerView recyclerView;
    RequestQueue requestQueue;
    LinearLayout lay_main,lay_signin;
    Dialog dialog_progress;
    ImageView imageview;
    Spinner spinner_bd,spinner_agent;
    String type="",board="";
    EditText agentet;
    EditText nameet,etmobile,etpin,etaddress;
    SessionHandler sessionHandler;
    String id,phone;
    String avaatar_id,avatar_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        define();



        spinner_bd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                board = String.valueOf(adapterView.getItemAtPosition(i));


            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        spinner_agent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type= String.valueOf(adapterView.getItemAtPosition(i));

                if(type.equals("Known through Agents"))
                {
                    agentet.setVisibility(View.VISIBLE);
                }
                else
                {
                    agentet.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        lay_signin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                String name=nameet.getText().toString();
                String address=etaddress.getText().toString();
                String pin=etpin.getText().toString();
                String agentid=agentet.getText().toString();
                avaatar_id= getIntent().getStringExtra("id");

                if(name.isEmpty())
                {
                    Snackbar snackbar=Snackbar.make(lay_main,"Please enter your name",Snackbar.LENGTH_LONG);
                    View sbView= snackbar.getView();
                    sbView.setBackgroundResource(R.color.colorprimarydark);
                    snackbar.show();
                }

               else if(address.isEmpty())
                {
                    Snackbar snackbar=Snackbar.make(lay_main,"Please enter your address",Snackbar.LENGTH_LONG);
                    View sbView= snackbar.getView();
                    sbView.setBackgroundResource(R.color.colorprimarydark);
                    snackbar.show();
                }
                else if(pin.isEmpty())
                {
                    Snackbar snackbar=Snackbar.make(lay_main,"Please enter your pin",Snackbar.LENGTH_LONG);
                    View sbView= snackbar.getView();
                    sbView.setBackgroundResource(R.color.colorprimarydark);
                    snackbar.show();
                }
                else if(avaatar_id.isEmpty())
                {
                    Snackbar snackbar=Snackbar.make(lay_main,"Please select your photo",Snackbar.LENGTH_LONG);
                    View sbView= snackbar.getView();
                    sbView.setBackgroundResource(R.color.colorprimarydark);
                    snackbar.show();
                }
                else if(board.contains("Select your Childrens Education Board") || board.isEmpty())
                {
                    Snackbar snackbar=Snackbar.make(lay_main,"Please select your student Board",Snackbar.LENGTH_LONG);
                    View sbView= snackbar.getView();
                    sbView.setBackgroundResource(R.color.colorprimarydark);
                    snackbar.show();
                }
                else if(agentid.contains("Known through Agents"))
                {
                  if(agentid.isEmpty())
                  {
                      Snackbar snackbar=Snackbar.make(lay_main,"Please enter Agent ID",Snackbar.LENGTH_LONG);
                      View sbView= snackbar.getView();
                      sbView.setBackgroundResource(R.color.colorprimarydark);
                      snackbar.show();
                  }
                }
                else
                {
                    register(name,board,address,id,avaatar_id,agentid,pin);
                }


            }
        });

        getavatar();


    }


    private   void define()
    {

        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        phone=intent.getStringExtra("phone");
        sessionHandler=new SessionHandler(this);
        nameet=findViewById(R.id.nameet);
        etmobile=findViewById(R.id.etmobile);
        etpin=findViewById(R.id.etpin);
        lay_signin=findViewById(R.id.lay_signin);
        etaddress=findViewById(R.id.etaddress);
        lay_main=findViewById(R.id.lay_main);

        recyclerView=findViewById(R.id.rid);
        imageview=findViewById(R.id.imageview);
        spinner_bd=findViewById(R.id.sp_bd);
        spinner_agent=findViewById(R.id.sp_agent);
        agentet=findViewById(R.id.etagent);
        requestQueue = RequestQueueSingleton.getInstance(this.getApplicationContext())
                .getRequestQueue();

        dialog_progress = new Dialog(this);
        dialog_progress.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_progress.setContentView(R.layout.progressbar);
        dialog_progress.setCancelable(false);
        dialog_progress.setCanceledOnTouchOutside(false);
        dialog_progress.setCancelable(false);
        dialog_progress.setCanceledOnTouchOutside(false);
        dialog_progress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        etmobile.setText(phone);
        etmobile.setEnabled(false);



    }


    private void getavatar()
    {
        ArrayList<AvatarModel> avatarModelArrayList=new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url.avatar,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s)
                    {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String status = jsonObject.getString("status");
                            if (status.contains("success"))
                            {
                                JSONArray jsonArray=jsonObject.getJSONArray("data");
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    AvatarModel avatarModel=new AvatarModel();
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    String id=jsonObject1.getString("id");
                                    String path=Url.base_avatar+jsonObject1.getString("path");
                                    avatarModel.setId(id);
                                    avatarModel.setImage(path);
                                    avatarModelArrayList.add(avatarModel);

                                }


                            }

                            AvatarAdapter adapter = new AvatarAdapter(Signup.this,avatarModelArrayList,imageview);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(Signup.this,RecyclerView.HORIZONTAL,false));
                            recyclerView.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
            }
        });
        requestQueue.add(stringRequest);
    }


    public void register(final  String name,final  String board,final  String address,final  String id,final String avatar,
                         final String agent_id,final String pin)
    {
        final ArrayList<HashMap<String,String>>list=new ArrayList<>();
        dialog_progress.show();
        JSONObject json = new JSONObject();

        try
        {
            json.put("key",sessionHandler.getuniquekey());
            json.put("name", name);
            json.put("board", board);
            json.put("address", address);
            json.put("id", id);
            json.put("avatar", avatar);
            json.put("agent_id", agent_id);
            json.put("pin", pin);




        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Url.editprofile, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject)
                    {
                        Toast.makeText(Signup.this, jsonObject.toString(), Toast.LENGTH_SHORT).show();

                        dialog_progress.dismiss();
                        try {
                            String response=jsonObject.getString("status");

                                if(response.contains("success"))
                                {
                                    Intent intent=new Intent(Signup.this,Homepage.class);
                                    startActivity(intent);

                                    HashMap<String, String> news = new HashMap<>();
                                    news.put("id", id);
                                    news.put("name", name);
                                    news.put("phone", phone);
                                    news.put("avatar_image", avatar_image);
                                    list.add(news);


                                    Gson gson = new Gson();
                                    List<HashMap<String, String>> textList = new ArrayList<>();
                                    textList.addAll(list);
                                    String jsonText = gson.toJson(textList);
                                    sessionHandler.createLoginSession(jsonText);


                                }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                dialog_progress.dismiss();

            }
        });

        requestQueue.add(jsonObjectRequest);
    }


    public class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.ViewHolder>
    {
        Context context;
        ArrayList<AvatarModel> avatarModelArrayList;
        ImageView imageView;

        public AvatarAdapter(Context context,  ArrayList<AvatarModel>  avatarModelArrayList,ImageView imageView) {
            this.context = context;
            this.avatarModelArrayList = avatarModelArrayList;
            this.imageView = imageView;
        }


        @NonNull
        @Override
        public AvatarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
            View listitem=layoutInflater.inflate(R.layout.avatarlay,parent,false);
            ViewHolder viewHolder=new ViewHolder(listitem);
            return  viewHolder;

        }

        @Override
        public void onBindViewHolder(@NonNull AvatarAdapter.ViewHolder holder, int i)
        {

            String id=avatarModelArrayList.get(i).getId();
            String image=avatarModelArrayList.get(i).getImage();

            Glide.with(context)
                    .load(image)
                    .centerCrop()
                    .into(holder.imageView);


            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Glide.with(context)
                            .load(image)
                            .centerCrop()
                            .into(imageView);
                    avaatar_id=id;
                    avatar_image=image;

                }
            });

        }

        @Override
        public int getItemCount() {
            return avatarModelArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView=itemView.findViewById(R.id.imageView);

            }
        }
    }


}