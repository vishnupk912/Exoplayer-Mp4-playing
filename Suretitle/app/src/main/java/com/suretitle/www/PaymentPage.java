package com.suretitle.www;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fragment.HomeFragment;
import helper.ApiInterface;
import helper.SessionHandler;
import model.Plansmodel;
import model.PropertyModel;

public class PaymentPage extends AppCompatActivity  implements PaymentResultListener {

    Dialog dialog,dialog_alert;
    String mobilenumber,customer_id,customer_name,country_id,state_id,country_name,state_name,email,profession,organisation,gender,place;

    String planid1,backvalue;
    RecyclerView plansrv;
    SessionHandler sessionHandler;
    LinearLayout laymain;
    ImageView backid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_payment_page);


        sessionHandler=new SessionHandler(PaymentPage.this);
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        HashMap<String, String> data3= sessionHandler.getbackpayment();
        backvalue = data3.get(sessionHandler.KEY_BACK);


        backid=findViewById(R.id.backid);
        plansrv=findViewById(R.id.plans);
        laymain=findViewById(R.id.laymain);
            getcustomerdetails();
            plansmeth();

//            subscriptionplans();

            backid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(backvalue.contains("1"))
                    {
                        finish();
                        sessionHandler.createbackpayment("0");
                    }
                    else
                    {
                        return;

                    }
                }
            });
    }

    @Override
    public void onBackPressed()
    {
        if(backvalue.contains("1"))
        {
            finish();
            sessionHandler.createbackpayment("0");
        }
        else
        {
            return;

        }

    }

    public void plansmeth()
    {
        dialog.show();
        final ArrayList<Plansmodel> plansmodelArrayList = new ArrayList<>();
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();


        StringRequest stringRequest=new StringRequest(Request.Method.POST, ApiInterface.plans, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                dialog.dismiss();
                try {
                    JSONObject jsonObject1=new JSONObject(s);
                    String response=jsonObject1.getString("status");
                    if(response.contains("success"))
                    {
                        JSONArray jsonArray=jsonObject1.getJSONArray("data");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            Plansmodel plansmodel=new Plansmodel();
                            JSONObject jsonObject11=jsonArray.getJSONObject(i);
                            String plan_id=jsonObject11.getString("plan_id");
                            String plan_name=jsonObject11.getString("plan_name");
                            String price=jsonObject11.getString("price");
                            String validity=jsonObject11.getString("validity");

                            plansmodel.setPlan_id(plan_id);
                            plansmodel.setPlan_name(plan_name);
                            plansmodel.setPrice(price);
                            plansmodel.setValidity(validity);
                            plansmodelArrayList.add(plansmodel);

                            PlansAdapter plansAdapter = new PlansAdapter(PaymentPage.this,plansmodelArrayList);
                            plansrv.setHasFixedSize(true);
                            plansrv.setLayoutManager(new LinearLayoutManager(PaymentPage.this,RecyclerView.HORIZONTAL,false));
                            plansrv.setAdapter(plansAdapter);
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
                Toast.makeText(PaymentPage.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();


                hashMap.put("key",ApiInterface.key);
                hashMap.put("customer_id",mobilenumber);

                return  hashMap;
            }
        };


        requestQueue.add(stringRequest);

    }



    class  PlansAdapter extends RecyclerView.Adapter<PlansAdapter.ViewHolder>
    {

        Context context;
        private ArrayList<Plansmodel> listdata;

        public PlansAdapter(Context context, ArrayList<Plansmodel> listdata) {
            this.context = context;
            this.listdata = listdata;
        }

        @NonNull
        @Override
        public PlansAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.list_item4, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull PlansAdapter.ViewHolder holder, int i) {
            final String planid=listdata.get(i).getPlan_id();
            final String planename=listdata.get(i).getPlan_name();
            final String planvalidity=listdata.get(i).getValidity();
            final String plancost=listdata.get(i).getPrice();

            holder.planname.setText(planename);
            holder.planvalidity.setText(planvalidity+" Days Validity");
            holder.plancost.setText("Rs "+plancost);

            if (plancost.contains("0.0"))
            {
                holder.paytv.setText("TRY FREE VERSION > ");

                holder.paylayid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        subscribefree(planid);
                    }
                });
            }
            else
            {

                holder.paylayid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        planid1=planid;
                        startPayment(plancost,planid);
                    }
                });
            }



        }

        @Override
        public int getItemCount() {
            return listdata.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView planname,plancost,planvalidity;
            LinearLayout paylayid;
            TextView paytv;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                planname=itemView.findViewById(R.id.plannameid);
                plancost=itemView.findViewById(R.id.amountid);
                planvalidity=itemView.findViewById(R.id.tvvalidityid);
                paylayid=itemView.findViewById(R.id.paynowbtid);
                paytv=itemView.findViewById(R.id.paytvid);

            }
        }
    }


    public void startPayment(final  String plancost,final String planid) {

        final Activity activity = this;
        final Checkout co = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Suretitle");
            options.put("description", "App Payment");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");
            String payment = plancost;
            // amount is in paise so please multiple it by 100
            //Payment failed Invalid amount (should be passed in integer paise. Minimum value is 100 paise, i.e. ₹ 1)
            double total = Double.parseDouble(payment);
            total = total * 100;
            options.put("amount", total);
            JSONObject preFill = new JSONObject();
            preFill.put("email", email);
            preFill.put("contact", mobilenumber);
            options.put("prefill", preFill);
            co.open(activity, options);
        }
        catch (Exception e)
        {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    @Override
    public void onPaymentSuccess(String s) {
        Log.e("TAG", " payment successfull "+ s.toString());


        Snackbar snackbar = Snackbar
                .make(laymain, "Payment successfully done! ", Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();

        subscribe(planid1);

    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e("TAG",  "error code "+String.valueOf(i)+" -- Payment failed "+s.toString()  );
        try {
            Snackbar snackbar = Snackbar
                    .make(laymain, "Payment error please try again", Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            sbView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
        }
        catch (Exception e)
        {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);


        }
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

    public void subscribe(final String plan_id)
    {
        dialog.show();
        final ArrayList<Plansmodel> plansmodelArrayList = new ArrayList<>();
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, ApiInterface.subscribe, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                dialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    String response=jsonObject.getString("status");
                    if(response.contains("success"))
                    {

                        Intent intent=new Intent(PaymentPage.this,DashBoard.class);
                        startActivity(intent);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("plan_id",plan_id);
                hashMap.put("key",ApiInterface.key);
                hashMap.put("customer_id",mobilenumber);
                return  hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }


    public void subscribefree(final String plan_id)
    {
        dialog.show();

        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, ApiInterface.subscribe, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                dialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    String response=jsonObject.getString("status");
                    if(response.contains("success"))
                    {


                        Snackbar snackbar = Snackbar
                                .make(laymain, "Now enjoy the app for free", Snackbar.LENGTH_LONG);
                        View sbView = snackbar.getView();
                        sbView.setBackgroundResource(R.color.colorPrimaryDark);
                        snackbar.show();

                        Intent intent=new Intent(PaymentPage.this,DashBoard.class);
                        startActivity(intent);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("plan_id",plan_id);
                hashMap.put("key",ApiInterface.key);
                hashMap.put("customer_id",mobilenumber);
                return  hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }


//    public void subscriptionplans()
//    {
//        dialog.show();
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
//                dialog.dismiss();
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
//
//
//                            }
//                        }
//                        else if(flag.contains("0"))
//                        {
//
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