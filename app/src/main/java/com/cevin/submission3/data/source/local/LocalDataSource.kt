package com.cevin.submission3.data.source.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.cevin.submission3.data.source.local.entity.MovieEntity
import com.cevin.submission3.data.source.local.entity.TvShowEntity
import com.cevin.submission3.data.source.local.room.MovieDao
import com.cevin.submission3.data.source.local.room.TvShowDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(
    private val movieDao: MovieDao,
    private val tvShowDao: TvShowDao
) {
    fun getFavoriteMovie(): DataSource.Factory<Int, MovieEntity> = movieDao.getAllFavoriteMovie()

    fun getFavoriteTvShow(): DataSource.Factory<Int, TvShowEntity> = tvShowDao.getAllFavoriteTvShow()

    fun getDetailMovie(id: Int): LiveData<MovieEntity> = movieDao.getDetailMovie(id)

    fun getDetailTvShow(id: Int): LiveData<TvShowEntity> = tvShowDao.getDetailTvShow(id)

    fun insertMovie(movieEntity: MovieEntity) = movieDao.insertMovie(movieEntity)

    fun insertTvShow(tvShowEntity: TvShowEntity) = tvShowDao.insertTvShow(tvShowEntity)

    fun setFavoriteMovie(movieEntity: MovieEntity, newState: Boolean) {
        movieEntity.isFavorite = newState
        if (getDetailMovie(movieEntity.id).value != null) {
            movieDao.insertMovie(movieEntity)
        } else {
            movieDao.updateMovie(movieEntity)
        }
    }

    fun setFavoriteTvShow(tvShowEntity: TvShowEntity, newState: Boolean) {
        tvShowEntity.isFavorite = newState
        if (getDetailTvShow(tvShowEntity.id).value != null) {
            tvShowDao.insertTvShow(tvShowEntity)
        } else {
            tvShowDao.updateTvShow(tvShowEntity)
        }
    }
}