package com.rojasdev.apprecconproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.data.dataModel.GeneratedReport
import com.rojasdev.apprecconproject.databinding.ItemReportBinding

class ReportsAdapter(
    private val reports: List<GeneratedReport>,
    private val onClick: (GeneratedReport) -> Unit
) : RecyclerView.Adapter<viewHolderReport>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): viewHolderReport {
        return viewHolderReport(
            LayoutInflater.from(parent.context).inflate(R.layout.item_report, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: viewHolderReport,
        position: Int
    ) {
        val item = reports[position]
        holder.render(item, onClick)
    }

    override fun getItemCount() = reports.size
}

class viewHolderReport(var view: View) : RecyclerView.ViewHolder(view) {
    val binding = ItemReportBinding.bind(view)

    fun render(
        item: GeneratedReport,
        onClickListenerNext: (GeneratedReport) -> Unit,
    ) {
        binding.tvReportTitle.text = item.name
        binding.tvReportDate.text = item.date
        binding.btnOpen.setOnClickListener {
            onClickListenerNext(item)
        }
    }
}
