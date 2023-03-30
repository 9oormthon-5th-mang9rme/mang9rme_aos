package com.goormthon.mang9rme.jihun.presentation.di

import com.goormthon.mang9rme.jihun.data.repo.StoneRepository
import com.goormthon.mang9rme.jihun.data.repo.StoneRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindStoneRepository(stoneRepositoryImpl : StoneRepositoryImpl): StoneRepository

}