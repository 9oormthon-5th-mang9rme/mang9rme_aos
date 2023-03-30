package com.goormthon.mang9rme.kimbsu.feature.intro.view

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.goormthon.mang9rme.R
import com.goormthon.mang9rme.common.data.StoneData
import com.goormthon.mang9rme.databinding.ActivityIntroBinding
import com.goormthon.mang9rme.jihun.presentation.ui.main.view.MainActivity
import com.goormthon.mang9rme.kimbsu.common.customview.MessageDialog
import com.goormthon.mang9rme.kimbsu.common.util.DLog
import com.goormthon.mang9rme.kimbsu.common.util.NetworkUtil
import com.goormthon.mang9rme.kimbsu.feature.base.view.BaseActivity
import com.goormthon.mang9rme.kimbsu.feature.base.viewmodel.BaseViewModel
import com.goormthon.mang9rme.kimbsu.feature.intro.viewmodel.IntroViewModel

class IntroActivity : BaseActivity() {

    private val model: IntroViewModel by lazy {
        ViewModelProvider(
            this,
            BaseViewModel.Factory(application)
        )[IntroViewModel::class.java]
    }
    private val binding: ActivityIntroBinding by lazy {
        ActivityIntroBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setObserver()
        init()
    }

    private fun setObserver() {
        model.progressFlag.observe(this, Observer { flag ->

        })

        model.exceptionData.observe(this, Observer { exception ->
            exception.message?.let { msg ->
                model.setProgressFlag(false)
                showErrorMsg()
                DLog.e(TAG, msg, exception)
            }
        })

        model.apiFailMsg.observe(this, Observer { msg ->
            model.setProgressFlag(false)
            showToastMsg(msg)
        })

        model.stoneDataList.observe(this, Observer { stoneDataList ->
            val valueAnimator = ValueAnimator
                .ofFloat(0f, 1f)
                .setDuration(500)
            valueAnimator.addUpdateListener { animation ->
                val animatedValue = animation.animatedValue as Float
                binding.tvIntroStart.alpha = animatedValue
            }
            AnimatorSet().let { animatorSet ->
                animatorSet.interpolator = AccelerateDecelerateInterpolator()
                animatorSet.play(valueAnimator)
                animatorSet.start()
            }
            
            val jumpAnimator = ValueAnimator
                .ofFloat(0f, -50f, 0f)
                .setDuration(500)
            jumpAnimator.addUpdateListener { animation ->
                val animatedValue = animation.animatedValue as Float
                binding.ivIntroStoneFriends.translationY = animatedValue
            }
            AnimatorSet().let { animatorSet ->
                animatorSet.interpolator = AccelerateDecelerateInterpolator()
                animatorSet.play(jumpAnimator)
                animatorSet.start()
            }
        })
    }

    private fun init() {
        binding.tvIntroStart.setOnClickListener { view ->
            val mainIntent = Intent(this, MainActivity::class.java).apply {
                putExtra("stone_data_list", model.stoneDataList.value ?: arrayListOf<StoneData>())
            }
            startActivity(mainIntent)
            finish()
        }

        // network connected
        if (NetworkUtil.checkNetworkEnable(this)) {
            model.requestStoneDataList()
        }
        // network not connected
        else {
            showNetworkDialog()
        }
    }

    private fun showNetworkDialog() {
        for (fragment in supportFragmentManager.fragments) {
            if (fragment is MessageDialog) {
                return
            }
        }

        MessageDialog(resources.getString(R.string.toast_msg_error_002)) { finish() }
            .show(supportFragmentManager, MessageDialog.TAG)
    }

    companion object {
        private const val TAG: String = "IntroActivity"
    }

}