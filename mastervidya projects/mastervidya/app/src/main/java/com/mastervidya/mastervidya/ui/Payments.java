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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.mastervidya.mastervidya.R;
import com.mastervidya.mastervidya.adapter.SubClassAdapter;
import com.mastervidya.mastervidya.adapter.SubscriptionAdapter;
import com.mastervidya.mastervidya.helper.RequestQueueSingleton;
import com.mastervidya.mastervidya.helper.SessionHandler;
import com.mastervidya.mastervidya.helper.Url;
import com.mastervidya.mastervidya.model.Paymentmodel;
import com.mastervidya.mastervidya.model.SubscibemonthsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Payments extends AppCompatActivity {

    SessionHandler sessionHandler;
    String id,name,phone,avatar_image;
    RequestQueue requestQueue;
    Dialog dialog_progress;
    LinearLayout addlay;
    RecyclerView recyclerView;
    ImageView back;
    LinearLayout noclasslay,classlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
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
                Intent intent=new Intent(Payments.this,Homepage.class);
                startActivity(intent);
            }
        });
        addlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Payments.this,AddClass.class);
                startActivity(intent);
            }
        });

        getcustomerdetails();
        getdata();

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

    public void getdata()
    {
        ArrayList<Paymentmodel> paymentmodelArrayList=new ArrayList<>();
        ArrayList<SubscibemonthsModel> subscibemonthsModelArrayList=new ArrayList<>();
        dialog_progress.show();
        JSONObject json = new JSONObject();
        try {
            json.put("key",sessionHandler.getuniquekey());
            json.put("id",id);
            Log.d("key",sessionHandler.getuniquekey());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Url.subscriptions, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog_progress.dismiss();
                        try {
                            String  status=response.getString("status");
                            if(status.contains("success"))
                            {
                                JSONArray jsonArray=response.getJSONArray("data");
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    Paymentmodel paymentmodel=new Paymentmodel();
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    String student_name=jsonObject.getString("student_name");
                                    String class_id=jsonObject.getString("class_id");
                                    String classs=jsonObject.getString("class");
                                    String payment_type=jsonObject.getString("payment_type");
                                    String amount=jsonObject.getString("amount");
                                    String subscription_type=jsonObject.getString("subscription_type");
                                    String board=jsonObject.getString("board");

                                    String date=jsonObject.getString("date");

                                    paymentmodel.setStudent_name(student_name);
                                    paymentmodel.setClass_id(class_id);
                                    paymentmodel.setClasss(classs);
                                    paymentmodel.setPayment_type(payment_type);
                                    paymentmodel.setAmount(amount);
                                    paymentmodel.setSubscription_type(subscription_type);
                                    paymentmodel.setBoard(board);
                                    paymentmodel.setDate(date);
                                    paymentmodelArrayList.add(paymentmodel);

                                    JSONArray jsonArray1_packages=jsonObject.getJSONArray("packages");

                                    for(int j=0;j<jsonArray1_packages.length();j++)
                                    {
                                        SubscibemonthsModel subscibemonthsModel=new SubscibemonthsModel();
                                        JSONObject jsonObject1=jsonArray1_packages.getJSONObject(j);
                                        String month=jsonObject1.getString("month");
                                        String year=jsonObject1.getString("year");

                                        subscibemonthsModel.setMonth(month);
                                        subscibemonthsModel.setYear(year);
                                        subscibemonthsModelArrayList.add(subscibemonthsModel);

                                    }

                                }
                                SubscriptionAdapter adapter = new SubscriptionAdapter(subscibemonthsModelArrayList,paymentmodelArrayList,Payments.this);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(Payments.this,RecyclerView.VERTICAL,false));
                                recyclerView.setAdapter(adapter);

                            }
                        } catch (JSONException e)
                        {
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
}