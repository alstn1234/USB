package com.example.abc123

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.adapters.NumberPickerBindingAdapter.setValue
import com.example.abc123.databinding.ActivitySetNameBinding
import com.example.abc123.fragments.MypageFragment
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_set_name.*


class Set_Name : AppCompatActivity() {
    private val fireDatabase = FirebaseDatabase.getInstance().getReference()
    private lateinit var mybinding: ActivitySetNameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        mybinding = ActivitySetNameBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(mybinding.root)
        mybinding.EditNickbt.setOnClickListener {
            val PlayerNickname = mybinding.editTextName.text
            if (PlayerNickname.toString() == ""){
                Toast.makeText(this,"닉네임을 입력해주세요",Toast.LENGTH_SHORT).show()
            }
            var nickdata = PlayerNickname.toString()
            fireDatabase.child("MyEX").child("UserExample").child("nickname").setValue(nickdata)
            Toast.makeText(this, "완료", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onBackPressed() {
        startActivity(Intent(this, MypageFragment::class.java))
        finish()
    }

}