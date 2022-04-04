package com.example.abc123

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.abc123.databinding.FragmentBoardBinding

class BoardFragment : Fragment() {
    private var mBinding : FragmentBoardBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentBoardBinding.inflate(inflater, container, false)

        mBinding = binding

        return mBinding?.root
    }
}