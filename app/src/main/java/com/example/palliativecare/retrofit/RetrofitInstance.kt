package com.example.palliativecare.retrofit

import com.example.palliativecare.constant.Constant
import com.example.palliativecare.interfac.NotificationApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object{
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val api by lazy {
            retrofit.create(NotificationApi::class.java)
        }
    }
}