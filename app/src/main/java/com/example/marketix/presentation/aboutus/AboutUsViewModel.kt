package com.example.marketix.presentation.aboutus

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

class AboutUsViewModel @Inject constructor(
    private val context: Context,
    private val preferencesRepository: PreferencesRepository,
    private val schedulersProvider: SchedulersProvider
) : BaseViewModel<AboutUsActivityListener>() {

    private val TAG = AboutUsViewModel::class.java.name

    var title = MutableLiveData<String>()
    var details = MutableLiveData<String>()
    var aboutUsContent = MutableLiveData<String>()

    lateinit var listener: AboutUsActivityListener
    private val compositeDisposable = CompositeDisposable()

    fun loadAboutUsData() {
        val aboutUsModel =
            Gson().fromJson(preferencesRepository.getAppSettings(), AppSettingsResponse::class.java)
        for (item in aboutUsModel.data.setting)
            if (item.name == Constants.ABOUT_US_SCREEN_CONTENT) {
                title.value = item.title
                details.value = item.description
                aboutUsContent.value = item.image
            }
    }


    fun backPressClick() {
        listener.openBackPressListener()
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

}