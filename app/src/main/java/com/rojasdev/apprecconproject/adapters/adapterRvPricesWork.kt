package com.rojasdev.apprecconproject.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.data.entities.SettingEntity
import com.rojasdev.apprecconproject.viewHolders.viewHolderRvPricesWork

class adapterRvPricesWork (
    private var items:List<SettingEntity>,
    private val onClickListener: (SettingEntity) -> Unit ) : RecyclerView.Adapter<viewHolderRvPricesWork>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolderRvPricesWork {
        return viewHolderRvPricesWork(LayoutInflater.from(parent.context).inflate(R.layout.item_price_work, parent,false))
    }

    override fun onBindViewHolder(holder: viewHolderRvPricesWork, position: Int) {
        val item = items[position]
        holder.render(item,onClickListener)
    }

    override fun getItemCount(): Int = items.size
}