package com.example.marketix.presentation.signals

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.marketix.R
import com.example.marketix.domain.model.DataItem
import com.example.marketix.util.Constants.VIEW_TYPE_ITEM


class MarketSignalsChildListAdapter(
    private val childItemList: List<DataItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CustomAdapterViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(
                    parent.context
                ), R.layout.child_signals_list_item, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CustomAdapterViewHolder)
            holder.onBindItems(getItem(position))
    }

    private fun getItem(position: Int): DataItem {
        return childItemList[position]
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE_ITEM
    }

    override fun getItemCount(): Int {
        return childItemList.size
    }

    inner class CustomAdapterViewHolder(holderOrderBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(holderOrderBinding.root) {
        fun onBindItems(model: DataItem) {

            itemView.findViewById<TextView>(R.id.tv_heading).text = model.title
            itemView.findViewById<TextView>(R.id.tv_detail).text = model.value

        }
    }

}
