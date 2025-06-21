package com.rojasdev.apprecconproject.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.data.dataModel.collectorCollection
import com.rojasdev.apprecconproject.data.dataModel.workSettings
import com.rojasdev.apprecconproject.data.entities.WorkEntity
import com.rojasdev.apprecconproject.viewHolders.viewHolderCvRecollection
import com.rojasdev.apprecconproject.viewHolders.viewHolderCvWork

class adapterRvWork(
    private var item: List<workSettings>,
    private var onClickListenerUpdate: (workSettings) -> Unit
):RecyclerView.Adapter<viewHolderCvWork>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolderCvWork {
        return viewHolderCvWork(LayoutInflater.from(parent.context).inflate(R.layout.item_rv_recolection, parent, false))
    }

    override fun onBindViewHolder(holder: viewHolderCvWork, position: Int) {
        val item = item[position]
        holder.render(item,onClickListenerUpdate)
    }

    override fun getItemCount(): Int = item.size

}