package com.cevin.submission3.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.cevin.submission3.R
import com.cevin.submission3.databinding.ActivityHomeBinding
import com.cevin.submission3.ui.favorited.FavoritedActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(activityHomeBinding.root)
        val sectionsPagerAdapter = SectionPagerAdapter(this, supportFragmentManager)
        with(activityHomeBinding) {
            viewPager.adapter = sectionsPagerAdapter
            tabs.setupWithViewPager(viewPager)
        }
        supportActionBar?.elevation = 0F
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionGoToFavorite -> {
                val intent = Intent(this, FavoritedActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}