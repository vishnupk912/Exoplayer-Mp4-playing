package com.mastervidya.mastervidya.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.progresviews.ProgressWheel;
import com.mastervidya.mastervidya.R;
import com.mastervidya.mastervidya.Subjects;
import com.mastervidya.mastervidya.model.ReportModel;
import com.mastervidya.mastervidya.model.SubscribedclassModel;

import java.util.ArrayList;

public class ReportAdapter1 extends RecyclerView.Adapter<ReportAdapter1.ViewHolder>
{

    Context context;
    ArrayList<ReportModel> reportModelArrayList=new ArrayList<>();


    public ReportAdapter1(Context context, ArrayList<ReportModel> reportModelArrayList) {
        this.context = context;
        this.reportModelArrayList = reportModelArrayList;
    }

    @NonNull
    @Override
    public ReportAdapter1.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View listitem=layoutInflater.inflate(R.layout.laychapterreport,parent,false);
        ViewHolder viewHolder=new ViewHolder(listitem);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReportAdapter1.ViewHolder holder, int position)
    {
        String chapter_id=reportModelArrayList.get(position).getChapter_id();
        String chapter=reportModelArrayList.get(position).getChapter();
        String correct=reportModelArrayList.get(position).getCorrect();
        String incorrect=reportModelArrayList.get(position).getIncorrect();
        String not_answered=reportModelArrayList.get(position).getNot_answered();
        String scored_percentage=reportModelArrayList.get(position).getScored_percentage();
        String status1=reportModelArrayList.get(position).getStatus1();
        String time=reportModelArrayList.get(position).getTime();

        holder.textView_chapname.setText(chapter);
        holder.textView_status.setText(status1);

        double data = Double.parseDouble(scored_percentage);
        int value = (int)data;

        holder.progresstv.setText(String.valueOf(value)+" %");
        holder.correctanstv.setText(String.valueOf(correct));
        holder.incorrectanstv.setText(String.valueOf(incorrect));
        holder.notanstv.setText(String.valueOf(not_answered));
        holder.timetv.setText("Attended On : "+String.valueOf(time));


        if(status1.contains("fail"))
        {
         holder.textView_status.setTextColor(context.getResources().getColor(R.color.red));
         holder.layprogress.setBackground(context.getResources().getDrawable(R.drawable.progress2));
        }
        else
        {
            holder.textView_status.setTextColor(context.getResources().getColor(R.color.green));
            holder.layprogress.setBackground(context.getResources().getDrawable(R.drawable.progress));

        }
    }

    @Override
    public int getItemCount()
    {
        return reportModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout lay,layprogress;
        TextView textView_chapname,textView_status;
        TextView progresstv,correctanstv,incorrectanstv,notanstv,timetv;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            textView_chapname=itemView.findViewById(R.id.name);
            textView_status=itemView.findViewById(R.id.statustv);
            lay=itemView.findViewById(R.id.lay);
            layprogress=itemView.findViewById(R.id.layprogress);
            progresstv=itemView.findViewById(R.id.progresstv);
            correctanstv=itemView.findViewById(R.id.correctanstv);
            incorrectanstv=itemView.findViewById(R.id.incorrectanstv);
            notanstv=itemView.findViewById(R.id.notanstv);
            timetv=itemView.findViewById(R.id.timetv);
        }
    }

}

