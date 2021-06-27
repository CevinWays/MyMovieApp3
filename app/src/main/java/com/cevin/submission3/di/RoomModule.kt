package com.cevin.submission3.di

import android.app.Application
import androidx.room.Room
import com.cevin.submission3.data.source.local.LocalDataSource
import com.cevin.submission3.data.source.local.room.MovieDao
import com.cevin.submission3.data.source.local.room.MovieDatabase
import com.cevin.submission3.data.source.local.room.TvShowDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule(mApplication: Application) {
    private val movieDatabase: MovieDatabase = Room.databaseBuilder(
        mApplication.applicationContext,
        MovieDatabase::class.java,
        "db_movie"
    ).build()

    @Singleton
    @Provides
    fun provideDatabase(): MovieDatabase = movieDatabase

    @Singleton
    @Provides
    fun provideMovieDao(movieDatabase: MovieDatabase): MovieDao = movieDatabase.movieDao()

    @Singleton
    @Provides
    fun provideTvShowDao(movieDatabase: MovieDatabase): TvShowDao = movieDatabase.tvShowDao()

    @Singleton
    @Provides
    fun provideLocalDataSource(movieDao: MovieDao, tvShowDao: TvShowDao): LocalDataSource =
        LocalDataSource(movieDao, tvShowDao)
}