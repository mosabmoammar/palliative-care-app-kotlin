package com.example.palliativecare.interfac

import com.example.palliativecare.constant.Constant.Companion.CONTENT_TYPE
import com.example.palliativecare.constant.Constant.Companion.SERVER_KEY
import com.example.palliativecare.model.PushNotification
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationApi {
    @Headers("Authorization: key=$SERVER_KEY","Content-type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification:PushNotification
    ): Response<ResponseBody>
}