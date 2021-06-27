package com.cevin.submission3.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.cevin.submission3.data.source.TheMovieRepository
import com.cevin.submission3.data.source.local.entity.MovieEntity
import com.cevin.submission3.data.source.local.entity.TvShowEntity
import com.cevin.submission3.vo.Resource
import javax.inject.Inject

class DetailViewModel @Inject constructor(private val theMovieRepository: TheMovieRepository) : ViewModel() {
    fun getMovie(id: Int): LiveData<Resource<MovieEntity>> = theMovieRepository.getDetailMovie(id)

    fun getTvShow(id: Int): LiveData<Resource<TvShowEntity>> = theMovieRepository.getDetailTv(id)

    fun bookmarkMovie(movieEntity: MovieEntity){
        theMovieRepository.setFavoriteMovie(movieEntity, !movieEntity.isFavorite)
    }

    fun bookmarkTvShow(tvShowEntity: TvShowEntity){
        theMovieRepository.setFavoriteTvShow(tvShowEntity, !tvShowEntity.isFavorite)
    }
}