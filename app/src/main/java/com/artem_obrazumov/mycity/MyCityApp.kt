package com.artem_obrazumov.mycity

import android.app.Application
import com.google.firebase.FirebaseApp

class MyCityApp: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}