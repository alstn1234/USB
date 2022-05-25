package com.example.abc123

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.abc123.databinding.ActivitySetMajorBinding
import com.example.abc123.databinding.ActivitySetNameBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_set_major.*
import kotlinx.android.synthetic.main.item_profile.*

class Set_Major : AppCompatActivity() {
    private val fireDatabase = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_major)
        val btn = findViewById<Button>(R.id.checkbtn)
        radio_group.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.humanities ->
                    fireDatabase.child("MyEX").child("UserExample").child("major").setValue("인문")
                R.id.law ->
                    fireDatabase.child("MyEX").child("UserExample").child("major").setValue("법과")
                R.id.natural->
                    fireDatabase.child("MyEX").child("UserExample").child("major").setValue("자연과학")
                R.id.software ->
                    fireDatabase.child("MyEX").child("UserExample").child("major").setValue("소프트웨어")
                R.id.art ->
                    fireDatabase.child("MyEX").child("UserExample").child("major").setValue("미술")
                R.id.social ->
                    fireDatabase.child("MyEX").child("UserExample").child("major").setValue("사회과학")
                R.id.operation ->
                    fireDatabase.child("MyEX").child("UserExample").child("major").setValue("경영")
                R.id.engineering ->
                    fireDatabase.child("MyEX").child("UserExample").child("major").setValue("공과")
                R.id.biology ->
                    fireDatabase.child("MyEX").child("UserExample").child("major").setValue("생명과학")
                R.id.arts ->
                    fireDatabase.child("MyEX").child("UserExample").child("major").setValue("예술")
            }
        }
        btn.setOnClickListener{
            finish()
        }
    }
}

