package com.example.abc123.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.abc123.MyBoard
import com.example.abc123.R
import com.example.abc123.Set_Name
import com.example.abc123.Set_School
import com.example.abc123.databinding.FragmentBoardBinding
import com.example.abc123.databinding.FragmentMypageBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class MypageFragment : Fragment() {
    private val fireDatabase = FirebaseDatabase.getInstance().getReference()
    private val fireDataStore = FirebaseFirestore.getInstance()
    private var mBinding : FragmentBoardBinding? = null
    private lateinit var mybinding : FragmentMypageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mybinding = FragmentMypageBinding.inflate(inflater, container, false)

        val Mypage_post1 = arrayOf("프로필 변경","학교/학과 설정","닉네임 변경")
        val Mypage_post2 = arrayOf("내가 쓴 글","내 댓글","제재 내역")
        // 마이페이지 계정탭 기본 어댑터
        mybinding.Mypagepost1lview.adapter = ArrayAdapter(requireActivity()!!, android.R.layout.simple_list_item_1, Mypage_post1)
        // 마이페이지 활동내역탭 기본 어댑터
        mybinding.Mypagepost2lview.adapter = ArrayAdapter(requireActivity()!!, android.R.layout.simple_list_item_1, Mypage_post2)
        val intent_myboard = Intent(requireActivity(), MyBoard::class.java)
        val intent_setname = Intent(requireActivity(), Set_Name::class.java)
        val intent_setschool = Intent(requireActivity(), Set_School::class.java)
        mybinding.Mypagepost1lview.setOnItemClickListener{parent, view, position, id->
            val element = parent.getItemAtPosition(position) as String
            if(element == "학교/학과 설정")
                startActivity(intent_setschool)
            if(element == "닉네임 변경")
                startActivity(intent_setname)
        }
        mybinding.Mypagepost2lview.setOnItemClickListener{parent, view, position, id->
            val element = parent.getItemAtPosition(position) as String
            if(element == "내가 쓴 글")
                startActivity(intent_myboard)
        }
        return mybinding?.root
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }
}