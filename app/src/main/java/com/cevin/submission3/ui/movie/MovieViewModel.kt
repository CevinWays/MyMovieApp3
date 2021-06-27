package com.cevin.submission3.ui.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.cevin.submission3.data.Movie
import com.cevin.submission3.data.source.TheMovieRepository
import javax.inject.Inject

class MovieViewModel @Inject constructor(private val theMovieRepository: TheMovieRepository) : ViewModel() {
    private var page = 0

    fun getListMovie(): LiveData<List<Movie>> {
        page++
        return theMovieRepository.getAllMovie(page)
    }
}