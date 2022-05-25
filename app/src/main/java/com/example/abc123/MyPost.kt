package com.example.abc123

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_mypost.*

class MyPost : AppCompatActivity() {
    private var fireDatabase = FirebaseDatabase.getInstance()
    private lateinit var MypostList: ArrayList<Mypostmodel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypost)

        MypostList = ArrayList<Mypostmodel>()
        MypostRecycler.layoutManager = LinearLayoutManager(this)
        val dbref = fireDatabase.getReference().child("MyEX/UserExample/mypost")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                MypostList.clear()

                for (data in snapshot.children) {
                    val item = data.getValue(Mypostmodel::class.java)
                    MypostList.add(item!!)
                }
                MypostRecycler.adapter = MypostAdapter(MypostList)

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}
