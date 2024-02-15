package com.poetry.on.quotescreaterwakeel.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.poetry.on.quotescreaterwakeel.R
import com.poetry.on.quotescreaterwakeel.model.Item

class GridAdapter(private val items: List<Item>) :
    RecyclerView.Adapter<GridAdapter.ViewHolder>() {

    var onClick : ((Int)->Unit) ? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.textView.text = item.title
        holder.mainBg.setBackgroundResource(item.image)
        holder?.itemView?.setOnClickListener {
            onClick?.invoke(position)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.detail_text_view)
        val mainBg: ConstraintLayout = itemView.findViewById(R.id.mainBg)
    }
}
