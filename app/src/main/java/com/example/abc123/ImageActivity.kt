package com.example.abc123

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage

class ImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        val chatimg = findViewById<ImageView>(R.id.image_full)
        val img = intent.getStringExtra("imageUri")
        val img_num = intent.getStringExtra("img_num")

        if (img_num == "1") {
            val storageReference = FirebaseStorage.getInstance().getReference("images/" + img)
            storageReference.downloadUrl.addOnSuccessListener {
                val photoUri = it
                Glide.with(this).load(photoUri).into(chatimg)
            }
        } else
            Glide.with(this).load(img).into(chatimg)


    }


}






