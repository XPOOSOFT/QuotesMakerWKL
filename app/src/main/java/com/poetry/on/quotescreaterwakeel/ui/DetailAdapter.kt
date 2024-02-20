package com.poetry.on.quotescreaterwakeel.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.poetry.on.quotescreaterwakeel.R

class DetailAdapter(private val items: List<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_CONTENT = 0
        private const val VIEW_TYPE_AD = 1
        private const val ITEMS_PER_AD = 5
    }

    var onClick: ((String, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_AD) {
            val adView = AdView(parent.context)
            adView.setAdSize(AdSize.BANNER)
            adView.adUnitId = "ca-app-pub-3940256099942544/2014213617" // Replace with your Ad Unit ID
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            adView.layoutParams = ViewGroup.LayoutParams(width, height)
            AdViewHolder(adView)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.detail_item, parent, false)
            ItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_CONTENT) {
            val itemHolder = holder as ItemViewHolder
            val itemPosition = getOriginalPosition(position)
            itemHolder.bind(items[itemPosition])
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
        } else {
            val adHolder = holder as AdViewHolder
            val adRequest = AdRequest.Builder().build()
            adHolder.adView.loadAd(adRequest)
        }
    }

    override fun getItemCount(): Int {
        val adCount = items.size / ITEMS_PER_AD
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return if ((position + 1) % (ITEMS_PER_AD + 1) == 0) {
            VIEW_TYPE_AD
        } else {
            VIEW_TYPE_CONTENT
        }
    }

    private fun getOriginalPosition(adapterPosition: Int): Int {
        return adapterPosition - adapterPosition / (ITEMS_PER_AD + 1)
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.detail_text_view)
        val copy: ImageFilterView = itemView.findViewById(R.id.copyIcon)
        val share: ImageFilterView = itemView.findViewById(R.id.shareIcon)
        val speak: ImageFilterView = itemView.findViewById(R.id.speakIcon)
        val fvrtIcon: ImageFilterView = itemView.findViewById(R.id.fvrtIcon)

        fun bind(item: String) {
            textView.text = item
        }
    }

    inner class AdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val adView: AdView = itemView as AdView
    }
}
