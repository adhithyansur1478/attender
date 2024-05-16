package com.example.attendance

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val context: Context, private val newarrayList: ArrayList<User>):RecyclerView.Adapter<RecyclerView.ViewHolder> (){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemview =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerviewitem, parent, false)
        return MyViewHolder(itemview)
    }

    override fun getItemCount(): Int {
        return newarrayList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MyViewHolder).bind(newarrayList[position])
    }


    class MyViewHolder(itemview: View):RecyclerView.ViewHolder(itemview) {
        fun bind(user: User) {
            itemView.findViewById<TextView>(R.id.sess_name_rec).text = user.session_name
            itemView.findViewById<TextView>(R.id.up_date_rec).text = "Updated Date: ${user.up_date}"
            itemView.findViewById<TextView>(R.id.loc_rec).text = user.location

        }

    }


}



