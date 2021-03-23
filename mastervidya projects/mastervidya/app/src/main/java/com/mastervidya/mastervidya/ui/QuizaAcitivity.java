package com.mastervidya.mastervidya.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.gson.Gson;
import com.mastervidya.mastervidya.R;
import com.mastervidya.mastervidya.helper.RequestQueueSingleton;
import com.mastervidya.mastervidya.helper.SessionHandler;
import com.mastervidya.mastervidya.helper.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class QuizaAcitivity extends AppCompatActivity {

    Dialog dialog_progress;
    ArrayList<String> answerid_array=new ArrayList<>();
    ArrayList<String> questionid_array=new ArrayList<>();

    ArrayList<HashMap<String,String>> answerid_arrayhasmap=new ArrayList<>();
    ArrayList<HashMap<String,String>> questionid_arrayhashmap=new ArrayList<>();

    ViewPager2 viewPager2;
    SessionHandler sessionHandler;
    String id,name,phone,avatar_image;
    RequestQueue requestQueue;

    private static TextView countdownTimerText;
    private static CountDownTimer countDownTimer;
    int selected=-1,selected1=-1;
    RecyclerView.Adapter adapter_ans;
    String duration;
    int viewpager_position;
    QuestionsAdapter questionsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiza_acitivity);
        viewPager2=findViewById(R.id.viewPager);
        countdownTimerText = (TextView) findViewById(R.id.countdownText);

        dialog_progress = new Dialog(this);
        dialog_progress.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_progress.setContentView(R.layout.progressbar);
        dialog_progress.setCancelable(false);
        dialog_progress.setCanceledOnTouchOutside(false);
        dialog_progress.setCancelable(false);
        dialog_progress.setCanceledOnTouchOutside(false);
        dialog_progress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));



        Intent intent=getIntent();
        duration=intent.getStringExtra("time");

        requestQueue = RequestQueueSingleton.getInstance(this)
                .getRequestQueue();

        sessionHandler=new SessionHandler(this);
        getcustomerdetails();

        viewPager2.setUserInputEnabled(false);

        getquestions();


        if (countDownTimer == null) {
            //Get minutes from edittexf
            //Check validation over edittext
            if (!duration.equals("") && duration.length() > 0) {
                int noOfMinutes = Integer.parseInt(duration) * 60 * 1000;//Convert minutes into milliseconds

                startTimer(noOfMinutes);//start countdown

            }
        }


    }

    private void startTimer(int noOfMinutes) {
        countDownTimer = new CountDownTimer(noOfMinutes, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                //Convert milliseconds into hour,minute and seconds
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                countdownTimerText.setText(hms);//set text
            }

            public void onFinish() {

                countdownTimerText.setText("TIME'S UP!!"); //On finish change timer text
                countDownTimer = null;//set CountDownTimer to null
            }
        }.start();

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



    public void getquestions()
    {
        ArrayList<HashMap<String,String>> arrayList_question=new ArrayList<>();
        ArrayList<HashMap<String,String>> arrayList_answers=new ArrayList<>();

        dialog_progress.show();
        JSONObject json = new JSONObject();
        try
        {
            json.put("key",sessionHandler.getuniquekey());
            json.put("id",id);
            json.put("chapter_id","1");

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, Url.quiz, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                dialog_progress.dismiss();
                Log.d("response",jsonObject.toString());
                try {
                    String status=jsonObject.getString("status");
                    if(status.contains("success"))
                    {
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            String id_question=jsonObject1.getString("id");
                            String question=jsonObject1.getString("question");

                            HashMap<String,String>  hashMap=new HashMap<>();
                            hashMap.put("id",id_question);
                            hashMap.put("question",question);
                            arrayList_question.add(hashMap);



                            JSONArray jsonArray1=jsonObject1.getJSONArray("options");

                            Toast.makeText(QuizaAcitivity.this, jsonArray1.toString(), Toast.LENGTH_SHORT).show();
                            for(int j=0;j<jsonArray1.length();j++)
                            {
                                JSONObject jsonObject2=jsonArray1.getJSONObject(j);
                                String option_id_answer=jsonObject2.getString("option_id");
                                String option=jsonObject2.getString("option");
                                HashMap<String,String>  hashMap1=new HashMap<>();

                                hashMap1.put("answeroption_id",option_id_answer);
                                hashMap1.put("option",option);
                                hashMap1.put("id",id_question);
                                arrayList_answers.add(hashMap1);

                            }
                        }
                    }
                    else if(status.contains("invalid api key"))
                    {
                        Dialog dialog;
                        dialog = new Dialog(QuizaAcitivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.alertdialog);
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.show();
                        LinearLayout linearLayout=dialog.findViewById(R.id.okid);


                        linearLayout.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view) {
                                sessionHandler.logoutUser();
                            }
                        });

                    }

                    Toast.makeText(QuizaAcitivity.this , arrayList_answers.toString(), Toast.LENGTH_SHORT).show();
                     questionsAdapter=new QuestionsAdapter(arrayList_question,arrayList_answers,QuizaAcitivity.this);
                    viewPager2.setAdapter(questionsAdapter);



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


    public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder>
    {

        ArrayList<HashMap<String,String>> list_question;
        ArrayList<HashMap<String,String>> list_answer;

        ArrayList<HashMap<String,String>> list_answer1=new ArrayList<>();
        Context context;

        public QuestionsAdapter(ArrayList<HashMap<String, String>> list_question, ArrayList<HashMap<String, String>> list_answer, Context context) {
            this.list_question = list_question;
            this.list_answer = list_answer;
            this.context = context;
        }

        @NonNull
        @Override
        public QuestionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.quizitem_lay, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;


        }

        @Override
        public void onBindViewHolder(@NonNull final QuestionsAdapter.ViewHolder holder, final int position)
        {
            final String question_id=list_question.get(position).get("id");
            final String question=list_question.get(position).get("question");

            viewpager_position=position;

            holder.questiontv.setText(String.valueOf(position+1)+" . "+ question);

            list_answer1.clear();
            for(int i=0;i<list_answer.size();i++)
            {
                if(question_id.equals(list_answer.get(i).get("id")))
                {
                    String option_id_answer=list_answer.get(i).get("answeroption_id");
                    String option=list_answer.get(i).get("option");

                    HashMap<String,String>  hashMap1=new HashMap<>();
                    hashMap1.put("question_id",question_id);
                    hashMap1.put("option_id_answer",option_id_answer);
                    hashMap1.put("option",option);
                    list_answer1.add(hashMap1);
                }


            }
            adapter_ans=new AnswerAdapter(list_answer1,context);
            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(adapter_ans);


            holder.linearLayout_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    viewPager2.setCurrentItem(position+1);

                }
            });

            if(position==list_question.size()-1)
            {
                holder.linearLayout_next.setVisibility(View.GONE);
                holder.linearLayout_submit.setVisibility(View.VISIBLE);
                holder.fwd.setVisibility(View.GONE);
            }

            if(position==0)
            {
                holder.back.setVisibility(View.GONE);
            }

            holder.linearLayout_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitanswer();
                }
            });
            holder.fwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager2.setCurrentItem(position+1);
                }
            });

            holder.back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager2.setCurrentItem(position-1);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list_question.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView questiontv;
            RecyclerView recyclerView;
            LinearLayout linearLayout_next,linearLayout_submit;
            ImageView fwd,back;
            public ViewHolder(@NonNull View itemView)
            {
                super(itemView);
                questiontv=itemView.findViewById(R.id.questiontv);
                recyclerView=itemView.findViewById(R.id.rvid);
                linearLayout_next=itemView.findViewById(R.id.linearLayout_next);
                linearLayout_submit=itemView.findViewById(R.id.linearLayout_submit);
                fwd=itemView.findViewById(R.id.fwd);
                back=itemView.findViewById(R.id.back);
            }
        }
    }

    public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.ViewHolder>
    {

        ArrayList<HashMap<String,String>> arrayList;
        Context context;

        public AnswerAdapter(ArrayList<HashMap<String, String>> arrayList, Context context) {
            this.arrayList = arrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.answeritem, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String name=arrayList.get(position).get("option");
            String question_id=arrayList.get(position).get("question_id");
            String option_id_answer=arrayList.get(position).get("option_id_answer");



            holder.tvans.setText(position+1+" ) "+name);

            holder.linearLayout.setBackgroundResource(R.drawable.etshape);
            holder.tvans.setTextColor(getResources().getColor(R.color.grey11));


            for(int i=0;i<questionid_array.size();i++)
            {
                if(questionid_array.get(i).equals(question_id))
                {
                    if(answerid_array.get(i).contains(option_id_answer))
                    {

                            holder.linearLayout.setBackgroundResource(R.drawable.buttonshape1);
                            holder.tvans.setTextColor(getResources().getColor(R.color.white));


                    }
                    break;
                }


            }




                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                     for(int i=0;i<questionid_array.size();i++)
                     {
                         if(questionid_array.get(i).equals(question_id))
                         {
                           answerid_array.remove(i);
                           questionid_array.remove(i);
                             break;
                         }


                     }
                     answerid_array.add(option_id_answer);
                     questionid_array.add(question_id);


                     Toast.makeText(context, questionid_array.toString(), Toast.LENGTH_SHORT).show();

                     selected=position;
                     questionsAdapter.notifyDataSetChanged();


                    }
                });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvans;
            LinearLayout linearLayout;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvans=itemView.findViewById(R.id.tvans);
                linearLayout=itemView.findViewById(R.id.linearlay);
            }
        }
    }



    public void submitanswer()
    {
        dialog_progress.show();
        JSONObject json = new JSONObject();
        try {

            json.put("key", sessionHandler.getuniquekey());
            json.put("id", id);
            json.put("chapter_id", "1");


            JSONArray array=new JSONArray();



                for(int i=0;i<questionid_array.size();i++)
                {
                    JSONObject obj=new JSONObject();

                    try {
                        obj.put("question_id",questionid_array.get(i));
                        obj.put("response_id",answerid_array.get(i));
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    array.put(obj);
                }



            json.put("response",array);





        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, Url.quiz_submit, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject)
            {
                dialog_progress.dismiss();
                Toast.makeText(QuizaAcitivity.this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
                Log.d("data",jsonObject.toString());
                try {
                    String status=jsonObject.getString("status");
                    if(status.contains("success"))
                    {
                        String  token=jsonObject.getString("t");
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);

                            String correct=jsonObject1.getString("correct");
                            String incorrect=jsonObject1.getString("incorrect");
                            String not_answered=jsonObject1.getString("not_answered");
                            String pass_percentage=jsonObject1.getString("pass_percentage");
                            String result=jsonObject1.getString("result");

                            Intent intent=new Intent(QuizaAcitivity.this,QuizResult.class);
                            intent.putExtra("token",token);
                            intent.putExtra("correct",correct);
                            intent.putExtra("incorrect",incorrect);
                            intent.putExtra("not_answered",not_answered);
                            intent.putExtra("pass_percentage",pass_percentage);
                            intent.putExtra("result",result);
                            startActivity(intent);


                        }

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

}