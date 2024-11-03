package com.example.project.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.example.project.ILocalService
import com.example.project.LocalService

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {

    @Singleton
    @Provides
    fun provideLocalService(): ILocalService {
        return LocalService(
            onStart = {},
            onStop = {}
        )
    }
}