package com.example.freelanceflow.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.freelanceflow.api.jobRespone
import com.example.freelanceflow.newsapi.NewsResponse
import com.example.freelanceflow.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RemoteJobRepository {
    private val remoteJobService = RetrofitInstance.remoteJobApi
    private val newsService = RetrofitInstance.newsApi
    val responseJobLivedata: MutableLiveData<jobRespone?> = MutableLiveData()
    val newsLiveData: MutableLiveData<NewsResponse?> = MutableLiveData()

    init {
        getRemoteJobResponse()
    }
    init {
        getTechnologyNews()
    }
    private fun getRemoteJobResponse(){
        remoteJobService.getRemoteJobResponse().enqueue(
            object : Callback<jobRespone> {
                override fun onResponse(
                    call: Call<jobRespone>,
                    response: Response<jobRespone>
                ) {
                    responseJobLivedata.postValue(response.body())
                }

                override fun onFailure(call: Call<jobRespone>, t: Throwable) {
                    responseJobLivedata.postValue(null)
                    Log.e("JOB", "Failure ${t.message}")
                }
            }
        )
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
                    Log.e("TECHs", "Failure ${t.message}")
                }
            }
        )
    }

    fun remoteJobResult(): MutableLiveData<jobRespone?> {
        return responseJobLivedata
    }

    fun newsResult() = newsLiveData
}
