package com.goormthon.mang9rme.kimbsu.feature.enroll.repository

import android.app.Application
import com.goormthon.mang9rme.kimbsu.common.util.DLog
import com.goormthon.mang9rme.kimbsu.common.util.JsonParserUtil
import com.goormthon.mang9rme.kimbsu.data.network.NetworkResult
import com.goormthon.mang9rme.kimbsu.feature.base.repository.BaseNetworkRepository
import com.goormthon.mang9rme.kimbsu.feature.enroll.data.UploadImageData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.DataOutputStream
import java.io.File
import java.io.FileInputStream
import java.net.HttpURLConnection
import java.net.URL

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

    suspend fun makeEnrollStoneRequest(pUploadImageData: UploadImageData) {
        withContext(Dispatchers.IO) {
            val fileName = pUploadImageData.fileName

            val strUrl = "${BASE_URL}/api/stone"
            try {
                val url = URL(strUrl)
                val connection: HttpURLConnection =
                    (url.openConnection() as HttpURLConnection).apply {
                        requestMethod = POST
                        doOutput = true

                        setRequestProperty(
                            "Content-Type",
                            "multipart/form-data; boundary=$BOUNDARY"
                        )
                        setRequestProperty("User-Agent", "Android")
                        setRequestProperty("Connection", "Keep-Alive")
                    }
                val outputStream = DataOutputStream(connection.outputStream)
                outputStream.writeBytes("--$BOUNDARY\r\n")
                outputStream.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"$fileName\"\r\n")
                outputStream.writeBytes("Content-Type: multipart/form-data; boundary=$BOUNDARY\r\n\r\n")

                val imgFile = File(pUploadImageData.filePath)
                val inputStream = FileInputStream(imgFile)

                val buffer = ByteArray(4096)
                var bytesRead: Int
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    DLog.d(TAG,"HTTP_OK")
                } else {
                    DLog.e(TAG,"FAIL")
                }
            } catch (e: Exception) {

            }
        }
    }

    companion object {
        private const val TAG: String = "EnrollNetworkRepository"

        private const val BOUNDARY: String = "----WebKitFormBoundary7MA4YWxkTrZu0gW"

        private const val KAKAO_API: String =
            "https://dapi.kakao.com/v2/local/geo/coord2address.json"
    }
}