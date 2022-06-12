package com.example.abc123

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.abc123.databinding.ActivityBoardViewBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.HashMap

class BoardViewActivity : AppCompatActivity() {
    lateinit var binding: ActivityBoardViewBinding
    lateinit var list: Board_Model2
    lateinit var board_title: String
    private val fireDatabase = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityBoardViewBinding.inflate(layoutInflater)

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("로딩중...")
        progressDialog.setCancelable(false)
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal)
        var uid = Firebase.auth.currentUser?.uid.toString()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        board_title = intent.getStringExtra("title").toString()
        list = intent.getSerializableExtra("data") as Board_Model2
        binding.toolbar.title = board_title

        fireDatabase.child("User").child(list.name).child("profileImageUrl").get()
            .addOnSuccessListener {
                Glide.with(this).load(it.value).into(binding.profile)
            }

        binding.title.text = list.title
        binding.nickname.text = list.nickname
        binding.timeDate.text = list.time
        binding.timeHd.text = list.time2
        binding.content.text = list.detail

        progressDialog.show()

        if (list.image_count == 1) {
            imagesize_change(binding.image1)
            image_load(list.img1, binding.image1)
            Handler(Looper.getMainLooper()).postDelayed({
                if(progressDialog.isShowing)
                    progressDialog.dismiss()
            }, 2000)
        } else if (list.image_count == 2) {
            imagesize_change(binding.image1)
            imagesize_change(binding.image2)
            image_load(list.img1, binding.image1)
            image_load(list.img2, binding.image2)
            Handler(Looper.getMainLooper()).postDelayed({
                if(progressDialog.isShowing)
                    progressDialog.dismiss()
            }, 2000)
        } else if (list.image_count == 3) {
            imagesize_change(binding.image1)
            imagesize_change(binding.image2)
            imagesize_change(binding.image3)
            image_load(list.img1, binding.image1)
            image_load(list.img2, binding.image2)
            image_load(list.img3, binding.image3)
            Handler(Looper.getMainLooper()).postDelayed({
                if(progressDialog.isShowing)
                    progressDialog.dismiss()
            }, 2000)
        } else {
            if(progressDialog.isShowing)
            progressDialog.dismiss()
        }


        //채팅으로 이동
        binding.chat1.setOnClickListener {
            Toast.makeText(this@BoardViewActivity,"본인이랑 채팅을 할 수 없습니다.",Toast.LENGTH_LONG).show()
            if(list.name==uid){
                Toast.makeText(this@BoardViewActivity,"본인이랑 채팅을 할 수 없습니다.",Toast.LENGTH_LONG).show()
            }
            else {
                val intent = Intent(this, MessageActivity::class.java)
                intent.putExtra("destinationUid", list.name)
                startActivity(intent)
            }
        }

        binding.image1.setOnClickListener{
            if(list.image_count!!.toInt() == 1) {
                val intent22 = Intent(this, ImageActivity::class.java)
                intent22.putExtra("imageUri", list.img1)
                intent22.putExtra("img_num", "1")
                ContextCompat.startActivity(this, intent22, null)
            }
        }

        binding.image2.setOnClickListener{
            val intent22 = Intent(this, ImageActivity::class.java)
            if(list.image_count!!.toInt() >= 2) {
                intent22.putExtra("imageUri", list.img2)
                intent22.putExtra("img_num", "1")
                ContextCompat.startActivity(this, intent22, null)
            }
        }

        binding.image3.setOnClickListener{
            val intent22 = Intent(this, ImageActivity::class.java)
            if(list.image_count!!.toInt() >= 3) {
                intent22.putExtra("imageUri", list.img3)
                intent22.putExtra("img_num", "1")
                ContextCompat.startActivity(this, intent22, null)
            }
        }



    }

    fun image_load(str: String, img: ImageView) {
        val storageReference = FirebaseStorage.getInstance().getReference("images/" + str)
        storageReference.downloadUrl.addOnSuccessListener {
            val photoUri: Uri = it
            Glide.with(this).load(photoUri.toString()).placeholder(R.drawable.ic_outline_image_24)
                .error(R.drawable.ic_baseline_error_24).into(img)
        }
    }


    fun imagesize_change(image: ImageView) {
        image.getLayoutParams().width = 300
        image.getLayoutParams().height = 300
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_boardview_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                //뒤로가기 버튼 눌렀을 때
                finish()
                return true
            }
            R.id.board_edit -> {
                val intent = Intent(this, boardupdateActivity::class.java)
                if (list.name == Firebase.auth.currentUser?.uid) {
                    intent.putExtra("data", list)
                    intent.putExtra("title", board_title)
                    finish()
                    startActivity(intent)
                }else{
                    Toast.makeText(this, "권한이 없습니다.", Toast.LENGTH_LONG).show()
                }
            }
            R.id.board_delete -> {
                if (list.name == Firebase.auth.currentUser?.uid) {
                    val dlg: AlertDialog.Builder = AlertDialog.Builder(this)
                    dlg.setTitle("경고") //제목
                    dlg.setMessage("해당 게시글을 삭제하시겠습니까?") // 메시지
                    dlg.setIcon(R.mipmap.ic_launcher)
                    dlg.setCancelable(false)

                    dlg.setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                        val key = list.key
                        val updates : MutableMap<String, Any> = HashMap()
                        fireDatabase.child("board").child(list.board_title + "_last_post").get()
                            .addOnSuccessListener {
                                val key2 = it.value.toString()
                                if(key==key2){
                                    updates["board/${list.board_title+"_last_post"}"] = ""
                                    FirebaseDatabase.getInstance().getReference().updateChildren(updates)
                                }
                            }.addOnFailureListener{
                            }
                        FirebaseDatabase.getInstance().getReference().child("board")
                            .child(list.board_title).child("$key").removeValue()
                        finish()
                    })
                    dlg.setNegativeButton("아니오", DialogInterface.OnClickListener { dialog, which ->
                    })

                    dlg.create().show()
                } else {
                    Toast.makeText(this, "권한이 없습니다.", Toast.LENGTH_LONG).show()
                }
            }

        }
        return super.onOptionsItemSelected(item)
    }

}