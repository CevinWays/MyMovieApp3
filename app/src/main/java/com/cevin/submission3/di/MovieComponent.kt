package com.cevin.submission3.di

import com.cevin.submission3.data.source.local.LocalDataSource
import com.cevin.submission3.data.source.local.room.MovieDao
import com.cevin.submission3.data.source.local.room.MovieDatabase
import com.cevin.submission3.data.source.local.room.TvShowDao
import com.cevin.submission3.ui.detail.DetailActivity
import com.cevin.submission3.ui.favorited.movie.FavoritedMovieFragment
import com.cevin.submission3.ui.favorited.tvshow.FavoritedTvShowFragment
import com.cevin.submission3.ui.movie.MovieFragment
import com.cevin.submission3.ui.tvshow.TvShowFragment
import com.cevin.submission3.utils.retrofit.ApiConfig
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [ApiConfig::class, RoomModule::class, AppExeModule::class])
interface MovieComponent {
    fun inject(fragment: MovieFragment)
    fun inject(fragment: TvShowFragment)
    fun inject(activity: DetailActivity)
    fun inject(fragment: FavoritedMovieFragment)
    fun inject(fragment: FavoritedTvShowFragment)
    fun movieDatabase(): MovieDatabase
    fun movieDao(): MovieDao
    fun tvDao(): TvShowDao
    fun localDataSource(): LocalDataSource
}