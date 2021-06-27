package io.github.wparks.androidApp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.wparks.shared.AppContainer
import io.github.wparks.shared.data.ParkRepository
import io.github.wparks.shared.data.db.DbContainer
import io.github.wparks.shared.data.db.DriverFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideDatabaseContainer(@ApplicationContext applicationContext: Context): DbContainer {
        return DbContainer(DriverFactory(applicationContext))
    }

    @Provides
    @Singleton
    fun provideAppContainer(dbContainer: DbContainer): AppContainer {
        return AppContainer(dbContainer)
    }

    @Provides
    @Singleton
    fun provideParkRepository(appContainer: AppContainer): ParkRepository {
        return appContainer.repository
    }

}