package com.example.marketix.presentation.fullvideo

import android.content.Context
import com.example.marketix.di.builder.BaseViewModel
import com.example.marketix.domain.repository.PreferencesRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class FullVideoViewModel @Inject constructor(
    private val context: Context,
    private val preferencesRepository: PreferencesRepository,
) : BaseViewModel<FullVideoActivityListener>() {

    private val TAG = FullVideoViewModel::class.java.name

    lateinit var listener: FullVideoActivityListener
    val compositeDisposable = CompositeDisposable()

    var playbackPosition: Long = 0

    fun getDeviceToken(): String {
        return preferencesRepository.getDeviceToken()
    }

    fun getAccessToken(): String {
        return preferencesRepository.getAccessToken()
    }

    fun backPressClick() {
        listener.backPressClick()
    }

    fun playPauseClick() {
        listener.playPauseClick()
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

}