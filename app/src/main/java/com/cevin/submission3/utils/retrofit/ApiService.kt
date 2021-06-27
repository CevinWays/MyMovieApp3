package com.cevin.submission3.utils.retrofit
import com.cevin.submission3.data.source.remote.response.MovieDetailResponse
import com.cevin.submission3.data.source.remote.response.MovieResponse
import com.cevin.submission3.data.source.remote.response.TvDetailResponse
import com.cevin.submission3.data.source.remote.response.TvResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("discover/movie")
    fun getMovie(@Query("page") page: Int): Call<MovieResponse>

    @GET("discover/tv")
    fun getTv(@Query("page") page: Int): Call<TvResponse>

    @GET("movie/{id}")
    fun getDetailMovie(@Path("id") id: Int): Call<MovieDetailResponse>

    @GET("tv/{id}")
    fun getDetailTv(@Path("id") id: Int): Call<TvDetailResponse>
}