package com.mastervidya.mastervidya.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class ViewAnswer extends AppCompatActivity {
    Dialog dialog_progress;
    RequestQueue requestQueue;
    String token;
    RecyclerView recyclerView;
    SessionHandler sessionHandler;
    String id,name,phone,avatar_image;
    QuestionsAdapter questionsAdapter;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_answer);
        recyclerView=findViewById(R.id.rvid);

        back=findViewById(R.id.back);

        dialog_progress = new Dialog(this);
        dialog_progress.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_progress.setContentView(R.layout.progressbar);
        dialog_progress.setCancelable(false);
        dialog_progress.setCanceledOnTouchOutside(false);
        dialog_progress.setCancelable(false);
        dialog_progress.setCanceledOnTouchOutside(false);
        dialog_progress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        sessionHandler=new SessionHandler(this);


        requestQueue = RequestQueueSingleton.getInstance(this)
                .getRequestQueue();
        Intent intent=getIntent();
        token=intent.getStringExtra("token");



        getcustomerdetails();


        getanswer();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    public void getanswer()
    {
        ArrayList<HashMap<String,String>> arrayList_question=new ArrayList<>();
        ArrayList<HashMap<String,String>> arrayList_answers=new ArrayList<>();

        dialog_progress.show();
        JSONObject json = new JSONObject();
        try {

            json.put("key", sessionHandler.getuniquekey());
            json.put("id", id);
            json.put("t", token);
            json.put("chapter_id", "1");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, Url.show_answers, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject)
            {
                dialog_progress.dismiss();
                Log.d("data",jsonObject.toString());
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
                            String correct_answer=jsonObject1.getString("correct_answer");
                            String choosen_answer=jsonObject1.getString("choosen_answer");
                            HashMap<String,String>  hashMap=new HashMap<>();
                            hashMap.put("id",id_question);
                            hashMap.put("question",question);
                            hashMap.put("correct_answer",correct_answer);
                            hashMap.put("choosen_answer",choosen_answer);
                            arrayList_question.add(hashMap);


                            JSONArray jsonArray1=jsonObject1.getJSONArray("options");

                            for(int j=0;j<jsonArray1.length();j++)
                            {
                                JSONObject jsonObject2=jsonArray1.getJSONObject(j);
                                String option_id_answer=jsonObject2.getString("option_id");
                                String option=jsonObject2.getString("option");

                                HashMap<String,String>  hashMap1=new HashMap<>();

                                hashMap1.put("answeroption_id",option_id_answer);
                                hashMap1.put("option",option);
                                hashMap1.put("id",id_question);
                                hashMap1.put("correct_answer",correct_answer);
                                hashMap1.put("choosen_answer",choosen_answer);

                                arrayList_answers.add(hashMap1);

                            }
                        }
                    }
                    else if(status.contains("invalid api key"))
                    {
                        Dialog dialog;
                        dialog = new Dialog(ViewAnswer.this);
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

                    questionsAdapter=new QuestionsAdapter(arrayList_question,arrayList_answers,ViewAnswer.this);
                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ViewAnswer.this,RecyclerView.VERTICAL,false);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(questionsAdapter);


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
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.quizitem_lay1, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;


        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position)
        {
            final String question_id=list_question.get(position).get("id");
            final String question=list_question.get(position).get("question");
            final String correct_answer=list_question.get(position).get("correct_answer");
            final String choosen_answer=list_question.get(position).get("choosen_answer");


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
                    hashMap1.put("correct_answer",correct_answer);
                    hashMap1.put("choosen_answer",choosen_answer);
                    hashMap1.put("option_id_answer",option_id_answer);
                    hashMap1.put("option",option);
                    list_answer1.add(hashMap1);
                }


            }
            AnswerAdapter adapter_ans=new AnswerAdapter(list_answer1,context);
            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(adapter_ans);


            holder.linearLayout_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {


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

            public ViewHolder(@NonNull View itemView)
            {
                super(itemView);
                questiontv=itemView.findViewById(R.id.questiontv);
                recyclerView=itemView.findViewById(R.id.rvid);
                linearLayout_next=itemView.findViewById(R.id.linearLayout_next);
                linearLayout_submit=itemView.findViewById(R.id.linearLayout_submit);

            }
        }
    }

    public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.ViewHolder> {

        ArrayList<HashMap<String, String>> arrayList;
        Context context;

        public AnswerAdapter(ArrayList<HashMap<String, String>> arrayList, Context context) {
            this.arrayList = arrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.answeritem, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String name = arrayList.get(position).get("option");
            String question_id = arrayList.get(position).get("question_id");
            String option_id_answer = arrayList.get(position).get("option_id_answer");
            String correct_answer = arrayList.get(position).get("correct_answer");
            String choosen_answer = arrayList.get(position).get("choosen_answer");

            holder.tvans.setText(name);


            if (correct_answer.equals(option_id_answer)) {
                holder.linearLayout.setBackgroundResource(R.drawable.buttonshapegreen);
                holder.tvans.setTextColor(getResources().getColor(R.color.white));
            }

            if (!choosen_answer.equals(correct_answer) && choosen_answer.equals(option_id_answer)) {
                holder.linearLayout.setBackgroundResource(R.drawable.buttonshapered);
                holder.tvans.setTextColor(getResources().getColor(R.color.white));
            } else if (choosen_answer.equals(correct_answer) && choosen_answer.equals(option_id_answer)) {
                holder.linearLayout.setBackgroundResource(R.drawable.buttonshapegreen);
                holder.tvans.setTextColor(getResources().getColor(R.color.white));
            }




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

}