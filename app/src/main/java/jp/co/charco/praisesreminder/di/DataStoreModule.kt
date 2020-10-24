package jp.co.charco.praisesreminder.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.co.charco.praisesreminder.data.datastore.SettingDataStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideSettingDataStore(application: Application): SettingDataStore {
        return SettingDataStore.createInstance(application)
    }
}