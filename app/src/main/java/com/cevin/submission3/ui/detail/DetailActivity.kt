package com.cevin.submission3.ui.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.cevin.submission3.App
import com.cevin.submission3.R
import com.cevin.submission3.data.source.local.entity.MovieEntity
import com.cevin.submission3.data.source.local.entity.TvShowEntity
import com.cevin.submission3.databinding.ActivityDetailBinding
import com.cevin.submission3.utils.Constant
import com.cevin.submission3.vo.Resource
import com.cevin.submission3.vo.Status
import javax.inject.Inject

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_KEY_MOVIE = "movie"
        const val EXTRA_KEY_TV_SHOW = "tv_show"
    }

    private var menu: Menu? = null
    private var detailType: String? = null
    private var id: Int = 0

    @Inject
    lateinit var detailViewModel : DetailViewModel
    private lateinit var detailBinding: ActivityDetailBinding
    private lateinit var movieEntity: MovieEntity
    private lateinit var tvShowEntity: TvShowEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        (applicationContext as App).movieComponent.inject(this)
        setContentView(detailBinding.root)
        detailBinding.progressBar.visibility = View.VISIBLE
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            if (bundle.containsKey(EXTRA_KEY_MOVIE)) {
                setDetailType(EXTRA_KEY_MOVIE, bundle.getInt(EXTRA_KEY_MOVIE))
            } else if (bundle.containsKey(EXTRA_KEY_TV_SHOW)) {
                setDetailType(EXTRA_KEY_TV_SHOW, bundle.getInt(EXTRA_KEY_TV_SHOW))
            }
        }
        detailBinding.progressBar.visibility = View.GONE
    }

    private fun setViewMovie(movie: Resource<MovieEntity>){
        when(movie.status){
            Status.SUCCESS -> {
                detailBinding.progressBar.visibility = View.GONE
                with(detailBinding){
                    textViewTitle.text = movie.data?.title ?: ""
                    textViewOverview.text = movie.data?.overview
                    textViewReleaseDate.text = movie.data?.releaseDate
                    textViewRating.text = movie.data?.voteAverage.toString()
                    Glide.with(this@DetailActivity)
                        .load(Constant.IMAGE_BASE_URL+movie.data?.posterPath)
                        .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
                        .error(R.drawable.ic_error)
                        .into(imageBackDrop)
                    val state = movie.data?.isFavorite == true
                    setBookmarkState(state)
                    movieEntity = movie.data!!
                }
            }
            Status.ERROR -> {
                Toast.makeText(this, "Failed get data", Toast.LENGTH_SHORT).show()
                onBackPressed()
            }
            Status.LOADING -> {
                detailBinding.progressBar.visibility = View.VISIBLE
            }
        }
    }

    private fun setViewTvShow(tvShow: Resource<TvShowEntity>){
        when(tvShow.status){
            Status.SUCCESS -> {
                detailBinding.progressBar.visibility = View.GONE
                with(detailBinding){
                    textViewTitle.text = tvShow.data?.name
                    textViewOverview.text = tvShow.data?.overview
                    textViewReleaseDate.text = tvShow.data?.firstAirDate
                    textViewRating.text = tvShow.data?.voteAverage.toString()
                    Glide.with(this@DetailActivity)
                        .load(Constant.IMAGE_BASE_URL+tvShow.data?.posterPath)
                        .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
                        .error(R.drawable.ic_error)
                        .into(imageBackDrop)
                    val state = tvShow.data?.isFavorite == true
                    setBookmarkState(state)
                    tvShowEntity = tvShow.data!!
                }
            }
            Status.ERROR -> {
                Toast.makeText(this, "Failed get data", Toast.LENGTH_SHORT).show()
                onBackPressed()
            }
            Status.LOADING -> {
                detailBinding.progressBar.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        this.menu = menu
        when(detailType){
            EXTRA_KEY_MOVIE -> detailViewModel.getMovie(id).observe(this, {
                setViewMovie(it)
            })
            EXTRA_KEY_TV_SHOW -> detailViewModel.getTvShow(id).observe(this, {
                setViewTvShow(it)
            })
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.actionFavorite) {
            when(detailType){
                EXTRA_KEY_MOVIE -> detailViewModel.bookmarkMovie(movieEntity)
                EXTRA_KEY_TV_SHOW -> detailViewModel.bookmarkTvShow(tvShowEntity)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBookmarkState(state: Boolean) {
        if (menu == null) return
        val menuItem = menu?.findItem(R.id.actionFavorite)
        if (state) {
            menuItem?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorited_white)
        } else {
            menuItem?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_white)
        }
    }

    private fun setDetailType(detailType: String, id: Int){
        this.detailType = detailType
        this.id = id
    }
}