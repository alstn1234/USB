package com.example.abc123.fragments

import android.R
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.abc123.databinding.FragmentBoardBinding

class BoardFragment : Fragment() {
    private var mBinding : FragmentBoardBinding? = null
    private lateinit var bbinding : FragmentBoardBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ):
        View? {
        bbinding = FragmentBoardBinding.inflate(inflater, container, false)
        val binding = FragmentBoardBinding.inflate(inflater, container, false)
        mBinding = binding
        val item = arrayOf("내가 쓴 글", "내가 댓글 단 글", "자유게시판", "질문게시판", "거래게시판", "단대별 게시판")
        bbinding.listview.adapter = ArrayAdapter(requireActivity()!!, android.R.layout.simple_list_item_1, item)


            return bbinding?.root
        }


    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }



}