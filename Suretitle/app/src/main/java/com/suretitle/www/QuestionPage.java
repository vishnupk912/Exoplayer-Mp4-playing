package com.suretitle.www;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import helper.ApiInterface;
import helper.SessionHandler;
import model.QuestionCategoryModel;
import model.QuestionModel;
import model.SubquestionModel;

public class QuestionPage extends AppCompatActivity {

    Dialog dialog,dialog_alert;
    String mobilenumber,customer_id,customer_name,country_id,state_id,country_name,state_name,email,profession,organisation,gender,place;
    SessionHandler sessionHandler;
    ViewPager2 viewPager;

    String propertyid,category_id,token,count,propertyname;
    final ArrayList<SubquestionModel> subquestionModelArrayList = new ArrayList<>();
    final ArrayList<QuestionModel> questionModelArrayList = new ArrayList<>();

    ArrayList<HashMap<String,String>> responselist =new ArrayList<>();

    HashMap<String,String> responsehashmap=new HashMap<>();


    int positon_viewpager;

    QuestionsAdapter questionsAdapter;
    String yes="1",no="0";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_question_page);



        sessionHandler=new SessionHandler(this);
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


        HashMap<String, String> data3= sessionHandler.getcount();
        count = data3.get(sessionHandler.KEY_COUNT);



        final Intent intent=getIntent();
        propertyid=intent.getStringExtra("propertyid");
        category_id=intent.getStringExtra("category_id");
        propertyname=intent.getStringExtra("propertyname");
        viewPager=findViewById(R.id.viewpager);
        viewPager.setUserInputEnabled(false);


        getcustomerdetails();
        getquestions();
//        subscriptionplans();



    }


    public void getquestions()
    {

        dialog.show();
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, ApiInterface.questions, new Response.Listener<String>() {
            @Override
            public void onResponse(String s)
            {
                Log.d("response",s);
                dialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    String response=jsonObject.getString("status");
                    if(response.contains("success"))
                    {
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        for(int i=0;i<jsonArray.length();i++)
                        {

                            QuestionModel questionModel=new QuestionModel();

                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            String question_id=jsonObject1.getString("question_id");
                            String question=jsonObject1.getString("question");
                            String question_credit=jsonObject1.getString("question_credit");
                            String sub_question_count=jsonObject1.getString("sub_question_count");

                            questionModel.setQuestion_id(question_id);
                            questionModel.setQuestion(question);
                            questionModel.setQuestion_credit(question_credit);
                            questionModel.setSubquestion_count(sub_question_count);

                            questionModelArrayList.add(questionModel);
                            questionsAdapter=new QuestionsAdapter(questionModelArrayList,QuestionPage.this);
                            viewPager.setAdapter(questionsAdapter);

                        }
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
                hashMap.put("key",ApiInterface.key);
                hashMap.put("country_id",country_id);
                hashMap.put("state_id",state_id);
                hashMap.put("property_type_id",propertyid);
                hashMap.put("category_id",category_id);

               Log.d("key",ApiInterface.key);
                Log.d("country_id",country_id);
                Log.d("state_id",state_id);
                Log.d("property_type_id",propertyid);
                Log.d("category_id",category_id);


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

    public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder>
    {

        List<QuestionModel> list;
        Context context;

        public QuestionsAdapter(List<QuestionModel> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @NonNull
        @Override
        public QuestionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.questionlay, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;


        }

        @Override
        public void onBindViewHolder(@NonNull final QuestionsAdapter.ViewHolder holder, final int position)
        {
           final String question_id=list.get(position).getQuestion_id();
            final String question=list.get(position).getQuestion();
            final String questioncredit=list.get(position).getQuestion_credit();

            holder.lay_yes.setBackgroundResource(R.drawable.buttonshape4);
            holder.lay_no.setBackgroundResource(R.drawable.buttonshape4);

            holder.questiontv.setText(String.valueOf(position+1+".")+" "+question);

            holder.qno.setText("Question : "+String.valueOf(position+1)+" / "+questionModelArrayList.size());

            holder.laysub.setVisibility(View.GONE);


            holder.lay_yes.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    positon_viewpager=position;
                    if(position+1==list.size())
                    {
                        holder.lay_yes.setBackgroundResource(R.drawable.buttonshape1);
                        holder.lay_no.setBackgroundResource(R.drawable.buttonshape4);

                        holder.submitresponse_id.setVisibility(View.VISIBLE);
                        holder.laysub.setVisibility(View.GONE);
                        Log.d("question_id",question_id);
                        responsehashmap.put(question_id,yes+":"+questioncredit+":"+"0"+":'"+question+"':"+"'NA'"+":"+category_id);


                    }
                    else
                    {
                        holder.lay_yes.setBackgroundResource(R.drawable.buttonshape1);
                        holder.lay_no.setBackgroundResource(R.drawable.buttonshape4);
                        holder.laysub.setVisibility(View.GONE);
                        viewPager.setCurrentItem(position+1);
                        holder.submitresponse_id.setVisibility(View.GONE);
                        Log.d("question_id",question_id);
//                        responsehashmap.put(question_id,yes+":"+questioncredit+":"+"0");
                        responsehashmap.put(question_id,yes+":"+questioncredit+":"+"0"+":'"+question+"':"+"'NA'"+":"+category_id);

                    }
                }
            });

            holder.lay_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    positon_viewpager=position;

                    if(position+1==list.size())
                    {
                        holder.lay_yes.setBackgroundResource(R.drawable.buttonshape4);
                        holder.lay_no.setBackgroundResource(R.drawable.buttonshape1);
                        holder.submitresponse_id.setVisibility(View.VISIBLE);

                      if(!list.get(position).getSubquestion_count().equals("0"))
                      {
                          holder.lay_yes.setBackgroundResource(R.drawable.buttonshape4);
                          holder.lay_no.setBackgroundResource(R.drawable.buttonshape1);

                        holder.laysub.setVisibility(View.VISIBLE);
                        holder.shimmerFrameLayout.setVisibility(View.VISIBLE);
                        holder.subrv.setVisibility(View.VISIBLE);

                        RequestQueue requestQueue;
                        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
                        Network network = new BasicNetwork(new HurlStack());
                        requestQueue = new RequestQueue(cache, network);
                        requestQueue.start();

                        StringRequest stringRequest=new StringRequest(Request.Method.POST, ApiInterface.sub_questions, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s)
                            {
                                holder.shimmerFrameLayout.setVisibility(View.GONE);
                                try {
                                    JSONObject jsonObject=new JSONObject(s);
                                    String response=jsonObject.getString("status");
                                    subquestionModelArrayList.clear();
                                    if(response.contains("success"))
                                    {
                                        JSONArray jsonArray=jsonObject.getJSONArray("data");

                                        for(int i=0;i<jsonArray.length();i++)
                                        {
                                            SubquestionModel subquestionModel=new SubquestionModel();
                                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                            String question_id=jsonObject1.getString("sub_question_id");
                                            String question=jsonObject1.getString("sub_question");
                                            String question_credit=jsonObject1.getString("credit_percent");
                                            String mark=jsonObject1.getString("mark");



                                            subquestionModel.setSub_question_id(question_id);
                                            subquestionModel.setSub_question(question);
                                            subquestionModel.setSub_question_credit(question_credit);
                                            subquestionModel.setMark(mark);

                                            subquestionModelArrayList.add(subquestionModel);


                                        }

                                        SubquestionAdapeter adapter = new SubquestionAdapeter(QuestionPage.this,subquestionModelArrayList,question_id,question);
                                        holder.subrv.setHasFixedSize(true);
                                        holder. subrv.setLayoutManager(new LinearLayoutManager(QuestionPage.this,RecyclerView.VERTICAL,false));
                                        holder. subrv.setAdapter(adapter);
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
                            }
                        })
                        {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String,String> hashMap=new HashMap<>();
                                hashMap.put("key",ApiInterface.key);
                                hashMap.put("question_id",question_id);

                                Log.d("key",ApiInterface.key);
                                Log.d("question_id",question_id);




                                return  hashMap;
                            }
                        };

                        requestQueue.add(stringRequest);

                    }

//                        responsehashmap.put(question_id,no+":"+"0"+":"+"0");
                        responsehashmap.put(question_id,no+":"+"0"+":"+"0"+":'"+question+"':"+"'NA'"+":"+category_id);



                    }

                    else if(!list.get(position).getSubquestion_count().equals("0"))
                    {
                        holder.lay_yes.setBackgroundResource(R.drawable.buttonshape4);
                        holder.lay_no.setBackgroundResource(R.drawable.buttonshape1);

                        holder.laysub.setVisibility(View.VISIBLE);
                    holder.shimmerFrameLayout.setVisibility(View.VISIBLE);
                    holder.subrv.setVisibility(View.VISIBLE);
                    RequestQueue requestQueue;
                    Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
                    Network network = new BasicNetwork(new HurlStack());
                    requestQueue = new RequestQueue(cache, network);
                    requestQueue.start();

                    StringRequest stringRequest=new StringRequest(Request.Method.POST, ApiInterface.sub_questions, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s)
                        {

                            holder.shimmerFrameLayout.setVisibility(View.GONE);
                            try {
                                JSONObject jsonObject=new JSONObject(s);
                                String response=jsonObject.getString("status");
                                subquestionModelArrayList.clear();
                                if(response.contains("success"))
                                {
                                    JSONArray jsonArray=jsonObject.getJSONArray("data");

                                    for(int i=0;i<jsonArray.length();i++)
                                    {
                                         SubquestionModel subquestionModel=new SubquestionModel();

                                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                        String question_id=jsonObject1.getString("sub_question_id");
                                        String question=jsonObject1.getString("sub_question");
                                        String question_credit=jsonObject1.getString("credit_percent");
                                        String mark=jsonObject1.getString("mark");

                                        subquestionModel.setSub_question_id(question_id);
                                        subquestionModel.setSub_question(question);
                                        subquestionModel.setSub_question_credit(question_credit);
                                        subquestionModel.setMark(mark);

                                        subquestionModelArrayList.add(subquestionModel);

                                    }
                                    SubquestionAdapeter adapter = new SubquestionAdapeter(QuestionPage.this,subquestionModelArrayList,question_id,question);
                                    holder.subrv.setHasFixedSize(true);
                                    holder. subrv.setLayoutManager(new LinearLayoutManager(QuestionPage.this,RecyclerView.VERTICAL,false));
                                    holder. subrv.setAdapter(adapter);
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
                        }
                    })
                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String,String> hashMap=new HashMap<>();
                            hashMap.put("key",ApiInterface.key);
                            hashMap.put("question_id",question_id);

                            Log.d("key",ApiInterface.key);
                            Log.d("question_id",question_id);




                            return  hashMap;
                        }
                    };

                    requestQueue.add(stringRequest);

                }

                    else
                    {
                        holder.lay_yes.setBackgroundResource(R.drawable.buttonshape4);
                        holder.lay_no.setBackgroundResource(R.drawable.buttonshape1);

                        viewPager.setCurrentItem(position+1);
                        holder.submitresponse_id.setVisibility(View.GONE);
                        holder.laysub.setVisibility(View.GONE);
//                        responsehashmap.put(question_id,no+":"+"0"+":"+"0");
                        responsehashmap.put(question_id,no+":"+"0"+":"+"0"+":'"+question+"':"+"'NA'"+":"+category_id);


                    }
                }
            });

            holder.submitresponse_id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    submit();
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView questiontv,qno;
            LinearLayout lay_yes,lay_no,submitresponse_id,laysub;
            RecyclerView subrv;
            ShimmerFrameLayout shimmerFrameLayout;

            public ViewHolder(@NonNull View itemView)
            {
                super(itemView);
                questiontv=itemView.findViewById(R.id.questionid);
                lay_yes=itemView.findViewById(R.id.yeslayid);
                lay_no=itemView.findViewById(R.id.nolayid);
                subrv=itemView.findViewById(R.id.subrv);
                qno=itemView.findViewById(R.id.qno);
                submitresponse_id=itemView.findViewById(R.id.submitresponse_id);
                shimmerFrameLayout=itemView.findViewById(R.id.shimmer_id1);
                laysub=itemView.findViewById(R.id.laysub);


            }
        }
    }

    class  SubquestionAdapeter extends RecyclerView.Adapter<SubquestionAdapeter.ViewHolder>
    {


            Context context;
            private ArrayList<SubquestionModel> listdata;
            String question_id;
            String mainquestion;


            public SubquestionAdapeter(Context context, ArrayList<SubquestionModel> listdata,String question_id,String mainquestion) {
                this.context = context;
                this.listdata = listdata;
                this.question_id = question_id;
                this.mainquestion = mainquestion;
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                View listItem= layoutInflater.inflate(R.layout.list_item2, parent, false);
                ViewHolder viewHolder = new ViewHolder(listItem);
                return viewHolder;
            }

            @Override
            public void onBindViewHolder(@NonNull final ViewHolder holder, final int i)
            {

               final String subquestion_id=listdata.get(i).getSub_question_id();
                final String question=listdata.get(i).getSub_question();
                final String questioncredit=listdata.get(i).getSub_question_credit();
                final String mark=listdata.get(i).getMark();

                holder.linearLayout.setBackgroundResource(R.drawable.buttonshape4);
                holder.subquestiontv.setText(question);




                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        holder.linearLayout.setBackgroundResource(R.drawable.buttonshape1);

                        Log.d("postion_viewpager", String.valueOf(positon_viewpager));
                        viewPager.setCurrentItem(positon_viewpager+1);

                            responsehashmap.put(question_id,no+":"+mark+":"+subquestion_id+":'"+mainquestion+"':'"+question+"'"+":"+category_id);
                            Log.d("question_id",question_id);





                    }
                });

            }

            @Override
            public int getItemCount() {
                return listdata.size();
            }

            public class ViewHolder extends RecyclerView.ViewHolder {
                TextView subquestiontv;
                LinearLayout linearLayout;
                public ViewHolder(@NonNull View itemView) {
                    super(itemView);
                    subquestiontv=itemView.findViewById(R.id.subquestiontv);
                    linearLayout=itemView.findViewById(R.id.nolayid);
                }
            }

    }

    public void submit()
    {


        sessionHandler.createcount(String.valueOf(Integer.valueOf(count)+1));
        responselist.add(responsehashmap);

        dialog.show();
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, ApiInterface.submit, new Response.Listener<String>() {
            @Override
            public void onResponse(String s)
            {
                dialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    String response=jsonObject.getString("status");
                    if(response.contains("success"))
                    {

                        Intent intent=new Intent(QuestionPage.this,QuestionCatogery.class);
                        intent.putExtra("propertyid",propertyid);
                        intent.putExtra("category_id",category_id);
                        intent.putExtra("propertyname",propertyname);
                        startActivity(intent);

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
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("key",ApiInterface.key);
                hashMap.put("response",String.valueOf(responselist));
                hashMap.put("customer_id",mobilenumber);
                hashMap.put("token",token);

                Log.d("key",ApiInterface.key);
                Log.d("response",String.valueOf(responselist));
                Log.d("customer_id",mobilenumber);
                Log.d("token",token);

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
//                            Intent intent=new Intent(QuestionPage.this,PaymentPage.class);
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