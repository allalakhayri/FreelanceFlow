package com.example.freelanceflow.repository


import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.freelanceflow.newsapi.NewsResponse
import com.example.freelanceflow.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TechNewsRepository {
    private val newsService = RetrofitInstance.newsApi
    val newsLiveData: MutableLiveData<NewsResponse?> = MutableLiveData()


    init {
        getTechnologyNews()
    }


    private fun getTechnologyNews(){
        newsService.getTechnologyNews("in","technology").enqueue(
            object : Callback<NewsResponse> {
                override fun onResponse(
                    call: Call<NewsResponse>,
                    response: Response<NewsResponse>
                ) {

                    newsLiveData.postValue(response.body())
                }

                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    newsLiveData.postValue(null)
                    Log.e("News", "Failure ${t.message}")
                }
            }
        )
    }



    fun newsResult() = newsLiveData


}

