package com.goormthon.mang9rme.feature.base.view

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.goormthon.mang9rme.R
import com.goormthon.mang9rme.common.customview.LoadingProgressDialog

open class BaseActivity : AppCompatActivity() {

    // region Base ProgressDialog Function
    private lateinit var loadingProgressDialog: LoadingProgressDialog

    protected fun showProgress(pMsg: String = "") {
        if (!this::loadingProgressDialog.isInitialized) {
            loadingProgressDialog = LoadingProgressDialog(this, "")
        }

        loadingProgressDialog.setMessage(pMsg)
        loadingProgressDialog.show()
    }

    protected fun dismissProgress() {
        loadingProgressDialog.dismiss()
    }
    // endregion Base ProgressDialog Function

    // region Base Toast Function
    protected fun showToastMsg(pMsg: String) {
        Toast.makeText(this, pMsg, Toast.LENGTH_SHORT).show()
    }

    protected fun showErrorMsg() {
        Toast.makeText(
            this,
            resources.getText(R.string.toast_msg_error_001),
            Toast.LENGTH_SHORT
        ).show()
    }
    // endregion Base Toast Function

}