package com.rojasdev.apprecconprojectPro.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import com.rojasdev.apprecconprojectPro.R
import com.rojasdev.apprecconprojectPro.data.entities.LoteEntity
import com.rojasdev.apprecconprojectPro.databinding.ItemLotesBinding
import com.rojasdev.apprecconprojectPro.databinding.ItemSpinerBinding

class adapterSpiner(val context:Context, val list: List<LoteEntity>) : BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return list[position].id!!.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            val binding = ItemSpinerBinding.bind(
                LayoutInflater.from(context).inflate(
                    R.layout.item_spiner,
                    parent,
                    false
                )
            )
        binding.tvName.text = list[position].name


        return binding.root
    }

}