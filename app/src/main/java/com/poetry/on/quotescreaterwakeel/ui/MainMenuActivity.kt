package com.poetry.on.quotescreaterwakeel.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.poetry.on.quotescreaterwakeel.R
import com.poetry.on.quotescreaterwakeel.adapter.GridAdapter
import com.poetry.on.quotescreaterwakeel.databinding.FragmentMainMenuActivityBinding
import com.poetry.on.quotescreaterwakeel.model.Item

class MainMenuActivity : AppCompatActivity() {

    private var adapterG: GridAdapter? = null
    private var binding: FragmentMainMenuActivityBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMainMenuActivityBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        Glide.with(this).load(R.drawable.nave_menu)
            .into(binding?.mainLayout?.topLay?.navMenu ?: return)
        val items = listOf(
            Item(R.drawable.image_1, "Motivational Quotes", "allama"),
            Item(R.drawable.image_2, "Love Quotes", "allama"),
            Item(R.drawable.image_3, "Success Quotes", "allama"),
            Item(R.drawable.image_4, "Funny Quotes", "allama"),
            Item(R.drawable.image_5, "Positive Quotes", "allama"),
            Item(R.drawable.image_6, "Life Quotes", "allama"),
            Item(R.drawable.image_7, "Birthday Wish Text", "allama"),
            Item(R.drawable.image_8, "Inspirational Quotes", "allama"),
            Item(R.drawable.image_9, "Short Love Letter", "allama"),
            Item(R.drawable.image_10, "Say Sorry", "allama"),
        )

        binding?.mainLayout?.topLay?.navMenu?.setOnClickListener {

        }
        binding?.navView?.topView?.setOnClickListener { }
        binding?.mainLayout?.recyclerViewGrid?.apply {
            adapterG = GridAdapter(items)
            adapter = adapterG
            adapterG?.onClick = {
                val selectedItem = items[it].file
                val intent = Intent(this@MainMenuActivity, DetailActivity::class.java).apply {
                    putExtra("item", selectedItem)
                }
                startActivity(intent)

            }
        }

    }
}
