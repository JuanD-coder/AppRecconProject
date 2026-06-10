package com.rojasdev.apprecconproject.legacy.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R

class adapterItemDate(
        private var itemDetail: List<com.rojasdev.apprecconproject.legacy.data.dataModel.allCollecionAndCollector>
    ) :RecyclerView.Adapter<com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderItemDate>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderItemDate {
        return _root_ide_package_.com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderItemDate(
            LayoutInflater.from(parent.context).inflate(R.layout.item_dates, parent, false)
        )
    }

    override fun onBindViewHolder(holder: com.rojasdev.apprecconproject.legacy.viewHolders.viewHolderItemDate, position: Int) {
        val item = itemDetail[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = itemDetail.size
}