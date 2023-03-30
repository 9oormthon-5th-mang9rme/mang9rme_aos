package com.goormthon.mang9rme.kimbsu.feature.enroll.repository

import android.app.Application
import com.goormthon.mang9rme.kimbsu.common.util.DLog
import com.goormthon.mang9rme.kimbsu.common.util.JsonParserUtil
import com.goormthon.mang9rme.kimbsu.data.network.NetworkResult
import com.goormthon.mang9rme.kimbsu.feature.base.repository.BaseNetworkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class EnrollNetworkRepository(
    private val application: Application
) : BaseNetworkRepository(application, TAG) {

    private val jsonParserUtil: JsonParserUtil = JsonParserUtil()

    suspend fun makeCoordToAddressRequest(
        pLat: String,
        pLng: String
    ): NetworkResult<String> {
        return withContext(Dispatchers.IO) {
            val strUrl = KAKAO_API
            val hsParams = HashMap<String, String>().apply {
                put("x", pLng)
                put("y", pLat)
            }
            val message = sendRequest(strUrl, hsParams, GET, isKakaoAPI = true)
            DLog.d("${TAG}_makeCoordToAddressRequest", "message=$message")

            if (message.isNotEmpty()) {
                try {
                    val jsonRoot = JSONObject(message)

                    val data = jsonParserUtil.getAddressData(jsonRoot)
                    DLog.d(TAG, "address=$data")
                    NetworkResult.Success(data.ifEmpty { "제주 어딘가" })
                } catch (e: Exception) {
                    NetworkResult.Error(e)
                }
            } else {
                NetworkResult.Error(Exception("API Request Fail!"))
            }
        }
    }

    suspend fun makeEnrollStoneRequest() {
        withContext(Dispatchers.IO) {
            val strUrl = "${BASE_URL}/api/stone"
        }
    }

    companion object {
        private const val TAG: String = "EnrollNetworkRepository"

        private const val KAKAO_API: String =
            "https://dapi.kakao.com/v2/local/geo/coord2address.json"
    }
}