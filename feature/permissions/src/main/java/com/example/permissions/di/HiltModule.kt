package com.example.permissions.di

import com.example.permissions.data.repository.PermissionRepositoryImpl
import com.example.permissions.domain.repository.PermissionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class HiltModule {

    @Binds
    @Singleton
    abstract fun bindPermissionRepository(
        permissionRepositoryImpl: PermissionRepositoryImpl
    ): PermissionRepository
}