package com.example.abc123.fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.abc123.*
import com.example.abc123.databinding.FragmentBoardBinding
import com.example.abc123.databinding.FragmentMypageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.NonCancellable.cancel

class MypageFragment : Fragment() {
    private var mBinding: FragmentBoardBinding? = null
    private lateinit var mybinding: FragmentMypageBinding
    private lateinit var auth: FirebaseAuth
    private val fireDatabase = FirebaseDatabase.getInstance().reference
    private lateinit var profileList: ArrayList<Profile>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mybinding = FragmentMypageBinding.inflate(inflater, container, false)
        profileList = arrayListOf<Profile>()
        val user = Firebase.auth.currentUser?.uid
        mybinding.profileRecycler.layoutManager = LinearLayoutManager(requireContext())
        fireDatabase
            .child("User").child(user.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    profileList.clear()
                    val item = snapshot.getValue(Profile::class.java)
                    profileList.add(item!!)
                    mybinding.profileRecycler.adapter = ProfileAdapter(profileList)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        val Mypage_post1 = arrayOf("프로필 변경", "학과 설정", "닉네임 변경")
        val Mypage_post2 = arrayOf("내가 쓴 글", "내 댓글", "제재 내역", "로그아웃")
        // 마이페이지 계정탭 기본 어댑터
        mybinding.Mypagepost1lview.adapter =
            ArrayAdapter(requireActivity()!!, android.R.layout.simple_list_item_1, Mypage_post1)
        // 마이페이지 활동내역탭 기본 어댑터
        mybinding.Mypagepost2lview.adapter =
            ArrayAdapter(requireActivity()!!, android.R.layout.simple_list_item_1, Mypage_post2)
        val intent_setprofile = Intent(requireActivity(), Set_Profile::class.java)
        val intent_resetprofile = Intent(requireActivity(), Reset_Profile::class.java)
        val intent_mypost = Intent(requireActivity(), MyPost::class.java)
        val intent_setmajor = Intent(requireActivity(), Set_Major::class.java)
        val intent_setname = Intent(requireActivity(), Set_Name::class.java)
        val intent_sanctions = Intent(requireActivity(), MySanctions::class.java)
        val intent_main = Intent(requireActivity(), MainActivity::class.java)
        var builder = AlertDialog.Builder(
            requireContext(),
            android.R.style.Theme_DeviceDefault_Light_Dialog_Alert
        )

        val selectProfile: Array<String> = arrayOf("프로필 변경", "기본 프로필 설정")
        mybinding.Mypagepost1lview.setOnItemClickListener { parent, view, position, id ->
            val element = parent.getItemAtPosition(position) as String
            if (element == "프로필 변경") {
                builder.setItems(selectProfile) { DialogInterface, which ->
                    when (which) {
                        0 -> startActivity(intent_setprofile)
                        1 -> startActivity(intent_resetprofile)
                    }
                }
                builder.show()
            }
            if (element == "학과 설정")
                startActivity(intent_setmajor)
            if (element == "닉네임 변경")
                startActivity(intent_setname)
        }
        mybinding.Mypagepost2lview.setOnItemClickListener { parent, view, position, id ->
            val element = parent.getItemAtPosition(position) as String
            if (element == "내가 쓴 글")
                startActivity(intent_mypost)
            if (element == "제재 내역")
                startActivity(intent_sanctions)
            if (element == "로그아웃") {
                var builder = AlertDialog.Builder(requireContext())
                builder.setTitle("로그아웃")
                    .setMessage("로그아웃 하시겠습니까?")
                    .setCancelable(false)
                    .setPositiveButton("확인",
                        DialogInterface.OnClickListener { dialog, id ->
                            startActivity(intent_main)
                            auth.signOut()
                        })
                    .setNegativeButton("취소",
                        DialogInterface.OnClickListener { dialog, id ->
                            requireContext()
                        })
                builder.show()
            }
        }

        return mybinding?.root
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }
}

