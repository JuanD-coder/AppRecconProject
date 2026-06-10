package com.rojasdev.apprecconproject.legacy.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R

class adapterRvPricesWork (
    private var items:List<com.rojasdev.apprecconproject.legacy.data.entities.SettingEntity>,
    private val onClickListener: (com.rojasdev.apprecconproject.legacy.data.entities.SettingEntity) -> Unit ) : RecyclerView.Adapter<com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderRvPricesWork>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderRvPricesWork {
        return _root_ide_package_.com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderRvPricesWork(
            LayoutInflater.from(parent.context).inflate(R.layout.item_price_work, parent, false)
        )
    }

    override fun onBindViewHolder(holder: com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderRvPricesWork, position: Int) {
        val item = items[position]
        holder.render(item,onClickListener)
    }

    override fun getItemCount(): Int = items.size
}