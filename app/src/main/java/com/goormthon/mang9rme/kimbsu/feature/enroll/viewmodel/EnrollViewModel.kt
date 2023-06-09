package com.goormthon.mang9rme.kimbsu.feature.enroll.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.goormthon.mang9rme.kimbsu.common.util.DLog
import com.goormthon.mang9rme.kimbsu.data.SelectPhotoStatus
import com.goormthon.mang9rme.kimbsu.data.network.NetworkResult
import com.goormthon.mang9rme.kimbsu.feature.base.viewmodel.BaseViewModel
import com.goormthon.mang9rme.kimbsu.feature.enroll.data.UploadImageData
import com.goormthon.mang9rme.kimbsu.feature.enroll.repository.EnrollImageRepository
import com.goormthon.mang9rme.kimbsu.feature.enroll.repository.EnrollNetworkRepository
import kotlinx.coroutines.launch
import java.io.File
import kotlin.math.min
import kotlin.math.sqrt

class EnrollViewModel(
    private val networkRepository: EnrollNetworkRepository,
    private val imageRepository: EnrollImageRepository
) : BaseViewModel() {

    private val _selectPhotoStatus: MutableLiveData<SelectPhotoStatus> =
        MutableLiveData(SelectPhotoStatus.NONE)
    val selectPhotoStatus: LiveData<SelectPhotoStatus> get() = _selectPhotoStatus

    private val _tempFileForCamera: MutableLiveData<File> = MutableLiveData()
    val tempFileForCamera: LiveData<File> get() = _tempFileForCamera

    private val _uploadImageData: MutableLiveData<UploadImageData?> = MutableLiveData(null)
    val uploadImageData: LiveData<UploadImageData?> get() = _uploadImageData

    private val _address: MutableLiveData<String> = MutableLiveData()
    val address: LiveData<String> get() = _address

    /**
     * 구멍 개수 질문
     */
    private val _holeQuestionAnswer: MutableLiveData<Int> = MutableLiveData(-1)
    val holeQuestionAnswer: LiveData<Int> get() = _holeQuestionAnswer

    /**
     * 점 개수 질문
     */
    private val _dotQuestionAnswer: MutableLiveData<Int> = MutableLiveData(-1)
    val dotQuestionAnswer: LiveData<Int> get() = _dotQuestionAnswer

    /**
     * 색상 질문
     */
    private val _colorQuestionAnswer: MutableLiveData<Int> = MutableLiveData(-1)
    val colorQuestionAnswer: LiveData<Int> get() = _colorQuestionAnswer

    /**
     * 모든 질문을 체크했는지 여부
     */
    val allQuestionCheck: Boolean get() = holeQuestionAnswer.value != -1 && dotQuestionAnswer.value != -1 && colorQuestionAnswer.value != -1

    fun requestCoordToAddress(pLat: Float?, pLng: Float?) {
        viewModelScope.launch {
            if (pLat != null && pLng != null) {
                val lat = pLat.toString()
                val lng = pLng.toString()

                when (val result = networkRepository.makeCoordToAddressRequest(lat, lng)) {
                    is NetworkResult.Success<String> -> {
                        val data = result.data
                        DLog.d("${TAG}_requestCoordToAddress", "address=$data")
                        _address.value = data
                    }
                    is NetworkResult.Fail -> {
                        val failMsg = result.msg
                        DLog.e("${TAG}_requestCoordToAddress", failMsg)
                        setApiFailMsg(failMsg)
                    }
                    is NetworkResult.Error -> {
                        val exception = result.exception
                        DLog.e(
                            "${TAG}_requestCoordToAddress",
                            "exception=${exception.message}",
                            exception
                        )
                        setExceptionData(exception)
                    }
                }
            } else {
                _address.value = "제주 어딘가"
            }
        }
    }

    fun requestStoneDataUpload() {
        viewModelScope.launch {
            setProgressFlag(true)
            uploadImageData.value?.let { uploadImageData ->
                networkRepository.makeEnrollStoneRequest(uploadImageData, address.value ?: "")
            }
        }
    }

    fun setSelectPhotoStatus(pStatus: SelectPhotoStatus) {
        if (selectPhotoStatus.value != pStatus)
            _selectPhotoStatus.value = pStatus
    }

    /**
     * 카메라 앱에서 찍은 사진을 저장할 임시 파일을 리턴하는 함수
     *
     * @return 파일 생성에 실패한 경우, null을 리턴함
     */
    fun requestTempFileForCamera(): File? {
        when (selectPhotoStatus.value) {
            SelectPhotoStatus.CAMERA -> {
                DLog.d("${TAG}_requestTempFileForCamera", "SelectPhotoStatus is CAMERA")
                _tempFileForCamera.value = imageRepository.makeTempFileForCamera()
            }
            else -> {
                DLog.e(
                    "${TAG}_requestTempFileForCamera",
                    "Inappropriate PhotoStatus, ${selectPhotoStatus.value}"
                )
            }
        }
        DLog.d("${TAG}_requestTempFileForCamera", "tempFileForCamera=${tempFileForCamera.value}")

        return tempFileForCamera.value
    }

    /**
     * 카메라 앱에서 돌아온 후에 진행되는 후처리 작업
     *
     * 1. 사진이 저장된 경우, 캐시에 로드한 후 Bitmap Resize 수행
     * 2. 사진이 저장되지 않은 경우, 임시 파일을 삭제함
     */
    fun requestImagePostProcessFromCamera() {
        viewModelScope.launch {
            tempFileForCamera.value?.let { file ->
                // 비어있는 파일인 경우, 삭제해야 함
                if (file.length() == 0L) {
                    imageRepository.requestDeleteFile(file.absolutePath)
                }
                // 비어있지 않은 경우, 이를 캐시에 로드한 뒤에 비트맵 리사이즈 수행
                else {
                    when (selectPhotoStatus.value) {
                        SelectPhotoStatus.CAMERA -> {
                            val tmpData = imageRepository.requestImagePostProcess(file)
                            _uploadImageData.value = tmpData
                            _selectPhotoStatus.value = SelectPhotoStatus.SUCCESS
                        }
                        else -> {
                            DLog.e(
                                "${TAG}_requestImagePostProcessFromCamera",
                                "Inappropriate PhotoStatus, ${selectPhotoStatus.value}"
                            )
                        }
                    }
                }
            }
        }
    }

    fun requestImagePostProcessFromGallery(contentUri: Uri) {
        viewModelScope.launch {
            when (selectPhotoStatus.value) {
                SelectPhotoStatus.GALLERY -> {
                    val tmpData = imageRepository.requestImagePostProcess(contentUri)
                    DLog.d(TAG,"contentUri=$contentUri, tmpData=$tmpData")
                    _uploadImageData.value = tmpData
                    _selectPhotoStatus.value = SelectPhotoStatus.SUCCESS
                }
                else -> {
                    DLog.e(
                        "${TAG}_requestImagePostProcessFromGallery",
                        "Inappropriate PhotoStatus, ${selectPhotoStatus.value}"
                    )
                }
            }
        }
    }

    fun setHoleQuestionAnswer(pAnswer: Int) {
        _holeQuestionAnswer.value = pAnswer
    }

    fun setDotQuestionAnswer(pAnswer: Int) {
        _dotQuestionAnswer.value = pAnswer
    }

    fun setColorQuestionAnswer(pAnswer: Int) {
        _colorQuestionAnswer.value = pAnswer
    }

    fun calculateStoneType() {
        val x = holeQuestionAnswer.value ?: 0
        val y = dotQuestionAnswer.value ?: 0
        val z = colorQuestionAnswer.value ?: 0

        /**
         * 화강암 vector (0, 2, 1)
         * 현무암 vector (1, 0, 1)
         * 화산송이 vector (2, 0, 2)
         * 석회암 vector (0, 0, 2)
         */
        val dist1 = sqrt(x * x + (y - 2.0) * (y - 2) + (z - 1.0) * (z - 1))
        val dist2 = sqrt((x - 1.0) * (x - 1) + y * y + (z - 1.0) * (z - 1))
        val dist3 = sqrt((x - 2.0) * (x - 2) + y * y + (z - 2.0) * (z - 2))
        val dist4 = sqrt(x * x + y * y + (z - 2.0) * (z - 2))
        val minValue = min(min(dist1, dist2), min(dist3, dist4))

        uploadImageData.value?.stoneType = when (minValue) {
            dist1 -> "화강암"
            dist2 -> "현무암"
            dist3 -> "화산송이"
            else -> "석회암"
        }
    }

    companion object {
        private const val TAG: String = "EnrollViewModel"
    }

}