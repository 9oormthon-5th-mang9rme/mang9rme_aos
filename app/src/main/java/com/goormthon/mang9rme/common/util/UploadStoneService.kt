package com.goormthon.mang9rme.common.util

import com.goormthon.mang9rme.jihun.data.BaseResponse
import com.goormthon.mang9rme.kimbsu.feature.enroll.data.UploadImageData
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UploadStoneService {

    @Multipart
    @POST("/api/stone")
    suspend fun uploadImage(
        @Part image : MultipartBody.Part?,
        @Body uploadStoneRequest : UploadImageData
    ) : Response<BaseResponse>
}