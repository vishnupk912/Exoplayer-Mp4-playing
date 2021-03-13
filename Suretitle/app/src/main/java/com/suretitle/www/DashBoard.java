package com.suretitle.www;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fragment.HomeFragment;
import fragment.ReportFragment;
import helper.ApiInterface;
import helper.CustomTypefaceclass;
import helper.SessionHandler;

public class DashBoard extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;

    String mobilenumber,customer_id,customer_name,country_id,state_id,country_name,state_name,email,profession,organisation,gender,place;
    SessionHandler sessionHandler;
    ImageView menu_button;

    Dialog dialog_alert;
    String block_status;
    Typeface fontRegular;
    LinearLayout drawer_layout;

    ImageView homeimage,reportsimage,closelayid;
    TextView hometvid,reportstvid;
    LinearLayout homelayid,reportlayid,accountlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_dash_board);

        homelayid=findViewById(R.id.homelayid);
        reportlayid=findViewById(R.id.reportlayid);
        homeimage=findViewById(R.id.homeid);
        reportsimage=findViewById(R.id.reportsid);
        hometvid=findViewById(R.id.hometvid);
        reportstvid=findViewById(R.id.reportstvid);
        accountlay=findViewById(R.id.accountlayid);
        closelayid=findViewById(R.id.closelayid);

        sessionHandler=new SessionHandler(this);
        menu_button=findViewById(R.id.menuid);
        fontRegular = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");

        drawer_layout = findViewById(R.id.drawer_layout);



        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DashBoard.this,Account.class);
                startActivity(intent);

            }
        });

        getcustomerdetails();
        token();
        is_blocked();
//        subscriptionplans();

        homeimage.setImageResource(R.drawable.ic_home2);
        reportsimage.setImageResource(R.drawable.ic_reports);
        hometvid.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        reportstvid.setTextColor(getResources().getColor(R.color.black1));
        loadFragment(new HomeFragment());

        homelayid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeimage.setImageResource(R.drawable.ic_home2);
                reportsimage.setImageResource(R.drawable.ic_reports);
                hometvid.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                reportstvid.setTextColor(getResources().getColor(R.color.black1));
                loadFragment(new HomeFragment());
            }
        });

        reportlayid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeimage.setImageResource(R.drawable.ic_home);
                reportsimage.setImageResource(R.drawable.ic_reports1);
                hometvid.setTextColor(getResources().getColor(R.color.black1));
                reportstvid.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                loadFragment(new ReportFragment());
            }
        });
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

    @Override
    public void onBackPressed() {
        try{
            LinearLayout drawer = (LinearLayout) findViewById(R.id.drawer_layout);

                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    ActivityCompat.finishAffinity(this);
                } else{
                    this.doubleBackToExitPressedOnce = true;
                    Snackbar snackbar = Snackbar
                            .make(drawer_layout, "Please click BACK again to exit", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundResource(R.color.colorPrimaryDark);
                    snackbar.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce=false;
                        }
                    }, 5000);
                }

        }catch (Exception e) {}
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    public void token()
    {
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, ApiInterface.token, new Response.Listener<String>() {
            @Override
            public void onResponse(String s)
            {
                Log.d("msg",s);
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    String response=jsonObject.getString("status");
                    if(response.contains("success"))
                    {
                        sessionHandler.createcount("0");
                        String token=jsonObject.getString("token");
                        sessionHandler.createtoken(token);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("key",ApiInterface.key);
                return  hashMap;
            }
        };
        requestQueue.add(stringRequest);
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
//
//                try {
//                    JSONObject jsonObject=new JSONObject(s);
//                    String response=jsonObject.getString("status");
//                    if(response.contains("success")) {
//                        String flag = jsonObject.getString("flag");
//
//
//                        if(flag.contains("1") || flag.contains("2"))
//
//                        {
//                            JSONArray jsonArray = jsonObject.getJSONArray("data");
//                            for (int i = 0; i < jsonArray.length(); i++)
//                            {
//                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
//
//                                String sid=jsonObject1.getString("sid");
//                                String plan_id=jsonObject1.getString("plan_id");
//                                String plan_name=jsonObject1.getString("plan_name");
//                                String price=jsonObject1.getString("price");
//                                String start=jsonObject1.getString("start");
//                                String end=jsonObject1.getString("end");
//
//                            }
//                        }
//                        else if(flag.contains("0"))
//                        {
//                            Intent intent=new Intent(DashBoard.this,PaymentPage.class);
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
    public void is_blocked()
    {

        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, ApiInterface.is_blocked, new Response.Listener<String>() {
            @Override
            public void onResponse(String s)
            {

                try {
                    JSONObject jsonObject=new JSONObject(s);
                    String response=jsonObject.getString("status");
                    if(response.contains("success"))
                    {

                        block_status=jsonObject.getString("block_status");
                        if(block_status.contains("1"))
                        {
                            Intent intent=new Intent(DashBoard.this,Login.class);
                            startActivity(intent);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("key",ApiInterface.key);
                hashMap.put("phone",mobilenumber);
                return  hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    public  void prompt(final  String headerdata,final  String subheaderdata)
    {
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

        headertv.setText(headerdata);
        subheadertv.setText(subheaderdata);

        cancellay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_alert.dismiss();
            }
        });
        oklay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_alert.dismiss();
            }
        });

        dialog_alert.show();

    }

}