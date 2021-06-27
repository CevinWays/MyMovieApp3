package com.cevin.submission3.ui.tvshow

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.cevin.submission3.R
import com.cevin.submission3.data.TvShow
import com.cevin.submission3.databinding.ItemTvShowBinding
import com.cevin.submission3.ui.detail.DetailActivity
import com.cevin.submission3.utils.Constant

class TvShowAdapter : RecyclerView.Adapter<TvShowAdapter.ViewHolder>() {

    private var listTvShow = ArrayList<TvShow>()

    fun setTvShow(tvShow: List<TvShow>?) {
        if (tvShow == null) return
        this.listTvShow.addAll(tvShow)
    }

    class ViewHolder(private val binding: ItemTvShowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tvShow: TvShow){
             with(binding){
                 textViewTitle.text = tvShow.title
                 textViewOverview.text = tvShow.overview
                 textViewRating.text = tvShow.voteAverage.toString()
                 textViewReleaseDate.text = tvShow.firstAirDate
                 itemView.setOnClickListener {
                     val intent = Intent(itemView.context, DetailActivity::class.java)
                     intent.putExtra(DetailActivity.EXTRA_KEY_TV_SHOW, tvShow.id)
                     itemView.context.startActivity(intent)
                 }
                 Glide.with(itemView.context)
                     .load(Constant.IMAGE_BASE_URL+tvShow.posterPath)
                     .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
                     .error(R.drawable.ic_error)
                     .into(imageViewThumbnail)
             }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemTvShowBinding = ItemTvShowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemTvShowBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tvShow = listTvShow[position]
        holder.bind(tvShow)
    }

    override fun getItemCount(): Int = listTvShow.size
}