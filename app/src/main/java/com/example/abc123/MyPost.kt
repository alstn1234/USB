package com.example.abc123

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.abc123.databinding.ActivityMypostBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyPost : AppCompatActivity() {
    private var fireDatabase = FirebaseDatabase.getInstance()
    private lateinit var MypostList: ArrayList<Mypostmodel>
    lateinit var binding: ActivityMypostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MypostList = ArrayList<Mypostmodel>()
        binding.MypostRecycler.layoutManager = LinearLayoutManager(this)
        val dbref = fireDatabase.getReference().child("MyEX/UserExample/mypost")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                MypostList.clear()

                for (data in snapshot.children) {
                    val item = data.getValue(Mypostmodel::class.java)
                    MypostList.add(item!!)
                }
                binding.MypostRecycler.adapter = MypostAdapter(MypostList)

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}
