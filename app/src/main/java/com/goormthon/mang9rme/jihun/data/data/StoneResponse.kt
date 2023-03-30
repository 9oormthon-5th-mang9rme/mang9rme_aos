package com.goormthon.mang9rme.jihun.data.data

import com.goormthon.mang9rme.common.data.StoneData
import com.goormthon.mang9rme.jihun.data.BaseResponse

data class StoneResponse(
    val data : StoneData,
    val resultCode : String,
    val message : String
)
