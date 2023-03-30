package com.goormthon.mang9rme

import android.annotation.SuppressLint
import android.app.Application
import com.goormthon.mang9rme.jihun.presentation.di.RetrofitModule.Companion.API_URL
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
@HiltAndroidApp
class ApplicationClass : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}