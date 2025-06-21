package com.rojasdev.apprecconproject.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.data.dataModel.workMen
import com.rojasdev.apprecconproject.data.dataModel.workTotalCollector
import com.rojasdev.apprecconproject.viewHolders.viewHolderCancelWork

class adapterRvCancelWork(
    private var items:List<workMen>
) : RecyclerView.Adapter<viewHolderCancelWork>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolderCancelWork {
        return viewHolderCancelWork(LayoutInflater.from(parent.context).inflate(R.layout.item_collection_cancel,parent,false))
    }

    override fun onBindViewHolder(holder: viewHolderCancelWork, position: Int) {
        val item = items[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = items.size
}