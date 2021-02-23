package com.mastervidya.mastervidya.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.mastervidya.mastervidya.Addclass.PayForclassFragment;
import com.mastervidya.mastervidya.R;
import com.mastervidya.mastervidya.helper.RequestQueueSingleton;
import com.mastervidya.mastervidya.helper.SessionHandler;
import com.mastervidya.mastervidya.helper.Url;
import com.mastervidya.mastervidya.model.SubModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Profile extends AppCompatActivity {

    RequestQueue requestQueue;

    LinearLayout lay1,lay2;
    TextView tv1,tv2;
    SessionHandler sessionHandler;
    String id,name,phone,avatar_image;
    Dialog dialog_progress,dialog_month;
    ImageView imageView;

    TextView nametv,phonetv,addresstv,boardtv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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


        lay1=findViewById(R.id.lay1);
        lay2=findViewById(R.id.lay2);
        tv1=findViewById(R.id.tv1);
        tv2=findViewById(R.id.tv2);

        imageView=findViewById(R.id.imageid);
        nametv=findViewById(R.id.nameid);
        phonetv=findViewById(R.id.phonetv);
        addresstv=findViewById(R.id.addresstv);
        boardtv=findViewById(R.id.boardtv);

        lay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                lay1.setBackgroundResource(R.drawable.buttonshape2);
                tv1.setTextColor(getResources().getColor(R.color.darkblack));

                lay2.setBackgroundResource(R.drawable.buttonshape9);
                tv2.setTextColor(getResources().getColor(R.color.dgrey2));
            }
        });

        lay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lay2.setBackgroundResource(R.drawable.buttonshape2);
                tv2.setTextColor(getResources().getColor(R.color.darkblack));

                lay1.setBackgroundResource(R.drawable.buttonshape9);
                tv1.setTextColor(getResources().getColor(R.color.dgrey2));
            }
        });

        getcustomerdetails();
        getprofiledetails();
    }

    public void getprofiledetails()
    {
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Url.viewprofile, json,
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
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    String id=jsonObject1.getString("id");
                                    String name=jsonObject1.getString("name");
                                    String phone=jsonObject1.getString("phone");
                                    String avatar=Url.base_avatar+jsonObject1.getString("avatar")+".png";
                                    String board=jsonObject1.getString("board");
                                    String address=jsonObject1.getString("address");
                                    String pin=jsonObject1.getString("pin");

                                    nametv.setText(name);
                                    phonetv.setText(phone);
                                    Glide.with(Profile.this)
                                            .load(avatar)
                                            .centerCrop()
                                            .into(imageView);
                                    boardtv.setText(board);
                                    addresstv.setText(address+","+pin);
                                }
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