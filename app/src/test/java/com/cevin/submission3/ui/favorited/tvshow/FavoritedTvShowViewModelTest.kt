package com.cevin.submission3.ui.favorited.tvshow

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.paging.PositionalDataSource
import com.cevin.submission3.data.source.TheMovieRepository
import com.cevin.submission3.data.source.local.entity.TvShowEntity
import com.cevin.submission3.utils.DataDummy
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import java.util.concurrent.Executors

@RunWith(MockitoJUnitRunner::class)
class FavoritedTvShowViewModelTest {

    private lateinit var viewModel: FavoritedTvShowViewModel

    @Mock
    private lateinit var theMovieRepository: TheMovieRepository

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var observer: Observer<PagedList<TvShowEntity>>

    @Before
    fun setUp() {
        viewModel = FavoritedTvShowViewModel(theMovieRepository)
    }

    @Test
    fun getFavoriteTvShow() {
        val expected = MutableLiveData<PagedList<TvShowEntity>>()
        expected.value = PagedTestDataSources.snapshot(DataDummy.generateDummyFavoriteTvShow())

        Mockito.`when`(theMovieRepository.getFavoriteTvShow()).thenReturn(expected)

        viewModel.getFavoriteTvShow().observeForever(observer)
        verify(observer).onChanged(expected.value)

        val expectedValue = expected.value
        val actualValue = viewModel.getFavoriteTvShow().value
        Assert.assertEquals(expectedValue, actualValue)
        Assert.assertEquals(expectedValue?.snapshot(), actualValue?.snapshot())
        Assert.assertEquals(expectedValue?.size, actualValue?.size)
    }

    @Test
    fun setFavoriteTv() {
        val tvShow = DataDummy.generateDummyFavoriteTvShow().first()
        theMovieRepository.setFavoriteTvShow(tvShow, true)
        verify(theMovieRepository).setFavoriteTvShow(tvShow, true)
    }
}

class PagedTestDataSources private constructor(private val items: List<TvShowEntity>) :
    PositionalDataSource<TvShowEntity>() {
    companion object {
        fun snapshot(items: List<TvShowEntity> = listOf()): PagedList<TvShowEntity> {
            return PagedList.Builder(PagedTestDataSources(items), 10)
                .setNotifyExecutor(Executors.newSingleThreadExecutor())
                .setFetchExecutor(Executors.newSingleThreadExecutor())
                .build()
        }
    }

    override fun loadInitial(
        params: LoadInitialParams,
        callback: LoadInitialCallback<TvShowEntity>
    ) {
        callback.onResult(items, 0, items.size)
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<TvShowEntity>) {
        val start = params.startPosition
        val end = params.startPosition + params.loadSize
        callback.onResult(items.subList(start, end))
    }
}