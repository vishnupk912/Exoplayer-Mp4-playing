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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.mastervidya.mastervidya.Addclass.PayForclassFragment;
import com.mastervidya.mastervidya.Addclass.SelectFragment;
import com.mastervidya.mastervidya.R;
import com.mastervidya.mastervidya.helper.RequestQueueSingleton;
import com.mastervidya.mastervidya.helper.SessionHandler;
import com.mastervidya.mastervidya.helper.Url;
import com.mastervidya.mastervidya.model.ClassModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AddClass1 extends AppCompatActivity {

    RecyclerView rclassesid;
    Dialog dialog_progress;

    LinearLayout lay_main;
    RequestQueue requestQueue;

    SessionHandler sessionHandler;

    String id,name,phone,avatar_image,classname_send="";
    ImageView laysubmit;
    String classid_send="",board_send;
    int selected=-1;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class1);


        requestQueue = RequestQueueSingleton.getInstance(AddClass1.this)
                .getRequestQueue();

        back=findViewById(R.id.back);
        sessionHandler=new SessionHandler(this);
        dialog_progress = new Dialog(this);
        dialog_progress.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_progress.setContentView(R.layout.progressbar);
        dialog_progress.setCancelable(false);
        dialog_progress.setCanceledOnTouchOutside(false);
        dialog_progress.setCancelable(false);
        dialog_progress.setCanceledOnTouchOutside(false);
        dialog_progress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        laysubmit=findViewById(R.id.laysubmit);
        lay_main =findViewById(R.id.lay_main);
        rclassesid =findViewById(R.id.rclassesid);

        getcustomerdetails();
        classlisting();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddClass1.this,MyClass.class);
                startActivity(intent);
            }
        });

        laysubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(classid_send.isEmpty())
                {
                    Snackbar snackbar = Snackbar.make(lay_main, "Please Select a Class", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundResource(R.color.colorprimarydark);
                    snackbar.show();
                }
                else
                {
                    Intent intent=new Intent(AddClass1.this,PaymentPage.class);
                    intent.putExtra("classid", classid_send);
                    intent.putExtra("classname", classname_send);
                    intent.putExtra("board", board_send);
                    startActivity(intent);
                }

            }
        });

    }
    private void classlisting()
    {
        ArrayList<ClassModel> classModelArrayList=new ArrayList<>();

        JSONObject json = new JSONObject();
        try
        {
            json.put("key",sessionHandler.getuniquekey());
            json.put("id",id);

            Log.d("key",sessionHandler.getuniquekey());
            Log.d("id",id);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        dialog_progress.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Url.classes, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject)
                    {
                        dialog_progress.dismiss();
                        try {
                            String status=jsonObject.getString("status");
                            if(status.contains("success"))
                            {
                                JSONArray jsonArray=jsonObject.getJSONArray("data");
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    ClassModel classModel=new ClassModel();

                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    String class_id=jsonObject1.getString("class_id");
                                    String board=jsonObject1.getString("board");
                                    String classname=jsonObject1.getString("class");

                                    classModel.setClass_id(class_id);
                                    classModel.setBoard(board);
                                    classModel.setClass_name(classname);
                                    classModelArrayList.add(classModel);


                                }
                            }
                            else if(status.contains("invalid api key"))
                            {
                                Dialog dialog;
                                dialog = new Dialog(AddClass1.this);
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

                            ClassAdapter adapter = new ClassAdapter(AddClass1.this,classModelArrayList);
                            rclassesid.setHasFixedSize(true);
                            rclassesid.setLayoutManager(new LinearLayoutManager(AddClass1.this,RecyclerView.VERTICAL,false));
                            rclassesid.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog_progress.dismiss();
                Snackbar snackbar = Snackbar.make(lay_main, "Some server error occurred .", Snackbar.LENGTH_LONG);
                View sbView = snackbar.getView();
                sbView.setBackgroundResource(R.color.colorprimarydark);
                snackbar.show();
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



    public class ClassAdapter  extends RecyclerView.Adapter<ClassAdapter.ViewHolder>

    {
        Context context;
        ArrayList<ClassModel> classModelArrayList=new ArrayList<>();

        public ClassAdapter(Context context, ArrayList<ClassModel> classModelArrayList) {
            this.context = context;
            this.classModelArrayList = classModelArrayList;
        }

        @NonNull
        @Override
        public ClassAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
            View listitem=layoutInflater.inflate(R.layout.classlay,parent,false);
            ClassAdapter.ViewHolder viewHolder=new ClassAdapter.ViewHolder(listitem);
            return  viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ClassAdapter.ViewHolder holder, int position)
        {
            String classname=classModelArrayList.get(position).getClass_name();
            String board=classModelArrayList.get(position).getBoard();
            String classid=classModelArrayList.get(position).getClass_id();
            holder.classnametv.setText(classname);

            if(selected == position)
            {
                holder.radioButton.setChecked(true);

            }

            else
            {
                holder.radioButton.setChecked(false);

            }

            holder.lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    classid_send=classid;
                    classname_send=classname;
                    board_send=board;

                    selected=position;
                    notifyDataSetChanged();

                }
            });
        }

        @Override
        public int getItemCount() {
            return classModelArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView classnametv;
            LinearLayout lay;
            RadioButton radioButton;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                classnametv=itemView.findViewById(R.id.classname);
                lay=itemView.findViewById(R.id.lay);
                radioButton=itemView.findViewById(R.id.rb);
            }
        }
    }

}