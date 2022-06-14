package com.example.abc123

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.abc123.databinding.ActivitySetMajorBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class Set_Major : AppCompatActivity() {
    private val fireDatabase = FirebaseDatabase.getInstance().reference
    private lateinit var binding: ActivitySetMajorBinding
    val user = Firebase.auth.currentUser?.uid.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetMajorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val btn = findViewById<Button>(R.id.checkbtn)
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.humanities ->
                    fireDatabase.child("User/$user").child("major").setValue("인문")
                R.id.law ->
                    fireDatabase.child("User/$user").child("major").setValue("법과")
                R.id.natural ->
                    fireDatabase.child("User/$user").child("major").setValue("자연과학")
                R.id.software ->
                    fireDatabase.child("User/$user").child("major").setValue("소프트웨어")
                R.id.art ->
                    fireDatabase.child("User/$user").child("major").setValue("미술")
                R.id.social ->
                    fireDatabase.child("User/$user").child("major").setValue("사회과학")
                R.id.operation ->
                    fireDatabase.child("User/$user").child("major").setValue("경영")
                R.id.engineering ->
                    fireDatabase.child("User/$user").child("major").setValue("공과")
                R.id.biology ->
                    fireDatabase.child("User/$user").child("major").setValue("생명과학")
                R.id.arts ->
                    fireDatabase.child("User/$user").child("major").setValue("예술")
            }
        }
        btn.setOnClickListener {
            finish()
        }
    }
}

