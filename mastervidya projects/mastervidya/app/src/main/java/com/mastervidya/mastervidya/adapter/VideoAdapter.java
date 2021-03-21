package com.mastervidya.mastervidya.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mastervidya.mastervidya.R;
import com.mastervidya.mastervidya.model.VideoModel1;
import com.mastervidya.mastervidya.video.OnlinePlayerActivity;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder>
{
    ArrayList<VideoModel1>  videoModelArrayList=new ArrayList<>();
    Context context;

    public VideoAdapter(ArrayList<VideoModel1> videoModelArrayList, Context context)
    {
        this.videoModelArrayList = videoModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public VideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View listitem=layoutInflater.inflate(R.layout.videolay,parent,false);
        ViewHolder viewHolder=new ViewHolder(listitem);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.ViewHolder holder, int position) {

        String name=videoModelArrayList.get(position).getTitle();
        String image=videoModelArrayList.get(position).getImage_file();
        holder.nametv.setText(name);

        Glide.with(context)
                .load(image)
                .centerCrop()
                .into(holder.imageView);

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
                        intent.putExtra("chapter_id",videoModelArrayList.get(position).getChapter_id());
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
        ImageView imageView;
        LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nametv=itemView.findViewById(R.id.nametv);
            linearLayout=itemView.findViewById(R.id.lay);
            imageView=itemView.findViewById(R.id.imageView);
        }
    }
}
