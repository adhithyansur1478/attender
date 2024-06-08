package com.example.attendance

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
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

        // Avoid triggering the listener when recycling views
        cbx.setOnCheckedChangeListener(null)

        // Set the current state of the checkbox
        cbx.isChecked = currentitem.mem_chbx

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
                "Member Name: ${member.member_name}"
            itemView.findViewById<TextView>(R.id.mem_gender).text = "Gender: ${member.mem_gender}"
            itemView.findViewById<CheckBox>(R.id.checkBox).isChecked = member.mem_chbx
            itemView.findViewById<CheckBox>(R.id.checkBox).isEnabled = !member.mem_chbx
        }
    }
}