package com.jinpyo.talk

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide

class ImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        val chatimg = findViewById<ImageView>(R.id.image_full)
        val img = intent.getStringExtra("imageUri")
Glide.with(this).load(img).into(chatimg)



    }


}






