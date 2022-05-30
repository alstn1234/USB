package com.example.abc123

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class Reset_Profile : AppCompatActivity() {
    private val fireDatabase = FirebaseDatabase.getInstance().getReference()
    val user = Firebase.auth.currentUser?.uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fireDatabase.child("User").child(user.toString()).child("profileImageUrl")
            .setValue("https://firebasestorage.googleapis.com/v0/b/abc123-34300.appspot.com/o/profile%2Fbasicprofile.png?alt=media&token=bdd5a488-aa8d-4bbd-b17f-49e51af4af12")
        finish()
    }
}