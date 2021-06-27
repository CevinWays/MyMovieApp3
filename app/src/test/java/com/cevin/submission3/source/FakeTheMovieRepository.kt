package com.cevin.submission3.source

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.cevin.submission3.data.Movie
import com.cevin.submission3.data.TvShow
import com.cevin.submission3.data.source.NetworkBoundResource
import com.cevin.submission3.data.source.TheMovieDataSource
import com.cevin.submission3.data.source.local.LocalDataSource
import com.cevin.submission3.data.source.local.entity.MovieEntity
import com.cevin.submission3.data.source.local.entity.TvShowEntity
import com.cevin.submission3.data.source.remote.ApiResponse
import com.cevin.submission3.data.source.remote.RemoteDataSource
import com.cevin.submission3.data.source.remote.response.MovieDetailResponse
import com.cevin.submission3.data.source.remote.response.MovieResponse
import com.cevin.submission3.data.source.remote.response.TvDetailResponse
import com.cevin.submission3.data.source.remote.response.TvResponse
import com.cevin.submission3.utils.AppExecutors
import com.cevin.submission3.utils.EspressoIdlingResource
import com.cevin.submission3.vo.Resource
import retrofit2.Call
import retrofit2.Response

class FakeTheMovieRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
    ) : TheMovieDataSource {
    companion object {
        private const val TAG = "FakeTheMovieRepository"
    }

    override fun getAllMovie(page: Int): LiveData<List<Movie>> {
        val movieResults = MutableLiveData<List<Movie>>()
        EspressoIdlingResource.increment()
        remoteDataSource.getAll(
            object : TheMovieDataSource.LoadMoviesCallback {
                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    if (response.isSuccessful) {
                        val movieList = ArrayList<Movie>()
                        for (movieResponse in response.body()?.results ?: listOf()) {
                            movieList.add(Movie(movieResponse))
                        }
                        movieResults.postValue(movieList)
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                    EspressoIdlingResource.decrement()
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                    EspressoIdlingResource.decrement()
                }
            },
            page
        )
        return movieResults
    }

    override fun getAllTvShow(page: Int): LiveData<List<TvShow>> {
        val tvResults = MutableLiveData<List<TvShow>>()
        EspressoIdlingResource.increment()
        remoteDataSource.getAll(object : TheMovieDataSource.LoadTvCallback {
            override fun onResponse(
                call: Call<TvResponse>,
                response: Response<TvResponse>
            ) {
                if (response.isSuccessful) {
                    val tvList = ArrayList<TvShow>()
                    for (tvResponse in response.body()?.results ?: listOf()) {
                        tvList.add(TvShow(tvResponse))
                    }
                    tvResults.postValue(tvList)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(call: Call<TvResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                EspressoIdlingResource.decrement()
            }

        }, page)
        return tvResults
    }

    override fun getDetailMovie(id: Int): LiveData<Resource<MovieEntity>> {
        return object : NetworkBoundResource<MovieEntity, MovieDetailResponse>(appExecutors) {
            override fun loadFromDB(): LiveData<MovieEntity> = localDataSource.getDetailMovie(id)

            override fun shouldFetch(data: MovieEntity?): Boolean = data == null

            override fun createCall(): LiveData<ApiResponse<MovieDetailResponse>> {
                EspressoIdlingResource.increment()
                val result = MutableLiveData<ApiResponse<MovieDetailResponse>>()
                remoteDataSource.getDetail(
                    object : TheMovieDataSource.LoadDetailMovieCallback {
                        override fun onResponse(
                            call: Call<MovieDetailResponse>,
                            response: Response<MovieDetailResponse>
                        ) {
                            EspressoIdlingResource.decrement()
                            if (response.body() != null) {
                                result.postValue(ApiResponse.success(response.body()!!))
                            } else {
                                result.postValue(
                                    ApiResponse.empty(
                                        response.message(),
                                        MovieDetailResponse()
                                    )
                                )
                            }
                        }

                        override fun onFailure(call: Call<MovieDetailResponse>, t: Throwable) {
                            EspressoIdlingResource.decrement()
                            result.postValue(
                                ApiResponse.error(
                                    t.message ?: "Error when call in api movie",
                                    call.execute().body()!!
                                )
                            )
                        }
                    }, id
                )
                return result
            }

            override fun saveCallResult(data: MovieDetailResponse) {
                localDataSource.insertMovie(MovieEntity(data))
            }

        }.asLiveData()
    }

    override fun getDetailTv(id: Int): LiveData<Resource<TvShowEntity>> {
        return object : NetworkBoundResource<TvShowEntity, TvDetailResponse>(appExecutors) {
            override fun loadFromDB(): LiveData<TvShowEntity> = localDataSource.getDetailTvShow(id)

            override fun shouldFetch(data: TvShowEntity?): Boolean = data == null

            override fun createCall(): LiveData<ApiResponse<TvDetailResponse>> {
                EspressoIdlingResource.increment()
                val result = MutableLiveData<ApiResponse<TvDetailResponse>>()
                remoteDataSource.getDetail(
                    object : TheMovieDataSource.LoadDetailTvCallback {
                        override fun onResponse(
                            call: Call<TvDetailResponse>,
                            response: Response<TvDetailResponse>
                        ) {
                            EspressoIdlingResource.decrement()
                            if (response.body() != null) {
                                result.postValue(ApiResponse.success(response.body()!!))
                            } else {
                                result.postValue(
                                    ApiResponse.empty(
                                        response.message(),
                                        TvDetailResponse()
                                    )
                                )
                            }
                        }

                        override fun onFailure(call: Call<TvDetailResponse>, t: Throwable) {
                            EspressoIdlingResource.decrement()
                            result.postValue(
                                ApiResponse.error(
                                    t.message ?: "Error when call in api movie",
                                    call.execute().body()!!
                                )
                            )
                        }

                    }, id
                )
                return result
            }

            override fun saveCallResult(data: TvDetailResponse) {
                localDataSource.insertTvShow(TvShowEntity(data))
            }

        }.asLiveData()
    }

    override fun setFavoriteMovie(movieEntity: MovieEntity, state: Boolean) =
        appExecutors.diskIO().execute { localDataSource.setFavoriteMovie(movieEntity, state) }

    override fun setFavoriteTvShow(tvShowEntity: TvShowEntity, state: Boolean) =
        appExecutors.diskIO().execute { localDataSource.setFavoriteTvShow(tvShowEntity, state) }

    override fun getFavoriteMovie(): LiveData<PagedList<MovieEntity>> =
        localDataSource.getFavoriteMovie().toLiveData(4)

    override fun getFavoriteTvShow(): LiveData<PagedList<TvShowEntity>> =
        localDataSource.getFavoriteTvShow().toLiveData(4)
}