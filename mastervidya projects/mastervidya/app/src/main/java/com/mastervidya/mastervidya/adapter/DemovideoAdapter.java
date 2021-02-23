package com.mastervidya.mastervidya.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mastervidya.mastervidya.R;
import com.mastervidya.mastervidya.model.Demovideomodel;
import com.mastervidya.mastervidya.model.PagerModel;

import java.util.ArrayList;


public class DemovideoAdapter extends RecyclerView.Adapter<DemovideoAdapter.ViewHolder>
{
    Context context;
    private ArrayList<Demovideomodel> demovideomodelArrayList;


    public DemovideoAdapter(Context context,   ArrayList<Demovideomodel> demovideomodelArrayList) {
        this.context = context;
        this.demovideomodelArrayList = demovideomodelArrayList;
    }

    @NonNull
    @Override
    public DemovideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View listitem=layoutInflater.inflate(R.layout.videolayitem,parent,false);
        ViewHolder viewHolder=new ViewHolder(listitem);
        return  viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull DemovideoAdapter.ViewHolder holder, int position)
    {


        Glide.with(context)
                .load(demovideomodelArrayList.get(position).getDemovideo_image())
                .centerCrop()
                .into(holder.imageView);

        Log.d("link",demovideomodelArrayList.get(position).getDemovideo_url());
        holder.playnowlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(demovideomodelArrayList.get(position).getDemovideo_url())));

            }
        });

    }

    @Override
    public int getItemCount() {
        return demovideomodelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
//        TextView tv1,tv2;
        LinearLayout playnowlay;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);
//            tv1=itemView.findViewById(R.id.tv1);
//            tv2=itemView.findViewById(R.id.tv2);
            playnowlay=itemView.findViewById(R.id.playnowlay);
        }
    }
}
