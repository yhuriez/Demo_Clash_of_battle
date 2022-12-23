package com.example.democlashofbattle

import android.app.Application
import com.example.democlashofbattle.database.AppDatabase

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        AppDatabase.init(this)
    }
}