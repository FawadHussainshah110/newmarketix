package com.example.marketix.presentation.start_trading

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.marketix.R
import com.example.marketix.domain.model.MarketItem
import com.example.marketix.presentation.customloadmore.LoadingViewHolder
import com.example.marketix.util.Constants.VIEW_TYPE_ITEM
import com.example.marketix.util.Constants.VIEW_TYPE_LOADING
import com.example.marketix.util.SvgLoader

class MarketsListAdapter(
    val context: Context,
    val listener: OnMarketsListItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var isHaveMoreItems = false
    var selectedItem = 0

    var list: MutableList<MarketItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            CustomAdapterViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), R.layout.courses_list_item, parent, false
                )
            )
        } else {
            LoadingViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), R.layout.load_more_item, parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CustomAdapterViewHolder)
            holder.onBindItems(getItem(position), position)
        else {
            val loadingViewHolder = holder as LoadingViewHolder
            loadingViewHolder.progressBar.isIndeterminate = true
        }
    }

    private fun getItem(position: Int): MarketItem {
        return list[position]
    }

    override fun getItemViewType(position: Int): Int {
        return if (list.size - 1 == position && isHaveMoreItems)
            VIEW_TYPE_LOADING
        else
            VIEW_TYPE_ITEM
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class CustomAdapterViewHolder(holderOrderBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(holderOrderBinding.root) {
        fun onBindItems(model: MarketItem, position: Int) {

            itemView.findViewById<TextView>(R.id.tv_heading).text = model.name
            itemView.findViewById<TextView>(R.id.tv_detail).text = model.description

//            Glide
//                .with(context)
//                .`as`(SVG::class.java)
//                .load(
//                    Uri.parse(model.image))
//                .fitCenter()
//                .placeholder(context.getPlaceHolder())
//                .into(SvgTarget(itemView.findViewById(R.id.iv_feature)))

            SvgLoader.loadSvg(context, model.image, itemView.findViewById(R.id.iv_feature))

            itemView.setOnClickListener {
                listener.clickMarketsListItem(model, position)
                notifyItemChanged(selectedItem)
                selectedItem = position
                notifyItemChanged(position)
            }

        }
    }

    fun addData(model: List<MarketItem>, isHaveMorePage: Boolean) {
        this.list.clear()
        isHaveMoreItems = isHaveMorePage
        this.list.addAll(model)
        notifyDataSetChanged()
    }

}
