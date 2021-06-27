package com.cevin.submission3.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.cevin.submission3.data.source.TheMovieRepository
import com.cevin.submission3.data.source.local.entity.MovieEntity
import com.cevin.submission3.data.source.local.entity.TvShowEntity
import com.cevin.submission3.utils.DataDummy
import com.cevin.submission3.vo.Resource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {
    @Mock
    private lateinit var theMovieRepository: TheMovieRepository
    private lateinit var detailViewModel: DetailViewModel
    private val dummyDetailMovie = DataDummy.generateMovieDetailResponse()
    private val dummyDetailTv = DataDummy.generateTvDetailResponse()
    private val idMovie = 460465
    private val idTv = 120168

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var observerMovie: Observer<Resource<MovieEntity>>

    @Mock
    private lateinit var observerTv: Observer<Resource<TvShowEntity>>

    @Before
    fun setUp() {
        detailViewModel = DetailViewModel(theMovieRepository)
    }

    @Test
    fun getMovie() {
        val dummyMovie = Resource.success(MovieEntity(DataDummy.generateMovieDetailResponse()))
        val movie = MutableLiveData<Resource<MovieEntity>>()
        movie.value = dummyMovie

        Mockito.`when`(theMovieRepository.getDetailMovie(idMovie)).thenReturn(movie)
        val movieDetail = detailViewModel.getMovie(idMovie)
        Mockito.verify(theMovieRepository).getDetailMovie(idMovie)

        movie.observeForever(observerMovie)
        Mockito.verify(observerMovie).onChanged(dummyMovie)

        assertNotNull(movieDetail)
        assertEquals(dummyDetailMovie.id, movieDetail.value?.data?.id)
        assertEquals(dummyDetailMovie.title, movieDetail.value?.data?.title)
        assertEquals(dummyDetailMovie.overview, movieDetail.value?.data?.overview)
        assertEquals(dummyDetailMovie.voteAverage ?: 0.0, movieDetail.value?.data?.voteAverage ?: 0.0, 0.0001)
        assertEquals(dummyDetailMovie.releaseDate, movieDetail.value?.data?.releaseDate)
        assertEquals(dummyDetailMovie.posterPath, movieDetail.value?.data?.posterPath)
    }

    @Test
    fun getTvShow() {
        val dummyTv = Resource.success(TvShowEntity(DataDummy.generateTvDetailResponse()))
        val tv = MutableLiveData<Resource<TvShowEntity>>()
        tv.value = dummyTv

        Mockito.`when`(theMovieRepository.getDetailTv(idTv)).thenReturn(tv)
        val tvDetail = detailViewModel.getTvShow(idTv)
        Mockito.verify(theMovieRepository).getDetailTv(idTv)

        tv.observeForever(observerTv)
        Mockito.verify(observerTv).onChanged(dummyTv)

        assertNotNull(tvDetail)
        assertEquals(dummyDetailTv.id, tvDetail.value?.data?.id)
        assertEquals(dummyDetailTv.name, tvDetail.value?.data?.name)
        assertEquals(dummyDetailTv.overview, tvDetail.value?.data?.overview)
        assertEquals(dummyDetailTv.voteAverage ?: 0.0, tvDetail.value?.data?.voteAverage ?: 0.0, 0.0001)
        assertEquals(dummyDetailTv.firstAirDate, tvDetail.value?.data?.firstAirDate)
        assertEquals(dummyDetailTv.posterPath, tvDetail.value?.data?.posterPath)
    }
}