package com.example.smart_beauty_salon

import android.app.Application
import com.example.smart_beauty_salon.repository.ServicesRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class SmartSalonApp  : Application()
