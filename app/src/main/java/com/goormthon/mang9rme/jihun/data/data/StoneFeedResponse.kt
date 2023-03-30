package com.goormthon.mang9rme.jihun.data.data

import com.goormthon.mang9rme.common.data.StoneData
import com.goormthon.mang9rme.jihun.data.BaseResponse

data class StoneFeedResponse (
    val resultCode : String,
    val message : String,
    val data : List<StoneData>
        )