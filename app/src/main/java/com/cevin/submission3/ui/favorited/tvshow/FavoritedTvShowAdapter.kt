package com.cevin.submission3.ui.favorited.tvshow

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.cevin.submission3.R
import com.cevin.submission3.data.source.local.entity.TvShowEntity
import com.cevin.submission3.databinding.ItemTvShowBinding
import com.cevin.submission3.ui.detail.DetailActivity
import com.cevin.submission3.utils.Constant

class FavoritedTvShowAdapter :
    PagedListAdapter<TvShowEntity, FavoritedTvShowAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TvShowEntity>() {
            override fun areItemsTheSame(oldItem: TvShowEntity, newItem: TvShowEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TvShowEntity, newItem: TvShowEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ViewHolder(private val binding: ItemTvShowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tvShow: TvShowEntity) {
            with(binding) {
                textViewTitle.text = tvShow.name
                textViewOverview.text = tvShow.overview
                textViewRating.text = tvShow.voteAverage.toString()
                textViewReleaseDate.text = tvShow.firstAirDate
                Glide.with(itemView.context)
                    .load(Constant.IMAGE_BASE_URL + tvShow.posterPath)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
                    .error(R.drawable.ic_error)
                    .into(imageViewThumbnail)
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_KEY_TV_SHOW, tvShow.id)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemTvShowBinding =
            ItemTvShowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemTvShowBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    fun getSwipedData(swipedPosition: Int): TvShowEntity? = getItem(swipedPosition)
}