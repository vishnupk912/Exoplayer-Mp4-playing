package com.mastervidya.mastervidya.Addclass;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.AsyncListUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


public class SelectFragment extends Fragment {


    RecyclerView rclassesid;
    Dialog dialog_progress;

    View view;
    LinearLayout lay_main;
    RequestQueue requestQueue;

    SessionHandler sessionHandler;

    String id,name,phone,avatar_image,classname_send="";
    ImageView laysubmit;
    String classid_send="",board_send;
    int selected=-1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.layaddclass, container, false);

        requestQueue = RequestQueueSingleton.getInstance(getActivity())
                .getRequestQueue();


        sessionHandler=new SessionHandler(getActivity());
        dialog_progress = new Dialog(getActivity());
        dialog_progress.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_progress.setContentView(R.layout.progressbar);
        dialog_progress.setCancelable(false);
        dialog_progress.setCanceledOnTouchOutside(false);
        dialog_progress.setCancelable(false);
        dialog_progress.setCanceledOnTouchOutside(false);
        dialog_progress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        laysubmit=view.findViewById(R.id.laysubmit);
        lay_main = view.findViewById(R.id.lay_main);
        rclassesid = view.findViewById(R.id.rclassesid);

        getcustomerdetails();
        classlisting();


        laysubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), classid_send, Toast.LENGTH_SHORT).show();
                if(classid_send.isEmpty())
                {
                    Snackbar snackbar = Snackbar.make(lay_main, "Please Select a Class", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundResource(R.color.colorprimarydark);
                    snackbar.show();
                }
                else
                {
                    loadfragment1(new PayForclassFragment());
                }

            }
        });
        return  view;
    }

    private void loadfragment1(Fragment fragment) {

        FragmentManager fm=getActivity().getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();

        Bundle args = new Bundle();
        args.putString("classid", classid_send);
        args.putString("classname", classname_send);
        args.putString("board", board_send);
        ft.setCustomAnimations(R.anim.slide_in1,R.anim.slide_out1);

        fragment.setArguments(args);

        ft.replace(R.id.frameDB,fragment);
        ft.commit();
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
                                dialog = new Dialog(getActivity());
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

                            ClassAdapter adapter = new ClassAdapter(getActivity(),classModelArrayList);
                            rclassesid.setHasFixedSize(true);
                            rclassesid.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
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
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
            View listitem=layoutInflater.inflate(R.layout.classlay,parent,false);
            ViewHolder viewHolder=new ViewHolder(listitem);
            return  viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position)
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