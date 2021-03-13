package com.suretitle.www;

import androidx.appcompat.app.AppCompatActivity;

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

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import helper.ApiInterface;
import helper.SessionHandler;
import model.QuestionCategoryModel;

public class ResultPage extends AppCompatActivity {

    SessionHandler sessionHandler;
    Dialog dialog,dialog_alert;
    Double sum_obt_mark = 0.0,sum_total_mark=0.0;
    ImageView imageView_status;
    TextView data_statustv,scoretvid;
    String token;
    String mobilenumber,customer_id,customer_name,country_id,state_id,country_name,state_name,email,profession,organisation,gender,place;
    ImageView closeidimage;
    LinearLayout layid;
    String propertyname;

    @Override
    public void onBackPressed()
    {
        Intent intent=new Intent(ResultPage.this,DashBoard.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_result_page);


        Intent intent=getIntent();
        propertyname=intent.getStringExtra("propertyname");

        layid=findViewById(R.id.layid);
        sessionHandler=new SessionHandler(this);
        imageView_status=findViewById(R.id.star);
        data_statustv=findViewById(R.id.tvid);
        scoretvid=findViewById(R.id.scoretvid);
        closeidimage=findViewById(R.id.closeid);

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        HashMap<String, String> data2= sessionHandler.gettoken();
        token = data2.get(sessionHandler.KEY_TOKEN);

        getcustomerdetails();
        results();
//        subscriptionplans();

        layid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionHandler.createbackpayment("1");
                Intent intent=new Intent(ResultPage.this,PaymentPage.class);
                startActivity(intent);
            }
        });

        closeidimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(ResultPage.this,DashBoard.class);
                startActivity(intent1);
            }
        });

    }


    public  void results()
    {

        final ArrayList<Double> listobtmark=new ArrayList<>();
        final ArrayList<Double> listtotalmark=new ArrayList<>();

        dialog.show();
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, ApiInterface.view_result, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                dialog.dismiss();
                try {
                    Log.d("resposne",s);
                    JSONObject jsonObject=new JSONObject(s);
                    String response=jsonObject.getString("status");
                    if(response.contains("success"))
                    {
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        for(int i =0;i<jsonArray.length();i++)
                        {


                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            String  category_id=jsonObject1.getString("category_id");
                            String  category_name=jsonObject1.getString("category_name");
                            String  obt_mark=jsonObject1.getString("obt_mark");
                            String  total_mark=jsonObject1.getString("total_mark");

                            Double obt_mark1=Double.valueOf(obt_mark);
                            Double total_mark1=Double.valueOf(total_mark);

                            listobtmark.add(obt_mark1);
                            listtotalmark.add(total_mark1);

                        }

                        for( Double num : listobtmark)
                        {
                            sum_obt_mark = sum_obt_mark+num;

                        }



                        for( Double num : listtotalmark)
                        {
                            sum_total_mark = sum_total_mark+num;
                        }
                        Log.d("sum_obt_mark", String.valueOf(sum_obt_mark));
                        Log.d("sum_total_mark", String.valueOf(sum_total_mark));



                        scoretvid.setText(String.valueOf(Math.round(sum_obt_mark)));

                        if(sum_obt_mark>=91.0 && sum_obt_mark<=100.0)
                        {
                            imageView_status.setImageResource(R.drawable.ic_winner);
                            data_statustv.setText("According to the analysis,you have earned SureTitle Class Badge,which indicates you can buy this "+propertyname+".");
                        }
                        else if(sum_obt_mark>=71.0 && sum_obt_mark<=90.0)
                        {
                            imageView_status.setImageResource(R.drawable.goldimg);
                            data_statustv.setText("According to the analysis ,you have earned Gold Badge,which indicates that you can buy this "+propertyname+".However after buying certain update of records and documentations are required.");
                        }
                        else if(sum_obt_mark>=51.0 && sum_obt_mark<=70.0)
                        {
                            imageView_status.setImageResource(R.drawable.silverimg);
                            data_statustv.setText("According to the analysis ,you have earned Silver Badge,which indicates that due to the certain risk you may buy this "+propertyname+" subject to the seller clearing the risks and /or cleaning the Title of this land"+propertyname+".");

                        }
                        else if(sum_obt_mark>=0.0 && sum_obt_mark<=50.0)
                        {
                            imageView_status.setImageResource(R.drawable.bronzeimg);
                            data_statustv.setText("According to the analysis, you have earned a BRONZE badge,which indicates that high risk is involved,hence not advisable to buy the "+propertyname+".");

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("key",ApiInterface.key);
                hashMap.put("customer_id",mobilenumber);
                hashMap.put("token",token);
                return  hashMap;
            }
        };

        requestQueue.add(stringRequest);
    }

    public void getcustomerdetails()
    {
        Gson gson = new Gson();
        HashMap<String, String> user = sessionHandler.getLoginSession();
        String json = user.get(SessionHandler.KEY_LOGIN);
        ArrayList alist = gson.fromJson(json, ArrayList.class);

        JSONArray jsonArrA = new JSONArray(alist);
        try {
            JSONObject userdata = jsonArrA.getJSONObject(0);
            mobilenumber = userdata.getString("mobile");
            customer_name = userdata.getString("customer_name");
            country_id = userdata.getString("country_id");
            state_id = userdata.getString("state_id");
            email = userdata.getString("email");
            profession = userdata.getString("profession");
            organisation = userdata.getString("organisation");
            gender = userdata.getString("gender");
            place = userdata.getString("place");


        } catch (JSONException e) {

        }
    }
//
//    public void subscriptionplans()
//    {
//
//
//        RequestQueue requestQueue;
//        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
//        Network network = new BasicNetwork(new HurlStack());
//        requestQueue = new RequestQueue(cache, network);
//        requestQueue.start();
//
//
//        StringRequest stringRequest=new StringRequest(Request.Method.POST, ApiInterface.subscription_status, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                try {
//                    JSONObject jsonObject=new JSONObject(s);
//                    String response=jsonObject.getString("status");
//                    if(response.contains("success")) {
//                        String flag = jsonObject.getString("flag");
//
//
//                        if(flag.contains("2"))
//                        {
//                            JSONArray jsonArray = jsonObject.getJSONArray("data");
//                            for (int i = 0; i < jsonArray.length(); i++)
//                            {
//                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
//
//                                String plan_name=jsonObject1.getString("plan_name");
//                                String sid=jsonObject1.getString("sid");
//                                String plan_id=jsonObject1.getString("plan_id");
//                                String price=jsonObject1.getString("price");
//                                String start=jsonObject1.getString("start");
//                                String end=jsonObject1.getString("end");
//
//
//                                layid.setVisibility(View.GONE);
//
//                            }
//                        }
//                        else if(flag.contains("1"))
//                        {
//                            JSONArray jsonArray = jsonObject.getJSONArray("data");
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//
//                                String plan_name = jsonObject1.getString("plan_name");
//                                String sid = jsonObject1.getString("sid");
//                                String plan_id = jsonObject1.getString("plan_id");
//                                String price = jsonObject1.getString("price");
//                                String start = jsonObject1.getString("start");
//                                String end = jsonObject1.getString("end");
//
//
//                                layid.setVisibility(View.VISIBLE);
//
//                            }
//                        }
//                        else if(flag.contains("0"))
//                        {
//                            Intent intent=new Intent(ResultPage.this, PaymentPage.class);
//                            startActivity(intent);
//
//                            layid.setVisibility(View.VISIBLE);
//                        }
//
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        })
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String,String> hashMap=new HashMap<>();
//                hashMap.put("key",ApiInterface.key);
//                hashMap.put("customer_id",mobilenumber);
//                return  hashMap;
//            }
//        };
//        requestQueue.add(stringRequest);
//
//
//    }

}