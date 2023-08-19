package com.example.freelanceflow.retrofit

import com.example.freelanceflow.util.Constants.NEWS_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitInstance {

    companion object{


        private val retrofit2 by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient
                .Builder()
                .addInterceptor(logging)
                .build()

            Retrofit.Builder().baseUrl(NEWS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

       // val remoteJobApi = retrofit.create(RemoteJobResponse::class.java)
        val newsApi = retrofit2.create(NewsApi::class.java)

    }
}