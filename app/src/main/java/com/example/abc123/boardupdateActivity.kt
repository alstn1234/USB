package com.example.abc123

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.abc123.databinding.ActivityBoardupdateBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class boardupdateActivity : AppCompatActivity() {

    val REQ_STORAGE = 101
    var image_count: Int = 0
    lateinit var wbinding: ActivityBoardupdateBinding
    var time: String = ""
    var time2: String = ""
    lateinit var array_img: ArrayList<String>
    lateinit var array_uri: ArrayList<Uri>
    lateinit var image_count_new: ArrayList<Boolean>
    lateinit var fileName: String
    lateinit var board_id: String
    var img_str: String = ""
    val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
    val now = Date()
    val range = (1..999999)
    val random = range.random()
    var state = false
    val user = Firebase.auth.currentUser?.uid
    lateinit var list: Board_Model2
    lateinit var dataInput: Board_Model2
    lateinit var board_title: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wbinding = ActivityBoardupdateBinding.inflate(layoutInflater)
        setContentView(wbinding.root)
        list = intent.getSerializableExtra("data") as Board_Model2
        board_title = intent.getStringExtra("title").toString()
        array_img = arrayListOf<String>(list.img1, list.img2, list.img3)
        array_uri = arrayListOf<Uri>("".toUri(), "".toUri(), "".toUri())
        image_count_new = arrayListOf<Boolean>(false, false, false)
        image_count = list.image_count!!.toInt()

        wbinding.titleEt.setText(list.title)
        wbinding.contentEt.setText(list.detail)

        if (list.image_count!!.toInt() >= 1) {
            val storageReference = FirebaseStorage.getInstance().getReference("images/${list.img1}")
            imagesize_change(wbinding.imageview1)
            storageReference.downloadUrl.addOnSuccessListener {
                val photoUri: Uri = it
                Glide.with(this).load(photoUri.toString())
                    .placeholder(R.drawable.ic_outline_image_24)
                    .error(R.drawable.ic_baseline_error_24).into(wbinding.imageview1)
            }
                .addOnFailureListener {
                }
        }
        if (list.image_count!!.toInt() >= 2) {
            val storageReference = FirebaseStorage.getInstance().getReference("images/${list.img2}")
            imagesize_change(wbinding.imageview2)
            storageReference.downloadUrl.addOnSuccessListener {
                val photoUri: Uri = it
                Glide.with(this).load(photoUri.toString())
                    .placeholder(R.drawable.ic_outline_image_24)
                    .error(R.drawable.ic_baseline_error_24).into(wbinding.imageview2)
            }
                .addOnFailureListener {
                }
        }
        if (list.image_count!!.toInt() >= 3) {
            val storageReference = FirebaseStorage.getInstance().getReference("images/${list.img3}")
            imagesize_change(wbinding.imageview3)
            storageReference.downloadUrl.addOnSuccessListener {
                val photoUri: Uri = it
                Glide.with(this).load(photoUri.toString())
                    .placeholder(R.drawable.ic_outline_image_24)
                    .error(R.drawable.ic_baseline_error_24).into(wbinding.imageview3)
            }
                .addOnFailureListener {
                }
        }


        val long_now = System.currentTimeMillis()

        // 현재 시간을 Date 타입으로 변환
        val t_date = Date(long_now)

        // 날짜, 시간을 가져오고 싶은 형태 선언
        val t_dateFormat = SimpleDateFormat("HH:mm")
        val t_dateFormat2 = SimpleDateFormat("yyyy.MM.dd")

        // 현재 시간을 dateFormat 에 선언한 형태의 String 으로 변환
        time2 = t_dateFormat.format(t_date)
        time = t_dateFormat2.format(t_date)

        setSupportActionBar(wbinding.nametoolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)

        wbinding.gallery.setOnClickListener {
            if (image_count >= 3) {
                Toast.makeText(this, "사진은 3장까지 업로드 가능합니다.", Toast.LENGTH_SHORT).show()
            } else {
                fromGallery()
            }
        }
    }

    fun fromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, REQ_STORAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val time_now = formatter.format(now)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQ_STORAGE -> {
                    data?.data?.let { uri ->
                        if (image_count == 0) {
                            wbinding.imageview1.setImageURI(uri)
                            imagesize_change(wbinding.imageview1)
                            array_uri[0] = uri
                            fileName = "1_" + time_now + "_" + random.toString()
                            img_str = time_now
                            array_img[0] = fileName
                            image_count++
                            image_count_new[0] = true
                        } else if (image_count == 1) {
                            wbinding.imageview2.setImageURI(uri)
                            imagesize_change(wbinding.imageview2)
                            array_uri[1] = uri
                            fileName = "2_" + time_now + "_" + random.toString()
                            array_img[1] = fileName
                            image_count++
                            image_count_new[1] = true
                        } else if (image_count == 2) {
                            wbinding.imageview3.setImageURI(uri)
                            imagesize_change(wbinding.imageview3)
                            array_uri[2] = uri
                            fileName = "3_" + time_now + "_" + random.toString()
                            array_img[2] = fileName
                            image_count++
                            image_count_new[2] = true
                        } else {
                            return
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu2, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setMessage("업로드중...")
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal)

        when (item?.itemId) {
            R.id.finish -> {
                if (wbinding.titleEt.text.toString() == "") {
                    Toast.makeText(this, "제목을 입력하세요", Toast.LENGTH_SHORT).show()
                    return false
                } else if (wbinding.contentEt.text.toString() == "") {
                    Toast.makeText(this, "내용을 입력하세요", Toast.LENGTH_SHORT).show()
                    return false
                } else {
                    val intent = Intent(this, BoardViewActivity::class.java)
                    progressDialog.show()
                    write(list.board_title)
                    if (image_count == 0) {
                        if (progressDialog.isShowing)
                            progressDialog.dismiss()
                        finish()
                        startActivity(intent)
                    } else if (image_count == 1) {
                        if (progressDialog.isShowing)
                            Handler(Looper.getMainLooper()).postDelayed({
                                if (progressDialog.isShowing)
                                    progressDialog.dismiss()
                                intent.putExtra("data", dataInput)
                                intent.putExtra("title", board_title)
                                finish()
                                startActivity(intent)
                            }, 2000)
                    } else if (image_count == 2) {
                        if (progressDialog.isShowing)
                            Handler(Looper.getMainLooper()).postDelayed({
                                progressDialog.dismiss()
                                intent.putExtra("data", dataInput)
                                intent.putExtra("title", board_title)
                                finish()
                                startActivity(intent)
                            }, 3000)
                    } else if (image_count == 3) {
                        if (progressDialog.isShowing)
                            Handler(Looper.getMainLooper()).postDelayed({
                                progressDialog.dismiss()
                                intent.putExtra("data", dataInput)
                                intent.putExtra("title", board_title)
                                finish()
                                startActivity(intent)
                            }, 4000)
                    }

                }
                return super.onOptionsItemSelected(item)

            }
            android.R.id.home -> {
                //뒤로가기 버튼 눌렀을 때
                intent.putExtra("data", dataInput)
                intent.putExtra("title", board_title)
                finish()
                startActivity(intent)
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }

        }
    }

    fun write(str: String) {
        val range = (1..999999)
        board_id = user.toString() + "_" + formatter.format(now) + "_" + range.random().toString()
        val database = FirebaseDatabase.getInstance().getReference()
        val location = str

        CoroutineScope(Main).launch {

            val key = list.key
            if (image_count_new[0])
                image_upload(array_img[0], array_uri[0])
            if (image_count_new[1])
                image_upload(array_img[1], array_uri[1])
            if (image_count_new[2])
                image_upload(array_img[2], array_uri[2])

            dataInput = Board_Model2(
                key,
                array_img[0],
                array_img[1],
                array_img[2],
                wbinding.titleEt.text.toString(),
                wbinding.contentEt.text.toString(),
                user.toString(),
                list.comment_count,
                list.views_count,
                time,
                time2,
                image_count,
                str,
                list.nickname
            )
            val childUpdates = hashMapOf<String, Any>(
                "board/$location/$key" to dataInput
            )
            database.updateChildren(childUpdates)
        }
    }

    fun imagesize_change(image: ImageView) {
        image.getLayoutParams().width = 200
        image.getLayoutParams().height = 200
    }

    fun image_upload(filename: String, uri: Uri) {
        var storageReference =
            FirebaseStorage.getInstance().getReference("images/$filename")
        storageReference.putFile(uri).addOnSuccessListener {
        }

    }

}


