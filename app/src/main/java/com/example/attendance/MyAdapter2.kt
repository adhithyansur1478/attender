package com.example.attendance

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter2(private val context: Context, private val newarrayList: ArrayList<Member>) :
    RecyclerView.Adapter<MyAdapter2.MyViewHolder1>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder1 {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerv_sec_item, parent, false)
        return MyViewHolder1(itemView)
    }

    override fun getItemCount(): Int {
        return newarrayList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder1, position: Int) {
        holder.bind(newarrayList[position])

        val currentitem = newarrayList[position]
        val cbx = holder.itemView.findViewById<CheckBox>(R.id.checkBox)
        val rgrp = holder.itemView.findViewById<RadioGroup>(R.id.radioGroup)
        var ryes = holder.itemView.findViewById<RadioButton>(R.id.rb_yes)
        var rno = holder.itemView.findViewById<RadioButton>(R.id.rb_no)

        rgrp.setOnCheckedChangeListener(null)
        ryes.setOnCheckedChangeListener(null)
        rno.setOnCheckedChangeListener(null)

        holder.itemView.findViewById<RadioGroup>(R.id.radioGroup).setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_yes -> {
                    if (currentitem.pres==1){
                        ryes.isChecked = true
                    }
                    if (currentitem.pres==2){
                        ryes.isChecked = false
                    }
                }
                R.id.rb_no -> {
                    if (currentitem.pres==1){
                        rno.isChecked = false
                    }
                    if (currentitem.pres==2){
                        rno.isChecked = true
                    }
                }
            }
        }


        when (currentitem.pres) {
            1 -> {
                ryes.isChecked = true
                rno.isChecked = false
            }
            2 -> {
                rno.isChecked = true
                ryes.isChecked = false
            }
            else -> {
                ryes.isChecked = false
                rno.isChecked = false
            }
        }

        ryes.setOnClickListener {

            MainActivity2().sketchbox2(currentitem.memid,1,context)
            currentitem.pres = 1
            ryes.isChecked = true
            notifyDataSetChanged()

        }

        rno.setOnClickListener {

            MainActivity2().sketchbox2(currentitem.memid,2,context)
            currentitem.pres = 2
            rno.isChecked = true
            notifyDataSetChanged()
        }

        // Avoid triggering the listener when recycling views
        cbx.setOnCheckedChangeListener(null)

        // Set the current state of the checkbox
        cbx.isChecked = currentitem.mem_chbx ///evdeeeeeeeee date vech nokknm condition koduthh

        // Disable the checkbox if it's already checked
        cbx.isEnabled = !currentitem.mem_chbx

        cbx.setOnClickListener {
            if (cbx.isChecked) {
                // Create the object of AlertDialog Builder class
                val builder = AlertDialog.Builder(context)

                // Set the message show for the Alert time
                builder.setMessage("Are You Sure To Confirm The Fee Payment?")

                // Set Alert Title
                builder.setTitle("Confirm!")

                // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
                builder.setCancelable(false)

                // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
                builder.setPositiveButton("Yes") { dialog, _ ->
                    MainActivity2().setchkbox(currentitem.memid, true, context)
                    currentitem.mem_chbx = true
                    cbx.isChecked = true
                    cbx.isEnabled = false
                    dialog.dismiss()
                }

                // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
                builder.setNegativeButton("No") { dialog, _ ->
                    cbx.isChecked = false
                    dialog.cancel()
                }

                // Create the Alert dialog
                val alertDialog = builder.create()
                // Show the Alert Dialog box
                alertDialog.show()
            }
        }
    }

    class MyViewHolder1(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(member: Member) {
            itemView.findViewById<TextView>(R.id.mem_name).text =
                "Member Name : ${member.member_name}"
            itemView.findViewById<TextView>(R.id.mem_gender).text = "Gender               : ${member.mem_gender}"
            itemView.findViewById<TextView>(R.id.mem_phn).text = "Phn Number     : +91 ${member.mem_phn}"
            itemView.findViewById<CheckBox>(R.id.checkBox).isChecked = member.mem_chbx
            itemView.findViewById<CheckBox>(R.id.checkBox).isEnabled = !member.mem_chbx
            var radioButtonYes = itemView.findViewById<RadioButton>(R.id.rb_yes)
            var radioButtonNo = itemView.findViewById<RadioButton>(R.id.rb_no)

            when (member.pres) {
                1 -> {
                    radioButtonYes.isChecked = true
                    radioButtonNo.isChecked = false
                }
                2 -> {
                    radioButtonNo.isChecked = true
                    radioButtonYes.isChecked = false
                }
                else -> {
                    radioButtonYes.isChecked = false
                    radioButtonNo.isChecked = false
                }
            }

        }
    }
}