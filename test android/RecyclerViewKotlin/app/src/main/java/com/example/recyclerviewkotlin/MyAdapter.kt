package com.example.recyclerviewkotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(val songs : List<datasongsModel>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.ViewHolder {
        val inflater=LayoutInflater.from(parent.context);
        val view:View=inflater.inflate(R.layout.itemlist,parent,false)
        return  ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: MyAdapter.ViewHolder, position: Int) {
        holder.title_tv.text=songs[position].title;
        holder.descption_tv.text=songs[position].description.toString()
    }

    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView)
    {
        var title_tv=itemView.findViewById<TextView>(R.id.tv1)
        var descption_tv=itemView.findViewById<TextView>(R.id.tv2)

    }

}