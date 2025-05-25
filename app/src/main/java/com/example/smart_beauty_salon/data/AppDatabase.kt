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

@Database(
    entities = [Service::class, CustomerPreferences::class, Appointment::class],
    version = 1, // Set to version 1
    exportSchema = false
)
@TypeConverters(UUIDConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun serviceDao(): ServiceDao
    abstract fun customerPreferencesDao(): CustomerPreferencesDao
    abstract fun appointmentDao(): AppointmentDao

    // The AppDatabaseCallback is now typically defined and added in your Hilt AppModule
    // when you build the database instance.
    // If you keep it here, it needs to be accessible to the AppModule.
    // For simplicity with Hilt, often the populate logic is moved or handled differently.
    // However, if you want to keep the callback structure for population via Hilt's builder:
    companion object {
        // This callback will be instantiated and used by Hilt's Room.databaseBuilder()
        class PopulateDbCallback(
            private val scope: CoroutineScope,
            // We need a way to get DAO instances if populateDatabase needs them.
            // This is tricky if the DB instance isn't fully available yet.
            // A common workaround is to inject a Provider<AppDatabase> into the callback,
            // but that requires the callback itself to be a Hilt-injected component,
            // which is more complex for a simple Room.Callback.

            // Alternative: The callback gets the `context` and can use it to get
            // a fresh instance of the DB to then get DAOs IF the DB is already built.
            // For `onCreate`, the DB is being built.
            // Let's assume for now populateDatabase can be called slightly differently
            // or the DAOs can be passed in if the AppDatabase instance is available at the time of callback creation.
            // For now, the callback won't directly call populateDatabase with DAOs from an INSTANCE.
            private var appDatabaseProvider: dagger.Lazy<AppDatabase>? = null // For lazy DAO access
        ) : RoomDatabase.Callback() {

            // This method allows Hilt to set the provider after the DB is created
            // This is a bit of a workaround to bridge Hilt's DI with Room's callback.
            fun setAppDatabaseProvider(provider: dagger.Lazy<AppDatabase>) {
                appDatabaseProvider = provider
            }

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                appDatabaseProvider?.get()?.let { databaseInstance ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(
                            databaseInstance.serviceDao(),
                            databaseInstance.customerPreferencesDao()
                        )
                    }
                }
            }

            // This makes the populate function independent of a static INSTANCE
            private suspend fun populateInitialData(
                serviceDao: ServiceDao,
                preferencesDao: CustomerPreferencesDao
            ) {
                serviceDao.insert(Service(name = "Haircut", description = "Standard haircut and style", price = 25.0, duration = 30))
                serviceDao.insert(Service(name = "Manicure", description = "Nail shaping, cuticle care, and polish", price = 30.0, duration = 45))
                serviceDao.insert(Service(name = "Facial", description = "Cleansing and rejuvenating facial treatment", price = 50.0, duration = 60))
                serviceDao.insert(Service(name = "Hair Coloring", description = "Full hair coloring service", price = 70.0, duration = 90))
                preferencesDao.savePreferences(CustomerPreferences())
            }
        }
    }
}