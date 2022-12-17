package com.urcloset.smartangle.di

import android.content.Context
import com.example.currencyapp.data.repo.HomeRepoUser
import com.urcloset.smartangle.api.AppApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class, FragmentComponent::class)
class RepoDi {
    @Provides
    @ViewModelScoped
    fun getHomeRepoUser(webService: AppApi, @ApplicationContext context: Context
    ): HomeRepoUser {
        return  HomeRepoUser(webService,context)
    }
}