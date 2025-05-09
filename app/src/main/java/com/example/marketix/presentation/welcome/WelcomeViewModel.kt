package com.example.marketix.presentation.welcome

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.marketix.data.schedulersprovider.SchedulersProvider
import com.example.marketix.di.builder.BaseViewModel
import com.example.marketix.domain.model.AppSettingsResponse
import com.example.marketix.domain.repository.PreferencesRepository
import com.example.marketix.util.Constants
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class WelcomeViewModel @Inject constructor(
    private val context: Context,
    private val preferencesRepository: PreferencesRepository,
    private val schedulersProvider: SchedulersProvider
) : BaseViewModel<WelcomeActivityListener>() {

    private val TAG = WelcomeViewModel::class.java.name

    lateinit var listener: WelcomeActivityListener
    private val compositeDisposable = CompositeDisposable()

    var title = MutableLiveData<String>()
    var detail = MutableLiveData<String>()
    var welcomeContent = MutableLiveData<String>()

    fun loadWelcomeData() {
        val welcomeModel =
            Gson().fromJson(preferencesRepository.getAppSettings(), AppSettingsResponse::class.java)
        for (item in welcomeModel.data.setting)
            if (item.name == Constants.WELCOME_SCREEN_CONTENT) {
                title.postValue(item.title)
                detail.postValue(item.description)
                welcomeContent.postValue(item.image)
            }
    }

    fun proceedClick() {
        listener.openDashboardActivity()
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

}