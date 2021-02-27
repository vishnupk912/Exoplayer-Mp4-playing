package com.mastervidya.mastervidya.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mastervidya.mastervidya.R;
import com.mastervidya.mastervidya.model.ChapterModel;
import com.mastervidya.mastervidya.ui.VideoListing;

import java.util.ArrayList;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ViewHolder>
{
    ArrayList<ChapterModel> chapterModelArrayList=new ArrayList<>();
    Context context;

    public ChapterAdapter(ArrayList<ChapterModel> chapterModelArrayList, Context context) {
        this.chapterModelArrayList = chapterModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View listitem=layoutInflater.inflate(R.layout.chapterlay,parent,false);
        ViewHolder viewHolder=new ViewHolder(listitem);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.textView.setText(chapterModelArrayList.get(position).getChaptername());
        holder.postitiontv.setText(String.valueOf(position+1));

        String status=chapterModelArrayList.get(position).getStatus();

        String year=chapterModelArrayList.get(position).getYear();
        String  month=chapterModelArrayList.get(position).getMonth();

        holder.yeartv.setText(year+" | "+month);
        if(status.contains("0"))
        {
            holder.statustv.setText("Not Subscribed");
            holder.statuslay.setBackgroundResource(R.drawable.buttonshape2light2);
            holder.linearLayout.setClickable(false);


        }
        else if(status.contains("1"))
        {
            holder.statuslay.setVisibility(View.GONE);
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, VideoListing.class);
                    intent.putExtra("chapname",chapterModelArrayList.get(position).getChaptername());
                    intent.putExtra("chapter_id",chapterModelArrayList.get(position).getId());
                    context.startActivity(intent);
                }
            });
        }
        else
        {
            holder.statustv.setText("Coming Soon");
            holder.linearLayout.setClickable(false);

        }

    }

    @Override
    public int getItemCount() {
        return chapterModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView,statustv,yeartv;
        TextView postitiontv;
        LinearLayout linearLayout,statuslay;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            statuslay=itemView.findViewById(R.id.statuslay);
            statustv=itemView.findViewById(R.id.statustv);
            textView=itemView.findViewById(R.id.chapternametv);
            postitiontv=itemView.findViewById(R.id.position);
            linearLayout=itemView.findViewById(R.id.lay);
            yeartv=itemView.findViewById(R.id.yeartv);
        }
    }
}
