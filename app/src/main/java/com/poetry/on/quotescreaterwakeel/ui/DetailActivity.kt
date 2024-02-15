package com.poetry.on.quotescreaterwakeel.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.poetry.on.quotescreaterwakeel.adapter.DetailAdapter
import com.poetry.on.quotescreaterwakeel.databinding.ActivityDetailBinding
import com.poetry.on.quotescreaterwakeel.model.Item
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

        GlobalScope.launch(Dispatchers.IO) {
            readTextFileFromAssets(item?:return@launch)
//            readTextFileFromAssets("allama")
        }
    }

    private fun readTextFileFromAssets(file : String) {
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
                binding?.recyclerViewLinear?.layoutManager = LinearLayoutManager(this@DetailActivity)
                binding?.recyclerViewLinear?.adapter = textAdapter
//                textAdapter?.notifyDataSetChanged()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
