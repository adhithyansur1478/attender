package com.example.attendance

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter2(private val context: Context, private val newarrayList: ArrayList<Member>):
    RecyclerView.Adapter<RecyclerView.ViewHolder> () {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemview =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerv_sec_item, parent, false)
        return MyAdapter2.MyViewHolder1(itemview)
    }

    override fun getItemCount(): Int {
        return newarrayList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MyViewHolder1).bind(newarrayList[position])

        val currentitem = newarrayList[position]

        holder.itemView.setOnClickListener {
            Log.i("initrecycler","Function called")
        }
    }

    class MyViewHolder1(itemview: View):RecyclerView.ViewHolder(itemview) {
        fun bind(member: Member) {
            itemView.findViewById<TextView>(R.id.mem_name).text = "Member Name: ${member.member_name}"
            itemView.findViewById<TextView>(R.id.mem_gender).text = "Gender: ${member.mem_gender}"


        }

    }
}