package com.goormthon.mang9rme.common.util

import com.goormthon.mang9rme.jihun.data.BaseResponse
import com.goormthon.mang9rme.kimbsu.feature.enroll.data.UploadImageData
import okhttp3.MultipartBody

interface UploadStoneRepository {

    suspend fun uploadImage(image : MultipartBody.Part?, body : UploadImageData) : Result<BaseResponse>
}