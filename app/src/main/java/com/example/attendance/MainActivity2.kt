package com.example.attendance

import android.os.Bundle
import android.util.Log
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.attendance.databinding.ActivityMain2Binding
import com.example.attendance.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity2 : AppCompatActivity() {


    private lateinit var binding: ActivityMain2Binding
    private lateinit var view: View
    private lateinit var dialog: AlertDialog
    lateinit var auth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    lateinit var uid:String
    lateinit var gender:String
    private lateinit var dbreff: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var newarraylist: ArrayList<Member>
    private lateinit var myAdapter: MyAdapter2

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

        dbreff = FirebaseDatabase.getInstance().getReference("Users")

        binding.sessNameRecMa2.text = SessName
        binding.locRecMa2.text = loca
        binding.upDateRecMa2.text = "${binding.upDateRecMa2.text} $update"

        val sdf = SimpleDateFormat("dd/M/yyyy ")
        val currentDate = sdf.format(Date())

        binding.dateTv.text = "${binding.dateTv.text} $currentDate"

        newarraylist = arrayListOf<Member>()
        recyclerView = binding.recyclerViewMa2
        myAdapter = MyAdapter2(view.context, newarraylist)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = myAdapter
        initrecycler()
        getData(sess_id)


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
        dbreff.child(uid).child("Members").child(sid).child(MemberId.toString()).setValue(uploadd).addOnCompleteListener {

            Toast.makeText(this, "Uploaded the file", Toast.LENGTH_LONG).show()
            db.dismiss()




        }
            .addOnFailureListener {
                Toast.makeText(this, "$it", Toast.LENGTH_LONG).show()
            }


    }

    fun initrecycler(){

        Log.i("initrecycler","Function called")
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView = binding.recyclerViewMa2
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.layoutManager = layoutManager
        myAdapter = MyAdapter2(this,newarraylist)
        recyclerView.adapter = myAdapter
        myAdapter.notifyDataSetChanged()




    }

    fun getData(session_id:String) {//gets the url of image from database

        dbreff.child("$uid").child("Members").child(session_id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) { //updates the array list after change in database
                //Log.i("egomatic", snapshot.children.toString())
                for (i in snapshot.children) {

                    val mem_name = i.child("member_name").value.toString()//Used to get the specific snapshot value of sessionName
                    val mem_gender = i.child("mem_gender").value.toString()
                    val mem_id = i.child("memid").value.toString()

                    val up = Member(member_name = mem_name, mem_gender = mem_gender,memid = mem_id)
                    newarraylist.add(0,up) //adding 0 to get the latest added data first in recyclerview it adds to the last of list

                    Log.i("egomatic", i.child("session_name").value.toString())//testing
                    Log.i("egomatic", i.child("location").value.toString())
                    Log.i("egomatic", i.child("up_date").value.toString())

                }

                try {
                    myAdapter = MyAdapter2(this@MainActivity2, newarraylist)
                    recyclerView.adapter = myAdapter
                }
                catch (e:Exception){
                    Log.i("exc","$e")
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


}