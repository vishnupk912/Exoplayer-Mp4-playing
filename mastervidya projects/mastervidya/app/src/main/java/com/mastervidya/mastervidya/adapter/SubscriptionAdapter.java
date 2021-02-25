package com.mastervidya.mastervidya.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mastervidya.mastervidya.R;
import com.mastervidya.mastervidya.model.Paymentmodel;
import com.mastervidya.mastervidya.model.SubscibemonthsModel;
import com.mastervidya.mastervidya.ui.AddClass;
import com.mastervidya.mastervidya.ui.Payments;
import com.mastervidya.mastervidya.ui.UpgradePayment;

import java.util.ArrayList;
import java.util.HashMap;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.ViewHolder>
{

    ArrayList<Paymentmodel> paymentmodelArrayList=new ArrayList<>();
    ArrayList<SubscibemonthsModel> subscibemonthsModelArrayList=new ArrayList<>();
    Context context;

    public SubscriptionAdapter(ArrayList<SubscibemonthsModel> subscibemonthsModelArrayList,ArrayList<Paymentmodel> paymentmodelArrayList, Context context) {
        this.paymentmodelArrayList = paymentmodelArrayList;
        this.subscibemonthsModelArrayList = subscibemonthsModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public SubscriptionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View listitem=layoutInflater.inflate(R.layout.paymentslay,parent,false);
        ViewHolder viewHolder=new ViewHolder(listitem);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SubscriptionAdapter.ViewHolder holder, int position)
    {
        String name=paymentmodelArrayList.get(position).getStudent_name();
        String classs=paymentmodelArrayList.get(position).getClasss();
        String classs_id=paymentmodelArrayList.get(position).getClass_id();
        String board=paymentmodelArrayList.get(position).getBoard();
        String paymentstatus=paymentmodelArrayList.get(position).getPayment_type();
        String subscriptiontype=paymentmodelArrayList.get(position).getSubscription_type();
        String amount=paymentmodelArrayList.get(position).getAmount();
        String date=paymentmodelArrayList.get(position).getDate();

        holder.datetv.setText("Enrolled Date : "+date);
        holder.nametv.setText("Name : "+name);
        holder.classtv.setText("Class : "+classs);
        if(paymentstatus.contains("1"))
        {
            holder.paymentstatustv.setText("Payment : Online");
        }
        else
        {
            holder.paymentstatustv.setText("Payment : Agent");
        }

        holder.boardtv.setText("Board : "+board);

        if(subscriptiontype.contains("2"))
        {
            holder.subscriptionstv.setText("Tenure : Yearly Subscription");
        }
        else
        {
            holder.subscriptionstv.setText("Tenure : Monthly Subscription");
        }

        holder.amounttv.setText("Amount : Rs "+amount);

        holder.lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, UpgradePayment.class);
                intent.putExtra("classid",classs_id);
                intent.putExtra("board",board);
                intent.putExtra("classname",classs);
                intent.putExtra("name",name);

                context.startActivity(intent);

            }
        });

        ArrayList<HashMap<String,String>> hashMapArrayList=new ArrayList<>();
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("month",subscibemonthsModelArrayList.get(position).getMonth());
        hashMap.put("year",subscibemonthsModelArrayList.get(position).getYear());
        hashMapArrayList.add(hashMap);

        SubscriptionMonthsAdapter adapter = new SubscriptionMonthsAdapter(hashMapArrayList, context);
        holder.rvid.setHasFixedSize(true);
        holder.rvid.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
        holder.rvid.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return paymentmodelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView nametv,classtv,boardtv,paymentstatustv,subscriptionstv,amounttv,datetv;
        RecyclerView rvid;
        LinearLayout lay;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            datetv=itemView.findViewById(R.id.datetv);
            nametv=itemView.findViewById(R.id.nametv);
            classtv=itemView.findViewById(R.id.classtv);
            boardtv=itemView.findViewById(R.id.boardtv);
            paymentstatustv=itemView.findViewById(R.id.paymentstatustv);
            subscriptionstv=itemView.findViewById(R.id.subscriptionstv);
            amounttv=itemView.findViewById(R.id.amounttv);
            rvid=itemView.findViewById(R.id.rvid);
            lay=itemView.findViewById(R.id.lay);

        }
    }
}
