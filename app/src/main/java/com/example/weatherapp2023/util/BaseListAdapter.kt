package com.example.weatherapp2023.util

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class BaseListAdapter<MODEL, VIEWHOLDER : BaseViewHolder<MODEL>>(diffUtilCallback: DiffUtil.ItemCallback<MODEL>) :
    ListAdapter<MODEL, VIEWHOLDER>(diffUtilCallback) {

    override fun onBindViewHolder(holder: VIEWHOLDER, position: Int) {
        holder.bind(getItem(position))
    }
}