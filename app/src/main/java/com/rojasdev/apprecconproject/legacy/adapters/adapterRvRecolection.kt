package com.rojasdev.apprecconproject.legacy.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R

class adapterRvRecolection(
    private var item: List<com.rojasdev.apprecconproject.legacy.data.dataModel.collectorCollection>,
    private var onClickListenerUpdate: (com.rojasdev.apprecconproject.legacy.data.dataModel.collectorCollection) -> Unit
        ):RecyclerView.Adapter<com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderCvRecollection>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderCvRecollection {
                return _root_ide_package_.com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderCvRecollection(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_rv_recolection, parent, false)
                )
        }

        override fun onBindViewHolder(holder: com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderCvRecollection, position: Int) {
                val item = item[position]
                holder.render(item,onClickListenerUpdate)
        }

        override fun getItemCount(): Int = item.size

}