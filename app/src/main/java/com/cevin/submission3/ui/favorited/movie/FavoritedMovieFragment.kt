package com.cevin.submission3.ui.favorited.movie

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
import com.cevin.submission3.data.source.local.entity.MovieEntity
import com.cevin.submission3.databinding.FragmentFavoritedMovieBinding
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class FavoritedMovieFragment: Fragment() {

    @Inject
    lateinit var viewModel: FavoriteMovieViewModel
    private lateinit var movieAdapter: FavoritedMovieAdapter
    private lateinit var favoritedMovieBinding: FragmentFavoritedMovieBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemTouchHelper.attachToRecyclerView(favoritedMovieBinding.rvFavoriteMovie)
        movieAdapter = FavoritedMovieAdapter()
        with(favoritedMovieBinding.rvFavoriteMovie) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = movieAdapter
            getMovie()
        }
    }


    private fun getMovie(){
        viewModel.getFavoriteMovie().observeForever {
            movieAdapter.submitList(it)
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
        favoritedMovieBinding = FragmentFavoritedMovieBinding.inflate(inflater, container, false)
        return favoritedMovieBinding.root
    }

    private val itemTouchHelper = ItemTouchHelper(object: ItemTouchHelper.Callback(){
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int = makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            if(view != null) {
                val swipedPosition = viewHolder.bindingAdapterPosition
                val movieEntity = movieAdapter.getSwipedData(swipedPosition)
                movieEntity?.let {
                    setFavoriteMovie(it)
                }

                val snackbar = Snackbar.make(view as View, R.string.message_undo, Snackbar.LENGTH_LONG)
                snackbar.setAction(R.string.message_ok) { v ->
                    movieEntity?.let {
                        setFavoriteMovie(it)
                    }
                }
                snackbar.show()
            }
        }
    })

    private fun setFavoriteMovie(movieEntity: MovieEntity) {
        viewModel.setFavoriteMovie(movieEntity)
    }
}