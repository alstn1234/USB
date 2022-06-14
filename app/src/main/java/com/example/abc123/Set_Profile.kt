package com.example.abc123

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class Set_Profile : AppCompatActivity() {
    private var imageUri: Uri? = null
    private val fireStorage = FirebaseStorage.getInstance().reference
    private val fireDatabase = FirebaseDatabase.getInstance().reference
    val user = Firebase.auth.currentUser?.uid.toString()

    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                imageUri = result.data?.data //이미지 경로 원본
                fireStorage.child("User/$user").putFile(imageUri!!)
                    .addOnSuccessListener {
                        fireStorage.child("User/$user").downloadUrl.addOnSuccessListener {
                            val photoUri: Uri = it
                            fireDatabase.child("User/$user/profileImageUrl")
                                .setValue(photoUri.toString())
                            finish()
                        }
                    }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intentImage = Intent(Intent.ACTION_PICK)
        intentImage.type = MediaStore.Images.Media.CONTENT_TYPE
        getContent.launch(intentImage)

    }
}