package com.example.attendance

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class signin_act : AppCompatActivity() {

    private lateinit var usermananger:UserMananger
    private lateinit var mauth: FirebaseAuth
    private lateinit var mprogressbar: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        var error: Boolean
        var loginn: Boolean
        val email_et = findViewById<EditText>(R.id.email_txt_signin_act)
        val email_TIL = findViewById<TextInputLayout>(R.id.email_TIL_signin_act)
        val password_et = findViewById<EditText>(R.id.password_txt_signin_act)
        val password_TIL = findViewById<TextInputLayout>(R.id.password_TIL_signin_act)
        val login_bttn = findViewById<Button>(R.id.signin_bttn)

        usermananger= UserMananger(this)
        mauth = FirebaseAuth.getInstance()

        fun validate(): Boolean {
            if (email_et.text.toString().isEmpty()){
                email_TIL.error = "Please Enter The Email"
                error = true
            }
            else{
                email_TIL.error = null
                error = false
            }
            if(password_et.text.toString().isEmpty()){
                password_TIL.error = "Please Enter The Password"
                error = true
            }
            else{
                password_TIL.error = null
                error = false
            }
            return error
        }

        login_bttn.setOnClickListener {
            validate()
            if(validate()==false){
                progressbar()
                mauth.signInWithEmailAndPassword(email_et.text.toString(),password_et.text.toString())
                    .addOnCompleteListener (this){
                        if (it.isSuccessful){
                            loginn = true
                            GlobalScope.launch {
                                usermananger.storeloginkey(loginn)
                            }
                            if (loginn) {
                                startActivity(Intent(this@signin_act, MainActivity::class.java))
                                finishAffinity()
                            }
                        }
                        else{
                            mprogressbar.dismiss()
                            Log.i("er",it.exception.toString())
                            Toast.makeText(this,"Error ${it.exception}",Toast.LENGTH_LONG)
                                .show()
                        }
                    }
            }
        }

    }

    fun progressbar(){
        mprogressbar = Dialog(this)
        mprogressbar.setContentView(R.layout.progressbar)
        mprogressbar.setCancelable(false)
        mprogressbar.setCanceledOnTouchOutside(false)
        mprogressbar.show()

    }


    }
