package com.example.abc123

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.abc123.fragments.MypageFragment

class Set_Name : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_name)
    }


    override fun onBackPressed() {
        startActivity(Intent(this, MypageFragment::class.java))
        finish()
    }
}