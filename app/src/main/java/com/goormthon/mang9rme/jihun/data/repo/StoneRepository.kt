package com.goormthon.mang9rme.jihun.data.repo

import com.goormthon.mang9rme.common.data.StoneData

interface StoneRepository {

    suspend fun getStoneData() : Result<List<StoneData>>
}