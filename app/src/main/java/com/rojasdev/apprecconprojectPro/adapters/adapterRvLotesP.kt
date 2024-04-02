package com.rojasdev.apprecconprojectPro.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconprojectPro.R
import com.rojasdev.apprecconprojectPro.data.dataModel.lotesAndCollection
import com.rojasdev.apprecconprojectPro.data.entities.LoteEntity
import com.rojasdev.apprecconprojectPro.viewHolders.viewHolderLotes
import com.rojasdev.apprecconprojectPro.viewHolders.viewHolderLotesp
import com.rojasdev.apprecconprojectPro.viewHolders.viewHolderSettings

class adapterRvLotesP (
    private var items:List<lotesAndCollection>
): RecyclerView.Adapter<viewHolderLotesp>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolderLotesp {
        return viewHolderLotesp(LayoutInflater.from(parent.context).inflate(R.layout.item_lotes_production, parent,false))
    }

    override fun onBindViewHolder(holder: viewHolderLotesp, position: Int) {
        val item = items[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = items.size
}