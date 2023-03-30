package com.goormthon.mang9rme.kimbsu.feature.enroll.data

data class UploadStonRequest(
    val dateTime : String,
    val address : String,
    val lat : String,
    val lng : String,
    val stoneType : String
)
