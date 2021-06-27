package com.cevin.submission3.ui.favorited

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cevin.submission3.databinding.ActivityFavoritedBinding

class FavoritedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val favoriteBinding = ActivityFavoritedBinding.inflate(layoutInflater)
        setContentView(favoriteBinding.root)
        val favoritePagerAdapter = FavoritedPagerAdapter(this, supportFragmentManager)
        with(favoriteBinding) {
            viewPager.adapter = favoritePagerAdapter
            tabs.setupWithViewPager(viewPager)
        }
        supportActionBar?.elevation = 0F
    }
}