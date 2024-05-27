package com.example.attendance

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.attendance.databinding.ActivityMain2Binding
import com.example.attendance.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity2 : AppCompatActivity() {


    private lateinit var binding: ActivityMain2Binding
    private lateinit var view: View
    private lateinit var dialog: AlertDialog
    lateinit var genderGroup:RadioGroup
    lateinit var gender:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        var SessName = intent.extras!!.getString("SessName")
        var loca = intent.extras!!.getString("Location")
        var update = intent.extras!!.getString("Update")

        binding.sessNameRecMa2.text = SessName
        binding.locRecMa2.text = loca
        binding.upDateRecMa2.text = "${binding.upDateRecMa2.text} $update"

        val sdf = SimpleDateFormat("dd/M/yyyy ")
        val currentDate = sdf.format(Date())

        binding.dateTv.text = "${binding.dateTv.text} $currentDate"


        binding.addBttn.setOnClickListener {


            showdb()

        }



    }

    fun showdb(){

        val dialogview = layoutInflater.inflate(R.layout.stud_detail_db,null)
        dialog = AlertDialog.Builder(this).setView(dialogview).show()
        //dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val rb1 = dialogview.findViewById<RadioButton>(R.id.male)
        val rb2:RadioButton = dialogview.findViewById(R.id.female)
        val rb3:RadioButton = dialogview.findViewById(R.id.other)
        val conf_bttn:Button = dialogview.findViewById(R.id.conf_bttn)
        val mem_name:TextView = dialogview.findViewById(R.id.memb_name_txt)

        conf_bttn.setOnClickListener {


            val mem_name = mem_name.text.toString()

            if (rb1.isChecked){
                gender = rb1.text.toString()
            }
            else if (rb2.isChecked){
                gender = rb2.text.toString()

            }
            else if (rb3.isChecked){
                gender = rb3.text.toString()

            }

            Toast.makeText(this, "$mem_name, $gender", Toast.LENGTH_LONG).show()

            dialog.dismiss()

        }


    }


}