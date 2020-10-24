package jp.co.charco.praisesreminder.data.datastore

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore

class SettingDataStore(private val dataStore: DataStore<Preferences>) {

    companion object {
        private const val DATA_STORE_NAME = "settings"

        fun createInstance(context: Context): SettingDataStore {
            val dataStore = context.createDataStore(name = DATA_STORE_NAME)
            return SettingDataStore(dataStore)
        }
    }
}