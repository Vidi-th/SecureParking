package com.protel.secureparking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class MyGateAdapter(private val dataList : ArrayList<GateRead>) : RecyclerView.Adapter<MyGateAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.gate_item,
            parent,false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentitem = dataList[position]

        holder.Name.text = currentitem.name
        holder.lokasi.text = currentitem.location
    }

    override fun getItemCount(): Int {

        return dataList.size
    }


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val Name : TextView = itemView.findViewById(R.id.tvName)
        val lokasi : TextView = itemView.findViewById(R.id.tvlokasi)
    }

}