package com.goormthon.mang9rme.kimbsu.feature.enroll.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import com.goormthon.mang9rme.kimbsu.common.util.DLog
import com.goormthon.mang9rme.databinding.ActivityEnrollBinding
import com.goormthon.mang9rme.kimbsu.feature.base.view.BaseActivity

class EnrollActivity : BaseActivity() {

    private val binding: ActivityEnrollBinding by lazy {
        ActivityEnrollBinding.inflate(layoutInflater)
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

    }

    // region 권한 요청 관리
    /**
     * 카메라 권한이 있는지 여부를 확인하는 함수
     *
     * 만약 카메라 권한이 없다면, 사용자에게 카메라 권한을 요청함
     *
     * @return 카메라 권한이 있다면 true, 없다면 false
     */
    private fun checkCameraPermission(): Boolean {
        // Android 6.0 Marshmallow 부터는 카메라 권한이 있어야 함
        val permissionResult = checkSelfPermission(Manifest.permission.CAMERA)

        if (permissionResult == PackageManager.PERMISSION_GRANTED) {
            return true
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                PERMISSION_CAMERA
            )
            return false
        }

        return true
    }

    /**
     * 저장공간 권한이 있는지 여부를 확인하는 함수
     *
     * 만약 저장공간 권한이 없다면, 사용자에게 저장공간 권한을 요청함
     *
     * @return 저장공간 권한이 있다면 true, 없다면 false
     */
    private fun checkStoragePermission(): Boolean {
        // Android 6.0 Marshmallow 부터는 저장공간 권한이 있어야 함
        val permissionReadResult =
            checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
        val permissionWriteResult =
            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permissionReadResult == PackageManager.PERMISSION_GRANTED
            && permissionWriteResult == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                PERMISSION_STORAGE
            )
            return false
        }

        return true
    }

    /**
     * 미디어 파일의 위치 정보 권한이 있는지 여부를 확인하는 함수
     *
     * 만약, 미디어 파일의 위치 정보 권한이 없다면, 사용자에게 위치 정보 권한을 요청함
     *
     * @return 미디어 파일의 위치 정보 권한이 있다면 true, 없다면 false
     */
    private fun checkMediaLocationPermission(): Boolean {
        // Android 10(Q) 부터는 미디어 파일의 위치 정보 권한을 요청해야 함
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val permissionMediaLocationResult =
                checkSelfPermission(Manifest.permission.ACCESS_MEDIA_LOCATION)
            DLog.d(TAG, "MEDIA_LOCATION_PERMISSION, $permissionMediaLocationResult")

            if (permissionMediaLocationResult == PackageManager.PERMISSION_GRANTED) {
                return true
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_MEDIA_LOCATION),
                    PERMISSION_MEDIA_LOCATION
                )
            }
        }

        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
            }
            PERMISSION_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                }
            }
            PERMISSION_MEDIA_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
            }
        }
    }
    // endregion 권한 요청 관리

    companion object {
        private const val TAG: String = "EnrollActivity"

        private const val PERMISSION_CAMERA: Int = 100
        private const val PERMISSION_STORAGE: Int = 101
        private const val PERMISSION_MEDIA_LOCATION: Int = 102
    }

}