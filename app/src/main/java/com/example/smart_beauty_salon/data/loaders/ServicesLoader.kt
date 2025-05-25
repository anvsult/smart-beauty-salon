package com.example.smart_beauty_salon.data.loaders

import android.content.Context
import com.example.smart_beauty_salon.data.model.Service
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

object ServicesLoader {
    private const val FILENAME = "services.json"

    fun loadAll(context: Context): List<Service> {
        return try {
            val jsonString = context.assets.open(FILENAME).bufferedReader().use { it.readText() }
            Gson().fromJson(jsonString, object : TypeToken<List<Service>>() {}.type)
        } catch (e: IOException) {
            emptyList()
        }
    }
}