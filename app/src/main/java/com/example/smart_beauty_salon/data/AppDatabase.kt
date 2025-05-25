package com.example.smart_beauty_salon.data

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.smart_beauty_salon.data.converters.UUIDConverter
import com.example.smart_beauty_salon.data.dao.AppointmentDao
import com.example.smart_beauty_salon.data.dao.CustomerPreferencesDao
import com.example.smart_beauty_salon.data.dao.ServiceDao
import com.example.smart_beauty_salon.data.model.Appointment
import com.example.smart_beauty_salon.data.model.CustomerPreferences
import com.example.smart_beauty_salon.data.model.Service
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.UUID

@Database(
    entities = [Service::class, CustomerPreferences::class, Appointment::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(UUIDConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun serviceDao(): ServiceDao
    abstract fun customerPreferencesDao(): CustomerPreferencesDao
    abstract fun appointmentDao(): AppointmentDao

    companion object {
        class PopulateDbCallback(
            private val context: Context,
            private val scope: CoroutineScope,
            private var appDatabaseProvider: dagger.Lazy<AppDatabase>? = null
        ) : RoomDatabase.Callback() {

            fun setAppDatabaseProvider(provider: dagger.Lazy<AppDatabase>) {
                appDatabaseProvider = provider
            }

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                appDatabaseProvider?.get()?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(
                            context,
                            database.serviceDao(),
                            database.customerPreferencesDao()
                        )
                    }
                }
            }
        }

        private suspend fun populateInitialData(
            context: Context,
            serviceDao: ServiceDao,
            preferencesDao: CustomerPreferencesDao
        ) {
            val json = JsonUtils.loadJsonFromAssets(context, "services.json")
            val gson = Gson()
            val type = object : TypeToken<List<Service>>() {}.type
            val services: List<Service> = gson.fromJson(json, type)
            // Assign random UUIDs to services missing an ID (or always override)
            val safeServices = services.map {
                it.copy(id = UUID.randomUUID())
            }
            safeServices.forEach { serviceDao.insert(it) }
            preferencesDao.savePreferences(CustomerPreferences())
        }
    }
}

object JsonUtils {
    fun loadJsonFromAssets(context: Context, fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }

    inline fun <reified T> parseJson(json: String): T {
        val gson = Gson()
        return gson.fromJson(json, T::class.java)
    }
}