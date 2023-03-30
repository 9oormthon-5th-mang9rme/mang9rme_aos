package com.goormthon.mang9rme.common.util

import com.goormthon.mang9rme.jihun.data.BaseResponse
import com.goormthon.mang9rme.kimbsu.feature.enroll.data.UploadImageData
import okhttp3.MultipartBody
import javax.inject.Inject

class UploadStoneRepositoryImpl @Inject constructor(private val uploadStoneService: UploadStoneService): UploadStoneRepository{

    override suspend fun uploadImage(
        image: MultipartBody.Part?,
        body: UploadImageData
    ): Result<BaseResponse> {
        val response = uploadStoneService.uploadImage(image, body)
        if(response.isSuccessful) {
            response.body()?.let { return Result.success(it) }
        }
        return Result.failure(IllegalArgumentException(response.message()))
    }

}