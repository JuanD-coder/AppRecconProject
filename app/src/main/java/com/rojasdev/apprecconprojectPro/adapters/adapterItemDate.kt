package com.rojasdev.apprecconprojectPro.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconprojectPro.R
import com.rojasdev.apprecconprojectPro.data.dataModel.allCollecionAndCollector
import com.rojasdev.apprecconprojectPro.viewHolders.viewHolderItemDate

class adapterItemDate(
        private var itemDetail: List<allCollecionAndCollector>
    ) :RecyclerView.Adapter<viewHolderItemDate>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolderItemDate {
        return viewHolderItemDate(LayoutInflater.from(parent.context).inflate(R.layout.item_rv_all_recolection_date, parent, false))
    }

    override fun onBindViewHolder(holder: viewHolderItemDate, position: Int) {
        val item = itemDetail[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = itemDetail.size
}