package com.goormthon.mang9rme.jihun.data.api

import com.goormthon.mang9rme.jihun.data.data.StoneFeedResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface StoneService {

    @GET("/api/stone")
    suspend fun getStoneFeedData() : Response<StoneFeedResponse>
}