package org.wit.shop19.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.shop19.models.PlacemarkJSONStore
import org.wit.shop19.models.PlacemarkStore

class MainApp : Application(), AnkoLogger {

    lateinit var placemarks: PlacemarkStore

    override fun onCreate() {
        super.onCreate()
        placemarks = PlacemarkJSONStore(applicationContext)
        info("Placemark Application started")
    }
}