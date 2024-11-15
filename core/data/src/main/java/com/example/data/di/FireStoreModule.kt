package com.example.data.di

import android.content.Context
import com.example.data.repository.FireStore
import com.example.domain.repository.FireStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FireStoreModule {

    @Singleton
    @Provides
    fun provideFireStoreRepository(
        @ApplicationContext context: Context
    ): FireStoreRepository {
        return FireStore(context)
    }
}