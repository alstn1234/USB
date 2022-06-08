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
import com.example.abc123.databinding.ActivityBoardBinding
import com.google.firebase.database.*


class BoardActivity : AppCompatActivity() {

    private lateinit var bbinding: ActivityBoardBinding
    private lateinit var dbref: DatabaseReference
    private lateinit var boardRecyclerview: RecyclerView
    private lateinit var boardArrayList: ArrayList<Board_Model2>
    private lateinit var boardArraylist2: ArrayList<Board_Model2>
    var ppage_max: Int = 0
    private val fireDatabase = FirebaseDatabase.getInstance().reference
    lateinit var mGlideRequestManager: RequestManager
    lateinit var title: String

    override fun onCreate(savedInstanceState: Bundle?) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("로딩중...")
        progressDialog.setCancelable(false)
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal)
        progressDialog.show();
        super.onCreate(savedInstanceState)
        bbinding = ActivityBoardBinding.inflate(layoutInflater)
        setContentView(bbinding.root)
        boardArraylist2 = arrayListOf<Board_Model2>()

        mGlideRequestManager = Glide.with(this)

        setSupportActionBar(bbinding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        bbinding.toolbar.title = intent.getStringExtra("name")
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)


        bbinding.fabEdit.setOnClickListener {
            val intent = Intent(this, WritingActivity::class.java)
            intent.putExtra("name", bbinding.toolbar.title)
            startActivity(intent)
        }
        val manager = LinearLayoutManager(this)
//db받아오기on
        boardRecyclerview = findViewById(R.id.board_list)
        boardRecyclerview.layoutManager = manager
        boardRecyclerview.setHasFixedSize(true)
        boardArrayList = arrayListOf<Board_Model2>()

        getBoardData()
        Handler(Looper.getMainLooper()).postDelayed({
            if (progressDialog.isShowing)
                progressDialog.dismiss()
        }, 1000)

        boardRecyclerview.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!boardRecyclerview.canScrollVertically(-1)) {
                } else if (!boardRecyclerview.canScrollVertically(1)) {
                    if (ppage_max >= 0) {
                        progressDialog.show()
                        for (i in ppage_max downTo ppage_max - 14) {
                            if (i >= 0)
                                boardArraylist2.add(boardArrayList[i])
                        }
                        ppage_max = ppage_max - 15
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

    private fun getBoardData() {
        when (intent.getStringExtra("name")) {
            "자유게시판" -> board_list_view("free")
            "친목게시판" -> board_list_view("friend")
            "홍보게시판" -> board_list_view("ad")
            "인문" -> board_list_view("human")
            "사회과학" -> board_list_view("socialscience")
            "법과" -> board_list_view("law")
            "경영" -> board_list_view("operation")
            "자연과학" -> board_list_view("naturalscience")
            "공과" -> board_list_view("lesson")
            "소프트웨어" -> board_list_view("sw")
            "생명과학" -> board_list_view("biology")
            "미술" -> board_list_view("fineart")
            "예술" -> board_list_view("art")
            "음악" -> board_list_view("music")
            "체육" -> board_list_view("athletic")
        }
    }


    fun board_list_view(str: String) {
        title = str
        dbref = FirebaseDatabase.getInstance().getReference().child("board")
            .child(str)
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    boardArrayList.clear()
                    boardArraylist2.clear()
                    for (boardSnapshot in snapshot.children) {
                        val board = boardSnapshot.getValue(Board_Model2::class.java)
                        boardArrayList.add(board!!)
                    }
                    ppage_max = boardArrayList.size - 1
                    for (i in ppage_max downTo ppage_max - 14) {
                        if (i >= 0)
                            boardArraylist2.add(boardArrayList[i])
                    }
                    fireDatabase.child("board").child(str + "_last_post").get()
                        .addOnSuccessListener {
                            val updates: MutableMap<String, Any> = HashMap()
                            val key = it.value.toString()
                            if (key == "") {
                                updates["board/${str + "_last_post"}"] = boardArrayList[0].key
                                FirebaseDatabase.getInstance().getReference()
                                    .updateChildren(updates)
                            }
                        }.addOnFailureListener {
                        }
                    ppage_max = ppage_max - 15
                    boardRecyclerview.adapter =
                        MyAdapter2(
                            boardArraylist2,
                            bbinding.toolbar.title.toString(),
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
                finish()
                startActivity(intent)

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
