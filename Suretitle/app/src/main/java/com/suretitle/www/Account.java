package com.suretitle.www;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class Account extends AppCompatActivity {
    String mobilenumber,customer_id,customer_name,country_id,state_id,country_name,state_name,email,profession,organisation,gender,place;

    SessionHandler sessionHandler;
    LinearLayout linearLayoutmanage,linearLayoutterms,linearLayoutprivacypolicy,linearLayoutsignout,linearLayoutmembershipid,backid;

    TextView nametv,numbertv,emailtv;

    TextView plan_nametv,tvexpires;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_account);

        sessionHandler=new SessionHandler(this);
        linearLayoutmanage=findViewById(R.id.manageaccountid);
        linearLayoutterms=findViewById(R.id.termsconditionid);
        linearLayoutprivacypolicy=findViewById(R.id.privacypolicyid);
        linearLayoutsignout=findViewById(R.id.signoutid);
        linearLayoutmembershipid=findViewById(R.id.membershipid);
        backid=findViewById(R.id.backid);

        nametv=findViewById(R.id.nametv);
        numbertv=findViewById(R.id.numbertv);
        emailtv=findViewById(R.id.emailtv);
        plan_nametv=findViewById(R.id.tvmembership);
        tvexpires=findViewById(R.id.tvexpires);




        backid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Account.this,DashBoard.class);
                startActivity(intent);
            }
        });
        linearLayoutmanage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                Intent intent=new Intent(Account.this,EditProfile.class);
                overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
                startActivity(intent);

            }
        });
        linearLayoutterms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.suretitle.in/termsandconditions.html"));
                startActivity(browserIntent);
            }
        });
        linearLayoutprivacypolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        linearLayoutsignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            signout("Sign Out","Do you want to sign out from Sure Title ? ");
            }
        });
        linearLayoutmembershipid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionHandler.createbackpayment("1");
                Intent intent=new Intent(Account.this,PaymentPage.class);
                overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
                startActivity(intent);
            }
        });

        getcustomerdetails();
//        subscriptionplans();

    }
    public  void signout(String headerstr,String subheaderstr)
    {
        final Dialog dialog_alert;
        dialog_alert = new Dialog(this);
        dialog_alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_alert.setContentView(R.layout.alertbox);
        dialog_alert.setCancelable(false);
        dialog_alert.setCanceledOnTouchOutside(false);
        dialog_alert.setCancelable(false);
        dialog_alert.setCanceledOnTouchOutside(false);
        dialog_alert.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LinearLayout cancellay,oklay;
        TextView headertv, subheadertv,canceltv,oktv;

        cancellay=dialog_alert.findViewById(R.id.cancellayid);
        oklay=dialog_alert.findViewById(R.id.oklayid);
        headertv=dialog_alert.findViewById(R.id.headerid);
        subheadertv=dialog_alert.findViewById(R.id.sheaderid);
        canceltv=dialog_alert.findViewById(R.id.canceltvid);
        oktv=dialog_alert.findViewById(R.id.oktvid);

        headertv.setText(headerstr);
        subheadertv.setText(subheaderstr);

        cancellay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_alert.dismiss();
            }
        });
        oklay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionHandler.logoutUser();
            }
        });

        dialog_alert.show();

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

            nametv.setText(customer_name);
            numbertv.setText(mobilenumber);
            emailtv.setText(email);

        } catch (JSONException e) {

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(Account.this,DashBoard.class);
        startActivity(intent);
    }


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
//                        if(flag.contains("1")||flag.contains("2"))
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
//                                plan_nametv.setText("Membership Type: "+plan_name);
//                                tvexpires.setText("Expires On : "+end);
//
//                            }
//                        }
//                        else if(flag.contains("0"))
//                        {
//                            Intent intent=new Intent(Account.this,PaymentPage.class);
//                            startActivity(intent);
//                        }
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