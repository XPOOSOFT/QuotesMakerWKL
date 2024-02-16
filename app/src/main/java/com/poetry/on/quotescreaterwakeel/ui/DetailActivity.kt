package com.poetry.on.quotescreaterwakeel.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.poetry.on.quotescreaterwakeel.R
import com.poetry.on.quotescreaterwakeel.adapter.DetailAdapter
import com.poetry.on.quotescreaterwakeel.databinding.ActivityDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

class DetailActivity : AppCompatActivity() {

    private var binding: ActivityDetailBinding? = null
    private var textAdapter: DetailAdapter? = null
    private val textList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val item = intent.getStringExtra("item")
        Glide.with(this).load(R.drawable.back_btn).into(binding?.topLay?.navMenu?:return)
        binding?.topLay?.title?.text = item
        GlobalScope.launch(Dispatchers.IO) {
            readTextFileFromAssets(item ?: return@launch)
        }
    }

    private fun readTextFileFromAssets(file: String) {
        try {
            val inputStream = assets.open("$file.txt")
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                textList.add(line.orEmpty())
            }
            inputStream.close()
            reader.close()

            GlobalScope.launch(Dispatchers.Main) {
                textAdapter = DetailAdapter(textList)
                binding?.recyclerViewLinear?.layoutManager =
                    LinearLayoutManager(this@DetailActivity)
                binding?.recyclerViewLinear?.adapter = textAdapter
//                textAdapter?.notifyDataSetChanged()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
