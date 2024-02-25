package com.exoapps.pickuplines.flirtymessages

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.exoapps.pickuplines.flirtymessages.utilities.id_app_open_screen
import com.exoapps.pickuplines.flirtymessages.utilities.val_ad_app_open_screen

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        Log.d("application_class", "onCreate")
        if (val_ad_app_open_screen) {
            AdOpenApp(this, id_app_open_screen)
        }
    }
}