package com.rojasdev.apprecconproject.legacy.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R

class adapterRvWork(
    private var item: List<com.rojasdev.apprecconproject.legacy.data.dataModel.workSettings>,
    private var onClickListenerUpdate: (com.rojasdev.apprecconproject.legacy.data.dataModel.workSettings) -> Unit
):RecyclerView.Adapter<com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderCvWork>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderCvWork {
        return com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderCvWork(
            LayoutInflater.from(parent.context).inflate(R.layout.item_rv_recolection, parent, false)
        )
    }

    override fun onBindViewHolder(holder: com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderCvWork, position: Int) {
        val item = item[position]
        holder.render(item,onClickListenerUpdate)
    }

    override fun getItemCount(): Int = item.size

}