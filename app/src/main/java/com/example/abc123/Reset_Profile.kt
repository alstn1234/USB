package com.example.abc123

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class Reset_Profile : AppCompatActivity() {
    private val fireDatabase = FirebaseDatabase.getInstance().getReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fireDatabase.child("MyEX/UserExample/profile")
            .setValue("https://firebasestorage.googleapis.com/v0/b/abc123-34300.appspot.com/o/profile%2Fbasicprofile.png?alt=media&token=bdd5a488-aa8d-4bbd-b17f-49e51af4af12")
        finish()
    }
}