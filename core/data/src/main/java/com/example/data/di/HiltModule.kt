package com.example.data.di

import android.content.Context
import com.example.data.repositoty.FireStore
import com.example.domain.repository.FireStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {

    @Singleton
    @Provides
    fun provideFireStoreRepository(
        context: Context
    ): FireStoreRepository {
        return FireStore(context)
    }
}