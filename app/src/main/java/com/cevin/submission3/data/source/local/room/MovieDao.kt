package com.cevin.submission3.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.cevin.submission3.data.source.local.entity.MovieEntity

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movieEntity: MovieEntity)

    @Query("Select * from movie WHERE is_favorite = 1")
    fun getAllFavoriteMovie(): DataSource.Factory<Int, MovieEntity>

    @Query("Select * from movie WHERE id = :id")
    fun getDetailMovie(id: Int): LiveData<MovieEntity>

    @Update
    fun updateMovie(movieEntity: MovieEntity)

    @Delete
    fun deleteMovie(movieEntity: MovieEntity)
}