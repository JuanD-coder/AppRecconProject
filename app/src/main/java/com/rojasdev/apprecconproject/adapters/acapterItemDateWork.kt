package com.rojasdev.apprecconproject.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.data.dataModel.allWorkAndCollector
import com.rojasdev.apprecconproject.viewHolders.viewHolderItemDatesWork

class acapterItemDateWork(
    private var itemDetail: List<allWorkAndCollector>
) :RecyclerView.Adapter<viewHolderItemDatesWork>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolderItemDatesWork {
        return viewHolderItemDatesWork(
            LayoutInflater.from(parent.context).inflate(R.layout.item_dates, parent, false)
        )
    }

    override fun onBindViewHolder(holder: viewHolderItemDatesWork, position: Int) {
        val item = itemDetail[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = itemDetail.size

}