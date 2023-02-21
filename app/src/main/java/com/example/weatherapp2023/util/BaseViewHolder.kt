package com.example.weatherapp2023.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<MODEL>(view: View): RecyclerView.ViewHolder(view) {

    protected var model: MODEL? = null

    open fun bind(model: MODEL){
        this.model = model
    }
}