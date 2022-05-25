package com.example.abc123

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.abc123.databinding.ActivitySetMajorBinding
import com.example.abc123.databinding.ActivitySetNameBinding
import com.google.firebase.database.FirebaseDatabase

class Set_Major : AppCompatActivity() {
    private val fireDatabase = FirebaseDatabase.getInstance().getReference()
    private lateinit var mybinding: ActivitySetMajorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_major)
    }
}
