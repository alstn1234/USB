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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment

        val navController = navHostFragment.navController

        NavigationUI.setupWithNavController(mBinding.myBottomNav, navController)
    }


}