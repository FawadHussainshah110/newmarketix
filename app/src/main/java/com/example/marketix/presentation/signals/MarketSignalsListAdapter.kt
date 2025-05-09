package com.example.marketix.presentation.signals

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.marketix.R
import com.example.marketix.domain.model.DataItem
import com.example.marketix.domain.model.SignalItem
import com.example.marketix.presentation.customloadmore.LoadingViewHolder
import com.example.marketix.util.Constants.VIEW_TYPE_ITEM
import com.example.marketix.util.Constants.VIEW_TYPE_LOADING
import com.example.marketix.util.SvgLoader
import com.example.marketix.util.getPlaceHolder
import org.json.JSONArray

class MarketSignalsListAdapter(
    val context: Context, val listener: OnMarketSignalsListItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var isHaveMoreItems = false
    var selectedItem = 0

    var list: MutableList<SignalItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            CustomAdapterViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), R.layout.signals_list_item, parent, false
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
        if (holder is CustomAdapterViewHolder) holder.onBindItems(getItem(position), position)
        else {
            val loadingViewHolder = holder as LoadingViewHolder
            loadingViewHolder.progressBar.isIndeterminate = true
        }
    }

    private fun getItem(position: Int): SignalItem {
        return list[position]
    }

    override fun getItemViewType(position: Int): Int {
        return if (list.size - 1 == position && isHaveMoreItems) VIEW_TYPE_LOADING
        else VIEW_TYPE_ITEM
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class CustomAdapterViewHolder(holderOrderBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(holderOrderBinding.root) {
        fun onBindItems(model: SignalItem, position: Int) {
            val eList = model.name.split(" ").toTypedArray()

            itemView.findViewById<TextView>(R.id.tv_date).text =
                "${eList[0]} ${eList[1]} ${eList[2]}"
            itemView.findViewById<TextView>(R.id.tv_time).text = "${eList[3]} ${eList[4]}"


            val dataItem: ArrayList<DataItem> = ArrayList()
            val resultMap = parseJsonString(model.data)

            resultMap.forEach { (key, value) ->
                println("$key: $value")
                dataItem.add(DataItem(key, value))
            }

            val childRecyclerView: RecyclerView = itemView.findViewById(R.id.rv_child)
            val childLayoutManager =
                LinearLayoutManager(childRecyclerView.context, RecyclerView.VERTICAL, false)
            childRecyclerView.layoutManager = childLayoutManager
            childRecyclerView.adapter = MarketSignalsChildListAdapter(dataItem)
            val clMedia: ConstraintLayout = itemView.findViewById(R.id.cl_media)
            val signalDec = itemView.findViewById<TextView>(R.id.tv_signal_dec)
            val signalTitle = itemView.findViewById<TextView>(R.id.tv_signal_title)
            val bottomPaddingView = itemView.findViewById<View>(R.id.bottomPaddingView)

            if (model.filename != null) {
                clMedia.visibility = View.VISIBLE
                if (model.filedescription != null) {
                    signalDec.visibility = View.VISIBLE
                    bottomPaddingView.visibility = View.VISIBLE
                    signalDec.text = model.filedescription
                }
                if (model.filetitle != null) {
                    signalTitle.visibility = View.VISIBLE
                    bottomPaddingView.visibility = View.VISIBLE
                    signalTitle.text = model.filetitle
                }
                if (model.image.endsWith(".svg") || model.image.endsWith(".SVG"))
                    SvgLoader.loadSvg(
                        context, model.image, itemView.findViewById(R.id.iv_feature)
                    )
                else
                    Glide.with(context).load(model.image).fitCenter()
                        .placeholder(context.getPlaceHolder())
                        .into(itemView.findViewById(R.id.iv_feature))

            }

            itemView.setOnClickListener {
                listener.clickMarketSignalsListItem(model, position)
                notifyItemChanged(selectedItem)
                selectedItem = position
                notifyItemChanged(position)
            }
        }
    }

    fun addData(model: List<SignalItem>, isHaveMorePage: Boolean) {
        this.list.clear()
        isHaveMoreItems = isHaveMorePage
        this.list.addAll(model)
        notifyDataSetChanged()
    }

    fun parseJsonString(jsonString: String): Map<String, String> {
        val result = mutableMapOf<String, String>()
        val jsonArray = JSONArray(jsonString)

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val keys = jsonObject.keys()

            while (keys.hasNext()) {
                val key = keys.next()
                result[key] = jsonObject.getString(key)
            }
        }

        return result
    }
}
