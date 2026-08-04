package com.rojasdev.apprecconproject.legacy.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R

class acapterItemDateWork(
    private var itemDetail: List<com.rojasdev.apprecconproject.legacy.data.dataModel.allWorkAndCollector>
) :RecyclerView.Adapter<com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderItemDatesWork>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderItemDatesWork {
        return com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderItemDatesWork(
            LayoutInflater.from(parent.context).inflate(R.layout.item_dates, parent, false)
        )
    }

    override fun onBindViewHolder(holder: com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderItemDatesWork, position: Int) {
        val item = itemDetail[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = itemDetail.size

}