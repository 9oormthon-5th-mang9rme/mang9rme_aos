package com.goormthon.mang9rme.feature.main.view

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.goormthon.mang9rme.R
import com.goormthon.mang9rme.databinding.ActivityMainBinding
import com.goormthon.mang9rme.feature.base.view.BaseActivity
import com.goormthon.mang9rme.feature.base.viewmodel.BaseViewModel
import com.goormthon.mang9rme.feature.main.viewmodel.MainViewModel

class MainActivity : BaseActivity() {

    private val model: MainViewModel by lazy {
        ViewModelProvider(
            this,
            BaseViewModel.Factory(application)
        )[MainViewModel::class.java]
    }
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    /**
     * 가장 최근에 뒤로가기 버튼을 눌렀던 시각
     */
    private var backPressedTime: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setObserver()
        init()
    }

    private fun setObserver() {
        model.progressFlag.observe(this, Observer { progressFlag ->
            if (progressFlag) {
                showProgress("")
            } else {
                dismissProgress()
            }
        })

        model.apiFailMsg.observe(this, Observer { failMsg ->
            if (failMsg.isNotEmpty()) {
                model.setProgressFlag(false)
                showToastMsg(failMsg)
            }
        })

        model.exceptionData.observe(this, Observer { exception ->
            model.setProgressFlag(false)
            showErrorMsg()
        })
    }

    private fun init() {
        model.setProgressFlag(true)
    }

    override fun onBackPressed() {
        val tempTime: Long = System.currentTimeMillis()
        val intervalTime = tempTime - backPressedTime

        if (0 <= intervalTime && intervalTime <= FINISH_INTERVAL_TIME) {
            super.onBackPressed()
        } else {
            backPressedTime = tempTime
            val msg: String = getString(R.string.toast_msg_back_pressed)
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val TAG: String = "MainActivity"

        private const val FINISH_INTERVAL_TIME: Long = 2000
    }

}