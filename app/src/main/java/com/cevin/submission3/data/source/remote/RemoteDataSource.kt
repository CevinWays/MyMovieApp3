package com.cevin.submission3.data.source.remote

import com.cevin.submission3.data.source.TheMovieDataSource.*
import com.cevin.submission3.utils.retrofit.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val apiService: ApiService) {
    fun getAll(callback: LoadMoviesCallback, page:Int) = apiService.getMovie(page).enqueue(callback)

    fun getAll(callback: LoadTvCallback, page: Int) =  apiService.getTv(page).enqueue(callback)

    fun getDetail(callback: LoadDetailMovieCallback, id: Int) = apiService.getDetailMovie(id).enqueue(callback)

    fun getDetail(callback: LoadDetailTvCallback, id: Int) = apiService.getDetailTv(id).enqueue(callback)
}