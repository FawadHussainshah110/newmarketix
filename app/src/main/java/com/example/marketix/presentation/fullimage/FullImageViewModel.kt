package com.example.marketix.presentation.fullimage

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.marketix.di.builder.BaseViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class FullImageViewModel @Inject constructor(
    private val context: Context
) : BaseViewModel<FullImageActivityListener>() {

    private val TAG = FullImageViewModel::class.java.name

    lateinit var listener: FullImageActivityListener
    val compositeDisposable = CompositeDisposable()
    var postImage = MutableLiveData<String>()

    fun backPressClick() {
        listener.backPressClick()
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

}