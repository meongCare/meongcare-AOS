package com.project.meongcare.home.model.data.repository

import android.util.Log
import com.project.meongcare.home.model.data.remote.HomeRetrofitClient
import com.project.meongcare.home.model.entities.HomeProfileResponse
import javax.inject.Inject

class HomeRepositoryImpl
    @Inject
    constructor(private val homeRetrofitClient: HomeRetrofitClient): HomeRepository {
        override suspend fun getUserProfile(accessToken: String): HomeProfileResponse? {
            try {
                val response = homeRetrofitClient.homeApi.getUserProfile(accessToken)
                if (response.isSuccessful) {
                    Log.d("HomeRepository", "통신 성공 : ${response.code()}")
                    return response.body()
                } else {
                    Log.d("HomeRepository", "통신 실패 : ${response.code()}")
                    return null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }
    }
