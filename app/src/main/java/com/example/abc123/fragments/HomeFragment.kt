package com.example.abc123.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.abc123.*
import com.example.abc123.R
import com.example.abc123.databinding.FragmentHomeBinding
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.lang.System.load

class HomeFragment : Fragment() {
    private lateinit var bbinding: FragmentHomeBinding
    private lateinit var dbref: DatabaseReference
    private lateinit var array_title: ArrayList<Board_Model2>
    private val fireDatabase = FirebaseDatabase.getInstance().reference
    private lateinit var item_free : Board_Model2
    private lateinit var item_friend : Board_Model2
    private lateinit var item_ad : Board_Model2
    private lateinit var item_buysell : Board_Model3
    private var loading_free = false
    private var loading_friend = false
    private var loading_ad = false
    private var loading_buysell = false

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

        fireDatabase.child("board").child("free_last_post").get()
            .addOnSuccessListener {
                val key = it.value.toString()
                fireDatabase.child("board").child("free").child(key)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            item_free = snapshot.getValue(Board_Model2::class.java)!!
                            bbinding.latelyFree.text = item_free.title
                            loading_free = true
                        }
                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
            }.addOnFailureListener{
                bbinding.latelyFree.text = "일시적으로 글을 불러올 수 없습니다"
            }

        bbinding.freeLayout.setOnClickListener{
            val intent = Intent(requireActivity(), BoardViewActivity::class.java)
            if(loading_free) {
                intent.putExtra("data", item_free)
                intent.putExtra("title", "자유게시판")
                startActivity(intent)
            } else{
                Toast.makeText(requireActivity(),"로딩중입니다",Toast.LENGTH_SHORT).show()
            }
        }



        fireDatabase.child("board").child("friend_last_post").get()
            .addOnSuccessListener {
                val key = it.value.toString()
                fireDatabase.child("board").child("friend").child(key)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            item_friend = snapshot.getValue(Board_Model2::class.java)!!
                            bbinding.latelyFriend.text = item_friend.title
                            loading_friend = true
                        }
                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
            }.addOnFailureListener{
                bbinding.latelyFriend.text = "일시적으로 글을 불러올 수 없습니다"
            }

        bbinding.friendLayout.setOnClickListener{
            val intent = Intent(requireActivity(), BoardViewActivity::class.java)
            if(loading_friend) {
                intent.putExtra("data", item_friend)
                intent.putExtra("title", "친목게시판")
                startActivity(intent)
            } else{
                Toast.makeText(requireActivity(),"로딩중입니다",Toast.LENGTH_SHORT).show()
            }
        }

        fireDatabase.child("board").child("ad_last_post").get()
            .addOnSuccessListener {
                val key = it.value.toString()
                fireDatabase.child("board").child("ad").child(key)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            item_ad = snapshot.getValue(Board_Model2::class.java)!!
                            bbinding.latelyAd.text = item_ad.title
                            loading_ad = true
                        }
                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
            }.addOnFailureListener{
                bbinding.latelyAd.text = "일시적으로 글을 불러올 수 없습니다"
            }

        bbinding.adLayout.setOnClickListener{
            val intent = Intent(requireActivity(), BoardViewActivity::class.java)
            if(loading_ad) {
                intent.putExtra("data", item_ad)
                intent.putExtra("title", "홍보게시판")
                startActivity(intent)
            } else{
                Toast.makeText(requireActivity(),"로딩중입니다",Toast.LENGTH_SHORT).show()
            }
        }

        fireDatabase.child("board").child("buysell_last_post").get()
            .addOnSuccessListener {
                val key = it.value.toString()
                fireDatabase.child("board").child("buysell").child(key)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            item_buysell = snapshot.getValue(Board_Model3::class.java)!!
                            bbinding.buyTitle1.text = item_buysell.title
                            bbinding.buyPrice1.text = "가격 : ${item_buysell.price} 원"
                            val storageReference = FirebaseStorage.getInstance().getReference("images/${item_buysell.img1}")

                            storageReference.downloadUrl.addOnSuccessListener {
                                val photoUri : Uri = it
                                Glide.with(requireActivity()).load(photoUri.toString())
                                    .placeholder(R.drawable.ic_outline_image_24).into(bbinding.buysellImg1)
                            }
                            loading_buysell = true
                        }
                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
            }.addOnFailureListener{
            }

        bbinding.buysellLayout.setOnClickListener{
            val intent = Intent(requireActivity(), buysellviewActivity::class.java)
            if(loading_buysell) {
                intent.putExtra("data", item_buysell)
                startActivity(intent)
            } else{
                Toast.makeText(requireActivity(),"로딩중입니다",Toast.LENGTH_SHORT).show()
            }
        }

        return bbinding?.root
    }


}