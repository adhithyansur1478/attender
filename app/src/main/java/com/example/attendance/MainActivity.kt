package com.example.attendance

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.attendance.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.GlobalScope

import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date



class MainActivity : AppCompatActivity() {
    private lateinit var usermananger: UserMananger
    lateinit var auth: FirebaseAuth
    var loginn = false
    private lateinit var binding: ActivityMainBinding
    private lateinit var view: View
    private lateinit var mdb: Dialog
    lateinit var database: DatabaseReference
    private lateinit var uid: String
    private lateinit var dbreff: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var newarraylist: ArrayList<User>
    private lateinit var myAdapter: MyAdapter


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        view = binding.root
        //setContentView(view)

        installSplashScreen()

        setSupportActionBar(binding.toolbar)

        usermananger = UserMananger(this)
        auth = FirebaseAuth.getInstance()

        ObserveData()













    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        if (id == R.id.menu_addbttn) {

            val intent = Intent(this, SessDetAct::class.java)
            startActivity(intent)

        }
        if (id == R.id.menu_searchbttn) {

        }

        if (id == R.id.menu_signoutbttn) {

            auth.signOut()
            loginn = false
            GlobalScope.launch {
                usermananger.storeloginkey(loginn)
            }

            val intent = Intent(this, Signup_act::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)


        }

        return true
    }

    fun ObserveData() {

        this.usermananger.userLoginFlow.asLiveData().observe(this) {
            loginn = it
            checkrr()

        }
    }


    fun checkrr() {
        if (loginn) {

            val currentuser = auth.currentUser
            if (currentuser != null) {
                uid = currentuser.uid
                Toast.makeText(this, "$uid", Toast.LENGTH_LONG).show()


            }
            else{

                val intent = Intent(this, Signup_act::class.java)
                startActivity(intent)
            }

            setContentView(view)
            database = FirebaseDatabase.getInstance().getReference("Users")


            newarraylist = arrayListOf<User>()
            recyclerView = binding.recyclerView
            myAdapter = MyAdapter(view.context, newarraylist)

            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = myAdapter

            initrecycler()
            getData()


        }
        if (!loginn) {

            val intent = Intent(this, Signup_act::class.java)
            startActivity(intent)
            finishAffinity()

        }


    }

    fun initrecycler(){

        Log.i("initrecycler","Function called")
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView = binding.recyclerView
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.layoutManager = layoutManager
        myAdapter = MyAdapter(this,newarraylist)
        recyclerView.adapter = myAdapter
        myAdapter.notifyDataSetChanged()




    }


    fun getData() {//gets the url of image from database

        database.child("$uid").child("Sessions").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) { //updates the array list after change in database
                //Log.i("egomatic", snapshot.children.toString())
                for (i in snapshot.children) {

                    val sess = i.child("session_name").value.toString()//Used to get the specific snapshot value of sessionName
                    val loc = i.child("location").value.toString()
                    val dat = i.child("up_date").value.toString()
                    val sessid = i.child("sessid").value.toString()

                    val up = User(session_name = sess,up_date = dat,location = loc,sessid=sessid)
                    newarraylist.add(0,up) //adding 0 to get the latest added data first in recyclerview it adds to the last of list

                    Log.i("egomatic", i.child("session_name").value.toString())//testing
                    Log.i("egomatic", i.child("location").value.toString())
                    Log.i("egomatic", i.child("up_date").value.toString())

                }

                try {
                    myAdapter = MyAdapter(this@MainActivity, newarraylist)
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




