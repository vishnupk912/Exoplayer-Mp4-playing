package com.suretitle.www;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.api.Api;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fragment.HomeFragment;
import helper.ApiInterface;
import helper.SessionHandler;
import model.PropertyModel;
import model.QuestionCategoryModel;

public class QuestionCatogery extends AppCompatActivity {
    Dialog dialog,dialog_alert;
    SessionHandler sessionHandler;
    String mobilenumber,customer_id,customer_name,country_id,state_id,country_name,state_name,email,profession,organisation,gender;
    String propertyid,propertyname,count,token;
    TextView propertynameid,propertymainname;
    LinearLayout backid;
    int listsize;

    @Override
    public void onBackPressed()
    {
        Intent intent1=new Intent(QuestionCatogery.this,DashBoard.class);
        startActivity(intent1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_question_catogery);



        backid=findViewById(R.id.backid);
        propertymainname=findViewById(R.id.propertynameid);
        propertynameid=findViewById(R.id.propertynameid);
        sessionHandler=new SessionHandler(this);

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));



        HashMap<String, String> data3= sessionHandler.getcount();
        count = data3.get(sessionHandler.KEY_COUNT);


        HashMap<String, String> data2= sessionHandler.gettoken();
        token = data2.get(sessionHandler.KEY_TOKEN);

        Intent intent=getIntent();
        propertyid=intent.getStringExtra("propertyid");
        propertyname=intent.getStringExtra("propertyname");

        propertymainname.setText(propertyname);

        getcustomerdetails();
        questcatmethod();
//        subscriptionplans();


        backid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent1=new Intent(QuestionCatogery.this,DashBoard.class);
            startActivity(intent1);
            }
        });

    }

    public void questcatmethod()
    {

        dialog.show();
        final ArrayList<QuestionCategoryModel > questionCategoryModelArrayList = new ArrayList<>();
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, ApiInterface.category, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("response",s);
                dialog.dismiss();
                try {
                    JSONObject jsonObject1=new JSONObject(s);
                    String response=jsonObject1.getString("status");
                    if(response.contains("success"))
                    {

                        JSONArray jsonArray=jsonObject1.getJSONArray("data");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            QuestionCategoryModel questionCategoryModel=new QuestionCategoryModel();
                            JSONObject jsonObject11=jsonArray.getJSONObject(i);
                            String category_id=jsonObject11.getString("category_id");
                            String category_name=jsonObject11.getString("category_name");
                            String filename=jsonObject11.getString("filename");
                            String status=jsonObject11.getString("status");


                            questionCategoryModel.setCategory_id(category_id);
                            questionCategoryModel.setCategory_name(category_name);
                            questionCategoryModel.setImage(filename);
                            questionCategoryModel.setStatus(status);
                            questionCategoryModelArrayList.add(questionCategoryModel);

                            listsize=questionCategoryModelArrayList.size();


                            if(Integer.valueOf(count)==jsonArray.length())
                            {

                                Intent intent=new Intent(QuestionCatogery.this,Termsandconditions.class);
                                intent.putExtra("propertyname",propertyname);
                                startActivity(intent);
                            }

                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvid);
                            CategoryQuestionAdapter adapter = new CategoryQuestionAdapter(QuestionCatogery.this,questionCategoryModelArrayList);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(QuestionCatogery.this,RecyclerView.VERTICAL,false));
                            recyclerView.setAdapter(adapter);

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
                dialog.dismiss();
                prompt("Server Error","The server encountered an internal error and was unable to complete your request");

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();

                hashMap.put("country_id",country_id);
                hashMap.put("state_id",state_id);
                hashMap.put("property_type_id",propertyid);
                hashMap.put("key",ApiInterface.key);
                hashMap.put("token",token);

//                Log.d("country_id",country_id);
//                Log.d("state_id",state_id);
//                Log.d("property_type_id",propertyid);
//                Log.d("key",ApiInterface.key);
//                Log.d("token",token);

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

        } catch (JSONException e) {

        }
    }

    class CategoryQuestionAdapter extends RecyclerView.Adapter<CategoryQuestionAdapter.ViewHolder>
    {

        Context context;
        private ArrayList<QuestionCategoryModel> listdata;

        public CategoryQuestionAdapter(Context context, ArrayList<QuestionCategoryModel> listdata) {
            this.context = context;
            this.listdata = listdata;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.list_item1, parent, false);
           ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int i)
        {
            final String category_id=listdata.get(i).getCategory_id();
            String category_name=listdata.get(i).getCategory_name();
            String filename=listdata.get(i).getImage();
            String status=listdata.get(i).getStatus();

            holder.categoryname.setText(category_name);

            if (status.contains("1"))
            {
                holder.completedlayid.setVisibility(View.VISIBLE);
                holder.lay.setClickable(false);
                holder.completedlayid.setClickable(false);
            }
            else
            {
                holder.completedlayid.setVisibility(View.GONE);
                holder.lay.setClickable(true);
                holder.lay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        Intent intent=new Intent(QuestionCatogery.this,QuestionPage.class);
                        intent.putExtra("category_id",category_id);
                        intent.putExtra("propertyid",propertyid);
                        intent.putExtra("propertyname",propertyname);

                        startActivity(intent);
                    }
                });
            }


            if(filename!=null && filename.length()>0)
            {
                Picasso.get().load(filename).fit().error(R.drawable.logosure).into(holder.imageView);

            }
            else
            {
                Picasso.get().load(R.drawable.logosure).fit().error(R.drawable.logosure).into(holder.imageView);
            }



        }

        @Override
        public int getItemCount() {
            return listdata.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView categoryname;
            LinearLayout lay,completedlayid;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView=itemView.findViewById(R.id.imageid);
                categoryname=itemView.findViewById(R.id.tv1);
                lay=itemView.findViewById(R.id.lay);
                completedlayid=itemView.findViewById(R.id.completedlayid);

            }
        }
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
//
//
//                            }
//                        }
//                        else if(flag.contains("0"))
//                        {
//                            Intent intent=new Intent(QuestionCatogery.this,PaymentPage.class);
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