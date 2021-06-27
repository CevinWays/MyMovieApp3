package com.cevin.submission3.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cevin.submission3.data.source.remote.response.TvDetailResponse

@Entity(tableName = "tvshow")
data class TvShowEntity(
    @ColumnInfo(name = "first_air_date")
    val firstAirDate: String?,

    @ColumnInfo(name = "overview")
    val overview: String?,

    @ColumnInfo(name = "original_language")
    val originalLanguage: String?,

    @ColumnInfo(name = "poster_path")
    val posterPath: String?,

    @ColumnInfo(name = "backdrop_path")
    val backdropPath: String?,

    @ColumnInfo(name = "original_name")
    val originalName: String?,

    @ColumnInfo(name = "popularity")
    val popularity: Double,

    @ColumnInfo(name = "vote_average")
    val voteAverage: Double,

    @ColumnInfo(name = "name")
    val name: String?,

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "vote_count")
    val voteCount: Int,

    @ColumnInfo(name = "is_favorite")
    var isFavorite: Boolean
) {
    constructor(tvDetailResponse: TvDetailResponse) : this(
        tvDetailResponse.firstAirDate,
        tvDetailResponse.overview,
        tvDetailResponse.originalLanguage,
        tvDetailResponse.posterPath,
        tvDetailResponse.backdropPath,
        tvDetailResponse.originalName,
        tvDetailResponse.popularity ?: 0.0,
        tvDetailResponse.voteAverage ?: 0.0,
        tvDetailResponse.name,
        tvDetailResponse.id?:0,
        tvDetailResponse.voteCount?:0,
        false
    )
}
