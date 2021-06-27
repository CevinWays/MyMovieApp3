package com.cevin.submission3.ui.favorited

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.cevin.submission3.R
import com.cevin.submission3.ui.favorited.movie.FavoritedMovieFragment
import com.cevin.submission3.ui.favorited.tvshow.FavoritedTvShowFragment

class FavoritedPagerAdapter(private val mContext: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.favorite_movie, R.string.favorite_tv_show)
    }

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment =
        when (position) {
            0 -> FavoritedMovieFragment()
            1 -> FavoritedTvShowFragment()
            else -> Fragment()
        }

    override fun getPageTitle(position: Int): CharSequence =
        mContext.resources.getString(TAB_TITLES[position])
}