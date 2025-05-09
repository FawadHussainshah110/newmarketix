package com.example.marketix.presentation.onboarding

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.marketix.data.schedulersprovider.SchedulersProvider
import com.example.marketix.di.builder.BaseViewModel
import com.example.marketix.domain.repository.PreferencesRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class OnBoardingViewModel @Inject constructor(
    private val context: Context,
    private val preferencesRepository: PreferencesRepository,
    private val schedulersProvider: SchedulersProvider
) : BaseViewModel<OnBoardingActivityListener>() {

    private val TAG = OnBoardingViewModel::class.java.name

    lateinit var listener: OnBoardingActivityListener
    private val compositeDisposable = CompositeDisposable()

    //    1 for setting, 2 for preference and 3 for dashboard
    var loginCheck = MutableLiveData<Int>()

    fun getStartedClick() {
        preferencesRepository.setSplashCheck(1)
        listener.openLoginActivity()
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

}