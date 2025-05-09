package com.example.marketix.presentation.courselearning

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.marketix.R
import com.example.marketix.domain.model.LessonItem
import com.example.marketix.presentation.customloadmore.LoadingViewHolder
import com.example.marketix.util.Constants.VIEW_TYPE_ITEM
import com.example.marketix.util.Constants.VIEW_TYPE_LOADING
import com.example.marketix.util.SvgLoader
import com.example.marketix.util.getPlaceHolder

class CourseLessonListAdapter(
    val context: Context, val listener: OnCourseLessonListItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var isHaveMoreItems = false
    var selectedItem = 0

    var list: MutableList<LessonItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            CustomAdapterViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), R.layout.course_lesson_list_item, parent, false
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

    private fun getItem(position: Int): LessonItem {
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
        fun onBindItems(model: LessonItem, position: Int) {
            itemView.findViewById<TextView>(R.id.tv_heading).text = model.name
            itemView.findViewById<TextView>(R.id.tv_detail).text = model.description
            val imageView: ImageView = itemView.findViewById(R.id.iv_feature)
            val icPlay: ImageView = itemView.findViewById(R.id.iv_play)

            if (model.thumbnail.endsWith(".svg") || model.thumbnail.endsWith(".SVG"))
                SvgLoader.loadSvg(context, model.thumbnail, imageView)
            else
                Glide.with(context).load(model.thumbnail).fitCenter()
                    .placeholder(context.getPlaceHolder()).into(imageView)

            icPlay.setOnClickListener {
                listener.clickCourseLessonListItem(model, position)
            }

            itemView.setOnClickListener {
                listener.clickCourseLessonListItem(model, position)
                notifyItemChanged(selectedItem)
                selectedItem = position
                notifyItemChanged(position)
            }

        }
    }

    fun addData(model: List<LessonItem>, isHaveMorePage: Boolean) {
        this.list.clear()
        isHaveMoreItems = isHaveMorePage
        this.list.addAll(model)
        notifyDataSetChanged()
    }

}
