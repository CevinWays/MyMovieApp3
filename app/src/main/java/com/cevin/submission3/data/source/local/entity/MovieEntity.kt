package com.cevin.submission3.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cevin.submission3.data.source.remote.response.MovieDetailResponse

@Entity(tableName = "movie")
data class MovieEntity(
    @ColumnInfo(name = "overview")
    val overview: String?,

    @ColumnInfo(name = "original_language")
    val originalLanguage: String?,

    @ColumnInfo(name = "original_title")
    val originalTitle: String?,

    @ColumnInfo(name = "video")
    val video: Boolean,

    @ColumnInfo(name = "title")
    val title: String?,

    @ColumnInfo(name = "poster_path")
    val posterPath: String?,

    @ColumnInfo(name = "backdrop_path")
    val backdropPath: String?,

    @ColumnInfo(name = "release_date")
    val releaseDate: String?,

    @ColumnInfo(name = "popularity")
    val popularity: Double?,

    @ColumnInfo(name = "vote_average")
    val voteAverage: Double?,

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "adult")
    val adult: Boolean,

    @ColumnInfo(name = "vote_count")
    val voteCount: Int?,

    @ColumnInfo(name = "is_favorite")
    var isFavorite: Boolean
) {

    constructor(movieDetailResponse: MovieDetailResponse) : this(
        movieDetailResponse.overview,
        movieDetailResponse.originalLanguage,
        movieDetailResponse.originalTitle,
        movieDetailResponse.video == true,
        movieDetailResponse.title,
        movieDetailResponse.posterPath,
        movieDetailResponse.backdropPath,
        movieDetailResponse.releaseDate,
        movieDetailResponse.popularity,
        movieDetailResponse.voteAverage,
        movieDetailResponse.id ?: 0,
        movieDetailResponse.adult == true,
        movieDetailResponse.voteCount,
        false
    )
}
