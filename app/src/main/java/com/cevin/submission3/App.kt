package com.cevin.submission3

import android.app.Application
import com.cevin.submission3.di.DaggerMovieComponent
import com.cevin.submission3.di.MovieComponent
import com.cevin.submission3.di.RoomModule

class App : Application() {
    lateinit var movieComponent: MovieComponent

    override fun onCreate() {
        super.onCreate()
        movieComponent = DaggerMovieComponent
            .builder()
            .roomModule(RoomModule(this))
            .build()
    }
}