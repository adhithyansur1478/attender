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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity2 : AppCompatActivity() {


    private lateinit var binding: ActivityMain2Binding
    private lateinit var view: View
    private lateinit var dialog: AlertDialog
    lateinit var auth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    lateinit var uid:String
    lateinit var genderGroup:RadioGroup
    lateinit var gender:String
    private lateinit var dbreff: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()

        var SessName = intent.extras!!.getString("SessName")
        var loca = intent.extras!!.getString("Location")
        var update = intent.extras!!.getString("Update")
        var sess_id = intent.extras!!.getString("sessid").toString()

        val currentuser = auth.currentUser
        if (currentuser != null) {
            uid = currentuser.uid
        }

        binding.sessNameRecMa2.text = SessName
        binding.locRecMa2.text = loca
        binding.upDateRecMa2.text = "${binding.upDateRecMa2.text} $update"

        val sdf = SimpleDateFormat("dd/M/yyyy ")
        val currentDate = sdf.format(Date())

        binding.dateTv.text = "${binding.dateTv.text} $currentDate"


        binding.addBttn.setOnClickListener {


            showdb(sess_id)

        }



    }

    fun showdb(sid:String){

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

            upload_memdet(mem_name,gender,sid,dialog)
        }


    }

    fun upload_memdet(nm:String,gn:String,sid:String,db:AlertDialog){

        database = FirebaseDatabase.getInstance()
        dbreff = database.getReference("Users")

        var MemberId: String? = dbreff.push().key//creates a random key
        var uploadd = Member(member_name =nm, mem_gender =gn , memid = MemberId.toString())
        dbreff.child(uid).child("Sessions").child("Members").child(sid).child(MemberId.toString()).setValue(uploadd).addOnCompleteListener {

            Toast.makeText(this, "Uploaded the file", Toast.LENGTH_LONG).show()
            db.dismiss()




        }
            .addOnFailureListener {
                Toast.makeText(this, "$it", Toast.LENGTH_LONG).show()
            }


    }


}