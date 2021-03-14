package com.mastervidya.mastervidya.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
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
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.mastervidya.mastervidya.R;
import com.mastervidya.mastervidya.adapter.DemovideoAdapter;
import com.mastervidya.mastervidya.adapter.HomeViewPagerAdapter;
import com.mastervidya.mastervidya.adapter.ViewPagerAdapter;
import com.mastervidya.mastervidya.helper.RequestQueueSingleton;
import com.mastervidya.mastervidya.helper.SessionHandler;
import com.mastervidya.mastervidya.helper.Url;
import com.mastervidya.mastervidya.model.Demovideomodel;
import com.mastervidya.mastervidya.model.Homepagermodel;
import com.mastervidya.mastervidya.model.PagerModel;
import com.mastervidya.mastervidya.video.OnlinePlayerActivity;
import com.razorpay.PaymentResultListener;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class Homepage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    NavigationView nav_view;
    DrawerLayout drawer_layout;
    RequestQueue requestQueue;

    WormDotsIndicator dotsIndicator;
    ViewPager2 viewPager;
    LinearLayout laytool;

    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 2000; // time in milliseconds between successive task executions.
    ArrayList<Homepagermodel> pagerModelArrayList=new ArrayList<>();

    RecyclerView recyclerView_demovideo;

    SessionHandler sessionHandler;
    String id,name,phone,avatar_image;
    boolean doubleBackToExitPressedOnce = false;

    TextView nametv,wishtv;
    ImageView image1;
    FrameLayout lay1;
    FrameLayout lay3,lay2,lay4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);


        dotsIndicator = (WormDotsIndicator) findViewById(R.id.dots_indicator);
        viewPager=findViewById(R.id.viewPager);
        lay1=findViewById(R.id.lay1);
        lay2=findViewById(R.id.lay2);
        lay3=findViewById(R.id.lay3);
        lay4=findViewById(R.id.lay4);
        requestQueue = RequestQueueSingleton.getInstance(this)
                .getRequestQueue();



        recyclerView_demovideo=findViewById(R.id.rvid);

        sessionHandler=new SessionHandler(this);
        image1=findViewById(R.id.image1);
        nametv=findViewById(R.id.nametv);
        wishtv=findViewById(R.id.wishtv);
        nav_view = findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(Homepage.this);
        nav_view.setItemIconTintList(null);
        drawer_layout = findViewById(R.id.drawer_layout);
        laytool=findViewById(R.id.laytool);
        viewPager=findViewById(R.id.viewPager);

        drawer_layout.closeDrawer(Gravity.LEFT);


        getcustomerdetails();











        CompositePageTransformer compositePageTransformer=new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r=1-Math.abs(position);
                page.setScaleY(0.85f+r*0.15f);

            }
        });
        viewPager.setPageTransformer(compositePageTransformer);

        autslider(viewPager);
        laytool.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
            setDrawer();

            }
        });

        View header = nav_view.getHeaderView(0);

        TextView tvNav1;
        LinearLayout chatlay,signoutlay,myprofile;
        ImageView imgNav;
        chatlay=header.findViewById(R.id.chatlay);
        signoutlay=header.findViewById(R.id.signoutlay);
        imgNav=header.findViewById(R.id.imgNav);
        myprofile=header.findViewById(R.id.myprofile);
        tvNav1=header.findViewById(R.id.tvNav1);
        myprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDrawer();
                Intent intent=new Intent(Homepage.this,Profile.class);
                startActivity(intent);
            }
        });

        Glide.with(this)
                .load(avatar_image)
                .centerCrop()
                .into(imgNav);
        tvNav1.setText(name);

        signoutlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                prompt("Sign Out","Do you want logout from the Application ?");
            }
        });
        chatlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                setDrawer();
                Intent intent=new Intent(Homepage.this,Chat.class);
                startActivity(intent);
            }
        });

        lay4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Homepage.this,Payments.class);
                startActivity(intent);
            }
        });

        lay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Homepage.this,SpecialStudy.class);
                startActivity(intent);
            }
        });
        lay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Homepage.this,MyClass.class);
                startActivity(intent);
            }
        });

        lay3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Homepage.this, OnlinePlayerActivity.class);
                startActivity(intent);
            }
        });
        getTimeFromAndroid();

        nametv.setText("Hello "+name);
        Glide.with(this)
                .load(avatar_image)
                .centerCrop()
                .into(image1);
        getdashboard();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        return false;
    }


    private void setDrawer()
    {
        if (drawer_layout.isDrawerOpen(Gravity.LEFT)) {
            drawer_layout.closeDrawer(Gravity.LEFT);
        } else {
            drawer_layout.openDrawer(Gravity.LEFT);
        }
    }

    public void setsize(ViewPager2 viewPager)
    {
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth(); // ((display.getWidth()*20)/100)
        int height =((display.getHeight()*20)/100);

        FrameLayout.LayoutParams parms = new FrameLayout.LayoutParams(width,height);
        viewPager.setLayoutParams(parms);
    }

    public void autslider(ViewPager2 viewPager)
    {
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == pagerModelArrayList.size())
                {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask()
        { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }




    @Override
    protected void onResume() {
        super.onResume();
        drawer_layout.closeDrawer(Gravity.LEFT);
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

    @Override
    public void onBackPressed()
    {
        try{
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(Gravity.LEFT)) {
                drawer.closeDrawer(Gravity.LEFT);
            } else {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    ActivityCompat.finishAffinity(this);
                } else{
                    this.doubleBackToExitPressedOnce = true;
                    Snackbar snackbar = Snackbar
                            .make(drawer_layout, "Please click BACK again to exit", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundResource(R.color.colorprimarydark);
                    snackbar.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce=false;
                        }
                    }, 5000);
                }
            }
        }
        catch (Exception e)
        {

        }
    }
    private void getTimeFromAndroid() {


        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12)
        {
            wishtv.setText("Good Morning");
        }
        else if(timeOfDay >= 12 && timeOfDay < 16)
        {
            wishtv.setText("Good Afternoon");
        }
        else if(timeOfDay >= 16 && timeOfDay < 21)
        {
            wishtv.setText("Good Evening");
        }
        else if(timeOfDay >= 21 && timeOfDay < 24)
        {
            wishtv.setText("Good Night");
        }

    }

    public void getdashboard()
    {
        ArrayList<Demovideomodel> demovideomodelArrayList=new ArrayList<>();

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


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Url.dashboard, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try {
                            String status=response.getString("status");
                            if(status.contains("success"))
                            {
                                JSONObject jsonObject1=response.getJSONObject("data");
                                JSONArray jsonArray_demovideos=jsonObject1.getJSONArray("demo_videos");
                                JSONArray jsonArray_slider=jsonObject1.getJSONArray("slider");


                                for(int i=0;i<jsonArray_demovideos.length();i++)
                                {
                                    Demovideomodel demovideomodel=new Demovideomodel();
                                    JSONObject jsonObject=jsonArray_demovideos.getJSONObject(i);
                                    String url=jsonObject.getString("url");
                                    String thumbnail=jsonObject.getString("thumbnail");

                                    demovideomodel.setDemovideo_image(thumbnail);
                                    demovideomodel.setDemovideo_url(url);
                                    demovideomodelArrayList.add(demovideomodel);

                                }

                                for(int i=0;i<jsonArray_slider.length();i++)
                                {
                                    Homepagermodel pagerModel=new Homepagermodel();
                                    JSONObject jsonObject=jsonArray_slider.getJSONObject(i);
                                    String url=jsonObject.getString("url");

                                    Log.d("imagesss",url);
                                    pagerModel.setImage(url);

                                    pagerModelArrayList.add(pagerModel);

                                }

                            }
                            else if(status.contains("invalid api key"))
                            {
                                Dialog dialog;
                                dialog = new Dialog(Homepage.this);
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

                            DemovideoAdapter adapter = new DemovideoAdapter(Homepage.this,demovideomodelArrayList);
                            recyclerView_demovideo.setHasFixedSize(true);
                            recyclerView_demovideo.setLayoutManager(new LinearLayoutManager(Homepage.this,RecyclerView.HORIZONTAL,false));
                            recyclerView_demovideo.setAdapter(adapter);



                            HomeViewPagerAdapter viewPagerAdapter=new HomeViewPagerAdapter(Homepage.this,pagerModelArrayList);
                            viewPager.setAdapter(viewPagerAdapter);
                            viewPager.setClipChildren(false);
                            viewPager.setClipToPadding(false);
                            viewPager.setOffscreenPageLimit(3);
                            viewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
                            dotsIndicator.setViewPager2(viewPager);

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

    public void prompt(final  String header,final String subheader)
    {
        Dialog dialog;
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alertdialog);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        LinearLayout oklay=dialog.findViewById(R.id.okid);
        LinearLayout cancellay=dialog.findViewById(R.id.cancelid);
        TextView headertv=dialog.findViewById(R.id.tvhead_id);
        TextView subheadertv=dialog.findViewById(R.id.tv1_id);

        headertv.setText(header);
        subheadertv.setText(subheader);

        cancellay.setVisibility(View.VISIBLE);
        oklay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                sessionHandler.logoutUser();
            }
        });

        cancellay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });

    }

}