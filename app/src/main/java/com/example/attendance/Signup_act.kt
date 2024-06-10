package com.example.attendance
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class Signup_act : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    var error = false
    private lateinit var mprogressbar: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_act)

        var emailtxt = findViewById<TextInputEditText>(R.id.email_txt)
        var psswdtxt = findViewById<TextInputEditText>(R.id.password_txt)
        val email_TIL = findViewById<TextInputLayout>(R.id.email_TIL)
        val password_TIL = findViewById<TextInputLayout>(R.id.password_TIL)
        val signup_bttn = findViewById<Button>(R.id.signup_bttn)




        auth = FirebaseAuth.getInstance()


        val txt_cl = findViewById<TextView>(R.id.tv_2_signup_act)

        txt_cl.setOnClickListener {

            startActivity(Intent(this@Signup_act, signin_act::class.java))

        }

        fun validate(): Boolean {
            if (emailtxt.text.toString().isEmpty()) {
                email_TIL.error = "Please Enter The Email"
                error = true
            } else {
                email_TIL.error = null
                error = false
            }
            if (psswdtxt.text.toString().isEmpty()) {
                password_TIL.error = "Please Enter The Password"
                error = true
            } else {
                password_TIL.error = null
                error = false
            }
            if (psswdtxt.text.toString().length <= 6) {
                password_TIL.error = "Password Should Be Atleast 6 Characters"
                error = true
            } else {
                password_TIL.error = null
                error = false
            }
            return error
        }


        fun progressbar() {
            mprogressbar = Dialog(this)
            mprogressbar.setContentView(R.layout.progressbar)
            mprogressbar.setCancelable(false)
            mprogressbar.setCanceledOnTouchOutside(false)
            mprogressbar.show()

        }





        signup_bttn.setOnClickListener {
            validate()
            if (validate() == false) {
                progressbar()
                auth.createUserWithEmailAndPassword(
                    emailtxt.text.toString(),
                    psswdtxt.text.toString()
                )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            auth.signOut()
                            startActivity(Intent(this@Signup_act, signin_act::class.java))
                            finish()
                        } else {
                            mprogressbar.dismiss()
                            Toast.makeText(
                                baseContext, "Authentication failed.${task.exception}",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.i("hya",task.exception.toString())
                        }
                    }
            }

        }


    }


}