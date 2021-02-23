package com.mastervidya.mastervidya.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mastervidya.mastervidya.R;
import com.mastervidya.mastervidya.Subjects;
import com.mastervidya.mastervidya.helper.SessionHandler;
import com.mastervidya.mastervidya.model.SubscribedclassModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SubClassAdapter extends RecyclerView.Adapter<SubClassAdapter.ViewHolder>
{

    Context context;
    ArrayList<SubscribedclassModel> subscribedclassModelArrayList=new ArrayList<>();


    public SubClassAdapter(Context context, ArrayList<SubscribedclassModel> subscribedclassModelArrayList) {
        this.context = context;
        this.subscribedclassModelArrayList = subscribedclassModelArrayList;
    }

    @NonNull
    @Override
    public SubClassAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View listitem=layoutInflater.inflate(R.layout.subclasslay,parent,false);
        ViewHolder viewHolder=new ViewHolder(listitem);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SubClassAdapter.ViewHolder holder, int position) {

         String classname=subscribedclassModelArrayList.get(position).getClasss();
         String board_name=subscribedclassModelArrayList.get(position).getBoard();
         String studentname=subscribedclassModelArrayList.get(position).getStudent_name();
        String id=subscribedclassModelArrayList.get(position).getClass_id();

         holder.classnametv.setText(classname);
         holder.boardnametv.setText(board_name);
         holder.childnametv.setText("For "+studentname);

         holder.lay.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent=new Intent(context, Subjects.class);
                 intent.putExtra("class_id",id);
                 context.startActivity(intent);
             }
         });
    }

    @Override
    public int getItemCount() {
        return subscribedclassModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView classnametv,childnametv,boardnametv;
        LinearLayout lay;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            classnametv=itemView.findViewById(R.id.classsnametv);
            childnametv=itemView.findViewById(R.id.childnametv);
            boardnametv=itemView.findViewById(R.id.boardnametv);
            lay=itemView.findViewById(R.id.lay);
        }
    }

}

