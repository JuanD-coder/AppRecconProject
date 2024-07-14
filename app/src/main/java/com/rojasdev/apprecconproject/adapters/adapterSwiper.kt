package com.rojasdev.apprecconproject.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.controller.price
import com.rojasdev.apprecconproject.data.entities.SettingEntity
import com.rojasdev.apprecconproject.databinding.ItemSpinerBinding

class adapterSwiper(val context: Context, val list: List<SettingEntity>) : BaseAdapter() {

    private var selectedItemId: Int? = null

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return list[position].Id!!.toLong()
    }


    fun setSelectedItemId(itemId: Int) {
        selectedItemId = itemId

        

        notifyDataSetChanged() // Notifica al Spinner sobre el cambio
    }


    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val binding = ItemSpinerBinding.bind(
            LayoutInflater.from(context).inflate(
                R.layout.item_spiner,
                parent,
                false
            )
        )

        if (selectedItemId == null){
            val description = " - " + list[position].feeding

            price.priceSplit(list[position].cost){
                binding.tvName.text = it + description
            }
        } else {
            val description = " - " + list[selectedItemId!!].feeding

            price.priceSplit(list[selectedItemId!!].cost){
                binding.tvName.text = it + description
            }
        }

        return binding.root
    }

}