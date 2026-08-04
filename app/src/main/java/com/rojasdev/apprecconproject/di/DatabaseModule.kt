package com.rojasdev.apprecconproject.di

import android.content.Context
import com.rojasdev.apprecconproject.legacy.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.legacy.data.dao.RecolectoresDao
import com.rojasdev.apprecconproject.legacy.data.dao.RecollectionDao
import com.rojasdev.apprecconproject.legacy.data.dao.SettingDao
import com.rojasdev.apprecconproject.legacy.data.dao.WorkDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDataBase {
        return AppDataBase.getInstance(context)
    }

    @Provides
    fun provideRecolectoresDao(db: AppDataBase): RecolectoresDao {
        return db.RecolectoresDao()
    }

    @Provides
    fun provideSettingDao(db: AppDataBase): SettingDao {
        return db.SettingDao()
    }

    @Provides
    fun provideRecollectionDao(db: AppDataBase): RecollectionDao {
        return db.RecollectionDao()
    }

    @Provides
    fun provideWorkDao(db: AppDataBase): WorkDao {
        return db.WorkDao()
    }
}
