package com.example.abc123

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.abc123.databinding.FragmentChatBinding

class ChatFragment : Fragment() {
    private var mBinding : FragmentChatBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentChatBinding.inflate(inflater, container, false)

        mBinding = binding

        return mBinding?.root
    }
}