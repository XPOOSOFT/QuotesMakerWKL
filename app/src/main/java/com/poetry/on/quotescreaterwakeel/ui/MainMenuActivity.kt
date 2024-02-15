package com.poetry.on.quotescreaterwakeel.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

        val items = listOf(
            Item(R.drawable.image_1, "Quotes 1", "allama"),
            Item(R.drawable.image_2, "Quotes 2", "allama"),
            Item(R.drawable.image_3, "Quotes 3", "allama"),
            Item(R.drawable.image_4, "Quotes 4", "allama"),
            Item(R.drawable.image_5, "Quotes 5", "allama"),
            Item(R.drawable.image_6, "Quotes 6", "allama"),
            Item(R.drawable.image_7, "Quotes 7", "allama"),
            Item(R.drawable.image_8, "Quotes 8", "allama"),
            Item(R.drawable.image_9, "Quotes 9", "allama"),
            Item(R.drawable.image_10, "Quotes 10", "allama"),
        )

        binding?.navView?.topView?.setBackgroundResource(R.drawable.nave_menu)
        binding?.mainLayout?.recyclerViewGrid?.apply {
//            layoutManager = GridLayoutManager(this@MainMenuActivity, 2)
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
