package com.example.abc123.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.abc123.Board_Model2
import com.example.abc123.ViewPagerAdapter
import com.example.abc123.databinding.FragmentHomeBinding
import com.google.firebase.database.*

class HomeFragment : Fragment() {
    private lateinit var bbinding: FragmentHomeBinding
    private lateinit var dbref: DatabaseReference
    private lateinit var array_title: ArrayList<Board_Model2>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        bbinding = FragmentHomeBinding.inflate(inflater, container, false)
        val pager = bbinding.viewpagerUni
        array_title = arrayListOf<Board_Model2>()
        pager.adapter = ViewPagerAdapter(requireActivity())
        dbref = FirebaseDatabase.getInstance().getReference().child("board").child("free")
            .child("title")

        dbref.limitToLast(1).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    array_title.clear()
                    val board = snapshot.getValue(Board_Model2::class.java)
                    array_title.add(board!!)

                    print(array_title[0].title + "asd")
                    bbinding.latelyFree.text = array_title[0].title
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        return bbinding?.root
    }


}