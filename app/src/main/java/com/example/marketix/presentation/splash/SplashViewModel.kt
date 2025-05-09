package com.example.marketix.presentation.splash

import android.content.Context
import com.example.marketix.data.schedulersprovider.SchedulersProvider
import com.example.marketix.di.builder.BaseViewModel
import com.example.marketix.domain.model.SigninUserResponse
import com.example.marketix.domain.repository.PreferencesRepository
import com.example.marketix.domain.repository.SettingsRepository
import com.example.marketix.util.Constants
import com.example.marketix.util.customLog
import com.example.marketix.util.showHTTPExceptionErrors
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException
import javax.inject.Inject

/*
* This class created to hold layout and manage UI related data in life-cycle.
* The main purpose of this class is to avoid Memory leak.
*/
class SplashViewModel @Inject constructor(
    private val context: Context,
    private val preferencesRepository: PreferencesRepository,
    private val settingsRepository: SettingsRepository,
    private val schedulersProvider: SchedulersProvider
) : BaseViewModel<SplashActivityListener>() {
    private val TAG = SplashViewModel::class.java.name

    lateinit var activityListener: SplashActivityListener
    private val compositeDisposable = CompositeDisposable()
    lateinit var user: SigninUserResponse

    /*
    * if splash check = 1 means need to login
    * if splash check = 2 means user profile have missing preferences
    * if splash check = 3 means user have complete profile
    * */

    //TODO testing device token
    fun setDeviceToken(deviceToken: String) {
        preferencesRepository.setDeviceToken(deviceToken)
    }

    fun getStartedClick() {
        when {
            preferencesRepository.getSplashCheck() == 1 -> activityListener.openLoginActivity()
            preferencesRepository.getSplashCheck() == 2 -> activityListener.openWelcomeActivity()
            else -> activityListener.openOnBoardingActivity()

        }
    }

    fun loadAllSettings() {
        if (preferencesRepository.getSplashCheck() == 2) {
            progressVisibility.postValue(false)
            settingsRepository.loadAllSettings(
                preferencesRepository.getDeviceToken(), preferencesRepository.getAccessToken()
            ).subscribeOn(schedulersProvider.io()).subscribe({
                it?.let {
                    context.customLog(it.toString())
                    if (it.status == Constants.STATUS_OK) {
                        preferencesRepository.setAppSettings(Gson().toJson(it))
                    }
                    preferencesRepository.setSplashCheck(2)
                }
            }, {
                context.customLog(it.toString())
                progressVisibility.postValue(false)
                context.showHTTPExceptionErrors(it as HttpException)
            }).let {
                compositeDisposable.add(it)
            }
        }
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

}