package com.example.weatherapp2023.forecast.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.weatherapp2023.databinding.DailyForecastViewholderBinding
import com.example.weatherapp2023.databinding.LayoutCurrentTempBinding
import com.example.weatherapp2023.forecast.data.CurrentTempUiModel
import com.example.weatherapp2023.forecast.data.ForecastRowModel
import com.example.weatherapp2023.forecast.data.ForecastUiModel
import com.example.weatherapp2023.util.BaseViewHolder

class DailyForecastAdapter(private val onWeatherRowClicked: (ForecastRowModel) -> Unit) :
    ListAdapter<ForecastUiModel, BaseViewHolder<ForecastUiModel>>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ForecastUiModel> {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = when (viewType) {
            VIEW_TYPE_CURRENT_TEMP ->
                CurrentTempViewHolder(LayoutCurrentTempBinding.inflate(inflater, parent, false))
            VIEW_TYPE_FORECAST_ROW -> DailyForecastViewHolder(
                DailyForecastViewholderBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            )
            else -> {
                throw IllegalArgumentException("Unknown type in $TAG")
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ForecastUiModel>, position: Int) {
        holder.bind(getItem((position)))
        if(getItemViewType(position) == VIEW_TYPE_FORECAST_ROW) {
            (holder as DailyForecastViewHolder)?.itemView?.setOnClickListener {
                onWeatherRowClicked(getItem(position) as ForecastRowModel)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is CurrentTempUiModel -> VIEW_TYPE_CURRENT_TEMP
            is ForecastRowModel -> VIEW_TYPE_FORECAST_ROW
            else -> {
                throw IllegalArgumentException("Unknown type in $TAG")
            }
        }
    }

    private companion object {
        const val VIEW_TYPE_CURRENT_TEMP = 0
        const val VIEW_TYPE_FORECAST_ROW = 1
        val TAG = DailyForecastAdapter::class.simpleName

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ForecastUiModel>() {
            override fun areItemsTheSame(
                oldItem: ForecastUiModel,
                newItem: ForecastUiModel
            ): Boolean {
                if (oldItem::class != newItem::class) {
                    return false
                }
                return when (oldItem) {
                    is CurrentTempUiModel -> false //always refresh current temp
                    is ForecastRowModel -> oldItem.date == (newItem as ForecastRowModel).date
                }
            }

            override fun areContentsTheSame(
                oldItem: ForecastUiModel,
                newItem: ForecastUiModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}