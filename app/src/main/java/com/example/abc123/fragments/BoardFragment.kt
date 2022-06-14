package com.example.abc123.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.abc123.BoardActivity
import com.example.abc123.BuysellActivity
import com.example.abc123.EndwordActivity
import com.example.abc123.databinding.FragmentBoardBinding

class BoardFragment : Fragment() {
    private var mBinding: FragmentBoardBinding? = null
    private lateinit var Fbbinding: FragmentBoardBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ):
            View? {
        Fbbinding = FragmentBoardBinding.inflate(inflater, container, false)
        val item = arrayOf("끝말잇기", "자유게시판", "친목게시판", "거래게시판", "홍보게시판")
        val item2 =
            arrayOf("인문", "사회과학", "법과", "경영", "자연과학", "공과", "소프트웨어", "생명과학", "미술", "예술", "음악", "체육")

        Fbbinding.listview.adapter =
            ArrayAdapter(requireActivity()!!, android.R.layout.simple_list_item_1, item)
        Fbbinding.gridview.adapter =
            ArrayAdapter(requireActivity()!!, android.R.layout.simple_list_item_1, item2)

        val intent = Intent(requireActivity(), EndwordActivity::class.java)
        val intent2 = Intent(requireActivity(), BoardActivity::class.java)

        Fbbinding.gridview.setOnItemClickListener { parent, view, position, id ->
            val element = parent.getItemAtPosition(position) as String
            intent2.putExtra("name", element)
            startActivity(intent2)
        }

        Fbbinding.listview.setOnItemClickListener { parent, view, position, id ->
            val element = parent.getItemAtPosition(position) as String
            if (element == "끝말잇기") {
                startActivity(intent)
            } else if (element == "거래게시판") {
                startActivity(Intent(requireActivity(), BuysellActivity::class.java))
            } else {
                intent2.putExtra("name", element)
                startActivity(intent2)
            }
        }
        return Fbbinding?.root
    }


    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }

}