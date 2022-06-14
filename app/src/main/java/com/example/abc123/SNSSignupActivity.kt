package com.example.abc123

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.abc123.databinding.ActivitySnssignupBinding
import com.example.test28.SNSDataModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

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


<<<<<<< HEAD
        binding.SnsSignupbutton.setOnClickListener {
=======
        binding.snssignupbutton.setOnClickListener {
>>>>>>> 7d16a551fe09ae78110c88ca11c363485ecabea6

            val database= FirebaseDatabase.getInstance()
            val myRef=database.getReference("SNSUsers")

            val dataInput= SNSDataModel(
                binding.snssignupname.text.toString(),
                binding.snssignupbirth.text.toString(),
                binding.snssignupnickname.text.toString()
            )
            var uid:String=auth.currentUser?.uid.toString()
            myRef.child(uid.toString()).push().setValue(dataInput)
        }

<<<<<<< HEAD
        binding.SnsCancelbutton.setOnClickListener{
=======
        binding.snscancelbutton.setOnClickListener{
>>>>>>> 7d16a551fe09ae78110c88ca11c363485ecabea6
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}