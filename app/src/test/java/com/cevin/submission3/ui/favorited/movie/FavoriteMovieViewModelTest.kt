package com.cevin.submission3.ui.favorited.movie

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.paging.PositionalDataSource
import com.cevin.submission3.data.source.TheMovieRepository
import com.cevin.submission3.data.source.local.entity.MovieEntity
import com.cevin.submission3.utils.DataDummy
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import java.util.concurrent.Executors

@RunWith(MockitoJUnitRunner::class)
class FavoriteMovieViewModelTest {
    private lateinit var viewModel: FavoriteMovieViewModel

    @Mock
    private lateinit var theMovieRepository: TheMovieRepository

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var observer: Observer<PagedList<MovieEntity>>

    @Before
    fun setUp() {
        viewModel = FavoriteMovieViewModel(theMovieRepository)
    }

    @Test
    fun getFavoriteMovie() {
        val expected = MutableLiveData<PagedList<MovieEntity>>()
        expected.value = PagedTestDataSources.snapshot(DataDummy.generateDummyFavoriteMovie())

        `when`(theMovieRepository.getFavoriteMovie()).thenReturn(expected)

        viewModel.getFavoriteMovie().observeForever(observer)
        verify(observer).onChanged(expected.value)

        val expectedValue = expected.value
        val actualValue = viewModel.getFavoriteMovie().value
        assertEquals(expectedValue, actualValue)
        assertEquals(expectedValue?.snapshot(), actualValue?.snapshot())
        assertEquals(expectedValue?.size, actualValue?.size)
    }

    @Test
    fun setFavoriteMovie() {
        val movie = DataDummy.generateDummyFavoriteMovie().first()
        theMovieRepository.setFavoriteMovie(movie, true)
        verify(theMovieRepository).setFavoriteMovie(movie, true)
    }
}

class PagedTestDataSources private constructor(private val items: List<MovieEntity>) :
    PositionalDataSource<MovieEntity>() {
    companion object {
        fun snapshot(items: List<MovieEntity> = listOf()): PagedList<MovieEntity> {
            return PagedList.Builder(PagedTestDataSources(items), 10)
                .setNotifyExecutor(Executors.newSingleThreadExecutor())
                .setFetchExecutor(Executors.newSingleThreadExecutor())
                .build()
        }
    }

    override fun loadInitial(
        params: LoadInitialParams,
        callback: LoadInitialCallback<MovieEntity>
    ) {
        callback.onResult(items, 0, items.size)
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<MovieEntity>) {
        val start = params.startPosition
        val end = params.startPosition + params.loadSize
        callback.onResult(items.subList(start, end))
    }
}