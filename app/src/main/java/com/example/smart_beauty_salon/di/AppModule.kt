package com.example.smart_beauty_salon.di

import android.content.Context
import androidx.room.Room
import com.example.smart_beauty_salon.data.AppDatabase
import com.example.smart_beauty_salon.data.dao.AppointmentDao
import com.example.smart_beauty_salon.data.dao.CustomerPreferencesDao
import com.example.smart_beauty_salon.data.dao.ServiceDao
import com.example.smart_beauty_salon.repository.AppointmentsRepository
import com.example.smart_beauty_salon.repository.PreferencesRepository
import com.example.smart_beauty_salon.repository.ServicesRepository
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApplicationScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    @Provides
    @Singleton
    fun providePopulateDbCallback(
        @ApplicationContext context: Context,
        scope: CoroutineScope
    ): AppDatabase.Companion.PopulateDbCallback {
        return AppDatabase.Companion.PopulateDbCallback(context, scope)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        populateDbCallback: AppDatabase.Companion.PopulateDbCallback
    ): AppDatabase {
        val db = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "smart_beauty_salon_db" // Changed DB name back to be consistent
        )
            .fallbackToDestructiveMigration(true)
            .addCallback(populateDbCallback)
            .build()

        populateDbCallback.setAppDatabaseProvider(Lazy { db })
        return db
    }

    @Provides
    @Singleton
    fun provideServiceDao(db: AppDatabase): ServiceDao = db.serviceDao()

    @Provides
    @Singleton
    fun provideAppointmentDao(db: AppDatabase): AppointmentDao = db.appointmentDao()

    @Provides
    @Singleton
    fun provideCustomerPreferencesDao(db: AppDatabase): CustomerPreferencesDao = db.customerPreferencesDao()

    @Provides
    @Singleton
    fun provideServicesRepository(serviceDao: ServiceDao): ServicesRepository {
        return ServicesRepository(serviceDao)
    }

    @Provides
    @Singleton
    fun provideAppointmentsRepository(appointmentDao: AppointmentDao): AppointmentsRepository {
        return AppointmentsRepository(appointmentDao)
    }

    @Provides
    @Singleton
    fun providePreferencesRepository(customerPreferenceDao: CustomerPreferencesDao): PreferencesRepository {
        return PreferencesRepository(customerPreferenceDao)
    }
}