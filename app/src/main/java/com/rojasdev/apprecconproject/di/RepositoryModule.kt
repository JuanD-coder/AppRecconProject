package com.rojasdev.apprecconproject.di

import com.rojasdev.apprecconproject.data.repository.CollectorRepository
import com.rojasdev.apprecconproject.data.repository.CollectorRepositoryImpl
import com.rojasdev.apprecconproject.data.repository.RecollectionRepository
import com.rojasdev.apprecconproject.data.repository.RecollectionRepositoryImpl
import com.rojasdev.apprecconproject.data.repository.SettingsRepository
import com.rojasdev.apprecconproject.data.repository.SettingsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCollectorRepository(
        collectorRepositoryImpl: CollectorRepositoryImpl
    ): CollectorRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        settingsRepositoryImpl: SettingsRepositoryImpl
    ): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindRecollectionRepository(
        recollectionRepositoryImpl: RecollectionRepositoryImpl
    ): RecollectionRepository
}
