package com.example.marketix.presentation.customloadmore

import android.widget.ProgressBar
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.marketix.R

class LoadingViewHolder(holderOrderBinding: ViewDataBinding) :
    RecyclerView.ViewHolder(holderOrderBinding.root) {
    var progressBar: ProgressBar = itemView.findViewById(R.id.loadMoreProgress)
}