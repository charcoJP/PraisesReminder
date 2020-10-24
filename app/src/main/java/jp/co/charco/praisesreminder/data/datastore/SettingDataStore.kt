package jp.co.charco.praisesreminder.data.datastore

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingDataStore(private val dataStore: DataStore<Preferences>) {

    val isAlarmInitialized: Flow<Boolean> = dataStore.data.map {
        it[KEY_IS_ALARM_INITIALIZED] ?: false
    }

    suspend fun updateIsAlarmInitialized(value: Boolean) {
        dataStore.edit { it[KEY_IS_ALARM_INITIALIZED] = value }
    }

    companion object {
        private const val DATA_STORE_NAME = "settings"
        private val KEY_IS_ALARM_INITIALIZED = preferencesKey<Boolean>("KEY_IS_ALARM_INITIALIZED")

        fun createInstance(context: Context): SettingDataStore {
            val dataStore = context.createDataStore(name = DATA_STORE_NAME)
            return SettingDataStore(dataStore)
        }
    }
}