package com.mastervidya.mastervidya.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mastervidya.mastervidya.R;
import com.mastervidya.mastervidya.helper.RequestQueueSingleton;
import com.mastervidya.mastervidya.helper.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TransactionActivity extends AppCompatActivity {

    String key,id,studentname,class_id,payment_type,agent_id,amount,transaction_id,subscription_type;
    ArrayList<String> arrayList_month=new ArrayList<>();
    ArrayList<String> arrayList_year=new ArrayList<>();

    TextView tv_status;

    ArrayList<String> arrayList_month1=new ArrayList<>();
    ArrayList<String> arrayList_year1=new ArrayList<>();
    RequestQueue requestQueue;
    LottieAnimationView lottieAnimationView;
    LinearLayout myclasslay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        requestQueue = RequestQueueSingleton.getInstance((this))
                .getRequestQueue();

        tv_status=findViewById(R.id.tv_status);
        lottieAnimationView=findViewById(R.id.animation_view);
        myclasslay=findViewById(R.id.myclasslay);

        Intent intent=getIntent();

       key= intent.getStringExtra("key");
        id=intent.getStringExtra("id");
        studentname=intent.getStringExtra("studentname");
        class_id=intent.getStringExtra("class_id");
        payment_type=intent.getStringExtra("payment_type");
        agent_id= intent.getStringExtra("agent_id");
        amount=intent.getStringExtra("amount");
        transaction_id=intent.getStringExtra("transaction_id");
        subscription_type=intent.getStringExtra("subscription_type");
       arrayList_month= intent.getStringArrayListExtra("arrayList_month");
        arrayList_year =intent.getStringArrayListExtra("arrayList_year");
        arrayList_month1=   intent.getStringArrayListExtra("arrayList_month1");
        arrayList_year1=  intent.getStringArrayListExtra("arrayList_year1");
        myclasslay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TransactionActivity.this,MyClass.class);
                startActivity(intent);
            }
        });

        subscribe();
    }



    public void subscribe()
    {
        JSONObject json = new JSONObject();
        try {

            json.put("key", key);
            json.put("id", id);
            json.put("student_name", studentname);
            json.put("class_id",class_id );
            json.put("payment_type",payment_type );
            json.put("agent_id",agent_id );
            json.put("amount",Double.valueOf(amount) );
            json.put("transaction_id", transaction_id);
            json.put("subscription_type", subscription_type);

            Log.d("amount",amount);
            JSONArray array=new JSONArray();

            if(subscription_type.contains("1"))
            {
                for(int i=0;i<arrayList_month.size();i++)
                {
                    JSONObject obj=new JSONObject();

                    try {
                        obj.put("month",arrayList_month.get(i));
                        obj.put("year",arrayList_year.get(i));
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    array.put(obj);
                }
            }
            else if(subscription_type.contains("2"))
            {
                for(int i=0;i<arrayList_month1.size();i++)
                {
                    JSONObject obj=new JSONObject();

                    try {
                        obj.put("month",arrayList_month1.get(i));
                        obj.put("year",arrayList_year1.get(i));
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    array.put(obj);
                }
            }



            json.put("packages",array);





        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, Url.subscribe, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject)
            {
                Log.d("data",jsonObject.toString());
                try {
                    String status=jsonObject.getString("status");
                    if(status.contains("success"))
                    {

                        tv_status.setText("Your payment has been sucessfully completed");
                        lottieAnimationView.setAnimation("success.json");
                        myclasslay.setVisibility(View.VISIBLE);

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

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(TransactionActivity.this,MyClass.class);
        startActivity(intent);
    }
}