package com.poetry.on.quotescreaterwakeel.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.poetry.on.quotescreaterwakeel.R
import com.poetry.on.quotescreaterwakeel.databinding.LanguageAppItemBinding
import com.poetry.on.quotescreaterwakeel.model.LanguageModel

class AppLanguageAdapter(
    private var clickItem: ((LanguageModel) -> Unit),
    private var languageData: ArrayList<LanguageModel>,
) : RecyclerView.Adapter<AppLanguageAdapter.ViewHolder>() {
    class ViewHolder(val binding: LanguageAppItemBinding) : RecyclerView.ViewHolder(binding.root)

    var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            LanguageAppItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return languageData.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.countryName.text = languageData[position].country_name
        holder.binding.radioButton.isChecked = languageData[position].check
        if (languageData[position].check) {
            holder.binding.mainItem.setBackgroundResource(R.drawable.rect_white_lang_select)
            holder.binding.countryName.setTextColor(R.color.white)
        } else {
            holder.binding.mainItem.setBackgroundResource(R.drawable.rect_white_lang)
        }

        Glide.with(context ?: return).load(languageData[position].country_flag)
            .into(holder.binding.flagIcon)
        holder.binding.root.setOnClickListener {
            checkSingleBox(position)
            clickItem.invoke(languageData[position])
        }

        holder.binding.radioButton.setOnClickListener {
            checkSingleBox(position)
            clickItem.invoke(languageData[position])
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun selectLanguage(langPositionSelected: String) {
        for (i in languageData) {
            i.check = i.country_code == langPositionSelected
        }
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun checkSingleBox(pos: Int) {
        for (i in languageData.indices) {
            languageData[i].check = i == pos
        }
        notifyDataSetChanged()
    }


}