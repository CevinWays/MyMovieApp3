package com.cevin.submission3.ui.favorited.tvshow

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cevin.submission3.App
import com.cevin.submission3.R
import com.cevin.submission3.data.source.local.entity.TvShowEntity
import com.cevin.submission3.databinding.FragmentFavoritedTvShowBinding
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class FavoritedTvShowFragment : Fragment() {

    @Inject
    lateinit var viewModel: FavoritedTvShowViewModel
    private lateinit var tvShowAdapter: FavoritedTvShowAdapter
    private lateinit var favoritedTvShowBinding: FragmentFavoritedTvShowBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemTouchHelper.attachToRecyclerView(favoritedTvShowBinding.rvFavoriteTvShow)
        tvShowAdapter = FavoritedTvShowAdapter()
        with(favoritedTvShowBinding.rvFavoriteTvShow) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = tvShowAdapter
            getMovie()
        }
    }


    private fun getMovie(){
        viewModel.getFavoriteTvShow().observeForever {
            tvShowAdapter.submitList(it)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.applicationContext as App).movieComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        favoritedTvShowBinding = FragmentFavoritedTvShowBinding.inflate(inflater, container, false)
        return favoritedTvShowBinding.root
    }

    private val itemTouchHelper = ItemTouchHelper(object: ItemTouchHelper.Callback(){
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int = makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            if(view != null) {
                val swipedPosition = viewHolder.bindingAdapterPosition
                val tvShowEntity = tvShowAdapter.getSwipedData(swipedPosition)
                tvShowEntity?.let {
                    setFavoriteTv(it)
                }

                val snackbar = Snackbar.make(view as View, R.string.message_undo, Snackbar.LENGTH_LONG)
                snackbar.setAction(R.string.message_ok) { _ ->
                    tvShowEntity?.let {
                        setFavoriteTv(it)
                    }
                }
                snackbar.show()

            }
        }
    })

    private fun setFavoriteTv(tvShowEntity: TvShowEntity) {
        viewModel.setFavoriteTv(tvShowEntity)
    }
}