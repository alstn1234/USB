package com.example.abc123.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.abc123.databinding.FragmentBoardBinding
import com.example.abc123.databinding.FragmentMypageBinding

class MypageFragment : Fragment() {
    private var mBinding : FragmentBoardBinding? = null
    private lateinit var mybinding : FragmentMypageBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mybinding = FragmentMypageBinding.inflate(inflater, container, false)
        val Mypage_post1 = arrayOf("프로필 변경","학교/학과 설정","닉네임 변경")
        val Mypage_post2 = arrayOf("내가 쓴 글","제제 내역","고민수")
        mybinding.Mypagepost1lview.adapter = ArrayAdapter(requireActivity()!!, android.R.layout.simple_list_item_1, Mypage_post1)
        mybinding.Mypagepost2lview.adapter = ArrayAdapter(requireActivity()!!, android.R.layout.simple_list_item_1, Mypage_post2)
        return mybinding?.root
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }
}