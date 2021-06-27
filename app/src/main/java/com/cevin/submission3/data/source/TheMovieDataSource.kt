package com.cevin.submission3.data.source

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.cevin.submission3.data.Movie
import com.cevin.submission3.data.TvShow
import com.cevin.submission3.data.source.local.entity.MovieEntity
import com.cevin.submission3.data.source.local.entity.TvShowEntity
import com.cevin.submission3.data.source.remote.response.MovieDetailResponse
import com.cevin.submission3.data.source.remote.response.MovieResponse
import com.cevin.submission3.data.source.remote.response.TvDetailResponse
import com.cevin.submission3.data.source.remote.response.TvResponse
import com.cevin.submission3.vo.Resource
import retrofit2.Callback

interface TheMovieDataSource {
    fun getAllMovie(page: Int): LiveData<List<Movie>>

    fun getAllTvShow(page: Int): LiveData<List<TvShow>>

    fun getDetailMovie(id: Int): LiveData<Resource<MovieEntity>>

    fun getDetailTv(id: Int): LiveData<Resource<TvShowEntity>>

    interface LoadMoviesCallback : Callback<MovieResponse>

    interface LoadTvCallback : Callback<TvResponse>

    interface LoadDetailMovieCallback : Callback<MovieDetailResponse>

    interface LoadDetailTvCallback : Callback<TvDetailResponse>

    fun setFavoriteMovie(movieEntity: MovieEntity, state: Boolean)

    fun setFavoriteTvShow(tvShowEntity: TvShowEntity, state: Boolean)

    fun getFavoriteMovie(): LiveData<PagedList<MovieEntity>>

    fun getFavoriteTvShow(): LiveData<PagedList<TvShowEntity>>
}