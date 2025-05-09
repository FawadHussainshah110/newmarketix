package com.example.marketix.util

interface ScrollViewListener {
    fun onScrollEnded(scrollView: ObservableScrollView, x: Int, y: Int, oldx: Int, oldy: Int)
}