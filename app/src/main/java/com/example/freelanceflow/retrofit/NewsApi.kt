package com.example.freelanceflow.retrofit
import com.example.freelanceflow.newsapi.NewsResponse
import com.example.freelanceflow.util.Constants.API_Key
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    fun getTechnologyNews(
        @Query("country")countryCode:String="in",
        @Query("category")category:String="technology",
        @Query("apiKey")apiKey: String= API_Key
    ): Call<NewsResponse>

}