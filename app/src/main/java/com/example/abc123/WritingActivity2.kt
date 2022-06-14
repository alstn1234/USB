package com.example.abc123

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.abc123.databinding.ActivityWriting2Binding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*


class WritingActivity2 : AppCompatActivity() {

    lateinit var wbinding: ActivityWriting2Binding
    private var uid: String = ""
    var time: String = ""
    var time2: String = ""
    lateinit var board_id: String
    val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
    val now = Date()
    val range = (1..999999)
    val user = Firebase.auth.currentUser?.uid
    var nick : String = ""
    private val fireDatabase = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        wbinding = ActivityWriting2Binding.inflate(layoutInflater)

        val long_now = System.currentTimeMillis()
        val t_date = Date(long_now)
        val t_dateFormat = SimpleDateFormat("HH:mm")
        val t_dateFormat2 = SimpleDateFormat("yyyy.MM.dd")

        board_id = user.toString() + "_" + formatter.format(now) + "_" + range.random().toString()
        time2 = t_dateFormat.format(t_date)
        time = t_dateFormat2.format(t_date)

        fireDatabase.child("User").child(user.toString()).child("nickname").get()
            .addOnSuccessListener {
                nick = it.value.toString()
            }.addOnFailureListener{
            }

        setSupportActionBar(wbinding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)

        super.onCreate(savedInstanceState)
        setContentView(wbinding?.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu2, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.finish -> {
                /* if (wbinding.titleEt.text.toString() == "") {
                     Toast.makeText(this, "단어를 입력하세요", Toast.LENGTH_SHORT).show()
                     return false
                 }

                 val database = FirebaseDatabase.getInstance().getReference()
                 val dataInput = Board_Model(
                     board_id,
                     wbinding.titleEt.text.toString(),
                     user.toString(),
                     time,
                     time2
                 )
                 database.child("board_endword").push().setValue(dataInput)*/
                if (wbinding.titleEt.text.toString() == "") {
                    Toast.makeText(this, "단어를 입력하세요", Toast.LENGTH_SHORT).show()
                    return false
                }
                val database = FirebaseDatabase.getInstance().getReference()
                val key = database.child("board").child("endword").push().key
                val dataInput = Board_Model(
                    key.toString(),
                    wbinding.titleEt.text.toString(),
                    user.toString(),
                    time,
                    time2,
                    nick
                )

                val childUpdates = hashMapOf<String, Any>(
                    "board/endword/$key" to dataInput
                )
                database.updateChildren(childUpdates)

                finish()
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

}