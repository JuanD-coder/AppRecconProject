package com.rojasdev.apprecconproject.legacy.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R

class adapterRvCancelCollection(
        private var items:List<com.rojasdev.apprecconproject.legacy.data.dataModel.collectorCollection>
    ) : RecyclerView.Adapter<com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderCancelCollection>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderCancelCollection {
        return _root_ide_package_.com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderCancelCollection(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_collection_cancel, parent, false)
        )
    }

    override fun onBindViewHolder(holder: com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderCancelCollection, position: Int) {
        val item = items[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = items.size
}