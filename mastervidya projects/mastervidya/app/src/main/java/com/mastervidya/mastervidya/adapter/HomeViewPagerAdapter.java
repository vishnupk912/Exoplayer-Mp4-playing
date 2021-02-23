package com.mastervidya.mastervidya.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mastervidya.mastervidya.R;
import com.mastervidya.mastervidya.model.Homepagermodel;
import com.mastervidya.mastervidya.model.PagerModel;

import java.util.ArrayList;


public class HomeViewPagerAdapter extends RecyclerView.Adapter<HomeViewPagerAdapter.ViewHolder>
{
    Context context;
    private ArrayList<Homepagermodel> listdata;

    public HomeViewPagerAdapter(Context context, ArrayList<Homepagermodel> listdata) {
        this.context = context;
        this.listdata = listdata;
    }

    @NonNull
    @Override
    public HomeViewPagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View listitem=layoutInflater.inflate(R.layout.viewpager_lay1,parent,false);
        ViewHolder viewHolder=new ViewHolder(listitem);
        return  viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewPagerAdapter.ViewHolder holder, int position) {


        Glide.with(context)
                .load(listdata.get(position).getImage())
                .centerCrop()
                .into(holder.imageView);
        Log.d("image",listdata.get(position).getImage());

    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
//        TextView tv1,tv2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);
//            tv1=itemView.findViewById(R.id.tv1);
//            tv2=itemView.findViewById(R.id.tv2);
        }
    }
}
