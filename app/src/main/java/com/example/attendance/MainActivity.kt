package com.example.attendance

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.asLiveData

class MainActivity : AppCompatActivity() {
    private lateinit var usermananger:UserMananger
    var loginn = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        //setContentView(R.layout.activity_main)


        usermananger = UserMananger(this)


       ObserveData()







    }

    fun ObserveData(){

        this.usermananger.userLoginFlow.asLiveData().observe(this){
            loginn = it
            checkrr()

        }
    }


    fun checkrr() {
        if (loginn) {

            setContentView(R.layout.activity_main)

        }
        if (!loginn) {

            val intent = Intent(this,Signup_act::class.java)
            startActivity(intent)

        }


    }


}