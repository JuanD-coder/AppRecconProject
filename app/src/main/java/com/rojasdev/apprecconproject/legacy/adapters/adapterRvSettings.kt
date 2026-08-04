package com.rojasdev.apprecconproject.legacy.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R

class adapterRvSettings (
    private var items:List<com.rojasdev.apprecconproject.legacy.data.entities.SettingEntity>,
    private val onClickListenerNext: (com.rojasdev.apprecconproject.legacy.data.entities.SettingEntity) -> Unit ) : RecyclerView.Adapter<com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderSettings>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderSettings {
        return com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderSettings(
            LayoutInflater.from(parent.context).inflate(R.layout.item_settings, parent, false)
        )
    }

    override fun onBindViewHolder(holder: com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderSettings, position: Int) {
        val item = items[position]
        holder.render(item,onClickListenerNext)
    }

    override fun getItemCount(): Int = items.size
}