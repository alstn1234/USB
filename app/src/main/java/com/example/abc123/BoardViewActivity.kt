package com.example.abc123

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.bumptech.glide.Glide
import com.example.abc123.databinding.ActivityBoardViewBinding
import com.google.common.collect.Range.all
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

data class FollowDto(
    var profileImageUrl: String? = ""
)

class BoardViewActivity : AppCompatActivity() {
    lateinit var binding: ActivityBoardViewBinding
    lateinit var list: Board_Model2
    lateinit var board_title: String
    private val fireDatabase = FirebaseDatabase.getInstance().getReference()
    private lateinit var CommentList: ArrayList<Commentmodel>
    val user = Firebase.auth.currentUser?.uid.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityBoardViewBinding.inflate(layoutInflater)
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("로딩중...")
        progressDialog.setCancelable(false)
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        board_title = intent.getStringExtra("title").toString()
        list = intent.getSerializableExtra("data") as Board_Model2
        binding.toolbar.title = board_title

        binding.title.text = list.title
        binding.nickname.text = list.nickname
        binding.timeDate.text = list.time
        binding.timeHd.text = list.time2
        binding.content.text = list.detail

        progressDialog.show()

        if (list.image_count == 1) {
            imagesize_change(binding.image1)
            image_load(list.img1, binding.image1)
        } else if (list.image_count == 2) {
            imagesize_change(binding.image1)
            imagesize_change(binding.image2)
            image_load(list.img1, binding.image1)
            image_load(list.img2, binding.image2)
        } else if (list.image_count == 3) {
            imagesize_change(binding.image1)
            imagesize_change(binding.image2)
            imagesize_change(binding.image3)
            image_load(list.img1, binding.image1)
            image_load(list.img2, binding.image2)
            image_load(list.img3, binding.image3)
        } else {
            if (progressDialog.isShowing)
                progressDialog.dismiss()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            if (progressDialog.isShowing)
                progressDialog.dismiss()
        }, 2000)

        binding.image1.setOnClickListener {
            if (list.image_count!!.toInt() == 1) {
                val intent22 = Intent(this, ImageActivity::class.java)
                intent22.putExtra("imageUri", list.img1)
                intent22.putExtra("img_num", "1")
                ContextCompat.startActivity(this, intent22, null)
            }
        }

        binding.image2.setOnClickListener {
            val intent22 = Intent(this, ImageActivity::class.java)
            if (list.image_count!!.toInt() >= 2) {
                intent22.putExtra("imageUri", list.img2)
                intent22.putExtra("img_num", "1")
                ContextCompat.startActivity(this, intent22, null)
            }
        }

        binding.image3.setOnClickListener {
            val intent22 = Intent(this, ImageActivity::class.java)
            if (list.image_count!!.toInt() >= 3) {
                intent22.putExtra("imageUri", list.img3)
                intent22.putExtra("img_num", "1")
                ContextCompat.startActivity(this, intent22, null)
            }
        }

        binding.commentupload.setOnClickListener {
            val contents = binding.commentcontents.text

            if (contents.toString() == "") {

            } else {
                val currentTime : Long = System.currentTimeMillis()
                var contents = contents.toString()
                val dates = SimpleDateFormat("yyyy-MM-dd hh:mm")
                val updates : MutableMap<String, Any> = HashMap()
                fireDatabase.child("board").child(list.board_title).child(list.key).child("comment_count").get().addOnSuccessListener {
                    val num = it.value.toString()
                    fireDatabase.child("User/$user/profileImageUrl").get().addOnSuccessListener{
                        fireDatabase.child("board").child(list.board_title).child(list.key).child("comment").child("$num/profileImageUrl").setValue(it.value.toString())
                    }
                    fireDatabase.child("board").child(list.board_title).child(list.key).child("comment").child("$num/uid").setValue(user)
                    fireDatabase.child("board").child(list.board_title).child(list.key).child("comment").child("$num/contents").setValue(contents)
                    fireDatabase.child("board").child(list.board_title).child(list.key).child("comment").child("$num/nickname").setValue(list.nickname)
                    fireDatabase.child("board").child(list.board_title).child(list.key).child("comment").child("$num/dates").setValue(dates.format(currentTime))
                    fireDatabase.child("board").child(list.board_title).child(list.key).child("comment").child("$num/favorite").setValue(0)
                }
                Toast.makeText(this, "완료", Toast.LENGTH_SHORT).show()
                updates["board/${list.board_title}/${list.key}/comment_count"] = ServerValue.increment(1)
                fireDatabase.updateChildren(updates)
            }
        }


        binding.comments.layoutManager = LinearLayoutManager(this)
        fireDatabase.child("board").child(list.board_title).child(list.key)
            .child("comment").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    CommentList = ArrayList<Commentmodel>()
                    if (snapshot.exists()){
                        CommentList.clear()
                        for (data in snapshot.children) {
                            val item = data.getValue(Commentmodel::class.java)
                            CommentList.add(item!!)
                        }
                    }
                    binding.comments.adapter = CommentAdapter(CommentList)
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })


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
                return super.onOptionsItemSelected(item)
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