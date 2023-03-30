package com.goormthon.mang9rme.kimbsu.feature.enroll.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.goormthon.mang9rme.R
import com.goormthon.mang9rme.databinding.ActivityEnrollBinding
import com.goormthon.mang9rme.kimbsu.common.util.DLog
import com.goormthon.mang9rme.kimbsu.data.SelectPhotoStatus
import com.goormthon.mang9rme.kimbsu.feature.base.view.BaseActivity
import com.goormthon.mang9rme.kimbsu.feature.base.viewmodel.BaseViewModel
import com.goormthon.mang9rme.kimbsu.feature.enroll.viewmodel.EnrollViewModel


class EnrollActivity : BaseActivity() {

    private val model: EnrollViewModel by lazy {
        ViewModelProvider(
            this,
            BaseViewModel.Factory(application)
        )[EnrollViewModel::class.java]
    }
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
        model.progressFlag.observe(this, Observer { flag ->
            if (flag) {
                showProgress("")
            } else {
                dismissProgress()
            }
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

        model.selectPhotoStatus.observe(this, Observer { selectPhotoStatus ->
            DLog.d("${TAG}_observe", "selectPhotoStatus=$selectPhotoStatus")
            when (selectPhotoStatus) {
                SelectPhotoStatus.NONE -> {

                }
                SelectPhotoStatus.CAMERA -> {
                    val cameraPermission = checkCameraPermission()
                    val storagePermission = checkStoragePermission()
                    val mediaLocationPermission = checkMediaLocationPermission()
                    DLog.d(
                        "${TAG}_observe",
                        "cameraPermission=$cameraPermission, storagePermission=$storagePermission, mediaLocationPermission=$mediaLocationPermission"
                    )
                    if (cameraPermission && storagePermission && mediaLocationPermission) {
                        dispatchCameraIntent()
                    }
                }
                SelectPhotoStatus.GALLERY -> {
                    val storagePermission = checkStoragePermission()
                    val mediaLocationPermission = checkMediaLocationPermission()
                    DLog.d(
                        "${TAG}_observe",
                        "storagePermission=$storagePermission, mediaLocationPermission=$mediaLocationPermission"
                    )
                    if (storagePermission && mediaLocationPermission) {
                        dispatchGalleryIntent()
                    }
                }
                SelectPhotoStatus.SUCCESS -> {
                    model.uploadImageData.value?.let { uploadImageData ->
                        setFragment(EnrollStepBFragment())
                    }
                }
                else -> {

                }
            }
        })
    }

    private fun init() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            setFragment(EnrollStepAFragment())
        }
    }

    fun setFragment(fragment: Fragment) {
        when (fragment) {
            is EnrollStepAFragment -> {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.addToBackStack(null)
                    .add(R.id.fl_enroll, fragment, EnrollStepAFragment.TAG)
                    .commitAllowingStateLoss()
            }
            is EnrollStepBFragment -> {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.setCustomAnimations(
                    R.anim.enter_from_right_200,
                    0,
                    0,
                    0
                ).addToBackStack(null)
                    .add(R.id.fl_enroll, fragment, EnrollStepBFragment.TAG)
                    .commitAllowingStateLoss()
            }
            is EnrollStepCFragment -> {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.setCustomAnimations(
                    R.anim.enter_from_right_200,
                    0,
                    0,
                    0
                ).addToBackStack(null)
                    .add(R.id.fl_enroll, fragment, EnrollStepCFragment.TAG)
                    .commitAllowingStateLoss()
            }
        }
    }

    fun popNaviStack() {
        val topFragment = getTopFragment()

        when (topFragment) {
            is EnrollStepBFragment,
            is EnrollStepCFragment -> {
                supportFragmentManager.beginTransaction()
                    .remove(topFragment)
                    .commitAllowingStateLoss()
                supportFragmentManager.popBackStackImmediate()
            }
        }
    }

    private fun getTopFragment(): Fragment? {
        return if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.findFragmentById(R.id.fl_enroll)
        } else {
            null
        }
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
        val permissionResult = checkSelfPermission(Manifest.permission.CAMERA)

        return if (permissionResult == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                PERMISSION_CAMERA
            )
            false
        }
    }

    /**
     * 저장공간 권한이 있는지 여부를 확인하는 함수
     *
     * 만약 저장공간 권한이 없다면, 사용자에게 저장공간 권한을 요청함
     *
     * @return 저장공간 권한이 있다면 true, 없다면 false
     */
    private fun checkStoragePermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionReadResult =
                checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES)

            return if (permissionReadResult == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                    PERMISSION_READ_MEDIA
                )
                false
            }
        } else {
            val permissionReadResult =
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            val permissionWriteResult =
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)

            return if (permissionReadResult == PackageManager.PERMISSION_GRANTED
                && permissionWriteResult == PackageManager.PERMISSION_GRANTED
            ) {
                true
            } else {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    PERMISSION_STORAGE
                )
                false
            }
        }
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
                    when (model.selectPhotoStatus.value) {
                        SelectPhotoStatus.CAMERA -> {
                            if (checkStoragePermission()) {
                                dispatchCameraIntent()
                            }
                        }
                        else -> {
                            DLog.e(
                                "${TAG}_onRequestPermissionResult",
                                "Inappropriate SelectPhotoStatus! ${model.selectPhotoStatus.value}"
                            )
                        }
                    }
                }
            }
            PERMISSION_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                    when (model.selectPhotoStatus.value) {
                        SelectPhotoStatus.CAMERA -> {
                            if (checkCameraPermission()) {
                                dispatchCameraIntent()
                            }
                        }
                        SelectPhotoStatus.GALLERY -> {
                            dispatchGalleryIntent()
                        }
                        else -> {
                            DLog.e(
                                "${TAG}_onRequestPermissionResult",
                                "Inappropriate SelectPhotoStatus! ${model.selectPhotoStatus.value}"
                            )
                        }
                    }
                }
            }
            PERMISSION_MEDIA_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
            }
            PERMISSION_READ_MEDIA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when (model.selectPhotoStatus.value) {
                        SelectPhotoStatus.CAMERA -> {
                            if (checkCameraPermission()) {
                                dispatchCameraIntent()
                            }
                        }
                        SelectPhotoStatus.GALLERY -> {

                        }
                        else -> {
                            DLog.e(
                                "${TAG}_onRequestPermissionResult",
                                "Inappropriate SelectPhotoStatus! ${model.selectPhotoStatus.value}"
                            )
                        }
                    }
                }
            }
        }
    }
    // endregion 권한 요청 관리

    // region 사진 가져오는 함수
    private fun dispatchCameraIntent() {
        DLog.d("${TAG}_dispatchCameraIntent", "dispatchCameraIntent is called")
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val tempFileForCamera = model.requestTempFileForCamera()
        if (tempFileForCamera != null) {
            val photoUri = FileProvider.getUriForFile(
                this,
                "com.goormthon.mang9rme.provider",
                tempFileForCamera
            )

            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)

            DLog.d("${TAG}_dispatchCameraIntent", "photoUri=$photoUri")
        } else {
            model.setSelectPhotoStatus(SelectPhotoStatus.NONE)
        }

        cameraIntentForResult.launch(cameraIntent)
    }

    private fun dispatchGalleryIntent() {
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        galleryIntentForResult.launch(galleryIntent)
    }
    // endregion 사진 가져오는 함수

    override fun onBackPressed() {
        when (val topFragment = getTopFragment()) {
            is EnrollStepBFragment,
            is EnrollStepCFragment -> {
                popNaviStack()
            }
            else -> {
                finish()
            }
        }
    }


    /**
     * 카메라 앱에서 촬영한 데이터를 처리함
     */
    private val cameraIntentForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    model.setProgressFlag(true)
                    model.tempFileForCamera.value?.let { tempFileForCamera ->
                        MediaScannerConnection.scanFile(
                            applicationContext,
                            arrayOf(tempFileForCamera.absolutePath),
                            arrayOf("image/*"),
                            null
                        )
                    }
                    model.requestImagePostProcessFromCamera()
                }
                else -> {
                    model.requestImagePostProcessFromCamera()
                    model.setSelectPhotoStatus(SelectPhotoStatus.NONE)
                }
            }
        }

    private val galleryIntentForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    model.setProgressFlag(true)
                    val data = result.data
                    val selectedImageUri: Uri? = data?.data
                    DLog.d(
                        "${TAG}_galleryIntentForResult",
                        "selectedImageUri=$selectedImageUri"
                    )
                    if (selectedImageUri != null) {
                        model.requestImagePostProcessFromGallery(selectedImageUri)
                    } else {
                        model.setSelectPhotoStatus(SelectPhotoStatus.NONE)
                    }
                }
                else -> {
                    model.requestImagePostProcessFromCamera()
                    model.setSelectPhotoStatus(SelectPhotoStatus.NONE)
                }
            }
        }

    companion object {
        private const val TAG: String = "EnrollActivity"

        private const val PERMISSION_CAMERA: Int = 100
        private const val PERMISSION_STORAGE: Int = 101
        private const val PERMISSION_MEDIA_LOCATION: Int = 102
        private const val PERMISSION_READ_MEDIA: Int = 103
    }

}