package com.mastervidya.mastervidya.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mastervidya.mastervidya.R;
import com.mastervidya.mastervidya.model.SubscibemonthsModel;

import java.util.ArrayList;
import java.util.HashMap;

public class SubscriptionMonthsAdapter extends RecyclerView.Adapter<SubscriptionMonthsAdapter.ViewHolder>
{
    ArrayList<HashMap<String,String>> subscibemonthsModelArrayList=new ArrayList<>();
    Context context;


    public SubscriptionMonthsAdapter( ArrayList<HashMap<String,String>> subscibemonthsModelArrayList , Context context) {
        this.subscibemonthsModelArrayList = subscibemonthsModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public SubscriptionMonthsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View listitem=layoutInflater.inflate(R.layout.laymonth1,parent,false);
        ViewHolder viewHolder=new ViewHolder(listitem);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SubscriptionMonthsAdapter.ViewHolder holder, int position)
    {
//        String month=month.get(position).getMonth();
//        String year=month.get(position).getYear();
        holder.tv1.setText(subscibemonthsModelArrayList.get(position).get("month"));
        holder.tv2.setText(subscibemonthsModelArrayList.get(position).get("year"));

    }

    @Override
    public int getItemCount() {
        return subscibemonthsModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv1,tv2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv1=itemView.findViewById(R.id.tv1);
            tv2=itemView.findViewById(R.id.tv2);
        }
    }
}
