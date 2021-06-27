package com.cevin.submission3.ui.favorited.tvshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.cevin.submission3.data.source.TheMovieRepository
import com.cevin.submission3.data.source.local.entity.TvShowEntity
import javax.inject.Inject

class FavoritedTvShowViewModel @Inject constructor(private val movieRepository: TheMovieRepository) :
    ViewModel() {

    fun getFavoriteTvShow() : LiveData<PagedList<TvShowEntity>> = movieRepository.getFavoriteTvShow()

    fun setFavoriteTv(tvShowEntity: TvShowEntity) {
        val newState = !tvShowEntity.isFavorite
        movieRepository.setFavoriteTvShow(tvShowEntity, newState)
    }
}