package com.example.abc123

import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage

class ImageActivity : AppCompatActivity() {

    private var mScaleGestureDetector: ScaleGestureDetector? = null
    private var scaleFactor = 1.0f
    private lateinit var chatimg: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
         chatimg = findViewById(R.id.image_full)


        val img = intent.getStringExtra("imageUri")
        val img_num = intent.getStringExtra("img_num")
mScaleGestureDetector= ScaleGestureDetector(this,ScaleListener())
        if (img_num == "1") {
            val storageReference = FirebaseStorage.getInstance().getReference("images/" + img)
            storageReference.downloadUrl.addOnSuccessListener {
                val photoUri = it
                Glide.with(this).load(photoUri).into(chatimg)
            }
        } else
            Glide.with(this).load(img).into(chatimg)


    }
    // 제스처 이벤트가 발생하면 실행되는 메소드
    override fun onTouchEvent(motionEvent: MotionEvent?): Boolean {

        // 제스처 이벤트를 처리하는 메소드를 호출
        mScaleGestureDetector!!.onTouchEvent(motionEvent)
        return true
    }

    // 제스처 이벤트를 처리하는 클래스
    inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {

            scaleFactor *= scaleGestureDetector.scaleFactor

            // 최소 0.5, 최대 2배
            scaleFactor = Math.max(0.5f, Math.min(scaleFactor, 2.0f))

            // 이미지에 적용
            chatimg.scaleX = scaleFactor
            chatimg.scaleY = scaleFactor
            return true
        }
    }

}






