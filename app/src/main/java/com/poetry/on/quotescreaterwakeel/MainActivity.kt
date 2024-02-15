package com.poetry.on.quotescreaterwakeel

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.poetry.on.quotescreaterwakeel.databinding.MainActivityApplicationBinding

class MainActivity : AppCompatActivity() {


    private var binding: MainActivityApplicationBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityApplicationBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    override fun attachBaseContext(newBase: Context?) {

        val newOverride = Configuration(newBase?.resources?.configuration)
        newOverride.fontScale = 1.0f
        applyOverrideConfiguration(newOverride)

        super.attachBaseContext(newBase)
    }
}
