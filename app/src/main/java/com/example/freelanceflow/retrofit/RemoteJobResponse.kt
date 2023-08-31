package com.example.freelanceflow.retrofit

import com.example.freelanceflow.api.jobRespone
import retrofit2.Call
import retrofit2.http.GET

interface RemoteJobResponse {

    @GET("remote-jobs")
    fun getRemoteJobResponse(): Call<jobRespone>
}