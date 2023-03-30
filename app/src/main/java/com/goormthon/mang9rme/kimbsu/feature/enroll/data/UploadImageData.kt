package com.goormthon.mang9rme.kimbsu.feature.enroll.data

import android.graphics.Bitmap

data class UploadImageData(
    var fileName: String,
    var filePath: String,
    var bitmap: Bitmap?,
    var imgLat: Float?,
    var imgLng: Float?,
    var imgCreateDate: String?,
    var stoneType: String? = null
) {

    override fun toString(): String {
        return "${TAG}{" +
                "fileName=$fileName, " +
                "filePath=$filePath, " +
                "imgLat=$imgLat, " +
                "imgLng=$imgLng, " +
                "imgCreateDate=$imgCreateDate}"
    }

    companion object {
        private const val TAG: String = "UploadImageData"
    }

}