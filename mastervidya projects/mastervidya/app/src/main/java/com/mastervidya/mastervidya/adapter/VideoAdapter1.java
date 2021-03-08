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

import com.mastervidya.mastervidya.R;
import com.mastervidya.mastervidya.model.VideoModel;
import com.mastervidya.mastervidya.video.OnlinePlayerActivity;

import java.util.ArrayList;

public class VideoAdapter1 extends RecyclerView.Adapter<VideoAdapter1.ViewHolder>
{

    ArrayList<VideoModel>  videoModelArrayList=new ArrayList<>();
    Context context;

    public VideoAdapter1(ArrayList<VideoModel> videoModelArrayList, Context context) {
        this.videoModelArrayList = videoModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public VideoAdapter1.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View listitem=layoutInflater.inflate(R.layout.videolay,parent,false);
        ViewHolder viewHolder=new ViewHolder(listitem);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter1.ViewHolder holder, int position) {

        String name=videoModelArrayList.get(position).getTitle();
        holder.nametv.setText(name);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Intent intent=new Intent(context, OnlinePlayerActivity.class);
                intent.putExtra("video_url",videoModelArrayList.get(position).getUrl());
                intent.putExtra("class",videoModelArrayList.get(position).getClasss());
                intent.putExtra("chapter",videoModelArrayList.get(position).getChapter());
                intent.putExtra("title",videoModelArrayList.get(position).getTitle());
                intent.putExtra("subject",videoModelArrayList.get(position).getSubject());
                intent.putExtra("desc",videoModelArrayList.get(position).getDescirption());
                intent.putExtra("chapter_id","0");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView nametv;
        LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nametv=itemView.findViewById(R.id.nametv);
            linearLayout=itemView.findViewById(R.id.lay);
        }
    }
}
