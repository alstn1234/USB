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
import com.example.abc123.databinding.WritingBinding
import com.example.test28.DataModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WritingActivity : AppCompatActivity() {

    val REQ_STORAGE = 101
    var image_count: Int = 0
    lateinit var wbinding: WritingBinding
    private var uid: String = ""
    var time: String = ""
    var time2: String = ""
    var str: String = ""
    lateinit var array_img: ArrayList<String>
    lateinit var array_uri: ArrayList<Uri>
    lateinit var fileName: String
    lateinit var board_id: String
    var img_str: String = ""
    val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
    val now = Date()
    val range = (1..999999)
    val random = range.random()
    var state = false
    val user = Firebase.auth.currentUser?.uid
    var nick : String = ""
    private val fireDatabase = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wbinding = WritingBinding.inflate(layoutInflater)
        setContentView(wbinding.root)

        array_img = arrayListOf<String>("", "", "")
        array_uri = arrayListOf<Uri>()
        str = intent.getStringExtra("name").toString()

        uid = intent.getStringExtra("uid").toString()


        fireDatabase.child("User").child(user.toString()).child("nickname").get()
            .addOnSuccessListener {
                nick = it.value.toString()
            }.addOnFailureListener{
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


        setSupportActionBar(wbinding.toolbar)
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
                            array_uri.add(uri)
                            fileName = "1_" + time_now + "_" + random.toString()
                            img_str = time_now
                            array_img[0] = fileName
                            image_count++
                        } else if (image_count == 1) {
                            wbinding.imageview2.setImageURI(uri)
                            imagesize_change(wbinding.imageview2)
                            array_uri.add(uri)
                            fileName = "2_" + time_now + "_" + random.toString()
                            array_img[1] = fileName
                            image_count++
                        } else if (image_count == 2) {
                            wbinding.imageview3.setImageURI(uri)
                            imagesize_change(wbinding.imageview3)
                            array_uri.add(uri)
                            fileName = "3_" + time_now + "_" + random.toString()
                            array_img[2] = fileName
                            image_count++
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
                    progressDialog.show()
                    when (str) {
                        "끝말잇기" -> write("endword")
                        "자유게시판" -> write("free")
                        "친목게시판" -> write("friend")
                        "거래게시판" -> write("buysell")
                        "홍보게시판" -> write("ad")
                        "인문" -> write("human")
                        "사회과학" -> write("socialscience")
                        "법과" -> write("law")
                        "경영" -> write("operation")
                        "자연과학" -> write("naturalscience")
                        "공과" -> write("lesson")
                        "소프트웨어" -> write("sw")
                        "생명과학" -> write("biology")
                        "미술" -> write("fineart")
                        "예술" -> write("art")
                        "음악" -> write("music")
                        "체육" -> write("athletic")

                    }
                    if (image_count == 0) {
                        if(progressDialog.isShowing)
                        progressDialog.dismiss()
                        finish()
                    } else if (image_count == 1) {
                        if(progressDialog.isShowing)
                        Handler(Looper.getMainLooper()).postDelayed({
                            progressDialog.dismiss()
                            finish()
                        }, 2000)
                    } else if (image_count == 2) {
                        if(progressDialog.isShowing)
                        Handler(Looper.getMainLooper()).postDelayed({
                            progressDialog.dismiss()
                            finish()
                        }, 3000)
                    } else if (image_count == 3) {
                        if(progressDialog.isShowing)
                        Handler(Looper.getMainLooper()).postDelayed({
                            progressDialog.dismiss()
                            finish()
                        }, 4000)
                    }

                }
                return super.onOptionsItemSelected(item)

            }
            android.R.id.home -> {
                //뒤로가기 버튼 눌렀을 때
                finish()
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
        val updates : MutableMap<String, Any> = HashMap()

        CoroutineScope(Main).launch {

        val key = database.child("board").child(location).push().key
            if (image_count == 0) {
                array_img.add("")
            } else if (image_count == 1) {
                uploadImage(array_uri, array_img, "1")
            } else if (image_count == 2) {
                uploadImage(array_uri, array_img, "2")
            } else {
                uploadImage(array_uri, array_img, "3")
            }
            val dataInput = Board_Model2(
                key.toString(),
                array_img[0],
                array_img[1],
                array_img[2],
                wbinding.titleEt.text.toString(),
                wbinding.contentEt.text.toString(),
                user.toString(),
                0,
                0,
                time,
                time2,
                image_count,
                str,
                nick
            )

            val childUpdates = hashMapOf<String, Any>(
                "board/$location/$key" to dataInput
            )
            database.updateChildren(childUpdates)

            updates["board/${location+"_last_post"}"] = key.toString()
            database.updateChildren(updates)
        }
    }

    fun imagesize_change(image: ImageView) {
        image.getLayoutParams().width = 200
        image.getLayoutParams().height = 200
    }

    fun uploadImage(uri: ArrayList<Uri>, array: ArrayList<String>, str: String) {
        if (str == "1") {
            image_upload(array[0], uri[0])
        } else if (str == "2") {
            image_upload(array[0], uri[0])
            image_upload(array[1], uri[1])
        } else if (str == "3") {
            image_upload(array[0], uri[0])
            image_upload(array[1], uri[1])
            image_upload(array[2], uri[2])
        }
    }

    fun image_upload(filename: String, uri: Uri) {
        var storageReference =
            FirebaseStorage.getInstance().getReference("images/$filename")
        storageReference.putFile(uri).addOnSuccessListener {
        }

    }

}


