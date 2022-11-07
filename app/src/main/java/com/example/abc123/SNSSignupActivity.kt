package com.example.abc123

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.abc123.databinding.ActivitySnssignupBinding
import com.example.test28.DataModel
import com.example.test28.SNSDataModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private lateinit var database: DatabaseReference

class SNSSignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val TAG: String = "CreateAccount"
    private  var uid:String?=null
    private lateinit var binding: ActivitySnssignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySnssignupBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        if(intent.hasExtra("uid")){
            uid=intent.getStringExtra("uid")
        }


        binding.SnsSignupbutton.setOnClickListener {

            database= Firebase.database.reference

            var uid:String=auth.currentUser?.uid.toString()
            val dataInput= DataModel(
                binding.snssignupname.text.toString(),
                binding.snssignupbirth.text.toString(),
                binding.snssignupnickname.text.toString(),
                uid
            )

            database.child("User").child(uid).setValue(dataInput)
            database.child("User").child(uid).child("profileImageUrl").setValue("https://firebasestorage.googleapis.com/v0/b/abc123-34300.appspot.com/o/profile%2Fbasicprofile.png?alt=media&token=bdd5a488-aa8d-4bbd-b17f-49e51af4af12")
            database.child("User").child(uid).child("major").setValue("")
            database.child("User").child(uid).child("school").setValue("")

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        binding.SnsCancelbutton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}