package com.example.attendance

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
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
    lateinit var database: FirebaseDatabase
    private lateinit var uid: String
    private lateinit var dbreff: DatabaseReference


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


        val currentuser = auth.currentUser
        if (currentuser != null) {
            uid = currentuser.uid
            Toast.makeText(this, "$uid", Toast.LENGTH_LONG).show()


        }









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

            setContentView(view)


        }
        if (!loginn) {

            val intent = Intent(this, Signup_act::class.java)
            startActivity(intent)
            finishAffinity()

        }


    }






}




