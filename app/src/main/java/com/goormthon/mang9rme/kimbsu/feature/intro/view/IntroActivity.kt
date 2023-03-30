package com.goormthon.mang9rme.kimbsu.feature.intro.view

import android.os.Bundle
import com.goormthon.mang9rme.R
import com.goormthon.mang9rme.databinding.ActivityIntroBinding
import com.goormthon.mang9rme.kimbsu.common.customview.MessageDialog
import com.goormthon.mang9rme.kimbsu.common.util.NetworkUtil
import com.goormthon.mang9rme.kimbsu.feature.base.view.BaseActivity

class IntroActivity : BaseActivity() {

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

    }

    private fun init() {
        // network connected
        if (NetworkUtil.checkNetworkEnable(this)) {

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