package com.example.abc123

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.abc123.databinding.ActivityMySanctionsBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class MySanctions : AppCompatActivity() {
    private var fireDatabase = FirebaseDatabase.getInstance()
    private lateinit var SanctionsList: ArrayList<Sanctionsmodel>
    lateinit var binding: ActivityMySanctionsBinding
    val user = Firebase.auth.currentUser?.uid.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMySanctionsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        SanctionsList = ArrayList<Sanctionsmodel>()
        binding.SanctionsRecycler.layoutManager = LinearLayoutManager(this)
        val dbref = fireDatabase.getReference().child("User/$user/sanctions")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                SanctionsList.clear()

                for (data in snapshot.children) {
                    val item = data.getValue(Sanctionsmodel::class.java)
                    SanctionsList.add(item!!)
                }
                binding.SanctionsRecycler.adapter = SanctionsAdapter(SanctionsList)

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}