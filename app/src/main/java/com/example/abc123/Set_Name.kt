package com.example.abc123

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.abc123.databinding.ActivitySetNameBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class Set_Name : AppCompatActivity() {
    private val fireDatabase = FirebaseDatabase.getInstance().getReference()
    private lateinit var mybinding: ActivitySetNameBinding
    val user = Firebase.auth.currentUser?.uid.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        mybinding = ActivitySetNameBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(mybinding.root)
        mybinding.EditNickbt.setOnClickListener {
            val PlayerNickname = mybinding.editTextName.text
            if (PlayerNickname.toString() == "") {
                Toast.makeText(this, "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                var nickdata = PlayerNickname.toString()
                fireDatabase.child("User/$user").child("nickname").setValue(nickdata)
                finish()
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }

}