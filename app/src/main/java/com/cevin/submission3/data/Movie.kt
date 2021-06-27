package com.cevin.submission3.data

import com.cevin.submission3.data.source.remote.response.MovieResultsItem
import java.io.Serializable

data class Movie(
    val id: Int,
    val title: String,
    val backdropPath: String?,
    val posterPath: String?,
    val overview: String?,
    val releaseDate: String?,
    val voteAverage: Double?,
) : Serializable {
    constructor(movieResponse: MovieResultsItem) : this(
        movieResponse.id,
        movieResponse.title,
        movieResponse.backdropPath,
        movieResponse.posterPath,
        movieResponse.overview,
        movieResponse.releaseDate,
        movieResponse.voteAverage
    )
}
