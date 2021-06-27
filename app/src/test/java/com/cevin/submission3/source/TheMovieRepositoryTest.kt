package com.cevin.submission3.source

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.DataSource
import com.cevin.submission3.data.source.TheMovieDataSource
import com.cevin.submission3.data.source.local.LocalDataSource
import com.cevin.submission3.data.source.local.entity.MovieEntity
import com.cevin.submission3.data.source.local.entity.TvShowEntity
import com.cevin.submission3.data.source.remote.RemoteDataSource
import com.cevin.submission3.data.source.remote.response.MovieResponse
import com.cevin.submission3.data.source.remote.response.TvResponse
import com.cevin.submission3.utils.AppExecutors
import com.cevin.submission3.utils.DataDummy
import com.cevin.submission3.utils.LiveDataTestUtil
import com.cevin.submission3.utils.PagedListUtil
import com.cevin.submission3.vo.Resource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*
import retrofit2.Call
import retrofit2.Response

@RunWith(MockitoJUnitRunner.Silent::class)
class TheMovieRepositoryTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val remote = mock(RemoteDataSource::class.java)
    private val local = mock(LocalDataSource::class.java)
    private val appExecutors = mock(AppExecutors::class.java)

    private val theMovieRepository = FakeTheMovieRepository(remote, local, appExecutors)

    @Mock
    private val fakeTheMovieRepository = FakeTheMovieRepository(remote, local, appExecutors)

    private val idMovie = 460465
    private val idTv = 120168

    private val movieResponse = DataDummy.generateMovieResponse()
    private val tvShowResponse = DataDummy.generateTvResponse()
    private val detailMovieResponse = DataDummy.generateMovieDetailResponse()
    private val detailTvResponse = DataDummy.generateTvDetailResponse()

    private val apiMovieResponse = Response.success(movieResponse)
    private val apiTvResponse = Response.success(tvShowResponse)
    private val apiDetailMovieResponse = Response.success(detailMovieResponse)
    private val apiDetailTvResponse = Response.success(detailTvResponse)

    @Mock
    private lateinit var observerMovie: Observer<Resource<MovieEntity>>

    @Mock
    private lateinit var observerTv: Observer<Resource<TvShowEntity>>

    private val mockCallMovie = mock<Call<MovieResponse>> {
        on { execute() } doReturn apiMovieResponse
    }

    private val mockCallTv = mock<Call<TvResponse>> {
        on { execute() } doReturn apiTvResponse
    }

    @Test
    fun getAllMovie() {
        doAnswer {
            (it.arguments[0] as TheMovieDataSource.LoadMoviesCallback).onResponse(mockCallMovie, apiMovieResponse)
            null
        }.`when`(remote).getAll(any<TheMovieDataSource.LoadMoviesCallback>(), eq(1))
        val movie = LiveDataTestUtil.getValue(theMovieRepository.getAllMovie(1))
        verify(remote).getAll(any<TheMovieDataSource.LoadMoviesCallback>(), eq(1))
        assertNotNull(movie)
        assertEquals(movieResponse.results.size.toLong(), movie.size.toLong())
    }

    @Test
    fun getAllTvShow() {
        doAnswer {
            (it.arguments[0] as TheMovieDataSource.LoadTvCallback).onResponse(mockCallTv, apiTvResponse)
            null
        }.`when`(remote).getAll(any<TheMovieDataSource.LoadTvCallback>(), eq(1))
        val tvShow = LiveDataTestUtil.getValue(theMovieRepository.getAllTvShow(1))
        verify(remote).getAll(any<TheMovieDataSource.LoadTvCallback>(), eq(1))
        assertNotNull(tvShow)
        assertEquals(tvShowResponse.results.size.toLong(), tvShow.size.toLong())
    }

    @Test
    fun getDetailMovie() {
        val dummyMovie = Resource.success(MovieEntity(DataDummy.generateMovieDetailResponse()))
        val movie = MutableLiveData<Resource<MovieEntity>>()
        movie.value = dummyMovie

        `when`(fakeTheMovieRepository.getDetailMovie(idMovie)).thenReturn(movie)
        val movieDetail = fakeTheMovieRepository.getDetailMovie(idMovie)
        verify(fakeTheMovieRepository).getDetailMovie(idMovie)

        movie.observeForever(observerMovie)
        verify(observerMovie).onChanged(dummyMovie)

        assertNotNull(movie)
        assertEquals(detailMovieResponse.id, movieDetail.value?.data?.id)
        assertEquals(detailMovieResponse.title, movieDetail.value?.data?.title)
        assertEquals(detailMovieResponse.overview, movieDetail.value?.data?.overview)
        assertEquals(detailMovieResponse.voteAverage ?: 0.0, movieDetail.value?.data?.voteAverage ?: 0.0, 0.0001)
        assertEquals(detailMovieResponse.releaseDate, movieDetail.value?.data?.releaseDate)
        assertEquals(detailMovieResponse.posterPath, movieDetail.value?.data?.posterPath)
    }

    @Test
    fun getDetailTvShow() {
        val dummyTv = Resource.success(TvShowEntity(DataDummy.generateTvDetailResponse()))
        val tv = MutableLiveData<Resource<TvShowEntity>>()
        tv.value = dummyTv

        `when`(fakeTheMovieRepository.getDetailTv(idTv)).thenReturn(tv)
        val tvShow = fakeTheMovieRepository.getDetailTv(idTv)
        Mockito.verify(fakeTheMovieRepository).getDetailTv(idTv)

        tv.observeForever(observerTv)
        Mockito.verify(observerTv).onChanged(dummyTv)

        assertNotNull(tvShow)
        assertEquals(detailTvResponse.id, tvShow.value?.data?.id)
        assertEquals(detailTvResponse.name, tvShow.value?.data?.name)
        assertEquals(detailTvResponse.overview, tvShow.value?.data?.overview)
        assertEquals(detailTvResponse.voteAverage ?: 0.0, tvShow.value?.data?.voteAverage ?: 0.0, 0.0001)
        assertEquals(detailTvResponse.firstAirDate, tvShow.value?.data?.firstAirDate)
        assertEquals(detailTvResponse.posterPath, tvShow.value?.data?.posterPath)
    }

    @Test
    fun setFavoriteMovie() {
        val movie = DataDummy.generateDummyFavoriteMovie().first()
        local.setFavoriteMovie(movie, true)
        verify(local).setFavoriteMovie(movie, true)
    }

    @Test
    fun setFavoriteTvShow() {
        val tvShow = DataDummy.generateDummyFavoriteTvShow().first()
        local.setFavoriteTvShow(tvShow, true)

        verify(local).setFavoriteTvShow(tvShow, true)
    }

    @Test
    fun getFavoriteMovie() {
        val dummyFavMovie = DataDummy.generateDummyFavoriteMovie()
        val dataSourceFactory =
            mock(DataSource.Factory::class.java) as DataSource.Factory<Int, MovieEntity>
        `when`(local.getFavoriteMovie()).thenReturn(dataSourceFactory)
        theMovieRepository.getFavoriteMovie()

        val favMovie = PagedListUtil.mockPagedList(dummyFavMovie)
        verify(local).getFavoriteMovie()

        assertNotNull(favMovie)
        assertEquals(dummyFavMovie.size, favMovie.size)
    }

    @Test
    fun getFavoriteTvShow() {
        val DummyFavTvShow = DataDummy.generateDummyFavoriteTvShow()
        val dataSourceFactory =
            mock(DataSource.Factory::class.java) as DataSource.Factory<Int, TvShowEntity>
        `when`(local.getFavoriteTvShow()).thenReturn(dataSourceFactory)
        theMovieRepository.getFavoriteTvShow()

        val favTvSHow = PagedListUtil.mockPagedList(DummyFavTvShow)
        verify(local).getFavoriteTvShow()

        assertNotNull(favTvSHow)
        assertEquals(DummyFavTvShow.size, favTvSHow.size)
    }
}