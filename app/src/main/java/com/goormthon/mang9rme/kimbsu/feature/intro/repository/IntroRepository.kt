package com.goormthon.mang9rme.kimbsu.feature.intro.repository

import android.app.Application
import com.goormthon.mang9rme.common.data.StoneData
import com.goormthon.mang9rme.kimbsu.common.util.DLog
import com.goormthon.mang9rme.kimbsu.common.util.JsonParserUtil
import com.goormthon.mang9rme.kimbsu.data.network.NetworkResult
import com.goormthon.mang9rme.kimbsu.feature.base.repository.BaseNetworkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class IntroRepository(
    private val application: Application
) : BaseNetworkRepository(application, TAG) {

    private val jsonParserUtil: JsonParserUtil = JsonParserUtil()

    suspend fun makeStoneDataListRequest(): NetworkResult<ArrayList<StoneData>> {
        return withContext(Dispatchers.IO) {
            val strUrl = "${BASE_URL}/api/stone"
            val hsParams = HashMap<String, String>().apply {
            }
            val message = sendRequest(strUrl, hsParams, GET)
            DLog.d("${TAG}_makeIntroRepository", "message=$message")

            val result = if (message.isNotEmpty()) {
                try {
                    val jsonRoot = JSONObject(message)

                    val data = jsonParserUtil.getStoneDataList(jsonRoot)

                    if (data != null) {
                        NetworkResult.Success(data)
                    } else {
                        val msg = jsonParserUtil.getString(jsonRoot, "result_msg")
                        NetworkResult.Fail(msg)
                    }
                } catch (e: Exception) {
                    NetworkResult.Error(e)
                }
            } else {
                NetworkResult.Error(Exception("API Request Fail!"))
            }

            result
        }
    }

    companion object {
        private const val TAG: String = "IntroRepository"
    }

}