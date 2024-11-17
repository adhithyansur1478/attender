package com.example.attendance

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.asLiveData
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date

private lateinit var sess_id: String
var uid:String = ""
private lateinit var sess_name:String
class MainActivity2 : AppCompatActivity() {

    companion object {
        private const val CAMERA_PERMISSION_CODE = 100
        private const val STORAGE_PERMISSION_CODE = 101
    }


    private lateinit var binding: ActivityMain2Binding
    private lateinit var view: View
    private lateinit var dialog: AlertDialog
    lateinit var auth: FirebaseAuth
    private lateinit var update:String
    lateinit var database: FirebaseDatabase
    lateinit var currentDate:String
    lateinit var gender:String
    private lateinit var dbreff: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var newarraylist: ArrayList<Member>
    private lateinit var myAdapter: MyAdapter2
    private val REQUEST_CODE_PERMISSION = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl")
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl")
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl")

        binding = ActivityMain2Binding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSION)
        }



        auth = FirebaseAuth.getInstance()
        update = ""


        var SessName = intent.extras!!.getString("SessName")
        var loca = intent.extras!!.getString("Location")
        //update = intent.extras!!.getString("Update")
        sess_id = intent.extras!!.getString("sessid").toString()
        sess_name = SessName.toString()


        val currentuser = auth.currentUser

        if (currentuser != null) {
            uid = currentuser.uid
        }
            Toast.makeText(this, "$uid", Toast.LENGTH_LONG).show()






        dbreff = FirebaseDatabase.getInstance().getReference("Users")


        binding.sessNameRecMa2.text = SessName
        binding.locRecMa2.text = loca






        val sdf = SimpleDateFormat("dd/M/yyyy")
        currentDate = sdf.format(Date())

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


        binding.uploadBttn.setOnClickListener {





            upload_bttn()

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
        var uploadd = Member(member_name =nm, mem_gender =gn , memid = MemberId.toString(),mem_chbx = false,up_date = currentDate, pres = 0)
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
                newarraylist.clear()
                //Log.i("egomatic", snapshot.children.toString())
                for (i in snapshot.children) {

                    val mem_name = i.child("member_name").value.toString()//Used to get the specific snapshot value of sessionName
                    val mem_gender = i.child("mem_gender").value.toString()
                    val mem_id = i.child("memid").value.toString()
                    val mem_chbx = i.child("mem_chbx").value.toString().toBoolean()
                    update = i.child("up_date").value.toString()
                    val pres  = i.child("pres").value.toString().toInt()


                    val up = Member(member_name = mem_name, mem_gender = mem_gender,memid = mem_id,mem_chbx=mem_chbx,up_date = update, pres = pres )
                    newarraylist.add(0,up) //adding 0 to get the latest added data first in recyclerview it adds to the last of list



                    Log.i("egomaticc", i.child("mem_chbx").value.toString())//testing
                    Log.i("egomatic", i.child("location").value.toString())
                    Log.i("hui", i.child("up_date").value.toString())

                }
                Log.i("hkk", newarraylist[0].member_name.toString())//testing

                if (newarraylist.isNotEmpty()) {
                    var i = 1

                    var dt = 0
                    var grt = newarraylist[0].up_date.toString().substringBefore("/").toInt()
                    //Log.i("jik",newarraylist[0].up_date.toString())
                    while (i < newarraylist.size) {

                        if (newarraylist[i].up_date.toString().substringBefore("/").toInt() > grt) {
                            grt = newarraylist[i].up_date.toString().substringBefore("/").toInt()
                            dt = i


                        }
                        i++
                    }



                    Log.i("jlkkkkk", newarraylist[dt].up_date.toString())
                    update = newarraylist[dt].up_date.toString()
                    binding.upDateRecMa2.text = "Updated Date: $update"

                }
                val sdf = SimpleDateFormat("dd/M/yyyy")
                var curtDate = sdf.format(Date())

                if(update.substringAfter("/")!=curtDate.substringAfter("/")){


                    updateAgeForAllPeople(false)
                    Log.i("jlk",update.substringAfter("/").toString())
                    Log.i("jlkk","${curtDate} ${curtDate.substringAfter("/").toString()}")

                }

                if (update!=curtDate){

                    updateAgeForAllPeople2(0)

                }



                val mem_count = newarraylist.size.toString()
                binding.membercountTv.text = "Total Number Of Members: $mem_count"

                try {
                    myAdapter = MyAdapter2(this@MainActivity2, newarraylist)
                    recyclerView.adapter = myAdapter
                    myAdapter.notifyDataSetChanged()
                    initrecycler()

                }
                catch (e:Exception){
                    Log.i("exc","$e")
                }



            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity2, error.toString(), Toast.LENGTH_LONG).show()
            }
        })



    }

    fun sketchbox2(MemberId: String?,value:Int,cont: Context){

        val sdf = SimpleDateFormat("dd/M/yyyy")
        var curtDate = sdf.format(Date())

        database = FirebaseDatabase.getInstance()
        dbreff = database.getReference("Users")



        dbreff.child(uid).child("Members").child(sess_id).child(MemberId.toString())
            .child("pres").setValue(value).addOnCompleteListener {

                dbreff.child(uid).child("Members").child(sess_id).child(MemberId.toString())
                    .child("up_date").setValue(curtDate).addOnCompleteListener {
                        //Toast.makeText(cont, "$uid huhu", Toast.LENGTH_LONG).show()
                        Log.i("haha", "$uid")
                        Log.i("hahaha", "$sess_id")


                    }
                    .addOnFailureListener {
                        Toast.makeText(cont, "$it", Toast.LENGTH_LONG).show()
                    }

            }.addOnFailureListener {
                Toast.makeText(cont, "$it", Toast.LENGTH_LONG).show()
            }
    }

    fun setchkbox(MemberId:String?,value:Boolean,cont:Context) {

        val sdf = SimpleDateFormat("dd/M/yyyy")
        var curtDate = sdf.format(Date())

        database = FirebaseDatabase.getInstance()
        dbreff = database.getReference("Users")



        dbreff.child(uid).child("Members").child(sess_id).child(MemberId.toString())
            .child("mem_chbx").setValue(value).addOnCompleteListener {

            dbreff.child(uid).child("Members").child(sess_id).child(MemberId.toString())
                .child("up_date").setValue(curtDate).addOnCompleteListener {
                //Toast.makeText(cont, "$uid huhu", Toast.LENGTH_LONG).show()
                Log.i("haha", "$uid")
                Log.i("hahaha", "$sess_id")


            }
                .addOnFailureListener {
                    Toast.makeText(cont, "$it", Toast.LENGTH_LONG).show()
                }

        }.addOnFailureListener {
                Toast.makeText(cont, "$it", Toast.LENGTH_LONG).show()
            }


    }

    fun updateAgeForAllPeople(newval: Boolean) {

        val sdf = SimpleDateFormat("dd/M/yyyy")
        var curtDate = sdf.format(Date())
        // Get the reference to the "people" node
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val peopleRef: DatabaseReference = database.getReference("Users")

        // Read the data of the "people" node
        peopleRef.child(uid).child("Members").child(sess_id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Iterate over each child node
                for (childSnapshot in dataSnapshot.children) {
                    // Get the key of the child node
                    val childKey = childSnapshot.key
                    Log.i("kyy",childKey.toString())
                    // Check if the child node exists
                    if (childKey != null) {
                        //Set the age to the new value (18)
                        val childUpdateMap = mapOf("mem_chbx" to newval)
                        val childUpdateMapp = mapOf("up_date" to curtDate)
                        peopleRef.child(uid).child("Members").child(sess_id).child(childKey).updateChildren(childUpdateMap).addOnCompleteListener { task ->
                            Log.i("huhu", "woooh")
                            if (task.isSuccessful) {
                                println("Age updated successfully for person: $childKey")
                                peopleRef.child(uid).child("Members").child(sess_id).child(childKey)
                                    .updateChildren(childUpdateMapp).addOnCompleteListener { task ->
                                    println("Age updated successfully for person: $childKey")

                                }
                            } else {
                                // Handle the error
                                println("Failed to update age for person: $childKey")
                            }
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            // Handle database error
            println("Database error: ${databaseError.message}")
        }
        })
    }


    fun updateAgeForAllPeople2(newval:Int){

        val sdf = SimpleDateFormat("dd/M/yyyy")
        var curtDate = sdf.format(Date())
        // Get the reference to the "people" node
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val peopleRef: DatabaseReference = database.getReference("Users")

        // Read the data of the "people" node
        peopleRef.child(uid).child("Members").child(sess_id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Iterate over each child node
                for (childSnapshot in dataSnapshot.children) {
                    // Get the key of the child node
                    val childKey = childSnapshot.key
                    Log.i("kyy",childKey.toString())
                    // Check if the child node exists
                    if (childKey != null) {
                        // Set the age to the new value (18)
                        val childUpdateMap = mapOf("pres" to newval)
                        val childUpdateMapp = mapOf("up_date" to curtDate)
                        peopleRef.child(uid).child("Members").child(sess_id).child(childKey).updateChildren(childUpdateMap).addOnCompleteListener { task ->
                            Log.i("huhu", "woooh")
                            if (task.isSuccessful) {
                                println("Age updated successfully for person: $childKey")
                                peopleRef.child(uid).child("Members").child(sess_id).child(childKey)
                                    .updateChildren(childUpdateMapp).addOnCompleteListener { task ->
                                    println("Age updated successfully for person: $childKey")

                                }
                            } else {
                                // Handle the error
                                println("Failed to update age for person: $childKey")
                            }
                        }
                        }

                    }
                }


            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
                println("Database error: ${databaseError.message}")
            }
        })

    }

    fun upload_bttn(){


        val emptlst = mutableListOf<List<String>>()

        for (i in newarraylist){


            Log.i("jio",i.toString())
            if (i.pres==1){

                emptlst.add(listOf<String>(i.member_name.toString(),"Present"))

            }
            else{
                emptlst.add(listOf<String>(i.member_name.toString(),"Absent"))
            }




        }

        emptlst.toList()//To make the list work in the down forloop
        Log.i("jwn",emptlst.toString())

        ExcelUtils(this).writeDataToExcel(currentDate,emptlst, "$sess_name")
    }







}