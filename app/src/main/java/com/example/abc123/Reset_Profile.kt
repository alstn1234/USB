package com.example.abc123

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class Reset_Profile : AppCompatActivity() {
    private val fireDatabase = FirebaseDatabase.getInstance().getReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fireDatabase.child("MyEX/UserExample/profile")
            .setValue("https://firebasestorage.googleapis.com/v0/b/abc123-34300.appspot.com/o/profile%2Fbasicprofile.png?alt=media&token=bdd5a488-aa8d-4bbd-b17f-49e51af4af12")
        finish()
    }
}