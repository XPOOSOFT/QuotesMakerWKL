package com.poetry.on.quotescreaterwakeel.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.recyclerview.widget.RecyclerView
import com.poetry.on.quotescreaterwakeel.R

class DetailAdapter(private val items: List<String>) : RecyclerView.Adapter<DetailAdapter.ViewHolder>() {

    var onClick : ((String,Int)->Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.detail_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = items[position]
        holder.copy.setOnClickListener {
            onClick?.invoke(items[position],0)
        }
        holder.share.setOnClickListener {
            onClick?.invoke(items[position],1)
        }
        holder.speak.setOnClickListener {
            onClick?.invoke(items[position],2)
        }
        holder.fvrtIcon.setOnClickListener {
            onClick?.invoke(items[position],3)
        }
    }
    override fun getItemCount(): Int = items.size
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.detail_text_view)
        val copy: ImageFilterView = itemView.findViewById(R.id.copyIcon)
        val share: ImageFilterView = itemView.findViewById(R.id.shareIcon)
        val speak: ImageFilterView = itemView.findViewById(R.id.speakIcon)
        val fvrtIcon: ImageFilterView = itemView.findViewById(R.id.fvrtIcon)
    }
}
