package com.goormthon.mang9rme.jihun.main.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.goormthon.mang9rme.R
import com.goormthon.mang9rme.databinding.ActivityMainBinding
import com.goormthon.mang9rme.jihun.main.viewmodel.MainViewModel
import retrofit2.http.Url
import java.net.URI

class MainActivity : AppCompatActivity(){
    private lateinit var binding : ActivityMainBinding
    private val viewModel : MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        initBinding()
    }

    private fun initBinding() {

    }

    override fun onStart() {
        super.onStart()
        //:TODO 스플래시에서 받아오는 로직 추가하기
    }


}