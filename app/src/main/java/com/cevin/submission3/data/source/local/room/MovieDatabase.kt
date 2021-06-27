package com.cevin.submission3.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cevin.submission3.data.source.local.entity.MovieEntity
import com.cevin.submission3.data.source.local.entity.TvShowEntity

@Database(entities = [MovieEntity::class, TvShowEntity::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun tvShowDao(): TvShowDao

    companion object{
        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getInstance(context: Context): MovieDatabase = INSTANCE ?: synchronized(this) {
            Room.databaseBuilder(
                context.applicationContext,
                MovieDatabase::class.java,
                "Academies.db"
            ).build().apply {
                INSTANCE = this
            }
        }
    }
}