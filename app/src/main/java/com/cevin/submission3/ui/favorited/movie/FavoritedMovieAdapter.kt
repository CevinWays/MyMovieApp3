package com.cevin.submission3.ui.favorited.movie

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.cevin.submission3.R
import com.cevin.submission3.data.source.local.entity.MovieEntity
import com.cevin.submission3.databinding.ItemMovieBinding
import com.cevin.submission3.ui.detail.DetailActivity
import com.cevin.submission3.utils.Constant

class FavoritedMovieAdapter: PagedListAdapter<MovieEntity, FavoritedMovieAdapter.ViewHolder>(
    DIFF_CALLBACK
) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieEntity>() {
            override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: MovieEntity){
            with(binding){
                textViewTitle.text = movie.title
                textViewOverview.text = movie.overview
                textViewRating.text = movie.voteAverage.toString()
                textViewReleaseDate.text = movie.releaseDate
                Glide.with(itemView.context)
                    .load(Constant.IMAGE_BASE_URL+movie.posterPath)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
                    .error(R.drawable.ic_error)
                    .into(imageViewThumbnail)
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_KEY_MOVIE, movie.id)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemMovieBinding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemMovieBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    fun getSwipedData(swipedPosition: Int): MovieEntity? = getItem(swipedPosition)
}