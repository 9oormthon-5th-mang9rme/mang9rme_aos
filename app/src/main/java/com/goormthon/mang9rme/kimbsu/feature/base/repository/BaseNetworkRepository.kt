package com.goormthon.mang9rme.kimbsu.feature.base.repository

import android.app.Application
import com.goormthon.mang9rme.kimbsu.common.util.DLog
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

open class BaseNetworkRepository(
    private val application: Application,
    private val TAG: String
) {

    protected fun sendRequest(
        strUrl: String,
        hsParams: HashMap<String, String>,
        urlState: String,
        isKakaoAPI: Boolean = false
    ): String {
        val connection: HttpURLConnection
        var message: String = ""
        try {
            val url: URL =
                if (urlState == "GET") URL("${strUrl}?${getParams(hsParams)}") else URL(strUrl)

            DLog.d(TAG, "strUrl=$strUrl")
            DLog.d(TAG, "hsParams=$hsParams")
            // Https Protocol Check
            connection = if (url.protocol == "https") {
                trustAllHosts()
                val https = url.openConnection() as HttpsURLConnection
                https.hostnameVerifier = DO_NOT_VERIFY
                https
            } else {
                url.openConnection() as HttpURLConnection
            }

            connection.apply {
                readTimeout = TIME_OUT
                connectTimeout = TIME_OUT
                requestMethod = urlState
                setRequestProperty("Accept", "application/json")

                if (isKakaoAPI) {
                    connection.setRequestProperty(
                        "Authorization",
                        "KakaoAK 033ebeba4caf1c6f3dbe0e0793175a1b"
                    )
                }
            }

            when (urlState) {
                PUT -> {
                    connection.setRequestProperty("Content-Type", "application/json")
                    connection.doOutput = true
                    throw UnsupportedOperationException("PUT Connection is not supported!")
                }
                POST -> {
                    val os = connection.outputStream
                    val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))

                    connection.doOutput = true
                    val strParams = getParams(hsParams)
                    writer.write(strParams)

                    writer.flush()
                    writer.close()

                    os.flush()
                    os.close()
                }
            }

            val bis = BufferedInputStream(connection.inputStream)
            message = getMessage(bis)

        } catch (e: Exception) {
            DLog.e(TAG, "msg=${e.message}", e)
            message = ""
        }

        return message
    }

    private fun getMessage(inputStream: InputStream?): String {
        val builder = java.lang.StringBuilder()
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                builder.append(line + "")
            }
        } catch (e: UnsupportedEncodingException) {
        } catch (e: IOException) {
        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (e: IOException) {
                }
            }
        }
        return builder.toString()
    }

    private fun getParams(hsParams: HashMap<String, String>): String {
        val sb = StringBuilder()

        val iterator = hsParams.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()

            if (sb.isNotEmpty())
                sb.append("&")

            sb.append(URLEncoder.encode(entry.key, "UTF-8"))
            sb.append("=")
            sb.append(URLEncoder.encode(entry.value, "UTF-8"))
        }

        return sb.toString()
    }

    companion object {
        const val BASE_URL: String = "http://a076aee9f550c48b79e31c682f9f7789-324101715.ap-northeast-2.elb.amazonaws.com"

        const val POST: String = "POST"
        const val GET: String = "GET"
        const val PUT: String = "PUT"

        private val TIME_OUT = 10 * 1000

        private val DO_NOT_VERIFY: HostnameVerifier = HostnameVerifier { hostname, session -> true }

        private fun trustAllHosts() {
            val trustAllCerts = arrayOf(object : X509TrustManager {
                override fun checkClientTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?
                ) {
                }

                override fun checkServerTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf<X509Certificate>()
                }
            })

            try {
                val sc = SSLContext.getInstance("TLS")
                sc.init(null, trustAllCerts, SecureRandom())
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
            } catch (e: Exception) {
            }
        }
    }

}