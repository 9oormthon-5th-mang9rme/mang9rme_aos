package com.goormthon.mang9rme.jihun.presentation.di

import com.goormthon.mang9rme.jihun.data.api.StoneService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Provides
    @Singleton
    fun provideStoneService(retrofit: Retrofit): StoneService =
        retrofit.create(StoneService::class.java)
}