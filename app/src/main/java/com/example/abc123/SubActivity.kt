package com.example.abc123

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.abc123.databinding.ActivityMainBinding
import com.example.abc123.databinding.FragmentBoardBinding
import com.example.abc123.fragments.BoardFragment
import java.util.zip.Inflater

class MainActivity : AppCompatActivity() {

    private lateinit var  mBinding : ActivityMainBinding
    private lateinit var binding: MainActivity
    private lateinit var bbinding: FragmentBoardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        bbinding = FragmentBoardBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setContentView(bbinding.root)

        val item = arrayOf("내가 쓴 글","댓글 단 글","자유게시판","질문게시판","거래게시판","단대별 게시판")

        bbinding.boardList.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, item)


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment

        val navController = navHostFragment.navController

        NavigationUI.setupWithNavController(mBinding.myBottomNav, navController)
    }


}