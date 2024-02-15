package com.poetry.on.quotescreaterwakeel

import android.app.Application
import android.util.Log
import com.poetry.on.quotescreaterwakeel.utilities.id_app_open_screen
import com.poetry.on.quotescreaterwakeel.utilities.val_ad_app_open_screen

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Log.d("application_class", "onCreate")
        if (val_ad_app_open_screen) {
            AdOpenApp(this, id_app_open_screen)
        }
    }
}