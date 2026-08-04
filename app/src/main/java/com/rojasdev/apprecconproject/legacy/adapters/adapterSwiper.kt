package com.rojasdev.apprecconproject.legacy.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.databinding.ItemSpinerBinding

class adapterSwiper(val context: Context, val list: List<com.rojasdev.apprecconproject.legacy.data.entities.SettingEntity>) : android.widget.BaseAdapter() {

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


    @SuppressLint("SetTextI18n", "ViewHolder")
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

            com.rojasdev.apprecconproject.legacy.controller.price.priceSplit(list[position].cost){
                binding.tvName.text = it + description
            }
        } else {
            val newList = list.toMutableList()
            newList.remove(list[position])

            val description = " - " + list[selectedItemId!!].feeding

            com.rojasdev.apprecconproject.legacy.controller.price.priceSplit(list[selectedItemId!!].cost){
                binding.tvName.text = it + description
            }

            val descriptionNew = " - " + list[position].feeding

            com.rojasdev.apprecconproject.legacy.controller.price.priceSplit(list[position].cost){
                binding.tvName.text = it + descriptionNew
            }
        }

        return binding.root
    }

}