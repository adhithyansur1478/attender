package com.example.attendance

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.attendance.databinding.ActivityMainBinding
import com.example.attendance.databinding.ActivitySessDetBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date

class SessDetAct : AppCompatActivity() {
    private lateinit var binding: ActivitySessDetBinding
    lateinit var database: FirebaseDatabase
    lateinit var auth: FirebaseAuth
    private lateinit var view: View
    private lateinit var uid: String
    private lateinit var dbreff: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)

        binding = ActivitySessDetBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        uid = auth.currentUser?.uid.toString()


        binding.addSessionBttn.setOnClickListener {

            upload(binding.sessnameTxtSessdetAct.text.toString(),binding.locaTxtSessdetAct.text.toString())

        }





    }

    fun upload(sess:String,loc:String){
        //Toast.makeText(this, "Uploaded the file", Toast.LENGTH_LONG).show()
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        var uploadd = User(session_name =sess, up_date =currentDate ,location = loc)
        database = FirebaseDatabase.getInstance()
        dbreff = database.getReference("Users")

        var SessionId: String? = dbreff.push().key//creates a random key
        dbreff.child(uid).child("Sessions").child(SessionId.toString()).setValue(uploadd).addOnCompleteListener {

            Toast.makeText(this, "Uploaded the file", Toast.LENGTH_LONG).show()

            Toast.makeText(this, "Uploaded the file", Toast.LENGTH_LONG).show()


        }
            .addOnFailureListener {
                Toast.makeText(this, "$it", Toast.LENGTH_LONG).show()
            }

    }




}