package com.cevin.submission3.di

import com.cevin.submission3.utils.AppExecutors
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppExeModule {
    @Provides
    @Singleton
    fun provideAppExecutor(): AppExecutors = AppExecutors()
}