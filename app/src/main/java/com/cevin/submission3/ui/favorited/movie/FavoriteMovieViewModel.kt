package com.cevin.submission3.ui.favorited.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.cevin.submission3.data.source.TheMovieRepository
import com.cevin.submission3.data.source.local.entity.MovieEntity
import javax.inject.Inject

class FavoriteMovieViewModel @Inject constructor(private val movieRepository: TheMovieRepository) :
    ViewModel() {

    fun getFavoriteMovie(): LiveData<PagedList<MovieEntity>> {
        return movieRepository.getFavoriteMovie()
    }

    fun setFavoriteMovie(movieEntity: MovieEntity) {
        val newState = !movieEntity.isFavorite
        movieRepository.setFavoriteMovie(movieEntity, newState)
    }
}