package com.example.abc123

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.abc123.databinding.ActivityBuysellBinding
import com.google.firebase.database.*


class BuysellActivity : AppCompatActivity() {

    private lateinit var bbinding: ActivityBuysellBinding
    private lateinit var dbref: DatabaseReference
    private lateinit var boardRecyclerview: RecyclerView
    private lateinit var boardArrayList: ArrayList<Board_Model3>
    private lateinit var boardArraylist2: ArrayList<Board_Model3>
    var ppage_max: Int = 0
    var count = 0
    private val fireDatabase = FirebaseDatabase.getInstance().reference
    lateinit var mGlideRequestManager: RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("로딩중...")
        progressDialog.setCancelable(false)
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal)
        progressDialog.show();
        super.onCreate(savedInstanceState)
        bbinding = ActivityBuysellBinding.inflate(layoutInflater)
        setContentView(bbinding.root)
        boardArraylist2 = arrayListOf<Board_Model3>()

        mGlideRequestManager = Glide.with(this)

        setSupportActionBar(bbinding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        bbinding.toolbar.title = "거래게시판"
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)

//수정 1
        bbinding.fabEdit.setOnClickListener {
            val intent = Intent(this, WritingActivity3::class.java)
            startActivity(intent)
        }

        val manager = LinearLayoutManager(this)
//db받아오기on
        boardRecyclerview = findViewById(R.id.board_list)
        boardRecyclerview.layoutManager = manager
        boardRecyclerview.setHasFixedSize(true)
        boardArrayList = arrayListOf<Board_Model3>()


        board_list_view()
        Handler(Looper.getMainLooper()).postDelayed({
            if (progressDialog.isShowing)
                progressDialog.dismiss()
        }, 1000)

        boardRecyclerview.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!boardRecyclerview.canScrollVertically(-1)) {
                } else if (!boardRecyclerview.canScrollVertically(1)) {
                    count++
                    if (ppage_max >= 0) {
                        progressDialog.show()
                        for (i in ppage_max downTo ppage_max - 9) {
                            if (i >= 0)
                                boardArraylist2.add(boardArrayList[i])
                        }
                        ppage_max = ppage_max - 10
                        boardRecyclerview.adapter!!.notifyDataSetChanged()
                        boardRecyclerview.isNestedScrollingEnabled = false
                        Handler(Looper.getMainLooper()).postDelayed({
                            if (progressDialog.isShowing)
                                progressDialog.dismiss()
                        }, 1000)
                        boardRecyclerview.isNestedScrollingEnabled = true
                    }
                }
            }
        })
    }

    fun board_list_view() {
        dbref = FirebaseDatabase.getInstance().getReference().child("board")
            .child("buysell")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                count++
                if (snapshot.exists()) {
                    boardArrayList.clear()
                    boardArraylist2.clear()
                    for (boardSnapshot in snapshot.children) {
                        val board = boardSnapshot.getValue(Board_Model3::class.java)
                        boardArrayList.add(board!!)
                    }
                    ppage_max = boardArrayList.size - 1
                    for (i in ppage_max downTo ppage_max - 9) {
                        if (i >= 0)
                            boardArraylist2.add(boardArrayList[i])
                    }
                    fireDatabase.child("board").child("buysell_last_post").get()
                        .addOnSuccessListener {
                            val updates: MutableMap<String, Any> = HashMap()
                            val key = it.value.toString()
                            if (key == "") {
                                updates["board/buysell_last_post"] = boardArrayList[0].key
                                FirebaseDatabase.getInstance().getReference()
                                    .updateChildren(updates)
                            }
                        }.addOnFailureListener {
                        }
                    ppage_max = ppage_max - 10
                    boardRecyclerview.adapter =
                        MyAdapter3(
                            boardArraylist2,
                            mGlideRequestManager
                        )
                }
                if (boardArraylist2.size >= 1)
                    boardRecyclerview.adapter!!.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.search -> {
                //검색 버튼 눌렀을 때
                Toast.makeText(applicationContext, "검색 이벤트 실행", Toast.LENGTH_LONG).show()
                return super.onOptionsItemSelected(item)
            }
            R.id.refresh -> {
                val intent = getIntent()
                finish();
                startActivity(intent);

                return super.onOptionsItemSelected(item)
            }
            android.R.id.home -> {
                //뒤로가기 버튼 눌렀을 때
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        boardRecyclerview.adapter = null
        finish()
        super.onDestroy()
    }

    override fun onResume() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("로딩중...")
        progressDialog.setCancelable(false)
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal)
        progressDialog.show();
        Handler(Looper.getMainLooper()).postDelayed({
            if (progressDialog.isShowing)
                progressDialog.dismiss()
        }, 1000)
        super.onResume()
    }

}
