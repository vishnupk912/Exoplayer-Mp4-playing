package com.mastervidya.mastervidya.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mastervidya.mastervidya.R;
import com.mastervidya.mastervidya.model.PagerModel;


public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>
{
    Context context;
    private PagerModel[] listdata;

    public ViewPagerAdapter(Context context, PagerModel[] listdata) {
        this.context = context;
        this.listdata = listdata;
    }

    @NonNull
    @Override
    public ViewPagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View listitem=layoutInflater.inflate(R.layout.viewpager_lay,parent,false);
        ViewHolder viewHolder=new ViewHolder(listitem);
        return  viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerAdapter.ViewHolder holder, int position) {

        final PagerModel myListData = listdata[position];
        holder.tv1.setText(listdata[position].getTitle());
        holder.tv2.setText(listdata[position].getDescription());
        holder.imageView.setImageResource(listdata[position].getImgId());


    }

    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tv1,tv2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);
            tv1=itemView.findViewById(R.id.tv1);
            tv2=itemView.findViewById(R.id.tv2);
        }
    }
}
