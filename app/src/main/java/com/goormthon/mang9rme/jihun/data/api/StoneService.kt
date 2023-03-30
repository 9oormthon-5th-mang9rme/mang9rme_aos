package com.goormthon.mang9rme.jihun.data.api

import com.goormthon.mang9rme.jihun.data.data.StoneFeedResponse
import com.goormthon.mang9rme.jihun.data.data.StoneResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface StoneService {

    @GET("/api/stone")
    suspend fun getStoneFeedData() : Response<StoneFeedResponse>

    @GET("/api/{stoneId}")
    suspend fun getStoneData(
        @Path("stoneId", encoded = true) stoneId : Int
    ) : Response<StoneResponse>
}