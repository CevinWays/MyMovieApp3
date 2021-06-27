package com.cevin.submission3.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.cevin.submission3.data.source.local.entity.TvShowEntity

@Dao
interface TvShowDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTvShow(tvShowEntity: TvShowEntity)

    @Query("Select * from tvshow WHERE is_favorite = 1")
    fun getAllFavoriteTvShow(): DataSource.Factory<Int, TvShowEntity>

    @Query("Select * from tvshow WHERE id = :id")
    fun getDetailTvShow(id: Int): LiveData<TvShowEntity>

    @Update
    fun updateTvShow(tvShowEntity: TvShowEntity)

    @Delete
    fun deleteTvShow(tvShowEntity: TvShowEntity)
}