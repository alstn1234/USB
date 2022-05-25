package com.example.abc123.fragments

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.abc123.*
import com.example.abc123.R
import com.example.abc123.databinding.FragmentBoardBinding
import com.example.abc123.databinding.FragmentMypageBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_mypage.*
import kotlinx.coroutines.NonDisposableHandle.parent

class MypageFragment : Fragment() {
    private var mBinding: FragmentBoardBinding? = null
    private lateinit var mybinding: FragmentMypageBinding
    private val fireDatabase = FirebaseDatabase.getInstance()
    private lateinit var profileList: ArrayList<Profile>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mybinding = FragmentMypageBinding.inflate(inflater, container, false)
        profileList = arrayListOf()
        val dbref = fireDatabase.getReference().child("MyEX")
        mybinding.profileRecycler.layoutManager = LinearLayoutManager(requireContext())
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                profileList.clear()
                for (data in snapshot.children) {
                    val item = data.getValue(Profile::class.java)
                    profileList.add(item!!)
                }
                mybinding.profileRecycler.adapter =
                    ProfileAdapter(profileList)

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        val Mypage_post1 = arrayOf("프로필 변경", "학과 설정", "닉네임 변경")
        val Mypage_post2 = arrayOf("내가 쓴 글", "내 댓글", "제재 내역")
        // 마이페이지 계정탭 기본 어댑터
        mybinding.Mypagepost1lview.adapter =
            ArrayAdapter(requireActivity()!!, android.R.layout.simple_list_item_1, Mypage_post1)
        // 마이페이지 활동내역탭 기본 어댑터
        mybinding.Mypagepost2lview.adapter =
            ArrayAdapter(requireActivity()!!, android.R.layout.simple_list_item_1, Mypage_post2)
        val intent_setprofile = Intent(requireActivity(), Set_Profile::class.java)
        val intent_resetprofile = Intent(requireActivity(), Reset_Profile::class.java)
        val intent_myboard = Intent(requireActivity(), MyBoard::class.java)
        val intent_setmajor = Intent(requireActivity(), Set_Major::class.java)
        val intent_setname = Intent(requireActivity(), Set_Name::class.java)
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
                startActivity(intent_myboard)
        }
        return mybinding?.root
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }
}

